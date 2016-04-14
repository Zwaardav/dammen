package haakjeopenen.dammen;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class AsciiDambord extends AppCompatActivity {

	private TextView dambordje;
	private int[][] bordarray;

	private String[] stenentekst = new String[]{".", "o", "x", "O", "X"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ascii_dambord);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
			}
		});

		dambordje = (TextView) findViewById(R.id.dambordje);

		bordarray = new int[10][10];

		initbord();

		displaybordje();
	}

	public void initbord()
	{
		// Both players...
		for (int yoffset = 0; yoffset <= 6; yoffset += 6)
		{
			// ...have four rows...
			for (int yrow = 0; yrow <= 3; yrow++)
			{
				// ...with 5 pieces on each...
				for (int x = 0; x <= 8; x += 2)
				{
					/*  o o o o
					   o o o o
					    o o o o
					   o o o o
					 */
					bordarray[yoffset+yrow][((yrow & 1) == 0 ? 1 : 0) + x] = yoffset == 0 ? 1 : 2;
				}
			}
		}
	}

	public void displaybordje()
	{
		StringBuilder asciibord = new StringBuilder("   A B C D E F G H I J");

		for (int row = 0; row <= 9; row++)
		{
			asciibord.append("\n" + (row == 9 ? "" : " ") + String.valueOf(row+1));

			for (int col = 0; col <= 9; col++)
			{

				asciibord.append(" " + stenentekst[bordarray[row][col]]);
			}
		}

		dambordje.setText(asciibord.toString());
	}
}
