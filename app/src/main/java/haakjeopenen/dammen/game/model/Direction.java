package haakjeopenen.dammen.game.model;

public enum Direction {
	
	NW(-1,-1), NE(1,-1),SW(-1,1),SE(1,1);
	
	final int dx,dy;
	
	private Direction(int dx, int dy)
	{
		this.dx=dx;
		this.dy=dy;
	}
	
	public static Direction between(float f, float g)
	{
		if(f < 0)
			if(g < 0)
				return NW;
			else
				return SW;
		else
			if(g < 0)
				return NE;
			else
				return SE;
	}

}
