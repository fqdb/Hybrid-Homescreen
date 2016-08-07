package fqdb.net.launcherproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class EditAppsActivity extends AppCompatActivity {

    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private RecyclerView rView;
    private SharedPreferences prefs;
    SharedPreferences.Editor prefseditor;
    public final static List<String> appLabelValues = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_apps);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        PackageManager pm = getApplicationContext().getPackageManager();

        final ArrayList<AppDetail> apps = new ArrayList<>();
        Set<String> appNames = prefs.getStringSet("app_names", null);
        if (appNames != null) {
            List<String> appNamesList = Arrays.asList(appNames.toArray(new String[0]));
            for (int ri = 0; ri < appNamesList.size(); ri++) {
                AppDetail app = new AppDetail();
                app.name = appNamesList.get(ri);
                try {
                    app.label = pm.getApplicationLabel(pm.getApplicationInfo(appNamesList.get(ri),
                            0));
                } catch (PackageManager.NameNotFoundException e) {
                    app.label = "";
                }
                try {
                    app.icon = this.getPackageManager().getApplicationIcon(appNamesList.get(ri).toString());
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                apps.add(app);
            }
            Collections.sort(apps, new Comparator<AppDetail>() {
                @Override
                public int compare(AppDetail a1, AppDetail a2) {
                    // String implements Comparable
                    return (a1.label.toString()).compareTo(a2.label.toString());
                }
            });
        } else {
            Toast.makeText(this, "No apps list found", Toast.LENGTH_SHORT).show();
        }


        for (int i=0; i< apps.size(); i++) {
            appLabelValues.add(prefs.getString(apps.get(i).name.toString(),apps.get(i).label
                    .toString()));

//            appLabelValues.add(prefs.getString(apps.get(i).name.toString(),apps.get(i).label.toString()));
        }

        rView = (RecyclerView) findViewById(R.id.listrecyclerview);
        rView.setHasFixedSize(true);
        RecyclerViewAdapterForList rcAdapter = new RecyclerViewAdapterForList(this, apps);
        rView.setAdapter(rcAdapter);
        rView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_apps_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset_btn:
                new AlertDialog.Builder(this)
                        .setTitle("Reset all?")
                        .setMessage("Reset app labels and icons to default ones?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                appLabelValues.clear();
                                //                clearOptions();
                            }
                        })
                        .show();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(i);
    }
}
