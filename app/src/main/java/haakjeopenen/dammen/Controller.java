package haakjeopenen.dammen;

import java.util.Observable;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import haakjeopenen.dammen.game.model.Direction;
import haakjeopenen.dammen.game.model.Game;

public class Controller extends Observable implements OnTouchListener {

    private float startx, starty;

    private final Game game;

    private Handler handler;

    private float cell_size;
    private float margin_horizontal;
    private float margin_vertical;

    /**
     * The time between steps in milliseconds
     */
    private static final long INTERVAL = 0;

    private final Runnable runner = new Runnable() {

        @Override
        public void run() {

            // perform a step
            performStep();

            // do this again
            if (!Controller.this.game.isGameOver())
                synchronized (Controller.this.handler) {
                    Controller.this.handler.postDelayed(runner, INTERVAL);
                }
            else
                Controller.this.finishGame();

        }

    };

    Controller(Game game) {
        this.game = game;
        this.handler = new Handler();
        this.resetTouchLocation();
        this.cell_size = 0;
        this.margin_vertical = 0;
        this.margin_horizontal = 0;
    }

    /**
     * Called to end the game.
     */
    public void finishGame() {

        this.setChanged();
        this.notifyObservers(game);

    }

    private void resetTouchLocation() {
        this.startx = -1;
        this.starty = -1;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        //Log.d("Controller","Touched");
        int x = (int) ((event.getX() - margin_horizontal) / cell_size);
        int y = (int) ((event.getY() - margin_vertical) / cell_size);

        game.setHighlight(x,y);


        if (startx == -1 || starty == -1) {

            startx = event.getX();
            starty = event.getY();

            cell_size = game.getCell_size();
            margin_horizontal = game.getMargin_horizontal();
            margin_vertical = game.getMargin_vertical();

            game.selectDamsteen(x, y);
        }


        if (event.getAction() == MotionEvent.ACTION_UP) {
            //Log.d("Controller"," " + Direction.between(event.getX()-startx, event.getY()-starty));

            game.moveDamsteen(Direction.between(event.getX() - startx, event.getY() - starty),x,y);
            game.unsetHighlight();
            this.resetTouchLocation();

        }


        return true;
    }


    /**
     * Let's the game advance.
     */
    private void performStep() {

        game.advance();
        if (game.isGameOver()) {
            Log.d("Controller", "Game is over");
            this.reset();
        }
    }

    /**
     * Starts or continues the game
     */
    public void start() {
        synchronized (this.handler) {
            this.handler.postDelayed(runner, INTERVAL);
        }
    }

    /**
     * Pauses the game
     */
    public void pause() {
        synchronized (this.handler) {
            this.handler.removeCallbacks(runner);
        }
    }

    /**
     *
     */
    private void reset() {
        synchronized (this.handler) {
            this.handler.removeCallbacks(runner);
        }

    }

    public void setCellInformation(float cell_size, float margin_horizontal, float margin_vertical) {
        this.cell_size = cell_size;
        this.margin_horizontal = margin_horizontal;
        this.margin_vertical = margin_vertical;
        Log.d("Controller", cell_size + " " + margin_horizontal + " " + margin_vertical);
    }

}
