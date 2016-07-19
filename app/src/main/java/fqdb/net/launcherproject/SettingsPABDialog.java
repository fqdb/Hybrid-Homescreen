package fqdb.net.launcherproject;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

/**
 * Created by fqdeb on 2016-07-19.
 */
public class SettingsPABDialog extends DialogPreference {

    public SettingsPABDialog(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void onDialogClosed(boolean ok) {
        super.onDialogClosed(ok);
        persistBoolean(ok);
    }
}
