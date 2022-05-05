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

import slaclau.diving.gas.Gas;
import slaclau.diving.gas.GasException;
import slaclau.diving.gas.Nitrox;

class DecompressionScheduleTest {

	@Test
	void test() {
		DecompressionSchedule ds = new DecompressionSchedule();
		try {
			ds.addStop(10, (new Gas()).atDepth(9));
			ds.addStop(30, (new Nitrox(1).atDepth(6)), "This is oxygen");
			
			assertEquals(ds.getDecoStopTime(0), 10);
			assertEquals(ds.getDecoStopTime(1), 30);

			assertEquals(ds.getDecoDivePoint(0).getDepth(), 9);
			assertEquals(ds.getDecoDivePoint(1).getDepth(), 6);
			
			assertTrue(ds.getDecoDivePoint(0).isEqual(new Gas()));
			assertTrue(ds.getDecoDivePoint(1).isEqual(new Nitrox(1)));
			
			assertEquals(ds.getNote(0),"");
			assertEquals(ds.getNote(1),"This is oxygen");
			
			assertEquals(ds.getNumberOfStops(), 2);
			
			assertEquals(ds.toString(), "Start of decompression:\n" +
					String.valueOf(9d) + " msw for " + String.valueOf(10d) + " minutes on Air\n" +
					String.valueOf(6d) + " msw for " + String.valueOf(30d) + " minutes on Oxygen, This is oxygen\n" +
					"End of decompression");
		} catch (GasException e) {
			e.printStackTrace();
		}
		
	}

}
