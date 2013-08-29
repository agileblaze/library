package agileblaze.sqlite;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;

public class chart extends Activity {
	private static final int SERIES_NR = 1;
	private DbHelper dbHelper;
	private SQLiteDatabase db;
	private int one, two, three, four, five, six, temp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chart);
		XYMultipleSeriesRenderer renderer = getTruitonBarRenderer();
		myChartSettings(renderer);
		Intent intent = ChartFactory.getBarChartIntent(this,
				getTruitonBarDataset(), renderer, Type.DEFAULT);
		startActivityForResult(intent,1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1){
			Intent myIntent = new Intent(chart.this,
					tabMain.class);
			startActivity(myIntent);
			finish();
		}else{
		
		}
	}

	private XYMultipleSeriesDataset getTruitonBarDataset() {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		String legendTitles = "";
		legendTitles = "No of books";
		dbHelper = new DbHelper(this);
		db = dbHelper.getReadableDatabase();
		String columns[] = { Variables.KEY_ID, Variables.CATEGORY_NAME,
				Variables.BOOK_NAME, Variables.PRICE, Variables.AUTHOR_NAME,
				Variables.BOOK_IMAGE, Variables.DOB_PURCHASED, Variables.RATING };
		Cursor cursor = db.query(Variables.TABLE_NAME, columns, null, null,
				null, null, null, null);
		int price = cursor.getColumnIndex(Variables.PRICE);

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

			String s = cursor.getString(price);
			temp = Integer.parseInt(s);
			if (temp < 99) {
				one++;
			} else if (temp < 499) {
				two++;
			} else if (temp < 999) {
				three++;
			} else if (temp < 1999) {
				four++;
			} else if (temp < 2999) {
				five++;
			} else if (temp < 10000) {
				six++;
			}

		}

		for (int i = 0; i < SERIES_NR; i++) {
			CategorySeries series = new CategorySeries(legendTitles);
			// for (int k = 0; k < nr; k++) {
			// series.add(100 + r.nextInt() % 100);
			// }
			series.add(one);
			series.add(two);
			series.add(three);
			series.add(four);
			series.add(five);
			series.add(six);
			dataset.addSeries(series.toXYSeries());
		}
		return dataset;
	}

	public XYMultipleSeriesRenderer getTruitonBarRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setMarginsColor(Color.rgb(255, 146, 1));
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(Color.rgb(234, 203, 140));
		renderer.setAxesColor(Color.BLACK);
		renderer.setXLabelsAngle(30);
		renderer.setXLabelsColor(Color.RED);
		renderer.setAxisTitleTextSize(25);
		renderer.setChartTitleTextSize(35);
		renderer.setLegendTextSize(15);
		renderer.setZoomEnabled(true);

		renderer.setMargins(new int[] { 70, 30, 30, 30 });
		SimpleSeriesRenderer r = new SimpleSeriesRenderer();
		r.setColor(Color.rgb(69, 68, 66));
		renderer.addSeriesRenderer(r);

		return renderer;
	}

	private void myChartSettings(XYMultipleSeriesRenderer renderer) {
		renderer.setChartTitle("Price Range by Books");

		renderer.setXAxisMin(0.5);
		renderer.setXAxisMax(8.5);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(10);
		renderer.setGridColor(Color.BLACK);
		renderer.addXTextLabel(1, "0 - 99");
		renderer.addXTextLabel(2, "100 - 499");
		renderer.addXTextLabel(3, "500 - 999");
		renderer.addXTextLabel(4, "1000 - 1999");
		renderer.addXTextLabel(5, "2000 - 2999");
		renderer.addXTextLabel(6, "2000 - 10000");
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setBarSpacing(.5);
		renderer.setXTitle("price");
		renderer.setYTitle("Books");
		renderer.setShowGrid(true);
		renderer.setGridColor(Color.GRAY);
		renderer.setXLabels(6); // sets the number of integer labels to appear
	}
}
