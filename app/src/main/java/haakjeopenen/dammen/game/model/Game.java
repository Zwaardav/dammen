package haakjeopenen.dammen.game.model;

import android.util.Log;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Random;

public class Game extends Observable {

    /*
     * Constants for the default dimensions
     */
    private static final int DEFAULT_WIDTH = 10;
    private static final int DEFAULT_HEIGHT = 10;
    /*
     * Constant for the setup procedure.
     */

    // The dimensions of the game
    private final int width, height;
    // Cell dimensions
    private float cell_size;
    private float margin_horizontal;
    private float margin_vertical;

    // Flag indicating whether the game is over.
    private boolean isGameOver = false;

    // All items that are on screen.
    private Damsteen item;
    private Damsteen.Kleur beurt;
    private LinkedList<Damsteen> damstenen;

    /**
     * Creates a new game using the default setup.
     */
    public Game() {
        this.width = Game.DEFAULT_WIDTH;
        this.height = Game.DEFAULT_HEIGHT;

        this.beurt = Damsteen.Kleur.WIT;

        this.damstenen = generateStartBoard();

    }

    private LinkedList<Damsteen> generateStartBoard() {
        LinkedList<Damsteen> stenen = new LinkedList<>();
        // Both players...
        for (int yoffset = 0; yoffset <= 6; yoffset += 6) {
            // ...have four rows...
            for (int yrow = 0; yrow <= 3; yrow++) {
                // ...with 5 pieces on each...
                for (int x = 0; x <= 8; x += 2) {
                    //TODO dit leesbaarder maken en uitleggen
                    Point point = new Point(to_int(isEven(yrow)) + x, yoffset + yrow);
                    Damsteen steen = new Damsteen(point, (yoffset == 0 ? Damsteen.Kleur.ZWART : Damsteen.Kleur.WIT));
                    stenen.add(steen);
                }
            }
        }
        return stenen;
    }

    private static int to_int(boolean c) {
        return c ? 1 : 0;
    }

    private static boolean isEven(int n) {
        return (n & 1) == 0;
    }

    /**
     * Advances the game one step. This includes moving the snake forward, and
     * checking whether it has eaten something. In case it has it will grow.
     * <p>
     * checking whether the snake can eat something, and if so growing.
     */
    public void advance() {

        if (this.isGameOver)
            throw new IllegalStateException("Can't advance game that is over.");

        this.setChanged();
        this.notifyObservers();

    }


    /**
     * Indicates whether the given point is within the game bounds.
     *
     * @param point
     * @return
     */
    private boolean isInGameBounds(Point point) {
        return point.x >= 0 && point.x < this.width && point.y >= 0
                && point.y < this.height;
    }

    public void selectDamsteen(int x, int y) {
        for (Damsteen steen : damstenen) {
            if (x == steen.getPoint().x && y == steen.getPoint().y && this.beurt == steen.getKleur()) {
                steen.setSelected(true);
            }
        }
    }

    public void moveDamsteen(Direction dir) {
        //TODO verplicht slaan
        //TODO niet terug kunnen springen
        //TODO achter elkaar slaan
        //TODO je mag niet uit het bord
        //TODO laat zien wat er met een zet gaat gebeuren

        for (Damsteen steen : damstenen) {
            if (steen.isSelected()) {
                steen.setSelected(false);

                Point p = steen.getPoint().toDirection(dir);
                Damsteen steen2 = getDamsteen(p);
                if (steen2 != null) {
                    //Is van de tegenstander?
                    if (steen2.getKleur() != steen.getKleur()) {
                        //Test of je kan slaan
                        Point p2 = p.toDirection(dir);
                        if (getDamsteen(p2) == null) {
                            damstenen.remove(steen2);
                            steen.setPoint(p2);
                            volgendeBeurt();
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    steen.setPoint(p);
                    volgendeBeurt();
                }
            }

        }
    }

    private boolean damsteenMoetSlaan(Damsteen damsteen) {
        for (Direction dir : Direction.values()) {
            Point p = damsteen.getPoint().toDirection(dir);
            Damsteen steen2 = getDamsteen(p);
            if (steen2 != null && steen2.getKleur() != damsteen.getKleur()) {
                Point p2 = p.toDirection(dir);
                if (getDamsteen(p2) == null) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPointFree(Point p) {
        for (Damsteen steen : damstenen) {
            if (steen.getPoint().equals(p)) return false;
        }
        return false;
    }

    private Damsteen getDamsteen(Point p) {
        for (Damsteen steen : damstenen) {
            if (steen.getPoint().equals(p)) return steen;
        }
        return null;
    }

    public Damsteen.Kleur getBeurt() {
        return beurt;
    }

    private void volgendeBeurt() {
        if (beurt == Damsteen.Kleur.WIT)
            beurt = Damsteen.Kleur.ZWART;
        else
            beurt = Damsteen.Kleur.WIT;
    }

    public boolean isGameOver() {
        return this.isGameOver;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public float getCell_size() {
        return cell_size;
    }

    public void setCell_size(float cell_size) {
        this.cell_size = cell_size;
    }

    public float getMargin_horizontal() {
        return margin_horizontal;
    }

    public void setMargin_horizontal(float margin_horizontal) {
        this.margin_horizontal = margin_horizontal;
    }

    public float getMargin_vertical() {
        return margin_vertical;
    }

    public void setMargin_vertical(float margin_vertical) {
        this.margin_vertical = margin_vertical;
    }

    public LinkedList<Damsteen> getDamstenen() {
        return damstenen;
    }
}
