package fqdb.net.launcherproject;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by fqdeb on 2016-07-01.
 */
public class Homescreenfragment extends Fragment {

    PackageManager pm;
    private SharedPreferences prefs;
    private ItemTouchHelper myItemTouchHelper;
    private CellLayout cellLayout;
    private RelativeLayout toAppsDropTarget;
    ArrayList<AppDetailHome> apps;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.homescreen_fragment, container, false);
        cellLayout = (CellLayout) rootView.findViewById(R.id.home_cell_layout);
        // set number of columns here

        RelativeLayout uninstall = (RelativeLayout) rootView.findViewById(R.id.trashan);
        toAppsDropTarget = (RelativeLayout) rootView.findViewById(R.id.drop_target_to_apps);
        if (toAppsDropTarget == null)
            Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor prefseditor = prefs.edit();
        apps = new ArrayList<>();

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

        // Listen for shortcut additions
        ((MainActivity)getActivity()).setShortcutListener(new ShortcutListener() {
            @Override
            public void addShortcut(String appName, float relX, float relY) {
                AppDetail shortcut = new AppDetail();
                shortcut.name = appName;
                if (addAppToView(appName, relX, relY)){
                    updateHomescreen();
                }
            }
        });

        return rootView;
    }

    private boolean addAppToView(String appName, float relX, float relY){
        try {
            pm = getActivity().getPackageManager();
            int gridSizeH = Integer.parseInt(prefs.getString("home_grid_h","4"));
            int gridSizeV = Integer.parseInt(prefs.getString("home_grid_v","6"));
            ApplicationInfo appInfo = pm.getApplicationInfo(appName,PackageManager.GET_META_DATA);
            AppDetailHome appDetail = new AppDetailHome();
            appDetail.label = pm.getApplicationLabel(appInfo);
            appDetail.icon = pm.getApplicationIcon(appInfo);
            appDetail.name = appName;
            appDetail.posLeft = Math.round(relX * (float) gridSizeH);
            appDetail.postTop = Math.round(relY * (float) gridSizeV);;
            apps.add(appDetail);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void updateHomescreen(){
        int count = apps.size();
//        shortcutLayout.removeAllViews();

        // Redraw the layout
        cellLayout.requestLayout();

        for (int i = 0; i < apps.size(); i++){
            final AppDetailHome app = apps.get(i);
            View convertView = getActivity().getLayoutInflater().inflate(R.layout.cell_item, null);
            ImageView im = (ImageView)convertView.findViewById(R.id.item_app_icon);
            im.setImageDrawable(app.icon);
            TextView tv = (TextView)convertView.findViewById(R.id.item_app_label);
            tv.setText(app.label);
            CellLayout.LayoutParams lp = new CellLayout.LayoutParams();
            // position in the cell grid to place the item
            lp.left = apps.get(i).posLeft; // from left
            lp.top = apps.get(i).postTop; // from top
            // Width and height, maybe allow setting this to '2' for finer grids.
            lp.height = 1; // item height
            lp.width = 1; // item width
            convertView.setLayoutParams(new CellLayout.LayoutParams(lp));
            cellLayout.addView(convertView);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = pm.getLaunchIntentForPackage(app.name.toString());
                    startActivity(i);
                }
            });

            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    AlertDialog.Builder mDialog = new AlertDialog.Builder(getActivity());
                    mDialog.setTitle("Delete shortcut?");
                    mDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    mDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // remove view
                            cellLayout.removeView(v);
                        }
                    });
                    AlertDialog deleteDialog = mDialog.create(); deleteDialog.show();
                    return false;
                }
            });
        }
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
