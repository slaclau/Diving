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
package slaclau.diving.decompression.model.buhlmann.constants;

import slaclau.diving.decompression.model.buhlmann.CompartmentOutOfRangeException;

public class ZHL16B extends BuhlmannConstants {
	private static final double[] NITROGEN_HALF_TIME = {4.0, 8.0, 12.5, 18.5, 27.0, 38.3, 54.3, 77.0, 109.0, 146.0, 187.0, 239.0, 305.0, 390.0, 498.0, 635.0};
	private static final double[] NITROGEN_A = {1.2599, 1.0000, 0.8618, 0.7562, 0.6667, 0.5600, 0.4947, 0.4500, 
												0.4187, 0.3798, 0.3497, 0.3223, 0.2850, 0.2737, 0.2523, 0.2327};
	private static final double[] NITROGEN_B = {0.5050, 0.6514, 0.7222, 0.7825, 0.8126, 0.8434, 0.8693, 0.8910, 
												0.9092, 0.9222, 0.9319, 0.9403, 0.9477, 0.9544, 0.9602, 0.9653};
	
	private static final double[] HELIUM_HALF_TIME = {1.51, 3.02, 4.72, 6.99, 10.21, 14.48, 20.53, 29.11, 41.20, 45.19, 70.69, 90.34, 115.29, 147.42, 188.24, 240.03};
	private static final double[] HELIUM_A = {1.7424, 1.3830, 1.1919, 1.0458, 0.9220, 0.8205, 0.7305, 0.6502,
											  0.5950, 0.5545, 0.5333, 0.5189, 0.5181, 0.5176, 0.5172, 0.5119};
	private static final double[] HELIUM_B = {0.4245, 0.5747, 0.6527, 0.7223, 0.7582, 0.7957, 0.8279, 0.8553,
											  0.8757, 0.8903, 0.8997, 0.9703, 0.9122, 0.9171, 0.9217, 0.9267};

	public ZHL16B() {
		
	}
	
	@Override
	public int getNumberOfCompartments() {
		return 16;
	}

	@Override
	public double getNitrogenHalfTime(int i) throws CompartmentOutOfRangeException {
		if (i < 0 || i > getNumberOfCompartments() ) throw new CompartmentOutOfRangeException( i, getNumberOfCompartments() );
		return NITROGEN_HALF_TIME[i];
	}

	@Override
	public double getNitrogenA(int i) throws CompartmentOutOfRangeException {
		if (i < 0 || i > getNumberOfCompartments() ) throw new CompartmentOutOfRangeException( i, getNumberOfCompartments() );
		return NITROGEN_A[i];
	}

	@Override
	public double getNitrogenB(int i) throws CompartmentOutOfRangeException {
		if (i < 0 || i > getNumberOfCompartments() ) throw new CompartmentOutOfRangeException( i, getNumberOfCompartments() );
		return NITROGEN_B[i];
	}

	@Override
	public double getHeliumHalfTime(int i) throws CompartmentOutOfRangeException {
		if (i < 0 || i > getNumberOfCompartments() ) throw new CompartmentOutOfRangeException( i, getNumberOfCompartments() );
		return HELIUM_HALF_TIME[i];
	}

	@Override
	public double getHeliumA(int i) throws CompartmentOutOfRangeException {
		if (i < 0 || i > getNumberOfCompartments() ) throw new CompartmentOutOfRangeException( i, getNumberOfCompartments() );
		return HELIUM_A[i];
	}

	@Override
	public double getHeliumB(int i) throws CompartmentOutOfRangeException {
		if (i < 0 || i > getNumberOfCompartments() ) throw new CompartmentOutOfRangeException( i, getNumberOfCompartments() );
		return HELIUM_B[i];
	}

}
