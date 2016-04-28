package haakjeopenen.dammen.game.model;

public class Damsteen {
    public enum Kleur {ZWART, WIT}

    private Point point; // Location on the board
    private final Kleur kleur;

    private boolean isDam;

    private boolean selected = false;

    public Damsteen(Point point, Kleur kleur) {
        this.point = point;
        this.kleur = kleur;
    }

    public boolean isDam() {
        return isDam;
    }

    public void setDam(boolean dam) {
        isDam = dam;
    }

    public void setPoint(Point p) {
        point = p;
        if ((kleur == Kleur.WIT && p.y == 0) || (kleur == Kleur.ZWART && p.y == 9))
            setDam(true);
    }

    public Point getPoint() {
        return this.point;
    }

    public Kleur getKleur() {
        return kleur;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
