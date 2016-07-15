package fqdb.net.launcherproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewAdapterForList extends RecyclerView.Adapter<RecyclerViewHolderForList> implements
        ItemTouchHelperAdapter {
    private Context context;
    private ArrayList<AppDetail> apps;
    SharedPreferences prefs;
    SharedPreferences.Editor prefseditor;

    public RecyclerViewAdapterForList(Context context, ArrayList<AppDetail> apps) {
        this.apps = apps;
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefseditor = prefs.edit();
    }

    @Override
    public RecyclerViewHolderForList onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.apps_list_item, null);
        RecyclerViewHolderForList rcv = new RecyclerViewHolderForList(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolderForList holder, final int position) {
        holder.appIcon.setImageDrawable(apps.get(position).icon);
        holder.appLabel.setText(apps.get(position).label);
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(prefs.getBoolean(apps.get(position).name + "_ishidden",
                false));
//        if (prefs.contains(apps.get(holder.getAdapterPosition()).toString())) {
//            holder.checkBox.setChecked(prefs.getBoolean(apps.get(position).toString() + "_ishidden",false));
//        } else {
//            holder.checkBox.setChecked(false);
//        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefseditor.putBoolean(apps.get(position).name + "_ishidden",true);
                prefseditor.commit();
            }
        });
        holder.appIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Open icon chooser for icon at position
                Toast.makeText(context,"Icon chooser", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
//        Collections.swap(mItems, fromPosition, toPosition);
        // Add here the code to remove from app drawer and add to home screen
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

}