package agileblaze.sqlite;

import java.util.ArrayList;
import java.util.HashMap;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
/* Import ZBar Class files */
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CameraTestActivity extends Activity {
	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;
	JSONArray books = null;
	ArrayList<HashMap<String, String>> arraylist;
	ProgressDialog mProgressDialog;
	private TextView scanText;
	Button scanButton, search;

	ImageScanner scanner;
	private boolean barcodeScanned = false;
	private boolean previewing = true;
	private static String kind = "";
	private static String totalitems = "";
	private static final String TAG_CONTACTS = "books#volumes";
	static {
		System.loadLibrary("iconv");
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.barcodescan);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		autoFocusHandler = new Handler();
		mCamera = getCameraInstance();
		scanner = new ImageScanner();
		scanner.setConfig(0, Config.X_DENSITY, 3);
		scanner.setConfig(0, Config.Y_DENSITY, 3);

		mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
		FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
		preview.addView(mPreview);

		scanText = (TextView) findViewById(R.id.scanText);
		scanButton = (Button) findViewById(R.id.ScanButton);
		search = (Button) findViewById(R.id.search);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String temp = scanText.getText().toString();
				if (temp.equals("Scanning...")) {

				}
				new DownloadJSON().execute();
			}
		});
		scanButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (barcodeScanned) {
					barcodeScanned = false;
					scanText.setText("Scanning...");
					mCamera.setPreviewCallback(previewCb);
					mCamera.startPreview();
					previewing = true;
					mCamera.autoFocus(autoFocusCB);
				}
			}
		});
	}

	public void onPause() {
		super.onPause();
		releaseCamera();
	}

	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e) {
		}
		return c;
	}

	private void releaseCamera() {
		if (mCamera != null) {
			previewing = false;
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (previewing)
				mCamera.autoFocus(autoFocusCB);
		}
	};

	PreviewCallback previewCb = new PreviewCallback() {
		@SuppressLint("ResourceAsColor")
		public void onPreviewFrame(byte[] data, Camera camera) {
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();

			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(data);

			int result = scanner.scanImage(barcode);

			if (result != 0) {
				previewing = false;
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();

				SymbolSet syms = scanner.getResults();
				for (Symbol sym : syms) {

					scanText.setText("barcode result " + sym.getData());
					barcodeScanned = true;
				}
			}
		}
	};
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	private class DownloadJSON extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(CameraTestActivity.this);
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			Log.i("Background", "Back");
			try {

				String address = "https://www.googleapis.com/books/v1/volumes?q=isbn:9780070141698";

				JSONParser jParser = new JSONParser();
				Log.i("total B4444444444444444444444",
						"Aeeeeeeeeeeeeeeeeeeeeeee");
				JSONObject urlobj = jParser.getJSONFromUrl(address);
				Log.i("total AAAAAAAAAAAAAAAAAAAAAAAA44444444444444444444444",
						"Beeeeeeeeeeeeeeeeeeeeeeeee");
				kind = urlobj.getString("kind");
				totalitems = urlobj.getString("totalItems");
				Log.i("total items", "" + totalitems);
				JSONArray json_result = urlobj.getJSONArray("items");
				for (int k = 0; k < json_result.length(); k++) {
					JSONObject jsonVolInfo = json_result.getJSONObject(k)
							.getJSONObject("volumeInfo");
					String bTitle = jsonVolInfo.getString("title");
					Log.i("Titleeeeeeeeeeeeeeeeeeee", bTitle);
				}
				Log.i("Background b4444444444444444 Toast", "Back");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			mProgressDialog.dismiss();
		}
	}
}
