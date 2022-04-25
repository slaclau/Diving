package slaclau.diving.gas;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GasTest {

	@Test
	void testDefaultConstructor() {
		try {
			Gas air = new Gas();
			assertEquals(air.getOxygenFraction(), 0.21);
			assertEquals(air.getNitrogenFraction(), 0.79);
			assertEquals(air.getHeliumFraction(), 0.0);
		} catch (GasException e) {
			e.printStackTrace();
		}
	}
	@Test
	void testConstructorWithDoubleArgs() {
		try {
			Gas air = new Gas(0.21, 0.79, 0.0);
			assertEquals(air.getOxygenFraction(), 0.21);
			assertEquals(air.getNitrogenFraction(), 0.79);
			assertEquals(air.getHeliumFraction(), 0.0);
		} catch (GasException e) {
			e.printStackTrace();
		}
	}
	void testConstructorWithGasArgs() {
		try {
			Gas tmx = new Gas(0.1,0.7,.2);
			Gas gas = new Gas(tmx);
			assertEquals(gas.getOxygenFraction(), 0.1);
			assertEquals(gas.getNitrogenFraction(), 0.7);
			assertEquals(gas.getHeliumFraction(), 0.2);
		} catch (GasException e) {
			e.printStackTrace();
		}
	}
}
