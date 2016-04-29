package haakjeopenen.dammen;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import haakjeopenen.dammen.game.model.Damsteen;
import haakjeopenen.dammen.game.model.Game;
import haakjeopenen.dammen.game.view.DamView;


public class GameActivity extends Activity implements Observer{

	Game game;
	boolean ai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		ai = intent.getBooleanExtra("ai", false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        game = new Game(ai);

        Controller controller = new Controller(game);
        controller.addObserver(this);
        
        DamView dv = (DamView)this.findViewById(R.id.damview);
        dv.setGame(game);
        dv.setOnTouchListener(controller);

        controller.start();
    }

    @Override
	public void update(Observable observable, Object data) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Game over");
		builder.setMessage(game.getWinnaar() == Damsteen.Kleur.WIT ? "Wit wint" : "Zwart wint");
		builder.create().show();
		
	}
    
    

}