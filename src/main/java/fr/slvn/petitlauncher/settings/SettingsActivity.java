package fr.slvn.petitlauncher.settings;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {
    public static final String PREF_SHOW_WALLPAPER = "PREF_SHOW_WALLPAPER";
    public static final boolean PREF_SHOW_WALLPAPER_DEFAULT = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }
}
