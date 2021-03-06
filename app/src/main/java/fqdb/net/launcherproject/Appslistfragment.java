package fqdb.net.launcherproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Appslistfragment extends Fragment implements OnStartDragListener {

    private GridLayoutManager lLayout;
    private PackageManager pm;
    private ArrayList<AppDetail> apps;
    private ArrayList<AppDetail> allApps;
    private RecyclerView rView;
    private ItemTouchHelper myItemTouchHelper;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefseditor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor prefseditor = prefs.edit();
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.appslist_fragment, container,
                false);

        final ArrayList<AppDetail> apps = getAppsList();

        // Save copy of all apps, including hidden ones
        allApps = apps;
        // Remove hidden apps, apps in folders from recyclerview input
        for (int i=0; i < apps.size(); i++) {
            if (prefs.getBoolean(apps.get(i).name + "_ishidden",false)) {
                apps.remove(i);
            }
        }

        setRecyclerView(rootView);

        EditText searchfield =(EditText) rootView.findViewById(R.id.search_field);
        searchfield.addTextChangedListener(searchTextWatcher);

//      Update drawer on package change
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        intentFilter.addDataScheme("package");
        getActivity().registerReceiver(new AppReceiver(), intentFilter);

        return rootView;
    }

    private ArrayList<AppDetail> getAppsList() {
        pm = getActivity().getPackageManager();
        apps = new ArrayList<AppDetail>();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> availableActivities = pm.queryIntentActivities(i, 0);
        for(ResolveInfo ri:availableActivities){
            AppDetail app = new AppDetail();
            app.name = ri.activityInfo.packageName;
            app.label = prefs.getString(app.name.toString(),ri.loadLabel(pm).toString());
            app.icon = ri.activityInfo.loadIcon(pm);
            app.isapp = true;
            apps.add(app);
        }

        Set<String> appNamesSet = new HashSet<String>();
        for (AppDetail app : apps) {
            appNamesSet.add(app.name.toString());
        }
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor prefseditor = prefs.edit();
        prefseditor.putStringSet("app_names", appNamesSet);
        prefseditor.apply();

        // add folders to recyclerview input
        AppDetail app = new AppDetail();
        app.label = "My Folder";
        app.icon = getResources().getDrawable(R.drawable.folder_icon_bg);
        app.isapp = false;
        apps.add(app);

        Collections.sort(apps, new Comparator<AppDetail>() {
            @Override
            public int compare(AppDetail a1, AppDetail a2) {
                // String implements Comparable
                return (a1.label.toString()).compareTo(a2.label.toString());
            }
        });
        Collections.sort(apps, new Comparator<AppDetail>() {
            @Override
            public int compare(AppDetail a1, AppDetail a2) {
                return (a1.isapp.toString()).compareTo(a2.isapp.toString());
            }
        });

        return apps;
    }

    public void setRecyclerView(View rootView) {
        // Move this to getApps() to allow easy refreshing, see Imma Wake P6
        lLayout = new GridLayoutManager(rootView.getContext(), 4);
        rView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);
        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(getActivity(), apps);
        rView.setAdapter(rcAdapter);
        // Allow dragging:
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(rcAdapter);
        myItemTouchHelper = new ItemTouchHelper(callback);
        myItemTouchHelper.attachToRecyclerView(rView);
    }

    public final TextWatcher searchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence query, int start, int before, int count) {
            final ArrayList<AppDetail> filteredApps = filter(allApps, query.toString());
            RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(getActivity(), filteredApps);
            rView.setAdapter(rcAdapter);
            prefseditor = prefs.edit();
            if (query != "") {prefseditor.putBoolean("void_query", false);}
            else {prefseditor.putBoolean("void_query", true);}
        }
        @Override
        public void afterTextChanged(Editable s) { }
    };

    private ArrayList<AppDetail> filter(ArrayList<AppDetail> apps, String query) {
        query = query.toLowerCase().trim();
        final ArrayList<AppDetail> filteredAppsList = new ArrayList<>();
        for (AppDetail app : apps) {
            final String text = app.label.toString().toLowerCase().trim();
            if (text.contains(query)) {
                filteredAppsList.add(app);
            }
        }
        return filteredAppsList;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        myItemTouchHelper.startDrag(viewHolder);
    }

    public class AppReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
