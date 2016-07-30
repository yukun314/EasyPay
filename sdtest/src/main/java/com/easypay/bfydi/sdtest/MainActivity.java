package com.easypay.bfydi.sdtest;

import android.app.Activity;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Button button = (Button) findViewById(R.id.activity_main_button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				test();
			}
		});
	}

	private void test(){
		// Create a path where we will place our picture in the user's
		// public pictures directory.  Note that you should be careful about
		// what you place here, since the user often manages these files.  For
		// pictures and other media owned by the application, consider
		// Context.getExternalMediaDir().
		File path = Environment.getExternalStoragePublicDirectory("");
		System.out.println("path:"+path.getAbsolutePath());
		System.out.println("path canWrite:"+path.canWrite()+"   path canRead:"+path.canRead());
		File file = new File(path, "DemoPicture.jpg");
		try {
			// Make sure the Pictures directory exists.
			path.mkdirs();

			// Very simple code to copy a picture from the application's
			// resource into the external file.  Note that this code does
			// no error checking, and assumes the picture is small (does not
			// try to copy it in chunks).  Note that if external storage is
			// not currently mounted this will silently fail.
			InputStream is = getResources().openRawResource(R.raw.icon);
			OutputStream os = new FileOutputStream(file);
			byte[] data = new byte[is.available()];
			is.read(data);
			os.write(data);
			is.close();
			os.close();

			// Tell the media scanner about the new file so that it is
			// immediately available to the user.
			MediaScannerConnection.scanFile(this,
					new String[] { file.toString() }, null,
					new MediaScannerConnection.OnScanCompletedListener() {
						public void onScanCompleted(String path, Uri uri) {
							Log.i("ExternalStorage", "Scanned " + path + ":");
							Log.i("ExternalStorage", "-> uri=" + uri);
						}
					});
		} catch (IOException e) {
			// Unable to create file, likely because external storage is
			// not currently mounted.
			Log.w("ExternalStorage", "Error writing " + file, e);
		}
	}
}
