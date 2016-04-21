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

import java.util.Observable;
import java.util.Observer;

import haakjeopenen.dammen.R;
import haakjeopenen.dammen.game.model.Damsteen;
import haakjeopenen.dammen.game.model.Game;

public class DamView extends View implements Observer {

    private Game game;

    private float cell_size;
    private float margin_horizontal;
    private float margin_vertical;
    private float cell_spacing;

    //TODO betere sprite referentie en opslag
    private static final int COLOR_DOGE = Color.GREEN;
    private static final int COLOR_WIT = Color.WHITE;
    private static final int COLOR_ZWART = Color.BLACK;

    Bitmap damsteenWit;
    Bitmap damsteenZwart;
    Bitmap doge;

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
        damsteenWit = BitmapFactory.decodeResource(getResources(), R.drawable.wit);
        damsteenZwart = BitmapFactory.decodeResource(getResources(), R.drawable.zwart);
        doge = BitmapFactory.decodeResource(getResources(), R.drawable.slang);
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

        Paint itemPaint = new Paint();
        itemPaint.setColor(COLOR_ZWART);

        for (Damsteen steen : game.getDamstenen()) {
            if (steen.isSelected())
                itemPaint.setColor(COLOR_DOGE);
            else if (steen.getKleur() == Damsteen.Kleur.WIT) {
                itemPaint.setColor(COLOR_WIT);
            } else {
                itemPaint.setColor(COLOR_ZWART);
            }

            drawBlock(canvas, steen.getPoint().x, steen.getPoint().y, itemPaint);
        }
    }

    /**
     * Draws a single block on the canvas
     *
     * @param canvas
     * @param x      The x coordinate as used in Point
     * @param y      The y coordinate as used in Point
     * @param paint
     */
    private void drawBlock(Canvas canvas, int x, int y, Paint paint) {

        float px = margin_horizontal + (x * cell_size);
        float py = margin_vertical + (y * cell_size);

        Rect rect = new Rect((int) (px + cell_spacing), (int) (py + cell_spacing), (int) (px + cell_spacing + cell_size),
                (int) (py + cell_spacing + cell_size));
        Bitmap bitmap;

        switch (paint.getColor()) {
            case COLOR_DOGE:
                bitmap = doge;
                break;
            case COLOR_WIT:
                bitmap = damsteenWit;
                break;
            case COLOR_ZWART:
                bitmap = damsteenZwart;
                break;
            default:
                bitmap = doge;
        }
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
}
