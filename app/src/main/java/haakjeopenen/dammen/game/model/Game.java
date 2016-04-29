package haakjeopenen.dammen.game.model;

import java.util.LinkedList;
import java.util.Observable;

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
    private Damsteen.Kleur winnaar = null;

    private Damsteen.Kleur beurt;
    private final LinkedList<Damsteen> damstenen;

    Point highlight;

    private Damsteen vorigeSteenDieSloeg;

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
            Damsteen.Kleur genereer_kleur;
            if (yoffset == 0)
                genereer_kleur = Damsteen.Kleur.ZWART;
            else
                genereer_kleur = Damsteen.Kleur.WIT;

            // ...have four rows...
            for (int yrow = 0; yrow <= 3; yrow++) {
                // ...with 5 pieces on each...
                for (int x = 0; x <= 8; x += 2) {
                    Point point = new Point(to_int(isEven(yrow)) + x, yoffset + yrow);
                    Damsteen steen = new Damsteen(point, genereer_kleur);
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

        if (this.isGameOver) {
            //throw new IllegalStateException("Can't advance game that is over.");
        }
        else {
            this.setChanged();
            this.notifyObservers();
        }
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

    public void moveDamsteen(Direction dir,int x,int y) {
        //DONE verplicht slaan
        //DONE niet terug kunnen springen
        //DONE achter elkaar slaan
        //DONE je mag niet uit het bord
        //TODO laat zien wat er met een zet gaat gebeuren
        //DONE superdammen fixen
        //DONE als je slaat en je kan met dezelfde steen nogmaals slaan, verplicht slaan met dezelfde steen
        //DONE win condition
        //TODO remise
        //TODO ai maybe?

        for (Damsteen steen : damstenen) {
            if (steen.isSelected()) {
                steen.setSelected(false);

                Point p;
                Damsteen steen2 = null;

                if (steen.isDam())
                {
                    int xoffset = x - steen.getPoint().x;
                    int yoffset = y - steen.getPoint().y;

                    if (Math.abs(xoffset) != Math.abs(yoffset))
                    {
                        System.out.println(Math.abs(xoffset) + " isn't " + Math.abs(yoffset));
                        return;
                    }

                    int diagdirection_x = (xoffset > 0 ? -1 : 1);
                    int diagdirection_y = (yoffset > 0 ? -1 : 1);

                    boolean steenfound = false;

                    for (int diagoffset = Math.abs(xoffset)-1; diagoffset >= 0; diagoffset--)
                    {
                        Point diagoffsetpoint = new Point(x+(diagdirection_x*diagoffset), y+(diagdirection_y*diagoffset));

                        if (getDamsteen(diagoffsetpoint) != null)
                        {
                            if (steenfound)
                                // 2 stenen
                                return;

                            steen2 = getDamsteen(diagoffsetpoint);

                            steenfound = true;
                        }
                    }

                    p = steen.getPoint().toDamDirection(steen, x, y);
                }
                else
                {
                    p = steen.getPoint().toDirection(dir);
                    steen2 = getDamsteen(p);
                }

                if (!isInGameBounds(p)) {return;}

                if (steen2 != null && (vorigeSteenDieSloeg == null || steen.equals(vorigeSteenDieSloeg) )) {
                    //Is van de tegenstander?
                    if (steen2.getKleur() != steen.getKleur()) {
                        //Test of je kan slaan
                        Point p2;
                        if (!steen.isDam())
                            p2 = p.toDirection(dir);
                        else
                            p2 = p;
                        if (getDamsteen(p2) == null && isInGameBounds(p2)) {
                            damstenen.remove(steen2);
                            vorigeSteenDieSloeg = steen;
                            steen.setPoint(p2);
                            if (!kleurKanSlaan(beurt))
                                volgendeBeurt();
                            return;
                        }
                    } else {
                        return;
                    }
                }
                else if (!kleurKanSlaan(beurt) && (steen.isDam() || !isTerug(beurt, steen.getPoint(), p))) {
                    steen.setPoint(p);
                    volgendeBeurt();
                }
            }
        }
    }

    public boolean damsteenMoetSlaan(Damsteen damsteen) {
        for (Direction dir : Direction.values()) {
            Point p = damsteen.getPoint().toDirection(dir);
            Damsteen steen2 = getDamsteen(p);
            if (steen2 != null && steen2.getKleur() != damsteen.getKleur()) {
                Point p2 = p.toDirection(dir);
                if (getDamsteen(p2) == null && isInGameBounds(p2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean kleurKanSlaan(Damsteen.Kleur kleur) {
        for (Damsteen steen : damstenen)
        {
            if (vorigeSteenDieSloeg != null && ! steen.equals(vorigeSteenDieSloeg))continue;
            if ( steen.getKleur().equals(kleur) && damsteenMoetSlaan(steen))
                // Deze steen kan slaan
                return true;
        }
        return false;
    }

    private boolean isTerug(Damsteen.Kleur kleur, Point p1, Point p2)
    {
        if (kleur == Damsteen.Kleur.WIT && p2.y > p1.y)
            return true;
        else if (kleur == Damsteen.Kleur.ZWART && p2.y < p1.y)
            return true;
        return false;
    }

    private boolean isPointFree(Point p) {
        for (Damsteen steen : damstenen) {
            if (steen.getPoint().equals(p))
                return false;
        }
        return true;
    }

    private Damsteen getDamsteen(Point p) {
        for (Damsteen steen : damstenen) {
            if (steen.getPoint().equals(p)) return steen;
        }
        return null;
    }

    public Damsteen getDamsteen(int x, int y) {
        for (Damsteen steen : damstenen) {
            if (steen.getPoint().x == x && steen.getPoint().y == y) return steen;
        }
        return null;
    }

    public Damsteen.Kleur getBeurt() {
        return beurt;
    }

    /**
     * Laat de volgende speler spelen, en bepaal of een van de spelers geen stenen meer heeft
     */
    private void volgendeBeurt() {
        vorigeSteenDieSloeg = null;
        if (beurt == Damsteen.Kleur.WIT)
            beurt = Damsteen.Kleur.ZWART;
        else
            beurt = Damsteen.Kleur.WIT;

        if (checkWin() || checkRemise(beurt))
            isGameOver = true;
    }

    private boolean checkWin() {
        Damsteen.Kleur eerstekleur = null;
        for (Damsteen steen : damstenen) {
            if (eerstekleur == null) {
                // Dit is de eerste steen die we zien
                eerstekleur = steen.getKleur();
            }
            else if (!steen.getKleur().equals(eerstekleur))
            {
                // Dit is een andere steen met een andere kleur, beide spelers hebben dus nog stenen
                return false;
            }
        }

        // Alle stenen hebben dezelfde kleur
        winnaar = eerstekleur;
        return true;
    }

	/**
	 * Kijk of alle stenen van een kleur niet verplaatst kunnen worden
     * @param kleur
     * @return
     */
    private boolean checkRemise(Damsteen.Kleur kleur) {
        for (Damsteen steen : damstenen) {
            if (steen.getKleur() == kleur)
            {
                // Kan deze?
                //TODO check of deze steen kan
                if (false)
                    return true;
            }
        }
        return false;
    }

    public void setHighlight(int x,int y) {
        Point p = new Point(x,y);
        if (this.isInGameBounds(p)) {
            highlight = p;
        } else {
            highlight = null;
        }
    }

    public void unsetHighlight() {
        highlight = null;
    }

    public Point getHighlight() {
        if(highlight != null)
            return highlight;
        else
            return null;
    }

    public boolean hasHighlight() {
        if (highlight == null) { return false; }
        else return true;
    }

    public boolean isGameOver() {
        return this.isGameOver;
    }

    public Damsteen.Kleur getWinnaar() {
        return this.winnaar;
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
