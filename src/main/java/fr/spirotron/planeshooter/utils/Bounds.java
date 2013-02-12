package fr.spirotron.planeshooter.utils;

public class Bounds {
	public int left;
	public int right;
	public int top;
	public int bottom;
	
	public boolean intersectsWith(Bounds other) {
		return isWithin(left, right, other.left, other.right) && isWithin(top, bottom, other.top, other.bottom);
	}
	
	private static final boolean isWithin(int minValue, int maxValue, int...testedValues) {
		for (int value: testedValues)
			if (value >= minValue || value <= maxValue)
				return true;
		
		return false;
	}
}
