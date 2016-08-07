package fqdb.net.launcherproject;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by fqdeb on 2016-07-30.
 */


public class FolderPopupDialog extends DialogFragment {
    private RecyclerView rView;
    private GridLayoutManager lLayout;
    private ArrayList<AppDetail> apps;
    private PackageManager pm;
    private SharedPreferences prefs;

    public FolderPopupDialog(){}

    public static FolderPopupDialog newInstance(String title) {
        FolderPopupDialog frag = new FolderPopupDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.folder_popup, container,
                false);
        // fetch folder contents
        pm = getActivity().getPackageManager();
//        prefs.getStringSet()
        //hardcoded set of apps
        Set<String> appNamesSet = new HashSet<String>();
        appNamesSet.add("com.chrome.beta");
        appNamesSet.add("org.adaway");
        appNamesSet.add("nl.negentwee");
        appNamesSet.add("com.box.android");
        appNamesSet.add("com.huawei.camera");

        apps = new ArrayList<AppDetail>();
        List<String> appNamesList = Arrays.asList(appNamesSet.toArray(new String[0]));
        for (int i = 0; i<appNamesList.size();i++) {
            AppDetail app = new AppDetail();
            app.name = appNamesList.get(i);
            try {
                app.label = pm.getApplicationLabel(pm.getApplicationInfo(appNamesList.get(i),
                        0));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            try {
                app.icon = getActivity().getPackageManager().getApplicationIcon(appNamesList.get(i)
                        .toString());
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            app.isapp = true;
            apps.add(app);
        }

        // set up recyclerview contents
        lLayout = new GridLayoutManager(rootView.getContext(), 3);
        rView = (RecyclerView) rootView.findViewById(R.id.folder_rview);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);
        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(getActivity(), apps);
        rView.setAdapter(rcAdapter);
        // Allow dragging:
//        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(rcAdapter);
//        myItemTouchHelper = new ItemTouchHelper(callback);
//        myItemTouchHelper.attachToRecyclerView(rView);

        return rootView;

    }
}
