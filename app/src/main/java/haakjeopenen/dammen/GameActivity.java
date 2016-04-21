package haakjeopenen.dammen;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import haakjeopenen.dammen.game.model.Game;
import haakjeopenen.dammen.game.view.SnakeView;


public class GameActivity extends Activity implements Observer{

	private Game game;
	private Controller controller;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        
        this.game = new Game();
        
        this.controller = new Controller(game);
        this.controller.addObserver(this);
        
        SnakeView sv = (SnakeView)this.findViewById(R.id.snakeview);
        sv.setGame(this.game);
        sv.setOnTouchListener(controller);
		sv.setOnGenericMotionListener(controller);

		sv.forceLayout();
		controller.setCellInformation(sv.getCell_size(),sv.getMargin_horizontal(),sv.getMargin_vertical());

        this.controller.start();


    }

    @Override
	public void update(Observable observable, Object data) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Hahaha Wat kan je wel.nl");
		builder.setMessage("Score: "+((Game)data).getScore());
		builder.create().show();
		
	}
    
    

}