package fqdb.net.launcherproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditSingleItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_single_item);
        int position = getIntent().getExtras().getInt("position");
        String appLabel = getIntent().getExtras().getString("appLabel");
        EditText editAppLabel = (EditText) findViewById(R.id.edit_app_label);
        editAppLabel.setText(appLabel);
//        ActionBar ab = getActionBar();
//        ab.setTitle("Edit entry");
//
//        Bitmap bitmap = (Bitmap) in.readParcelable(getClass().getClassLoader());
//            appIcon = new BitmapDrawable(bitmap);
    }

    private void pressOK(View v) {
//        SharedPreferences prefs = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        int size = prefs.getInt("num_of_apps", 0);
//
//        EditText editAppLabel = (EditText) findViewById(R.id.edit_app_label);
//        editor.putStringSet("customAppLabels",customAppLabels);
//        editor.commit();

        finish();
    }

    private void pressCancel(View v) {
        finish();
    }
}
