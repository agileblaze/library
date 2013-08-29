package agileblaze.sqlite;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//http://stackoverflow.com/questions/2309227/sqlite-select-with-condition-on-date
public class Showbooks extends Activity implements OnClickListener {
	private SQLiteDatabase db;
	ListView listview;
	List<RowItem> rowItems;
	ListViewAdapter adapter;
	private DbHelper dbHelper;
	ArrayList<HashMap<String, String>> arraylist;
	byte[] byteImage2 = null;
	private static String dbkey, dbbook_name, dbAuthor, dbDate, dbPrice;
	private ImageButton datepicker, edtStartbut, edttobut;
	private EditText book_name, author_name, date, price;
	private Button save;
	private TextView error, dlgHeading, Error, Price_disp;
	private Spinner category;
	Context context;
	private RatingBar rating;
	static final int DATE_DIALOG_ID = 0;
	String rtbook_valuestr = "";
	float rtbookfloat;
	private int mYear, date_disp_value = 0;
	private int mMonth;
	private int mDay;
	private EditText edtSearch;
	private EditText dlgText, edtDateStart, edtDateto;
	private Button dlgSearch;
	private SeekBar skPrice;
	private int progressChanged = 0;
	private InputMethodManager im;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showbooks);
		edtSearch = (EditText) findViewById(R.id.edtSearch);
		edtSearch.setKeyListener(null);
		rowItems = new ArrayList<RowItem>();
		dbHelper = new DbHelper(this);
		db = dbHelper.getReadableDatabase();
		String columns[] = { Variables.KEY_ID, Variables.CATEGORY_NAME,
				Variables.BOOK_NAME, Variables.PRICE, Variables.AUTHOR_NAME,
				Variables.BOOK_IMAGE, Variables.DOB_PURCHASED, Variables.RATING };
		Cursor cursor = db.query(Variables.TABLE_NAME, columns, null, null,
				null, null, null, null);
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

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				TextView tvkey = (TextView) view.findViewById(R.id.tvkey);
				dbkey = tvkey.getText().toString();
				TextView edtbukname = (TextView) view
						.findViewById(R.id.book_name);
				dbbook_name = edtbukname.getText().toString();
				TextView edtAuthor = (TextView) view.findViewById(R.id.author);
				dbAuthor = edtAuthor.getText().toString();
				TextView edtdate = (TextView) view.findViewById(R.id.date);
				dbDate = edtdate.getText().toString();
				TextView edtPrice = (TextView) view.findViewById(R.id.price);
				dbPrice = edtPrice.getText().toString();
				RatingBar edtRating = (RatingBar) view
						.findViewById(R.id.rtBook);
				rtbookfloat = edtRating.getRating();

				openOptionsMenu();
			}
		});

		registerForContextMenu(edtSearch);
		// dbImage;

		edtSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Showbooks.this
						.getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				openContextMenu(v);
			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Search");
		menu.add(0, v.getId(), 0, "Show All");
		menu.add(0, v.getId(), 0, "By Book name");
		menu.add(0, v.getId(), 0, "By Author name");
		menu.add(0, v.getId(), 0, "By Price");
		menu.add(0, v.getId(), 0, "By Rating");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater getMenu = getMenuInflater();
		getMenu.inflate(R.menu.edit, menu);
		return true;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		if (item.getTitle() == "Show All") {
			showall(item.getItemId());
		} else if (item.getTitle() == "By Book name") {
			book(item.getItemId());
		} else if (item.getTitle() == "By Author name") {
			authorname(item.getItemId());
		} else if (item.getTitle() == "By Price") {
			price(item.getItemId());
		} else if (item.getTitle() == "By Rating") {
			rating(item.getItemId());
		}
		return true;
	}

	public void showall(int id) {

		Intent showall = new Intent(Showbooks.this, result.class);
		showall.putExtra("Show all", "Show all");
		startActivity(showall);
	}

	public void authorname(int id) {

		final Dialog mDialog = new Dialog(Showbooks.this);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.searchbybookname);
		dlgText = (EditText) mDialog.findViewById(R.id.edtdgValue);
		dlgSearch = (Button) mDialog.findViewById(R.id.btnSearch);
		dlgHeading = (TextView) mDialog.findViewById(R.id.tvHeading);
		dlgHeading.setText("Search by Author Name");
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = mDialog.getWindow();
		lp.copyFrom(mDialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);
		dlgSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String edtValue = dlgText.getText().toString();
				Intent showall = new Intent(Showbooks.this, result.class);
				showall.putExtra("Show all", "Author name");
				showall.putExtra("Authorname", edtValue);
				startActivity(showall);
				mDialog.dismiss();

			}
		});
		mDialog.show();
	}

	public void book(int id) {

		final Dialog mDialog = new Dialog(Showbooks.this);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.searchbybookname);
		dlgText = (EditText) mDialog.findViewById(R.id.edtdgValue);
		dlgSearch = (Button) mDialog.findViewById(R.id.btnSearch);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = mDialog.getWindow();
		lp.copyFrom(mDialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);
		dlgSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String edtValue = dlgText.getText().toString();
				Intent showall = new Intent(Showbooks.this, result.class);
				showall.putExtra("Show all", "Book name");
				showall.putExtra("Bookname", edtValue);
				startActivity(showall);
				mDialog.dismiss();
			}
		});
		mDialog.show();
	}

	public void rating(int id) {
		final Dialog mDialog = new Dialog(Showbooks.this);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.searchbybookname);
		dlgText = (EditText) mDialog.findViewById(R.id.edtdgValue);
		dlgSearch = (Button) mDialog.findViewById(R.id.btnSearch);
		dlgHeading = (TextView) mDialog.findViewById(R.id.tvHeading);
		dlgHeading.setText("Search by Rating");
		dlgText.setHint("Enter the Rating value");
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = mDialog.getWindow();
		lp.copyFrom(mDialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);
		dlgSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String edtValue = dlgText.getText().toString();
				Intent showall = new Intent(Showbooks.this, result.class);
				showall.putExtra("Show all", "Rating");
				showall.putExtra("Rating", edtValue);
				startActivity(showall);
				mDialog.dismiss();
			}
		});
		mDialog.show();
	}

	public void price(int id) {
		final Dialog mDialog = new Dialog(Showbooks.this);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.price_search);
		skPrice = (SeekBar) mDialog.findViewById(R.id.price_bar);
		Price_disp = (TextView) mDialog.findViewById(R.id.tv_price_display);
		dlgSearch = (Button) mDialog.findViewById(R.id.butSearch);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(mDialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

		skPrice.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				Price_disp.setText("Rs." + progressChanged + " and below");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				progressChanged = progress;
			}
		});
		dlgSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent showall = new Intent(Showbooks.this, result.class);
				showall.putExtra("Show all", "Price");
				showall.putExtra("price", progressChanged);
				startActivity(showall);
				mDialog.dismiss();
			}
		});
		mDialog.show();
		mDialog.getWindow().setAttributes(lp);

	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear + 1;
			mDay = dayOfMonth;
			updateDisplay();
		}

	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_edit:
			Dialog mDialog = new Dialog(Showbooks.this);
			Window window = mDialog.getWindow();
			window.setLayout(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT);
			mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialog.setContentView(R.layout.edit_books);
			category = (Spinner) mDialog.findViewById(R.id.sp_category);
			book_name = (EditText) mDialog.findViewById(R.id.edt_bukname);
			author_name = (EditText) mDialog.findViewById(R.id.edt_Author);
			date = (EditText) mDialog.findViewById(R.id.edt_date);
			datepicker = (ImageButton) mDialog
					.findViewById(R.id.Img_but_calender);
			price = (EditText) mDialog.findViewById(R.id.edt_price);
			save = (Button) mDialog.findViewById(R.id.save_data);
			error = (TextView) mDialog.findViewById(R.id.tvError);
			rating = (RatingBar) mDialog.findViewById(R.id.rtBook);
			book_name.setText(dbbook_name);
			author_name.setText(dbAuthor);
			date.setText(dbDate);
			price.setText(dbPrice);
			rating.setRating(rtbookfloat);
			save.setOnClickListener(this);
			datepicker.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					showDialog(DATE_DIALOG_ID);
					date_disp_value = 0;
				}
			});
			mDialog.show();
			return true;
		case R.id.action_delete:
			dbHelper = new DbHelper(this);
			db = dbHelper.getWritableDatabase();

			new AlertDialog.Builder(this)
					.setTitle("Delete Book?")
					.setMessage("Are you sure you want to delete this Book?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// continue with delete
									String deleteQuery = "DELETE FROM  "
											+ Variables.TABLE_NAME + " where "
											+ Variables.KEY_ID + "='" + dbkey
											+ "'";
									db.execSQL(deleteQuery);
									Intent refresh = new Intent(Showbooks.this,
											tabMain.class);
									startActivity(refresh);
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// do nothing
								}
							}).show();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	private void updateDisplay() {
		if (date_disp_value == 0) {
			date.setText(mDay + "-" + mMonth + "-" + mYear);
		} else if (date_disp_value == 1) {
			edtDateStart.setText(mDay + "-" + mMonth + "-" + mYear);
		} else if (date_disp_value == 2) {
			edtDateto.setText(mDay + "-" + mMonth + "-" + mYear);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.save_data:

			Variables.buk_name = book_name.getText().toString();
			Variables.Author_name = author_name.getText().toString();
			Variables.dobpurchased_name = date.getText().toString();
			Variables.price = price.getText().toString();
			Variables.error = "";
			Variables.submit = 0;
			if (Variables.buk_name.equals("")) {
				Variables.submit = 1;
				Variables.error = Variables.error + "book";
				Variables.errorcount++;
			}
			if (Variables.Author_name.equals("")) {
				Variables.submit = 1;
				if (Variables.errorcount >= 1)
					Variables.error = Variables.error + ",";
				Variables.errorcount++;
				Variables.error = Variables.error + "Author Name";
			}
			if (Variables.dobpurchased_name.equals("")) {
				Variables.submit = 1;
				if (Variables.errorcount >= 1)
					Variables.error = Variables.error + ",";
				Variables.errorcount++;
				Variables.error = Variables.error + "Date of Purchased";
			}
			if (Variables.price.equals("")) {
				Variables.submit = 1;
				if (Variables.errorcount >= 1)
					Variables.error = Variables.error + ",";
				Variables.errorcount++;
				Variables.error = Variables.error + "Price";
			}

			if (Variables.submit == 0) {
				dbHelper = new DbHelper(Showbooks.this);
				db = dbHelper.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put(Variables.CATEGORY_NAME, category.getSelectedItem()
						.toString());
				values.put(Variables.BOOK_NAME, book_name.getText().toString());
				values.put(Variables.DOB_PURCHASED, date.getText().toString());
				values.put(Variables.AUTHOR_NAME, author_name.getText()
						.toString());
				values.put(Variables.RATING, rating.getRating());
				values.put(Variables.PRICE, price.getText().toString());
				String where = "key_id=" + dbkey;
				db.update(Variables.TABLE_NAME, values, where, null);
				Log.i("Table Created", "Hai");
				db.close();
				Toast.makeText(getApplicationContext(), "Changes Saved",
						Toast.LENGTH_LONG).show();
				Intent refresh = new Intent(Showbooks.this, tabMain.class);
				startActivity(refresh);
				finish();
			} else {
				error.setVisibility(View.VISIBLE);
				String text = "Field(s) missing " + "(" + Variables.error + ")";
				error.setText(text);
			}

			break;

		}
	}
}
