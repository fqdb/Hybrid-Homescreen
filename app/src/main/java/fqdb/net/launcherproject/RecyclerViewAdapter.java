package fqdb.net.launcherproject;

import java.util.ArrayList;
import java.util.List;

import android.content.ClipData;
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
    private SharedPreferences prefs;
    private AppDetail app;

    public RecyclerViewAdapter(Context context, ArrayList<AppDetail> apps) {
        this.apps = apps;
        this.context = context;
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        pm = context.getPackageManager();
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
        Toast.makeText(context, "moving", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onDrop(int fromPosition, int toPosition) {
        return false;
    }


    public class AppsViewHolder extends RecyclerView.ViewHolder {
        public ImageView appIcon;
        public TextView appLabel;

        public AppsViewHolder(View itemView) {
            super(itemView);
            appIcon = (ImageView) itemView.findViewById(R.id.item_app_icon);
            appLabel = (TextView) itemView.findViewById(R.id.item_app_label);
        }
    }

    public void onBindViewHolder(AppsViewHolder holder, final int position) {
        holder.appIcon.setImageDrawable(apps.get(position).icon);
        ViewGroup.LayoutParams params = holder.appIcon.getLayoutParams();
//        params.width = prefs.getInt("icon_size",600); // width also adjusts height apparently
        holder.appIcon.setLayoutParams(params);
        holder.appLabel.setText(apps.get(position).label);
        if (apps.get(position).isapp) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = pm.getLaunchIntentForPackage(apps.get(position).name.toString());
                    context.startActivity(i);
                }
            });
        }
        holder.itemView.setLongClickable(true);
        if (!prefs.getBoolean("show_labels_drawer", true)) {
            holder.appLabel.setText("");
        } else {
            holder.appLabel.setText(apps.get(position).label);
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                Toast.makeText(context, "dragging", Toast.LENGTH_SHORT).show();
                // pass the following to the the main activity
                String appName = (String) apps.get(position).name;
                // Create draggable copy of the item
                ClipData data = ClipData.newPlainText("","");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data,shadowBuilder,v,0);
                v.setVisibility(View.INVISIBLE);
                // Go to page 1 and pass the app name
                if (context instanceof DragFragmentChanger) {
                    ((DragFragmentChanger)context).onBackPressed();
                    ((DragFragmentChanger)context).addDraggeditem(appName);
                }
                return true;
            }
        });
    }
    
}