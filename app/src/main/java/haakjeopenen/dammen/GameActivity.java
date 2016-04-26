package haakjeopenen.dammen;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

import haakjeopenen.dammen.game.model.Game;
import haakjeopenen.dammen.game.view.DamView;


public class GameActivity extends Activity implements Observer{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Game game = new Game();

        Controller controller = new Controller(game);
        controller.addObserver(this);
        
        DamView dv = (DamView)this.findViewById(R.id.snakeview);
        dv.setGame(game);
        dv.setOnTouchListener(controller);

        controller.start();
    }

    @Override
	public void update(Observable observable, Object data) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Hahaha Wat kan je wel.nl");
		builder.setMessage("");
		builder.create().show();
		
	}
    
    

}