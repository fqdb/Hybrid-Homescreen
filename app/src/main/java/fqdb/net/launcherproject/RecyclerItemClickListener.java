package fqdb.net.launcherproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by fqdeb on 2016-07-08.
 * Source: http://sapandiwakar.in/recycler-view-item-click-handler/
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener myListener;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    GestureDetector myGestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        myListener = listener;
        myGestureDetector = new GestureDetector(context, new GestureDetector
                .SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && myListener != null && myGestureDetector.onTouchEvent(e)) {
            myListener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }
}
