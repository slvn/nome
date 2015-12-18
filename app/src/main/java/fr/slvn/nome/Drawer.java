package fr.slvn.nome;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.slvn.nome.settings.SettingsActivity;

public class Drawer extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = "Drawer";

    private LauncherActivityInfo selectedItemInfo;

    @Bind(R.id.drawer_layout) protected DrawerLayout drawerLayout;
    @Bind(R.id.drawer) protected GridView mainDrawer;
    @Bind(R.id.drawer_right) protected GridView rightDrawer;

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
                    uninstallPackage(selectedItemInfo.getApplicationInfo().packageName);
                    actionMode.finish();
                    return true;
                case R.id.action_mode_store:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + selectedItemInfo.getApplicationInfo().packageName)));
                    actionMode.finish();
                    return true;
                case R.id.action_mode_info:
                    launchPackageInfo(selectedItemInfo.getApplicationInfo().packageName);
                    actionMode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            selectedItemInfo = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean showWallPaper = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(SettingsActivity.PREF_SHOW_WALLPAPER,
                        SettingsActivity.PREF_SHOW_WALLPAPER_DEFAULT);

        setTheme(showWallPaper ? R.style.AppTheme : R.style.AppThemeNoWallpaper);
        setContentView(R.layout.drawer);
        ButterKnife.bind(this);

        UserManager um = (UserManager) getSystemService(USER_SERVICE);
        List<UserHandle> userHandles = um.getUserProfiles();
        if (userHandles.size() > 1) {
            // Oh, managed profiles, nice.
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, rightDrawer);
            rightDrawer.setAdapter(new ApplicationAdapter(this, userHandles.get(1)));
            rightDrawer.setOnItemClickListener(this);
            rightDrawer.setOnItemLongClickListener(this);
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, rightDrawer);
        }

        mainDrawer.setAdapter(new ApplicationAdapter(this, userHandles.get(0)));
        mainDrawer.setOnItemClickListener(this);
        mainDrawer.setOnItemLongClickListener(this);

        updateDate();
    }

    private void updateDate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            final Locale l = Locale.getDefault();
            final String fmt = DateFormat.getBestDateTimePattern(l, "EEEMMMMd");
            setTitle(new SimpleDateFormat(fmt, l).format(new Date()));
        } else {
            setTitle(DateFormat.format("EEE, MMMM d", new Date()));
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LauncherActivityInfo info = (LauncherActivityInfo) parent.getItemAtPosition(position);
        ComponentName activity = info.getComponentName();
        Intent intent = new Intent();
        intent.setClassName(activity.getPackageName(), activity.getClassName());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ActionMode actionMode = Drawer.this.startActionMode(actionModeCallback);

        if (actionMode == null) {
            return false;
        }

        view.setSelected(true);

        selectedItemInfo = (LauncherActivityInfo) parent.getItemAtPosition(position);
        CharSequence name = "  " + selectedItemInfo.getLabel();
        actionMode.setTitle(name);
        actionMode.setSubtitle("   " + selectedItemInfo.getApplicationInfo().packageName);
        return true;
    }
}
