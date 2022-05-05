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
	@Test
	void testConstructorWithGasArgs() {
		try {
			Gas tmx = new Gas(0.1,0.7,0.2);
			Gas gas = new Gas(tmx);
			assertEquals(gas.getOxygenFraction(), 0.1);
			assertEquals(gas.getNitrogenFraction(), 0.7);
			assertEquals(gas.getHeliumFraction(), 0.2);
		} catch (GasException e) {
			e.printStackTrace();
		}
	}
	@Test
	void testConstructorWithDoubleArgsForException() {
		Exception e;
		e = assertThrows(GasException.class, () -> {
			new Gas(1,1,1);
		});
		e = assertThrows(GasException.class, () -> {
			new Gas(-1,1,1);
		});
		e = assertThrows(GasException.class, () -> {
			new Gas(1,-1,1);
		});
		e = assertThrows(GasException.class, () -> {
			new Gas(1,1,-1);
		});
		e = assertThrows(GasException.class, () -> {
			new Gas(2,0,0);
		});
		e = assertThrows(GasException.class, () -> {
			new Gas(0,2,0);
		});
		e = assertThrows(GasException.class, () -> {
			new Gas(0,0,2);
		});
	}
	@Test
	void testGetMOD() {
		try {
			Gas gas = new Gas(1,0,0);
			assertEquals(gas.getMOD(), 6.000000000000001);
		} catch (GasException e) {
			e.printStackTrace();
		}
	}
	@Test
	void testGetMSD() {
		try {
			Gas gas = new Gas(1,0,0);
			assertEquals(gas.getMSD(), 0);
		} catch (GasException e) {
			e.printStackTrace();
		}
		try {
			Gas gas = new Gas(0.1,0.2,0.7);
			assertEquals(gas.getMSD(), 5.999999999999998);
		} catch (GasException e) {
			e.printStackTrace();
		}
	}
	@Test
	void testIsEqual() {
		try {
			Gas gas = new Gas(0.21, 0.79, 0);
			Gas gas2 = new Gas(0.21, 0, 0.79);
			Gas gas3 = new Gas(0, 0.79, 0.21);
			Gas air = new Gas();
			assertTrue(gas.isEqual(air));
			assertFalse(gas2.isEqual(air));
			assertFalse(gas3.isEqual(air));
		} catch (GasException e) {
			e.printStackTrace();
		}
	}
	@Test
	void testAtDepth() {
		try {
			Gas gas = new Gas();
			GasAtDepth gasAtDepth = gas.atDepth(10);
			assertTrue(gas.isEqual(gasAtDepth));
			assertEquals(gasAtDepth.getDepth(),10);
		} catch (GasException e) {
			e.printStackTrace();
		}
	}
	@Test
	void testToString() {
		try {
			Gas oxygen = new Gas(1,0,0), air = new Gas(), EAN40 = new Gas(0.4,0.6,0), trimix = new Gas(0.1,0.2,0.7);
			assertEquals(oxygen.toString(), "Oxygen");
			assertEquals(air.toString(), "Air");
			assertEquals(EAN40.toString(), "EAN40");
			assertEquals(trimix.toString(), "Trimix 10/70");
		} catch (GasException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	void testNitroxAndTrimix() {
		try {
			Gas EAN40 = new Nitrox(0.4);
			Gas trimix = new Trimix(0.1,0.7);
			assertEquals(EAN40.getOxygenFraction(), 0.4);
			assertEquals(EAN40.getNitrogenFraction(), 0.6);
			assertEquals(EAN40.getHeliumFraction(), 0);
			assertEquals(trimix.getOxygenFraction(), 0.1);
			assertEquals(trimix.getNitrogenFraction(), 1 - 0.1 - 0.7);
			assertEquals(trimix.getHeliumFraction(), 0.7);
		} catch (GasException e) {
			e.printStackTrace();
		}
	}
	@Test
	void testException() {
		Exception e;
		e = new GasException(-1,-1,-1);
		assertEquals(e.toString(), "gas exception: oxygen component is too small, nitrogen component is too small, helium component is too small, components do not sum to 1");
		e = new GasException(2,2,2);
		assertEquals(e.toString(), "gas exception: oxygen component is too large, nitrogen component is too large, helium component is too large, components do not sum to 1");
		e = new GasException(1,0,0);
		assertEquals(e.toString(), "gas exception:");
	}
	@Test
	void testGasAtDepth() {
		try {
			var gas = new GasAtDepth();
			assertTrue(gas.isEqual(new Gas()));
			assertEquals(gas.getDepth(),0);
			assertEquals(gas.getOxygenPartialPressure(), Gas.getAtmosphericPressure() * gas.getOxygenFraction() );
			assertEquals(gas.getNitrogenPartialPressure(), Gas.getAtmosphericPressure() * gas.getNitrogenFraction() );
			assertEquals(gas.getHeliumPartialPressure(), Gas.getAtmosphericPressure() * gas.getHeliumFraction() );
			assertEquals(gas.getEND(), 0);
		} catch (GasException e) {
			e.printStackTrace();
		}
	}
}
