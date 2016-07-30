package fqdb.net.launcherproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by fqdeb on 2016-07-30.
 */


public class FolderPopupDialog extends Dialog {
    public FolderPopupDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.folder_popup);

    }
}
