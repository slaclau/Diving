package slaclau.diving.dive;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import slaclau.diving.gas.Gas;
import slaclau.diving.gas.GasAtDepth;
import slaclau.diving.gas.GasException;
import slaclau.diving.gas.Nitrox;

class SimpleDiveTest {

	@Test
	void testConstructors() {
		try {
			var dive = new SimpleDive(new Gas());
			assertTrue(dive.getCurrentPoint().isEqual(new Gas()));
		} catch (GasException e) {
			e.printStackTrace();
		}
	}
	@Test
	void testGetSetAndSomeMore() {
		try {
		ArrayList<Double> times = new ArrayList<Double>( Arrays.asList(0d, 1d,2d,3d) );
		ArrayList<GasAtDepth> divePoints = new ArrayList<GasAtDepth>( 
				Arrays.asList( new GasAtDepth( new Gas() , 0 ), 
						new GasAtDepth( new Gas() , 10 ),
						new GasAtDepth( new Nitrox(0.4), 10),
						new GasAtDepth( new Nitrox(0.4), 20) ) );
		var dive = new SimpleDive(new Gas() );
		dive.setTimes(times);
		dive.setDivePoints(divePoints);
		assertEquals(dive.getTimes(), times);
		assertEquals(dive.getDivePoints(), divePoints);
		
		assertEquals(dive.getNumberOfPoints(), 4);
		for ( int i = 0 ; i < 4 ; i++ ) {
			assertEquals(dive.getTime(i), times.get(i) );
			assertEquals(dive.getPoint(i), divePoints.get(i) );
		}
		assertEquals(dive.getTime(), times.get(3) );
		
		var newDive = dive.clone();
		assertEquals(newDive.getTimes(), times);
		assertEquals(newDive.getDivePoints(), divePoints);
		newDive = (SimpleDive) dive.cloneAndReset();
		assertEquals(newDive.getTime(), dive.getInitialTime() );
		assertEquals(newDive.getCurrentPoint(), dive.getInitialPoint() );

		} catch (GasException e) {
			e.printStackTrace();
		}
	}
}
