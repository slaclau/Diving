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

@SuppressWarnings("serial")
public class GasException extends Exception {
	private double oxygenFraction;
	private double nitrogenFraction;
	private double heliumFraction;

	public GasException(double oxygenFraction, double nitrogenFraction, double heliumFraction) {
		this.oxygenFraction = oxygenFraction;
		this.nitrogenFraction = nitrogenFraction;
		this.heliumFraction = heliumFraction;
	}
	
	public String toString() {
		String string = "gas exception:";
		if (oxygenFraction < 0) string += " oxygen component is too small,";
		if (nitrogenFraction < 0) string += " nitrogen component is too small,";
		if (heliumFraction < 0) string += " helium component is too small,";
		
		if (oxygenFraction > 1) string += " oxygen component is too large,";
		if (nitrogenFraction > 1) string += " nitrogen component is too large,";
		if (heliumFraction > 1) string += " helium component is too large,";
		
		if (oxygenFraction + nitrogenFraction + heliumFraction != 1) string += " components do not sum to 1";
		return string;
	}
}
