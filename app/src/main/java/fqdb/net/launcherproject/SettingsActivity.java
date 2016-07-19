package fqdb.net.launcherproject;

import android.app.Activity;
import android.os.Bundle;
import android.preference.ListPreference;

public class SettingsActivity extends Activity {

    private ListPreference pabList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}