package fr.slvn.nome.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import fr.slvn.nome.Drawer;

public class SettingsActivity extends Activity {
    public static final String PREF_SHOW_WALLPAPER = "PREF_SHOW_WALLPAPER";
    public static final boolean PREF_SHOW_WALLPAPER_DEFAULT = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Relaunch DrawerActivity
        startActivity(new Intent(this, Drawer.class));
    }
}
