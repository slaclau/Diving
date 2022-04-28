package slaclau.diving.decompression;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import slaclau.diving.gas.GasException;
import slaclau.diving.gas.Nitrox;

class DecoGasPlanTest {

	@Test
	void test() {
		DecoGasPlan dgp = new DecoGasPlan();
		try {
			dgp.addDecoGas(6, new Nitrox(1));
			dgp.addDecoGas(12, new Nitrox(.5));
			
			assertTrue(dgp.getCorrectGas(3).isEqual(new Nitrox(1)));
			assertTrue(dgp.getCorrectGas(6).isEqual(new Nitrox(1)));
			assertTrue(dgp.getCorrectGas(9).isEqual(new Nitrox(0.5)));
			assertTrue(dgp.getCorrectGas(12).isEqual(new Nitrox(0.5)));
			assertNull(dgp.getCorrectGas(15));
			
			assertEquals(dgp.clone().getDepths(), dgp.getDepths());
			assertEquals(dgp.clone().getGasSwitchPoints(), dgp.getGasSwitchPoints());
			
			assertTrue(dgp.getDecoGases()[0].isEqual(new Nitrox(1)));
			assertTrue(dgp.getDecoGases()[1].isEqual(new Nitrox(0.5)));

		} catch (GasException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
