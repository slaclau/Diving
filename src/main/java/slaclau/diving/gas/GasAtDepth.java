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

public class GasAtDepth extends Gas {
	private double depth;
	
	public GasAtDepth() throws GasException {
		super();
		setDepth(0);
	}
	public GasAtDepth (Gas gas, double depth) {
		super(gas);
		setDepth(depth);
	}
	
	public double getDepth() {
		return depth;
	}

	public void setDepth(double depth) {
		this.depth = depth;
	}
	public static double getPressure(double depth) {
		return depth / 10 + getAtmosphericPressure();
	}
	
	
	public double getPressure() {
		return getPressure(depth);
	}
	
	public double getEND() {
		return ( depth + 10 * getAtmosphericPressure() ) * ( 1 - getHeliumFraction() ) - ( 10 * getAtmosphericPressure() );
	}
	
	public double getOxygenPartialPressure() {
		return getOxygenFraction() * getPressure();
	}
	
	public double getNitrogenPartialPressure() {
		return getNitrogenFraction() * getPressure();
	}
	
	public double getHeliumPartialPressure() {
		return getHeliumFraction() * getPressure();
	}
}
