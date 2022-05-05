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
package slaclau.diving.decompression.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import slaclau.diving.dive.Dive;
import slaclau.diving.dive.SimpleDive;
import slaclau.diving.gas.Gas;
import slaclau.diving.gas.GasException;
import slaclau.diving.gas.Nitrox;

class CNSOxygenToxicityModelTest {

	@Test
	void test() {
		Dive dive;
		try {
			dive = new SimpleDive(new Gas());
			var cnsModel = new CNSOxygenToxicityModel(dive);
			cnsModel.descend(40,10);
			dive.descend(40,10);
			
			assertDiffersAtMostOne( cnsModel.get() , 1 );
			
			cnsModel.stay(20);
			dive.stay(20);

			assertDiffersAtMostOne( cnsModel.get() , 8 );
			
			cnsModel.ascend(10,10);
			dive.ascend(10,10);
			
			assertDiffersAtMostOne( cnsModel.get() , 9 );
			
			double[] limits = { 9, 9, 9, 9, 9, 10, 11, 13, 15, 18, 21, 26, 30, 36, 43, 52, 79 };   
			
			for ( int i = 0 ; i <= 17 ; i++ ) {
				dive.switchGas( new Nitrox( ( (double) i * 5 ) / 100d ) );
				cnsModel.stay(10);
				dive.stay(10);
				if (dive.getCurrentPoint().getOxygenPartialPressure() >= 1.6 ) assertThrows(CNSException.class, () -> cnsModel.stay(10));
				else assertDiffersAtMostOne( cnsModel.get() , limits[i] );
			}
			
		} catch (GasException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CNSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void assertDiffersAtMostOne(double a, double b) {
		assertTrue( a - b < 1 && b - a < 1);
	}

}
