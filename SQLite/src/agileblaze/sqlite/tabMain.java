package agileblaze.sqlite;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class tabMain extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tabmain);
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);

		TabSpec firstTabSpec = tabHost.newTabSpec("tab1");
		TabSpec secondTabSpec = tabHost.newTabSpec("tab2");
		TabSpec thirdTabSpec = tabHost.newTabSpec("tab3");
		firstTabSpec.setIndicator("Add Books",
				getResources().getDrawable(R.drawable.ic_input_add))
				.setContent(new Intent(this, addbooks.class));
		secondTabSpec.setIndicator("Show Books",
				getResources().getDrawable(R.drawable.ic_menu_today))
				.setContent(new Intent(this, Showbooks.class));
		thirdTabSpec.setIndicator("Report",
				getResources().getDrawable(R.drawable.chart))
				.setContent(new Intent(this, chart.class));
		tabHost.addTab(firstTabSpec);
		tabHost.addTab(secondTabSpec);
		tabHost.addTab(thirdTabSpec);
		// tabHost.addTab(secondTabSpec);

		for (int i = 0; i < tabHost.getTabWidget().getTabCount(); i++) {
			tabHost.getTabWidget().getChildAt(i)
					.setBackgroundColor(Color.TRANSPARENT);
		}

	}
}
