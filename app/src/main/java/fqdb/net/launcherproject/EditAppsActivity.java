package fqdb.net.launcherproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
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

//        Boolean[] hiddenItems = getCheckBoxStates(appNames);

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
            case R.id.save_btn:
                // add save prefs function
                // prefseditor.commit();
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                return true;
        }
    }

//    private Boolean[] getCheckBoxStates(Set<String> appNames) {
//        Boolean hiddenItems[] = new Boolean[appNames.size()];
//        if (prefs.getInt("hidden_size", 0) != 0) {
//            final int listSize = prefs.getInt("hidden_size", appNames.size());
//            for (int i = 0; i < listSize; i++) {
//                hiddenItems[i] = prefs.getBoolean("hidden_checked_item", true);
//            }
//        } else {
//            prefseditor.putInt("hidden_size", appNames.size());
//            prefseditor.commit();
//            for (int i = 0; i < prefs.getInt("hidden_size", 0); i++) {
//                prefs.getBoolean(appNames.toString() + "_ishidden",false);
//                hiddenItems[i] = prefs.getBoolean("hidden_checked_item", false);
//            }
//        }
//        return hiddenItems;
//    }
}
