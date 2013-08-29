package agileblaze.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

	public DbHelper(Context c) {
		// TODO Auto-generated constructor stub
		super(c, Variables.DB_NAME, null, Variables.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		createTable(db);
	}

	private void createTable(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_BOOK_TABLE = "CREATE TABLE " + Variables.TABLE_NAME + "("
				+ Variables.KEY_ID + " integer  primary key autoincrement,"
				+ Variables.CATEGORY_ID + " INTEGER," + Variables.CATEGORY_NAME
				+ " TEXT," + Variables.BOOK_ID + " INTEGER,"
				+ Variables.BOOK_NAME + " TEXT," + Variables.AUTHOR_NAME
				+ " TEXT," + Variables.DOB_PURCHASED + " TEXT,"
				+ Variables.PRICE + " INTEGER," + Variables.BOOK_IMAGE
				+ " BLOB, " + Variables.RATING + " TEXT )";
		try {
			db.execSQL(CREATE_BOOK_TABLE);
			Log.d("Accounts", "Tables created!");
		} catch (Exception ex) {
			Log.d("Accounts",
					"Error in DBHelper.onCreate() : " + ex.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + Variables.TABLE_NAME);
		onCreate(db);
	}

}
