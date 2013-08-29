package agileblaze.sqlite;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class SplashScreenActivity extends Activity {
	long Delay = 800;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_activity);
		Timer RunSplash = new Timer();
		TimerTask ShowSplash = new TimerTask() {
			@Override
			public void run() {

				
				Intent myIntent = new Intent(SplashScreenActivity.this,
						tabMain.class);
				startActivity(myIntent);
				finish();
			}
		};
		RunSplash.schedule(ShowSplash, Delay);
	}

}
