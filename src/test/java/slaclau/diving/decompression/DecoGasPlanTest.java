/* Copyright (C) 2022 Sebastien Laclau
   
   This file belongs to the Diving project.
   
   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
   
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   
   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>. */
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
