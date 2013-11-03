package fr.slvn.petitlauncher;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import fr.slvn.petitlauncher.settings.SettingsActivity;

public class Drawer extends Activity {

    private static final String TAG = "Drawer";

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
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ResolveInfo info = (ResolveInfo) parent.getItemAtPosition(position);
                CharSequence name = info.loadLabel(getPackageManager());
                Toast.makeText(Drawer.this, name, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        boolean showWallPaper = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsActivity.PREF_SHOW_WALLPAPER,
                SettingsActivity.PREF_SHOW_WALLPAPER_DEFAULT);
        ActionBar bar = getActionBar();
        if (showWallPaper) {
            bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_transparent));
            gridView.setBackground(null);
        } else {
            bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.ab_solid));
            gridView.setBackgroundResource(android.R.color.background_dark);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
