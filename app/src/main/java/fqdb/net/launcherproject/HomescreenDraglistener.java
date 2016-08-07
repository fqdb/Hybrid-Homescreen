package fqdb.net.launcherproject;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by fqdeb on 2016-07-25.
 */
public class HomeScreenDragListener implements View.OnDragListener{
    // set up drop target background here
//    Drawable enterShape = getResources().getDrawable(
//            R.drawable.shape_droptarget);

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        CellLayout homescreen = (CellLayout) v;
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                homescreen.setBackgroundResource(R.drawable.drag_bg);
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                homescreen.setBackgroundResource(R.drawable.drag_bg);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                // reset default background
                homescreen.setBackgroundColor(Color.argb(0, 255, 175, 64));
                break;
            case DragEvent.ACTION_DROP:
                // reset default background
                // Dropped, reassign View to ViewGroup
                View view = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) view.getParent();
                owner.removeView(view);
//                LinearLayout container = (LinearLayout) v;
//                container.addView(view);
//                view.setVisibility(View.VISIBLE);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                homescreen.setBackgroundColor(Color.argb(0, 255, 175, 64));
            default:
                break;
        }
        return true;
    }
}
