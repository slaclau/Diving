package slaclau.diving.dive;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DiveTimeOutOfBoundsExceptionTest {

	@Test
	void test() {
		var e = new DiveTimeOutOfBoundsException(20, 10);
		assertEquals(e.toString(), "Time (" + Double.valueOf(20d) + ") cannot exceed duration (" + String.valueOf(10d) + ")");
		e = new DiveTimeOutOfBoundsException(-10, 10);
		assertEquals(e.toString(), "Time ("+ String.valueOf(-10d) + ") cannot be negative");
		e = new DiveTimeOutOfBoundsException(5, 10);
		assertEquals(e.toString(), "Dive time out of bounds");
	}

}
