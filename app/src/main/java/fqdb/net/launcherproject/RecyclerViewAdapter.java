package fqdb.net.launcherproject;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.AppsViewHolder>
        implements ItemTouchHelperAdapter {

    private Context context;
    private List<AppDetail> apps;
    PackageManager pm;
//    private SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

    public RecyclerViewAdapter(Context context, ArrayList<AppDetail> apps) {
        this.apps = apps;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    @Override
    public AppsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView;
        layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, null);
        return new AppsViewHolder(layoutView);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }


    public class AppsViewHolder extends RecyclerView.ViewHolder {
        public ImageView appIcon;
        public TextView appLabel;
        public AppDetail app;

        public AppsViewHolder(View itemView) {
            super(itemView);
            appIcon = (ImageView) itemView.findViewById(R.id.item_app_icon);
            appLabel = (TextView) itemView.findViewById(R.id.item_app_label);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent i = pm.getLaunchIntentForPackage(app.name.toString());
//                    context.startActivity(i);
                    Toast.makeText(v.getContext(), app.name.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void onBindViewHolder(AppsViewHolder holder, final int position) {
        holder.appIcon.setImageDrawable(apps.get(position).icon);
        ViewGroup.LayoutParams params = holder.appIcon.getLayoutParams();
//        params.width = prefs.getInt("icon_size",600); // width also adjusts height apparently
        holder.appIcon.setLayoutParams(params);
        holder.appLabel.setText(apps.get(position).label);
        holder.itemView.setLongClickable(true);
//        if (!prefs.getBoolean("show_labels_drawer", true)) {
//            holder.appLabel.setText("");
//        } else {
            holder.appLabel.setText(apps.get(position).label);
//        }

    }
    
}



//
//public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> implements
//        ItemTouchHelperAdapter {
//    private Context context;
//    private List<AppDetail> apps;
//    private PackageManager pm;
//    private SharedPreferences prefs;
//
//    public RecyclerViewAdapter(Context context, ArrayList<AppDetail> apps) {
//        this.apps = apps;
//        this.context = context;
//    }
//
//    @Override
//    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
//        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, null);
//        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
//        return rcv;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerViewHolders holder, final int position) {
//        prefs = PreferenceManager.getDefaultSharedPreferences(context);
//        holder.appIcon.setImageDrawable(apps.get(position).icon);
//        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) holder.appIcon.getLayoutParams();
//        int iconSize = prefs.getInt("icon_size",600);
//        params.width = iconSize; // width also adjusts height apparently
//        holder.appIcon.setLayoutParams(params);
//        holder.appLabel.setText(apps.get(position).label);
//        holder.itemView.setLongClickable(true);
//        if (!prefs.getBoolean("show_labels_drawer", true)) {
//            holder.appLabel.setText("");
//        } else {
//            holder.appLabel.setText(apps.get(position).label);
//        }
//    }
//
//    @Override
//    public boolean onItemMove(int fromPosition, int toPosition) {
////        Collections.swap(mItems, fromPosition, toPosition);
//        // Add here the code to remove from app drawer and add to home screen
//        notifyItemMoved(fromPosition, toPosition);
//        return true;
//    }
//
//    // Return the size of your dataset (invoked by the layout manager)
//    @Override
//    public int getItemCount() {
//        return apps.size();
//    }
//
//}