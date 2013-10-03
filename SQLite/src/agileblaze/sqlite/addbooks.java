package agileblaze.sqlite;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class addbooks extends Activity implements OnClickListener {
	Button addPics;
	ImageView imgView;
	Uri photoUri;
	private ImageButton datepicker;
	private EditText book_name, author_name, date, price;
	private Button pick, save, scan;
	private TextView path_display, error;
	private Spinner category;
	private RatingBar rating;
	Context context;
	private ImageView buk_image;
	File capturePath = null;
	private static final int SELECT_PICTURE = 1;
	private static final int REQUEST_CODE_SCANNER = 2;
	Uri mImageCaptureUri;
	byte[] byteImage1 = null;
	static final int DATE_DIALOG_ID = 0;
	private int mYear;
	private int mMonth;
	private int mDay;

	// http://mobile.tutsplus.com/tutorials/android/android-sdk-create-a-barcode-reader/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addbooks);
		category = (Spinner) findViewById(R.id.sp_category);
		book_name = (EditText) findViewById(R.id.edt_bukname);
		author_name = (EditText) findViewById(R.id.edt_Author);
		date = (EditText) findViewById(R.id.edt_date);
		datepicker = (ImageButton) findViewById(R.id.Img_but_calender);
		price = (EditText) findViewById(R.id.edt_price);
		save = (Button) findViewById(R.id.save_data);
		scan = (Button) findViewById(R.id.butScan);
		pick = (Button) findViewById(R.id.pick_image);
		error = (TextView) findViewById(R.id.tvError);
		buk_image = (ImageView) findViewById(R.id.bukImage);
		path_display = (TextView) findViewById(R.id.tvLocation);
		rating = (RatingBar) findViewById(R.id.rtBook);
		pick.setOnClickListener(this);
		save.setOnClickListener(this);
		scan.setOnClickListener(this);

		datepicker.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);

			}
		});
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
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	private void updateDisplay() {
		date.setText(mDay + "-" + mMonth + "-" + mYear);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Variables.selectedImageUri = data.getData();
				Variables.SelectedImagePath = getPath(Variables.selectedImageUri);
				System.out.println("Image Path : "
						+ Variables.SelectedImagePath);
				capturePath = new File(Variables.SelectedImagePath);
				long length = capturePath.length();
				length = length / 1024;
				if (length <= 99) {
					buk_image.setImageURI(Variables.selectedImageUri);
					path_display.setText(Variables.SelectedImagePath);
				} else {
					Toast.makeText(
							getApplicationContext(),
							"Image Size is too large! please select an image below 100kb",
							Toast.LENGTH_LONG).show();
				}

			}
		}
	}

	private String getPath(Uri selectedImageUri) {
		// TODO Auto-generated method stub
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
				null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Toast pieceToast = null;
		switch (v.getId()) {
		case R.id.save_data:

			Variables.buk_name = book_name.getText().toString();
			Variables.Author_name = author_name.getText().toString();
			Variables.dobpurchased_name = date.getText().toString();
			Variables.price = price.getText().toString();
			Variables.location = path_display.getText().toString();
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
			if (Variables.location.equals("")) {
				Variables.submit = 1;
				if (Variables.errorcount >= 1)
					Variables.error = Variables.error + ",";
				Variables.errorcount++;
				Variables.error = Variables.error + "Image location";
			}
			if (Variables.submit == 0) {
				DbHelper dbentry = new DbHelper(addbooks.this);
				SQLiteDatabase db = dbentry.getWritableDatabase();
				ContentValues values = new ContentValues();
				String buk_name1 = "sndfvg";
				FileInputStream instream;
				try {
					instream = new FileInputStream(path_display.getText()
							.toString());
					BufferedInputStream bif = new BufferedInputStream(instream);
					byteImage1 = new byte[bif.available()];
					bif.read(byteImage1);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				values.put(Variables.RATING, rating.getRating());
				values.put(Variables.CATEGORY_ID, buk_name1);
				values.put(Variables.CATEGORY_NAME, category.getSelectedItem()
						.toString());
				values.put(Variables.BOOK_ID, "1");
				values.put(Variables.BOOK_NAME, book_name.getText().toString());
				values.put(Variables.DOB_PURCHASED, date.getText().toString());
				values.put(Variables.AUTHOR_NAME, author_name.getText()
						.toString());
				values.put(Variables.PRICE, price.getText().toString());
				values.put("image", byteImage1);
				db.insert(Variables.TABLE_NAME, null, values);
				db.close();
				LayoutInflater inflater = getLayoutInflater();

				View layout = inflater.inflate(R.layout.toast,
						(ViewGroup) findViewById(R.id.toast));

				TextView text = (TextView) layout
						.findViewById(R.id.tv_buk_name);
				text.setText(" " + book_name.getText().toString() + " ");
				Toast toast = new Toast(getApplicationContext());
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(layout);
				toast.show();
				category.setSelection(0);
				book_name.setText("");
				date.setText("");
				author_name.setText("");
				price.setText("");
				buk_image.setImageResource(R.drawable.empty_photo);
				rating.setRating(1.0f);
				path_display.setText("Image Path : ");
			} else {
				error.setVisibility(View.VISIBLE);
				String text = "Field(s) missing " + "(" + Variables.error + ")";
				error.setText(text);
			}

			break;
		case R.id.butScan:
			Log.i("Click", "Button Click");
			Intent barcode = new Intent(addbooks.this, CameraTestActivity.class);
			startActivity(barcode);
			break;
		case R.id.pick_image:
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_PICK);
			startActivityForResult(
					Intent.createChooser(intent, "Select Picture"),
					SELECT_PICTURE);
			break;

		}
	}

}