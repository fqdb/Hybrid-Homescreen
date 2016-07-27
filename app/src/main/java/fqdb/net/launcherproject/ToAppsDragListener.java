package fqdb.net.launcherproject;

import android.graphics.Color;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by fqdeb on 2016-07-25.
 */
public class ToAppsDragListener implements View.OnDragListener{
    // set up drop target background here
//    Drawable enterShape = getResources().getDrawable(
//            R.drawable.shape_droptarget);

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        RelativeLayout dropTarget = (RelativeLayout) v;
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                // reset default background
                dropTarget.setBackgroundColor(Color.argb(50, 255, 175, 64));
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
//                if (context instanceof DragFragmentChanger) {
//                    ((DragFragmentChanger)context).openAppsList(View v);
//                }
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                dropTarget.setBackgroundColor(Color.argb(0, 255, 175, 64));
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
                dropTarget.setBackgroundColor(Color.argb(0, 255, 175, 64));
            default:
                break;
        }
        return true;
    }
}
