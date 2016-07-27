package fqdb.net.launcherproject;

import android.view.View;

/**
 * Created by fqdeb on 2016-07-25.
 */
public interface DragFragmentChanger {
    void onBackPressed();
    void addDraggeditem(String appName);
    void openAppDrawer(View view);
}
