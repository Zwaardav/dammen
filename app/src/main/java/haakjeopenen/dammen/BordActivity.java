package haakjeopenen.dammen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class BordActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bord);

		/*
		ImageView iv = new ImageView(this);
		iv.setImageResource(R.drawable.zwart);
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.bordimage);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.BELOW, R.id.bordimage);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rl.addView(iv, lp);
		*/
	}
}
