package fr.slvn.nome;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import fr.slvn.nome.settings.SettingsActivity;

public class Drawer extends Activity {

    private static final String TAG = "Drawer";

    private ActionMode actionMode;

    private ResolveInfo currentItem;

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.action_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_mode_delete:
                    uninstallPackage(currentItem.activityInfo.packageName);
                    actionMode.finish();
                    return true;
                case R.id.action_mode_store:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+currentItem.activityInfo.packageName)));
                    actionMode.finish();
                    return true;
                case R.id.action_mode_info:
                    launchPackageInfo(currentItem.activityInfo.packageName);
                    actionMode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            Drawer.this.actionMode = null;
            currentItem = null;
        }
    };


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
                actionMode = Drawer.this.startActionMode(actionModeCallback);
                view.setSelected(true);

                currentItem = (ResolveInfo) parent.getItemAtPosition(position);
                CharSequence name = "  " + currentItem.loadLabel(getPackageManager());
                actionMode.setTitle(name);
                actionMode.setSubtitle("   " + currentItem.activityInfo.applicationInfo.packageName);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION |
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
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

    private void uninstallPackage(String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, uri);
        startActivity(intent);
    }

    private void launchPackageInfo(String packageName) {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + packageName));
        startActivity(intent);
    }

}
