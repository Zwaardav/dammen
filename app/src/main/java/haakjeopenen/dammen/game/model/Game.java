package haakjeopenen.dammen.game.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Random;

import android.util.Log;

public class Game extends Observable {

	private float cell_size;
	private float margin_horizontal;
	private float margin_vertical;

	/*
	 * Constants for the default dimensions
	 */
	/*private static final int DEFAULT_WIDTH = 9;
	private static final int DEFAULT_HEIGHT = 16;*/
	private static final int DEFAULT_WIDTH = 10;
	private static final int DEFAULT_HEIGHT = 10;

	/*
	 * Constant for the setup procedure.
	 */
	private static final Point SETUP_POINT = new Point(4, 1);
	//private static final Direction SETUP_DIRECTION = Direction.DOWN;
	private static final int SETUP_SNAKE_LENGTH = 4;

	/*
	 * Constant for the score
	 */
	private static final int SCORE_DEFAULT_INCREMENT = 0;

	// Flag indicating whether the game is over.
	private boolean isGameOver = false;

	// The obtained score
	private int score;

	// The current heading
	private Direction direction;

	// The Snake
	private LinkedList<Point> snake;

	// All items that are on screen.
	private Damsteen item;

	private Damsteen.Kleur beurt;

	private LinkedList<Damsteen> damstenen;

	// The dimensions of the game
	private final int width, height;

	// Random number generator
	private final Random random = new Random();

	/**
	 * Creates a new game using the default setup.
	 */
	public Game() {
		this.width = Game.DEFAULT_WIDTH;
		this.height = Game.DEFAULT_HEIGHT;

		this.score = 0;

		this.beurt = Damsteen.Kleur.WIT;

		//this.setupSnake();
		this.damstenen = generateStartBoard();

		//this.item = new Damsteen(this.getRandomFreePoint(), Damsteen.Kleur.ZWART);
	}

	private LinkedList<Damsteen> generateStartBoard() {
		LinkedList<Damsteen> stenen = new LinkedList<>();
		// Both players...
		for (int yoffset = 0; yoffset <= 6; yoffset += 6)
		{
			// ...have four rows...
			for (int yrow = 0; yrow <= 3; yrow++)
			{
				// ...with 5 pieces on each...
				for (int x = 0; x <= 8; x += 2)
				{
					/*  o o o o
					   o o o o
					    o o o o
					   o o o o
					 */
					Damsteen steen = new Damsteen(new Point(((yrow & 1) == 0 ? 1 : 0) + x,yoffset+yrow),(yoffset == 0 ? Damsteen.Kleur.ZWART : Damsteen.Kleur.WIT));
					stenen.add(steen);
				}
			}
		}
		return stenen;
	}

	private void setupSnake() {

		this.snake = new LinkedList<Point>();

		Point curPoint = Game.SETUP_POINT;
		//this.direction = Game.SETUP_DIRECTION;

		for (int i = 0; i < Game.SETUP_SNAKE_LENGTH; ++i) {
			curPoint = curPoint.toDirection(this.direction);
			this.moveSnake(curPoint, true);
		}

	}

	/**
	 * Advances the game one step. This includes moving the snake forward, and
	 * checking whether it has eaten something. In case it has it will grow.
	 * 
	 * checking whether the snake can eat something, and if so growing.
	 */
	public void advance() {

		if (this.isGameOver)
			throw new IllegalStateException("Can't advance game that is over.");

		this.setChanged();
		this.notifyObservers();

	}

	/**
	 * Sets the direction in which the Snake will be going.
	 * 
	 * @param direction
	 */
	public void changeDirection(Direction direction) {
		this.direction = direction;
	}

	/**
	 * Move the snake once. This will make the snake move in its current
	 * direction.
	 * 
	 * @param grow
	 *            Indicates whether the snake must grow one block while moving.
	 */
	private void moveSnake(Point point, boolean grow) {
		if (!grow)
			snake.removeLast();
		snake.addFirst(point);
	}

	/**
	 * Indicates whether the snake can move in the given direction. The snake
	 * won't be able to move in the given direction if the resulting position is
	 * out of game bounds or if it would mean that it bites itself.
	 * 
	 * @param direction
	 * @return
	 */
	private boolean canSnakeMove(Point point, boolean willGrow) {

		Log.d("Game", "Checking whether the snake can move");
		return this.isInGameBounds(point) && this.isNotInSnake(point, willGrow);
	}

	/**
	 * Checks whether the given point is part of the snake
	 * 
	 * @param point
	 *            The point to compare to all other points within the snake.
	 * @param willGrow
	 *            indicates whether the snake will grow, thus making its tail
	 *            important.
	 * @return
	 */
	private boolean isNotInSnake(Point point, boolean willGrow) {
		Iterator<Point> iterator = snake.descendingIterator();

		// If it won't grow, simply skip over the last block as it won't exist
		// when the snake moves.
		if (!willGrow && iterator.hasNext())
			iterator.next();

		while (iterator.hasNext())
			if (iterator.next().equals(point))
				return false;

		return true;
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

	/**
	 * Updates the score, given a Damsteen that has been eaten.
	 * 
	 * @param item
	 *            The Damsteen that has been eaten
	 */
	private void updateScore(Damsteen item) {

		this.score += item.getValue();

	}

	/**
	 * checks whether the given Point is occupied
	 * 
	 * @param point
	 * @return
	 */
	private boolean isPointFree(Point point) {
		return this.isNotInSnake(point, true)
				&& (this.item == null || !this.item.getPoint().equals(point));
	}

	/**
	 * gets a random point in the game that is not occupied.
	 * 
	 * @return
	 */
	public Point getRandomFreePoint() {
		Point p;

		do {
			p = new Point(random.nextInt(width), random.nextInt(height));
		} while (!isPointFree(p));

		return p;
	}

	public void selectDamsteen(int x,int y) {
		for(Damsteen steen: damstenen) {
			if ( x == steen.getPoint().x && y == steen.getPoint().y && this.beurt == steen.getKleur() ) {
				steen.setSelected(true);
			}
		}
	}
	public void moveDamsteen(Direction dir) {
		for(Damsteen steen: damstenen) {
			if (steen.isSelected()) {
				steen.setSelected(false);

				Point p = steen.getPoint().toDirection(dir);
				Damsteen steen2 = getDamsteen(p);
				if (steen2 != null) {
					//Is van de tegenstander?
					if (steen2.getKleur() != steen.getKleur()) {
						//Test of je kan slaan
						Point p2 = p.toDirection(dir);
						if(getDamsteen(p2) == null) {
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
		for(Direction dir: Direction.values()) {
			Point p = damsteen.getPoint().toDirection(dir);
			Damsteen steen2 = getDamsteen(p);
			if(steen2 != null && steen2.getKleur() != damsteen.getKleur()) {
				Point p2 = p.toDirection(dir);
				if(getDamsteen(p2) == null) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isFree(Point p) {
		for (Damsteen steen: damstenen) {
			if (steen.getPoint().equals(p)) return false;
		}
		return false;
	}

	private Damsteen getDamsteen(Point p) {
		for (Damsteen steen: damstenen) {
			if (steen.getPoint().equals(p)) return steen;
		}
		return null;
	}

	public int getScore() {
		return score;
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

	public LinkedList<Point> getSnake() {
		return this.snake;
	}

	public Damsteen getItem() {
		return item;
	}

	public void setCell_size(float cell_size) {
		this.cell_size = cell_size;
	}

	public void setMargin_horizontal(float margin_horizontal) {
		this.margin_horizontal = margin_horizontal;
	}

	public void setMargin_vertical(float margin_vertical) {
		this.margin_vertical = margin_vertical;
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

	public LinkedList<Damsteen> getDamstenen() {
		return damstenen;
	}
}
