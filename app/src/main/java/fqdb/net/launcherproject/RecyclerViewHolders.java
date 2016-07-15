package fqdb.net.launcherproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by fqdeb on 2016-07-06.
 */
public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView appIcon;
    public TextView appLabel;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        // itemView.setOnLongClickListener(this);
        appIcon = (ImageView) itemView.findViewById(R.id.item_app_icon);
        appLabel = (TextView) itemView.findViewById(R.id.item_app_label);
    }

    @Override
    public void onClick(View view) {
//        Toast.makeText(view.getContext(), "Clicked Country: " + getAdapterPosition(), Toast
//                .LENGTH_SHORT).show();

//        Intent i = pm.getLaunchIntentForPackage(apps.getPosition().name.toString());
//        this.startActivity(i);
    }


}
