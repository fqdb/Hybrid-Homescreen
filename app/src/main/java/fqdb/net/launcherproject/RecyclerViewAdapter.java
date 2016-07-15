package fqdb.net.launcherproject;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> implements
        ItemTouchHelperAdapter {
    private Context context;
    private List<AppDetail> apps;
    private PackageManager pm;
    private SharedPreferences prefs;

    public RecyclerViewAdapter(Context context, ArrayList<AppDetail> apps) {
        this.apps = apps;
        this.context = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, final int position) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        holder.appIcon.setImageDrawable(apps.get(position).icon);
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) holder.appIcon.getLayoutParams();
        int iconSize = prefs.getInt("icon_size",600);
        params.width = iconSize; // width also adjusts height apparently
        holder.appIcon.setLayoutParams(params);
        holder.appLabel.setText(apps.get(position).label);
        holder.itemView.setLongClickable(true);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (prefs.getBoolean("show_labels_drawer", true)) {
            holder.appLabel.setText(apps.get(position).label);
        } else {
            holder.appLabel.setText("");
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
//        Collections.swap(mItems, fromPosition, toPosition);
        // Add here the code to remove from app drawer and add to home screen
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return apps.size();
    }

}