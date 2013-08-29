package agileblaze.sqlite;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

public class result extends Activity {
	private SQLiteDatabase db;
	ListView listview;
	List<RowItem> rowItems;
	ListViewAdapter adapter;
	private DbHelper dbHelper;
	ArrayList<HashMap<String, String>> arraylist;
	byte[] byteImage2 = null;
	TextView  searchresult;

	// Dremher
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.searchbooks);
		rowItems = new ArrayList<RowItem>();
		searchresult = (TextView) findViewById(R.id.searchresult);
		dbHelper = new DbHelper(this);
		db = dbHelper.getReadableDatabase();
		String getallbookvalue = getIntent().getStringExtra("Show all");
		String columns[] = { Variables.KEY_ID, Variables.CATEGORY_NAME,
				Variables.BOOK_NAME, Variables.PRICE, Variables.AUTHOR_NAME,
				Variables.BOOK_IMAGE, Variables.DOB_PURCHASED, Variables.RATING };
		Cursor cursor = null;
		if (getallbookvalue.equals("Show all")) {
			cursor = db.query(Variables.TABLE_NAME, columns, null, null, null,
					null, null, null);
		} else if (getallbookvalue.equals("Author name")) {
			String getauthorname = getIntent().getStringExtra("Authorname");
			String where = Variables.AUTHOR_NAME + "= \"" + getauthorname
					+ "\"";
			cursor = db.query(Variables.TABLE_NAME, columns, where, null, null,
					null, null, null);
		} else if (getallbookvalue.equals("Book name")) {
			String getbookname = getIntent().getStringExtra("Bookname");
			String where = Variables.BOOK_NAME + "= \"" + getbookname + "\"";

			cursor = db.query(Variables.TABLE_NAME, columns, where, null, null,
					null, null, null);
		} else if (getallbookvalue.equals("Price")) {
			int getpricevalue = getIntent().getIntExtra("price", 0);
			String where = Variables.PRICE + " < \"" + getpricevalue + "\"";
			cursor = db.query(Variables.TABLE_NAME, columns, where, null, null,
					null, null, null);
		} else if (getallbookvalue.equals("Rating")) {
			String getRatingvalue = getIntent().getStringExtra("Rating");
			String where = Variables.RATING + " = \"" + getRatingvalue + "\"";
			cursor = db.query(Variables.TABLE_NAME, columns, where, null, null,
					null, null, null);
		}

		int categoryname = cursor.getColumnIndex(Variables.CATEGORY_NAME);
		int bookname = cursor.getColumnIndex(Variables.BOOK_NAME);
		int price = cursor.getColumnIndex(Variables.PRICE);
		int auhtor = cursor.getColumnIndex(Variables.AUTHOR_NAME);
		int date = cursor.getColumnIndex(Variables.DOB_PURCHASED);
		int key = cursor.getColumnIndex(Variables.KEY_ID);
		int rating = cursor.getColumnIndex(Variables.RATING);
		ArrayList<String> arrCategory = new ArrayList<String>(), arrratting = new ArrayList<String>(), arrkey = new ArrayList<String>(), arrdate = new ArrayList<String>(), arrPrice = new ArrayList<String>(), arrAuthor = new ArrayList<String>(), arrbukName = new ArrayList<String>();
		ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			arrkey.add(cursor.getString(key));
			arrCategory.add(cursor.getString(categoryname));
			byteImage2 = cursor.getBlob(cursor
					.getColumnIndex(Variables.BOOK_IMAGE));
			arrbukName.add(cursor.getString(bookname));
			arrPrice.add(cursor.getString(price));
			arrAuthor.add(cursor.getString(auhtor));
			arrdate.add(cursor.getString(date));
			arrratting.add(cursor.getString(rating));
			ByteArrayInputStream imageStream = new ByteArrayInputStream(
					byteImage2);
			Bitmap theImage = BitmapFactory.decodeStream(imageStream);
			bitmapArray.add(theImage);
		}
		String[] strbukkey = new String[arrkey.size()];
		String[] strCategory = new String[arrCategory.size()];
		String[] strPrice = new String[arrPrice.size()];
		String[] strAuhtor = new String[arrAuthor.size()];
		String[] strbukname = new String[arrbukName.size()];
		String[] strdate = new String[arrdate.size()];
		String[] strRating = new String[arrratting.size()];
		arrkey.toArray(strbukkey);
		arrCategory.toArray(strCategory);
		arrPrice.toArray(strPrice);
		arrbukName.toArray(strbukname);
		arrAuthor.toArray(strAuhtor);
		arrdate.toArray(strdate);
		arrratting.toArray(strRating);
		db.close();
		for (int i = 0; i < strCategory.length; i++) {
			RowItem item = new RowItem(strbukkey[i], strCategory[i],
					strbukname[i], strPrice[i], strAuhtor[i],
					bitmapArray.get(i), strdate[i], strRating[i]);
			rowItems.add(item);
		}
		listview = (ListView) findViewById(R.id.buklist);
		ListViewAdapter adapter = new ListViewAdapter(this,
				R.layout.listviewelements, rowItems);
		listview.setAdapter(adapter);
		searchresult.setText(strCategory.length+ " Results Found for " + getallbookvalue);
	}
}
