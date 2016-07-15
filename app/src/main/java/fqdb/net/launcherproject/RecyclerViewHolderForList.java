package fqdb.net.launcherproject;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by fqdeb on 2016-07-06.
 */
public class RecyclerViewHolderForList extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView appIcon;
    public TextView appLabel;
    public CheckBox checkBox;

    public RecyclerViewHolderForList(View itemView) {
        super(itemView);
        appIcon = (ImageView) itemView.findViewById(R.id.item_app_icon);
        appLabel = (TextView) itemView.findViewById(R.id.item_app_label);
        checkBox = (CheckBox) itemView.findViewById(R.id.hide_apps_checkbox);
    }

    @Override
    public void onClick(View view) {
//        Toast.makeText(view.getContext(), "Clicked Country: " + getAdapterPosition(), Toast
//                .LENGTH_SHORT).show();

//        Intent i = pm.getLaunchIntentForPackage(apps.getPosition().name.toString());
//        this.startActivity(i);
    }


}
