package fr.slvn.petitlauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class Drawer extends Activity {

    private static final String TAG = "Home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);

        GridView gridView = (GridView) findViewById(R.id.drawer);
        gridView.setAdapter(new ApplicationAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResolveInfo info = (ResolveInfo) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.setClassName(info.activityInfo.applicationInfo.packageName, info.activityInfo.name);
                startActivity(intent);
                finish();
            }
        });

    }

}
