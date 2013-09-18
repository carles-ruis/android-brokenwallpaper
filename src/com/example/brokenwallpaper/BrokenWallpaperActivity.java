package com.example.brokenwallpaper;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;

public class BrokenWallpaperActivity extends PreferenceActivity {

/*- ********************************************************************************** */
/*- *********** OVERRIDE ************* */
/*- ********************************************************************************** */
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	addPreferencesFromResource(R.xml.prefs);
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	return true;
}
/*- ********************************************************************************** */
/*- *********** PRIVATE ************* */
/*- ********************************************************************************** */

}
