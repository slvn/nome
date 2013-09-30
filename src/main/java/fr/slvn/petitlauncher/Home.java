package fr.slvn.petitlauncher;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class Home extends Activity {

    private static final String TAG = "Home";

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "[onNewIntent]");
        // Pretty sure we need to launch the Drawer here !
        startActivity(new Intent(this, Drawer.class));
    }
}
