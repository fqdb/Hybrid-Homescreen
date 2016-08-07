package fqdb.net.launcherproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterForList extends RecyclerView.Adapter<RecyclerViewAdapterForList.RecyclerViewHolderForList> {
    private Context context;
    private ArrayList<AppDetail> apps;
    SharedPreferences prefs;
    SharedPreferences.Editor prefseditor;

    public RecyclerViewAdapterForList(Context context, ArrayList<AppDetail> apps) {
        this.apps = apps;
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefseditor = prefs.edit();
        notifyDataSetChanged();
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
        holder.appLabel.setText(EditAppsActivity.appLabelValues.get(position));
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(prefs.getBoolean(apps.get(position).name + "_ishidden",
                false));

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!prefs.getBoolean(apps.get(position).name + "_ishidden",false)) {
                    prefseditor.putBoolean(apps.get(position).name + "_ishidden", true);
                    prefseditor.apply();
                } else {
                    prefseditor.putBoolean(apps.get(position).name + "_ishidden", false);
                    prefseditor.apply();
                }
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
    public int getItemCount() {
        return apps.size();
    }

    public class RecyclerViewHolderForList extends RecyclerView.ViewHolder {
        public ImageView appIcon;
        public EditText appLabel;
        public CheckBox checkBox;

        public RecyclerViewHolderForList(View itemView) {
            super(itemView);
            appIcon = (ImageView) itemView.findViewById(R.id.item_app_icon);
            appLabel = (EditText) itemView.findViewById(R.id.item_app_label);
            checkBox = (CheckBox) itemView.findViewById(R.id.hide_apps_checkbox);
            appLabel.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // do nothing
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    EditAppsActivity.appLabelValues.set(getAdapterPosition(),s.toString());
                    prefseditor.putString(apps.get(getAdapterPosition()).name.toString(),s.toString());
                    prefseditor.commit();
                }
                @Override
                public void afterTextChanged(Editable s) {
                    if (s != apps.get(getAdapterPosition()).label) {
                        // save
                    }
                }
            });
        }
    }
}