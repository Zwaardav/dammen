package haakjeopenen.dammen.game.model;

public class Damsteen {
	public enum Kleur { ZWART,WIT};
	private Point point;
	private int value;
	private Kleur kleur;



	private boolean selected = false;

	public Damsteen(Point point,Kleur kleur) {
		this.point = point;
		this.value = DEFAULT_VALUE;
		this.kleur = kleur;
	}

	public void setPoint(Point p) {
		point = p;
	}

	public int getValue() {
		return value;
	}

	public Point getPoint() {
		return this.point;
	}

	public Kleur getKleur() { return kleur; }

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public static final int DEFAULT_VALUE = 50;

}
