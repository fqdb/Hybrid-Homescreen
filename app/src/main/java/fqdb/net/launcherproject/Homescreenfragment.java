package fqdb.net.launcherproject;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by fqdeb on 2016-07-01.
 */
public class Homescreenfragment extends Fragment {

    private GridLayoutManager lHomeLayout;
    private GestureDetector gd;
    private RecyclerView rHomeView;
    PackageManager pm;
    private SharedPreferences prefs;
    private ItemTouchHelper myItemTouchHelper;
    private CellLayout cellLayout;
    ArrayList<AppDetail> apps;
    ArrayList<HomeScreenItem> homeScreenItems;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.homescreenpage, container, false);
        cellLayout = (CellLayout) rootView.findViewById(R.id.home_cell_layout);
        // Animate pseudo-action bar
        RelativeLayout pAB = (RelativeLayout) rootView.findViewById(R.id.pseudo_action_bar);
        Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.pseudoactionbar);
        a.setFillAfter(true);
        a.reset();
        pAB.startAnimation(a);

        // Set up GestureDetector
        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {

                        float sensitivity = 50;
                        try {
                            //Swipe Up Check
                            if(e1.getY() - e2.getY() > sensitivity){
                                Toast.makeText(getActivity(), "Swiped up", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return true;
//                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        cellLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

        //temporarily hardcoded set of apps
//        Set<String> muh_apps = new HashSet<String>();
//        muh_apps.add("com.chrome.beta");
//        muh_apps.add("org.telegram.messenger");
//        muh_apps.add("com.mapswithme.maps.pro");
//        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        SharedPreferences.Editor prefseditor = prefs.edit();
//        prefseditor.putStringSet("app_names_homescreen", muh_apps);
//        prefseditor.commit();

        // Fetch home screen setup
        Set<String> muh_apps = new HashSet<String>();
        muh_apps.add("f1122com.chrome.beta");
        muh_apps.add("f1112org.telegram.messenger");

        ArrayList<HomeScreenItem> homeScreenItems = getHomeScreenItems();

        if (homeScreenItems.size() > 0) {
            for (int i = 0; i < homeScreenItems.size(); i++) {
                LinearLayout viewItem = new LinearLayout(getActivity());
                viewItem.setOrientation(LinearLayout.HORIZONTAL);
                viewItem.setId(i);
//                ImageView appIcon = rootView.findViewById();
                cellLayout.addView(viewItem);
            }
        }


        return rootView;
    }

    private ArrayList<HomeScreenItem> getHomeScreenItems() {
        PackageManager pm = getActivity().getPackageManager();
        Set<String> muh_apps = new HashSet<String>();
        muh_apps.add("f1122com.chrome.beta");
        muh_apps.add("f1112org.telegram.messenger");
        homeScreenItems = new ArrayList<HomeScreenItem>();
        for (String i:muh_apps) {
            HomeScreenItem item = new HomeScreenItem();
            if (i.substring(0, 1) == "t") {
                item.isWidget = true;
            } else {
                item.isWidget = false;
            }
            item.itemHeight = Integer.parseInt(i.substring(1, 2));
            item.itemWidth = Integer.parseInt(i.substring(2, 3));
            item.posLeft = Integer.parseInt(i.substring(3, 4));
            item.posTop = Integer.parseInt(i.substring(4, 5));
            item.name = i.substring(5);
            try {
                item.icon = pm.getApplicationIcon(pm.getApplicationInfo(item.name.toString(), 0));
                item.label = pm.getApplicationLabel(pm.getApplicationInfo(item.name.toString(), 0));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            homeScreenItems.add(item);
        }
        return homeScreenItems;
    }
}
