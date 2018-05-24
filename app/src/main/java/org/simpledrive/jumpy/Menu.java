package org.simpledrive.jumpy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Menu extends Activity {

	RelativeLayout start;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);

		start = (RelativeLayout) findViewById(R.id.menu);
		start.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent("org.simpledrive.jumpy.GAME"));
			}
		});
	}
}
