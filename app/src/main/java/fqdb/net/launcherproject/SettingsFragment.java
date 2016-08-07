package fqdb.net.launcherproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.SeekBar;

/**
 * Created by fqdeb on 2016-07-14.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences
        .OnSharedPreferenceChangeListener {

    SharedPreferences prefs;
    SharedPreferences.Editor prefseditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        Preference contact = findPreference("contact_me");
        contact.setOnPreferenceClickListener(new OnPreferenceClickListener()
        {
            public boolean onPreferenceClick(Preference preference)
            {
                Intent mailto = new Intent(Intent.ACTION_SEND);
                mailto.setType("message/rfc822") ; // use from live device
                mailto.putExtra(Intent.EXTRA_EMAIL, new String[]{"felix@fqdeboer.net"});
                mailto.putExtra(Intent.EXTRA_SUBJECT,"Hybrid Homescreen");
//                mailto.putExtra(Intent.EXTRA_TEXT,"Body Here");
                startActivity(Intent.createChooser(mailto, "Select email application"));
                return true;
            }
        });

        Preference website = findPreference("open_my_website");
        website.setOnPreferenceClickListener(new OnPreferenceClickListener()
        {
            public boolean onPreferenceClick(Preference preference)
            {
                Intent openWebsite = new Intent(Intent.ACTION_VIEW);
                openWebsite.setData(Uri.parse("http://fqdeboer.net"));
                startActivity(openWebsite);
                return true;
            }
        });

        Preference licences = findPreference("licences");
        licences.setOnPreferenceClickListener(new OnPreferenceClickListener()
        {
            public boolean onPreferenceClick(Preference preference)
            {
                startActivity(new Intent(getActivity(),LicencesActivity.class));
                return true;
            }
        });

        final Preference folderPosition = findPreference("folder_position");
        folderPosition.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                // !!! this doesn't work!
                if (prefs.getString("folder_position", "") == "first") {
                    folderPosition.setSummary("Before apps");
                } else if (prefs.getString("folder_position", "") == "last") {
                    folderPosition.setSummary("After apps");
                } else if (prefs.getString("folder_position", "") == "with_apps") {
                    folderPosition.setSummary("Sort with apps");
                } else {
                    folderPosition.setSummary("");
                    // error, do nothing
                }

                return true;
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("show_labels_home")) {
            prefseditor.putBoolean("show_labels_home", false);
        } else if (key.equals("show_labels_drawer")) {
            prefseditor.putBoolean("show_labels_drawer", false);
        } else if (key.equals("icon_size")) {
            prefseditor.putInt("icon_size", 600); // how to adjust this?
        } else if (key.equals("action_bar")) {
            Preference actionBar = findPreference("action_bar");
            actionBar.setSummary(prefs.getString("action_bar",""));
        } else if (key.equals("swipe_up")) {
            Preference actionBar = findPreference("swipe_up");
            actionBar.setSummary(prefs.getString("swipe_up","Open search"));
        } else if (key.equals("swipe_down")) {
            Preference actionBar = findPreference("swipe_down");
            actionBar.setSummary(prefs.getString("swipe_down","Expand notification bar"));
        }
    }
}
