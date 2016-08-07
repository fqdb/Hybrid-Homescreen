package fqdb.net.launcherproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class LicencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licences);
        TextView hayai = (TextView) findViewById(R.id.hayai);
        hayai.setText(Html.fromHtml(
                "<a href=\"https://github.com/seizonsenryaku/HayaiLauncher/blob/HEAD/README.md\">Hayai Launcher</a> <a href\"https://www.gnu.org/licenses/license-list.html#apache2\">(Apache2 licence)</a>"));
        hayai.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
