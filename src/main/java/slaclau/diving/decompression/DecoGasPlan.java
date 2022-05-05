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

import java.util.HashMap;
import java.util.TreeSet;

import slaclau.diving.gas.Gas;

public class DecoGasPlan implements Cloneable {
	private HashMap<Double,Gas> gasSwitchPoints;
	private TreeSet<Double> depths;
	
	public void setGasSwitchPoints(HashMap<Double, Gas> gasSwitchPoints) {
		this.gasSwitchPoints = gasSwitchPoints;
	}

	public void setDepths(TreeSet<Double> depths) {
		this.depths = depths;
	}
	public HashMap<Double, Gas> getGasSwitchPoints() {
		return gasSwitchPoints;
	}

	public TreeSet<Double> getDepths() {
		return depths;
	}
	public DecoGasPlan() {
		gasSwitchPoints = new HashMap<Double,Gas>();
		depths = new TreeSet<Double>();
	}
	
	@SuppressWarnings("unchecked")
	public DecoGasPlan clone() {
		DecoGasPlan clone = new DecoGasPlan();
		clone.setGasSwitchPoints((HashMap<Double, Gas>) getGasSwitchPoints().clone());
		clone.setDepths((TreeSet<Double>) getDepths().clone());
		return clone;
	}
	
	public void addDecoGas(double depth, Gas gas) {
		gasSwitchPoints.put(depth, gas);
		depths.add(depth);
	}
	
	public Gas getCorrectGas(double depth) {
		Gas gas = null;
		Double d = depths.ceiling(depth);
		if ( d != null) gas = gasSwitchPoints.get(d);
		return gas;
	}
	public Gas[] getDecoGases() {
		Gas[] gases = new Gas[gasSwitchPoints.size()];
		int i = 0;
		for ( Gas gas : gasSwitchPoints.values() ) {
			gases[i] = gas;
			i++;
			
		}
		return gases;
	}
}
