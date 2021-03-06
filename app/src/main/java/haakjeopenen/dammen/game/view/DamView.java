package haakjeopenen.dammen.game.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import haakjeopenen.dammen.R;
import haakjeopenen.dammen.game.model.Damsteen;
import haakjeopenen.dammen.game.model.Game;
import haakjeopenen.dammen.game.model.Point;

public class DamView extends View implements Observer {

    private Game game;

    private float cell_size;
    private float margin_horizontal;
    private float margin_vertical;
    private float cell_spacing;

    private HashMap<Sprite, Bitmap> sprites;

    public DamView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DamView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DamView(Context context) {
        super(context);
        init();
    }

    private void init() {

        //Load sprites
        sprites = new HashMap();

        for (Sprite s : Sprite.values()) {
            sprites.put(s, BitmapFactory.decodeResource(getResources(), s.id));
        }
    }


    public void setGame(Game game) {

        if (this.game != null)
            this.game.deleteObserver(this);
        this.game = game;
        this.game.addObserver(this);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (game == null)
            return;

        // calculate the dimensions
        cell_size = Math.min(w / game.getWidth(), h / game.getHeight());

        // Spacing will be 2 percent of the width, with a minimum of 1px
        cell_spacing = 0;

        // calculate the required margin
        margin_horizontal = (w - (cell_size * game.getWidth())) / 2;
        margin_vertical = (h - (cell_size * game.getHeight())) / 2;

        game.setCell_size(cell_size);
        game.setMargin_horizontal(margin_horizontal);
        game.setMargin_vertical(margin_vertical);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.drawBackground(canvas);

        if (game == null)
            return;

        this.drawDamstenen(canvas);

        if(game.hasHighlight() ) {
            this.drawBlock(canvas,game.getHighlight(),Sprite.HIGHLIGHT);
        }

        this.drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setFakeBoldText(true);
        p.setTextSize(50);
        canvas.drawText(String.valueOf(game.getBeurt()), 8, 50, p);
    }

    private void drawBackground(Canvas canvas) {
        // Make it 'black'
        canvas.drawARGB(255, 0, 0, 0);

        boolean toggle = false;
        Paint paint = new Paint();

        for (float height = 0; height < game.getHeight(); height++) {
            for (float width = 0; width < game.getWidth(); width++) {
                if (toggle)
                    paint.setColor(Color.rgb(101, 67, 33));
                else
                    paint.setColor(Color.rgb(244, 164, 96));

                canvas.drawRect(width * cell_size + margin_horizontal, height * cell_size + margin_vertical,
                        (width + 1) * cell_size + margin_horizontal, (height + 1) * cell_size + margin_vertical, paint);
                toggle = !toggle;
            }
            // Bord van 10x10
            toggle = !toggle;
        }
    }

    /**
     * Draws all items on the canvas
     *
     * @param canvas
     */
    private void drawDamstenen(Canvas canvas) {

        Sprite sprite;

        for (Damsteen steen : game.getDamstenen()) {
            if (steen.getKleur() == Damsteen.Kleur.WIT) {
                if (steen.isDam())
                    sprite = (steen.isSelected() ? Sprite.WIT_DAM_SELECTED : Sprite.WIT_DAM);
                else
                    sprite = (steen.isSelected() ? Sprite.WIT_SELECTED : Sprite.WIT);
            } else {
                if (steen.isDam())
                    sprite = (steen.isSelected() ? Sprite.ZWART_DAM_SELECTED : Sprite.ZWART_DAM);
                else
                    sprite = (steen.isSelected() ? Sprite.ZWART_SELECTED : Sprite.ZWART);
            }

            //if(game.getBeurt() == steen.getKleur() && game.damsteenMoetSlaan(steen)) sprite = Sprite.DOGE;

            drawBlock(canvas, steen.getPoint(), sprite);
        }
    }

    /**
     * Draws a single block on the canvas
     *
     * @param canvas
     * @param point
     * @param sprite
     */
    private void drawBlock(Canvas canvas, Point point, Sprite sprite) {

        Paint paint = new Paint();

        float px = margin_horizontal + (point.x * cell_size);
        float py = margin_vertical + (point.y * cell_size);

        Rect rect = new Rect((int) (px + cell_spacing), (int) (py + cell_spacing), (int) (px + cell_spacing + cell_size),
                (int) (py + cell_spacing + cell_size));

        Bitmap bitmap = sprites.get(sprite);

        canvas.drawBitmap(bitmap, null, rect, paint);
    }

    @Override
    public void update(Observable observable, Object data) {
        this.postInvalidate();
    }

    public float getCell_size() {
        return cell_size;
    }

    public float getMargin_horizontal() {
        return margin_horizontal;
    }

    public float getMargin_vertical() {
        return margin_vertical;
    }

    public enum Sprite {
        WIT(R.drawable.wit),
        WIT_DAM(R.drawable.wit_dam),
        WIT_SELECTED(R.drawable.wit_selected),
        WIT_DAM_SELECTED(R.drawable.wit_dam_selected),
        ZWART(R.drawable.zwart),
        ZWART_DAM(R.drawable.zwart_dam),
        ZWART_SELECTED(R.drawable.zwart_selected),
        ZWART_DAM_SELECTED(R.drawable.zwart_dam_selected),

        HIGHLIGHT(R.drawable.highlight),

        DOGE(R.drawable.slang);

        public final int id;

        Sprite(int id) {
            this.id = id;
        }
    }

}
