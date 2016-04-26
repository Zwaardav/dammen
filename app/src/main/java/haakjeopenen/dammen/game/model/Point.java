package haakjeopenen.dammen.game.model;


public class Point{

	public final int x,y;
	
	public Point(int x, int y) {
		
		this.x=x;
		this.y=y;
	}
	
	public Point toDirection(Direction direction)
	{
		return new Point(this.x+direction.dx, this.y+direction.dy);
	}

	public String toString()
	{
		return "("+x+","+y+")";
	}
	@Override
	public boolean equals(Object o) {
		if(o instanceof Point)
		{
			Point p = (Point)o;			
			return this.x == p.x && this.y == p.y;
		}
		else
			return false;
	}
	

}
