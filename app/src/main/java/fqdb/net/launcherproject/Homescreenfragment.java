package fqdb.net.launcherproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by fqdeb on 2016-07-01.
 */
public class Homescreenfragment extends Fragment {

    private GestureDetector gd;
    PackageManager pm;
    private SharedPreferences prefs;
    private ItemTouchHelper myItemTouchHelper;
    private CellLayout cellLayout;
    private RelativeLayout toAppsDropTarget;
    ArrayList<AppDetail> apps;
    ArrayList<HomeScreenItem> homeScreenItems;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.homescreen_fragment, container, false);
        cellLayout = (CellLayout) rootView.findViewById(R.id.home_cell_layout);
        RelativeLayout uninstall = (RelativeLayout) rootView.findViewById(R.id.trashan);
        toAppsDropTarget = (RelativeLayout) rootView.findViewById(R.id.drop_target_to_apps);
        if (toAppsDropTarget == null)
            Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        SharedPreferences.Editor prefseditor = prefs.edit();
//        prefseditor.putStringSet("app_names_homescreen", muh_apps);
//        prefseditor.commit();

        // Fetch home screen setup
//        Set<String> muh_apps = prefs.getStringSet("home_screen_items", null);
        Set<String> muh_apps = new HashSet<String>();

        ArrayList<HomeScreenItem> homeScreenItems = getHomeScreenItems();
        if (homeScreenItems.size() > 0) {
//            for (int i = 0; i < homeScreenItems.size(); i++) {
//                View viewItem;
//                viewItem = createShortcut
//                LinearLayout viewItem = new LinearLayout(getActivity());
//                viewItem.setOrientation(LinearLayout.HORIZONTAL);
//                viewItem.setId(i);
//                ImageView appIcon = rootView.findViewById();
//                cellLayout.addView(viewItem);
//            }
        }

        // Pseudo-action bar
        pseudoActionBar(rootView);

        // Set up GestureDetector
        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {

                        float sensitivity = 50;
                        try {
                            //Swipe up Check
                            if(e1.getY() - e2.getY() > sensitivity){
                                swipeUp();
                            }
                            //Swipe down Check
                            if(e2.getY() - e1.getY() > sensitivity){
                                swipeDown();
                            }
                        } catch (Exception e) {
                            // nothing
                        }
//                        return true;
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        cellLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

        // Set up draglistener
        cellLayout.setOnDragListener(new HomeScreenDragListener());
        toAppsDropTarget.setOnDragListener(new ToAppsDragListener());

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

    private void swipeUp() {
        switch (prefs.getString("swipe_up","search")) {
            case "search" :
                MainActivity activity = (MainActivity) getActivity();
                activity.openSearch(getView());
                break;
            case "settings" :
                startActivity(new Intent(Settings.ACTION_SETTINGS));
                break;
            case "custom" :
                Toast.makeText(getActivity(), "custom action", Toast.LENGTH_SHORT).show();
                break;
            case "nothing" : // do nothing
            default:
                //do nothing
        }
    }

    private void swipeDown() {
        switch (prefs.getString("swipe_down","notification_bar")) {
            case "notification_bar":
                expandNotificationBar();
                break;
            case "settings" :
                startActivity(new Intent(Settings.ACTION_SETTINGS));
                break;
            case "custom" :
                Toast.makeText(getActivity(), "custom action", Toast.LENGTH_SHORT).show();
                break;
            case "search" :
                MainActivity activity = (MainActivity) getActivity();
                activity.openSearch(getView());
                break;
            case "nothing" : // do nothing
            default:
                //do nothing
        }
    }

    private void expandNotificationBar() {
        try {
            //noinspection WrongConstant
            Object service = getContext().getSystemService("statusbar");
            Class<?> statusbarManager = Class
                    .forName("android.app.StatusBarManager");
            Method expand = null;
            if (service != null) {
                expand = statusbarManager
                        .getMethod("expandNotificationsPanel");
                expand.setAccessible(true);
                expand.invoke(service);
            }
        } catch (Exception e) {
        }
    }

    private void pseudoActionBar(View rootView) {
        RelativeLayout pAB = (RelativeLayout) rootView.findViewById(R.id.pseudo_action_bar);
        switch (prefs.getString("action_bar","2")) {
            case "1":
                pAB.setVisibility(View.GONE);
                break;
            case "2":
                // Animate pseudo-action bar
                Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.pseudoactionbar);
                a.setFillAfter(true);
                a.reset();
                pAB.startAnimation(a);
                break;
            case "3":
                // keep fixed layout as defined in xml
            case "4":
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) pAB.getLayoutParams();
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
    }
}
