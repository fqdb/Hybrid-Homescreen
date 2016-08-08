package fqdb.net.launcherproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;
import java.util.Vector;

public class MainActivity extends FragmentActivity implements DragFragmentChanger {
    private static final int num_of_pages = 2;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private PackageManager pm;
    private ShortcutListener addShortcut;
    RelativeLayout navDrawer;
    DrawerLayout drawerLayout;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fetch settings
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        List<Fragment> fragments = new Vector<Fragment>();
        Bundle page = new Bundle();
//        for each fragment you want to add to the pager:
        fragments.add(Fragment.instantiate(this,Homescreenfragment.class.getName(),page));
        fragments.add(Fragment.instantiate(this,Appslistfragment.class.getName(),page));

        mPager = (ViewPager) findViewById(R.id.viewpager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            LinearLayout search_bar =(LinearLayout) findViewById(R.id.searchbar);
            @Override
            public void onPageSelected(int position) {
                if (position==0) {closeSearch();}
                if (position==1) {
                    EditText searchfield = (EditText) findViewById(R.id.search_field);
                    searchfield.setText("");
                }
            }
        });
        setUpDragListener();
        navDrawer = (RelativeLayout)findViewById(R.id.left_nav_drawer);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        // Transparent navbar
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    private void setUpDragListener() {
        mPager.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        // reset default background
                        break;
                    case DragEvent.ACTION_DROP:
                        // reset default background
                        dropItem(event);
                        // also remove item from app drawer
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void dropItem(DragEvent dragEvent){
        // if the user drops on the homescreen workspace...
        if (mPager.getCurrentItem() == 0){
            String appName = dragEvent.getClipData().getItemAt(0).getText().toString();

            if(getShortcutListener() != null){
                getShortcutListener().addShortcut(appName);
            }

        }
    }


    public ShortcutListener getShortcutListener(){
        return addShortcut;
    }
    public void setShortcutListener(ShortcutListener addShortcut){
        this.addShortcut = addShortcut;

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0: return new Homescreenfragment();
                case 1: return new Appslistfragment();
                default: return new Homescreenfragment();
            }
        }

        @Override
        public int getCount() {
            return num_of_pages;
        }
    }

    public void openSearch(View rootView) {
        LinearLayout search_bar =(LinearLayout) findViewById(R.id.searchbar);
        if (search_bar.getVisibility() == View.GONE) {
            mPager.setCurrentItem(1, true);
            search_bar.setVisibility(View.VISIBLE);
            search_bar.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(this
                    .INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        } else {
            closeSearch();
        }
    }
    public void closeSearch() {
        LinearLayout search_bar =(LinearLayout) findViewById(R.id.searchbar);
        mPager.setCurrentItem(0, true);

        EditText searchfield =(EditText) findViewById(R.id.search_field);
        InputMethodManager imm = (InputMethodManager) getSystemService(this
                .INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchfield.getWindowToken(), 0);
        search_bar.setVisibility(View.GONE);
    }

    public void openEditActivity(View rootView) {
        drawerLayout.closeDrawer(navDrawer);
        startActivity(new Intent(this,EditAppsActivity.class));
    }

    public void openSettings(View rootView) {
        drawerLayout.closeDrawer(navDrawer);
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void openDeviceSettings(View rootView) {
        drawerLayout.closeDrawer(navDrawer);
        startActivity(new Intent(Settings.ACTION_SETTINGS));
    }

    public void wallPickerIntent(View rootView) {
        drawerLayout.closeDrawer(navDrawer);
        startActivity(new Intent(Intent.ACTION_SET_WALLPAPER));
    }

    public void onLeft(View view)
    {
        drawerLayout.openDrawer(navDrawer);
    }

    public void onBackPressed() //  Moves to screen 0 on back press
    {
        mPager.setCurrentItem(0, true);
        drawerLayout.closeDrawer(navDrawer);
    }

    public void openAppDrawer(View view) {mPager.setCurrentItem(1, true); }

    public void addDraggeditem(String appName) {
        Toast.makeText(this, appName, Toast.LENGTH_SHORT).show();

        pm = this.getPackageManager();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        AppDetail app = new AppDetail();
        app.name = appName;
        try {
            pm.getApplicationLabel(pm.getApplicationInfo(appName,
                    0));
            pm.getApplicationIcon(appName);
        } catch (PackageManager.NameNotFoundException e) {
            app.label = "";
            app.icon = null;
            e.printStackTrace();
        }
        // create bitmap of dragged item

        // pass AppDetail to Homescreenfragment on drop

        // call the drag ended function here
    }

    public static void refresh() {
//            Fragment fragment = getFragmentManager().findFragmentById();
//            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.detach(fragment);
//            ft.attach(fragment);
//            ft.commit();
    }
}
