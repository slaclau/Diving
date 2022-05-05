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
