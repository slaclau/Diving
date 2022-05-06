package slaclau.diving.dive;

import java.util.ArrayList;

public class DivePlan {
	ArrayList<Level> levels = new ArrayList<Level>();
	
	public void addLevel(double depth, double time) {
		levels.add( new Level( depth , time ) );
	}
	
	class Level {
		double depth;
		double time;
		
		Level(double depth, double time) {
			this.depth = depth;
			this.time = time;
		}
	}
}
