package fqdb.net.launcherproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by fqdeb on 2016-07-01.
 */
public class Homescreenfragment extends Fragment{

    private GridLayoutManager lHomeLayout;
    private RecyclerView rHomeView;
    PackageManager pm;
    private SharedPreferences prefs;
    private ItemTouchHelper myItemTouchHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.homescreenpage, container, false);
        lHomeLayout = new GridLayoutManager(getActivity(), 4);

        //temporarily hardcoded set of apps
        Set<String> muh_apps = new HashSet<String>();
        muh_apps.add("com.chrome.beta");
        muh_apps.add("org.telegram.messenger");
        muh_apps.add("com.mapswithme.maps.pro");
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor prefseditor = prefs.edit();
        prefseditor.putStringSet("app_names_homescreen", muh_apps);
        prefseditor.commit();
        setRecyclerView(rootView);

        return rootView;
    }

    private ArrayList<AppDetail> getHomeScreenApps() {
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        PackageManager pm = getActivity().getPackageManager();

        final ArrayList<AppDetail> apps = new ArrayList<>();
        Set<String> appNames = prefs.getStringSet("app_names_homescreen", null);
        if (appNames != null) {
            List<String> appNamesList = Arrays.asList(appNames.toArray(new String[0]));
            for (int ri = 0; ri < appNamesList.size(); ri++) {
                AppDetail app = new AppDetail();
                app.name = appNamesList.get(ri);
                if (prefs.getBoolean("show_labels_home", true)) {
                    try {
                        app.label = pm.getApplicationLabel(pm.getApplicationInfo(appNamesList.get(ri),
                                0));
                    } catch (PackageManager.NameNotFoundException e) {
                        app.label = "";
                    }
                } else {app.label = "";}
                try {
                    app.icon = getActivity().getPackageManager().getApplicationIcon(appNamesList.get(ri)
                            .toString());
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
            return apps;
        } else {
            return apps;
        }
    }

    public void setRecyclerView(View rootView) {
        final ArrayList<AppDetail> apps = getHomeScreenApps();
        rHomeView = (RecyclerView) rootView.findViewById(R.id.home_recycler_view);
        rHomeView.setHasFixedSize(true);
        rHomeView.setLayoutManager(lHomeLayout);
        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(getActivity(), apps);
        rHomeView.setAdapter(rcAdapter);
        rHomeView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener
                        .OnItemClickListener() {
                    @Override
                    public void onItemClick(View rootView, int position) {
                        Intent i = pm.getLaunchIntentForPackage(apps.get(position).name.toString());
                        Homescreenfragment.this.startActivity(i);
                    }
                })
        );

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(rcAdapter);
        myItemTouchHelper = new ItemTouchHelper(callback);
        myItemTouchHelper.attachToRecyclerView(rHomeView);
    }
}
