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

import slaclau.diving.dive.Dive;
import slaclau.diving.gas.Gas;
import slaclau.diving.gas.GasAtDepth;

public class GasConsumptionModel implements AccessoryModel<Double> {
	private Dive dive;
	private Gas gas;
	private double gasConsumed;
	
	private double getSAC() {
		return 20;
	}
	
	public GasConsumptionModel(Dive dive, Gas gas) {
		this.dive = dive;
		this.gas = gas;
	}

	@Override
	public Double get() {
		return gasConsumed;
	}
	public String getf() {
		return Math.round( get() * 10d ) / 10d + " l";
	}

	@Override
	public void descend(double depth, double rate) {
		GasAtDepth gas = dive.getCurrentPoint();
		if( this.gas.isEqual(gas) ) {
			double oldDepth = gas.getDepth();
			double time = ( depth - oldDepth ) /rate;
			gasConsumed += GasAtDepth.getPressure( ( depth + oldDepth ) / 2 ) / GasAtDepth.getAtmosphericPressure() * getSAC() * time;
		}
	}

	@Override
	public void stay(double time) {
		GasAtDepth gas = dive.getCurrentPoint();
		if( this.gas.isEqual(gas) ) gasConsumed += gas.getPressure() / GasAtDepth.getAtmosphericPressure() * getSAC() * time;
	}

	@Override
	public void ascend(double depth, double rate) {
		GasAtDepth gas = dive.getCurrentPoint();
		if( this.gas.isEqual(gas) ) {
			double oldDepth = gas.getDepth();
			double time = ( oldDepth - depth ) /rate;
			gasConsumed += GasAtDepth.getPressure( ( depth + oldDepth ) / 2 ) / GasAtDepth.getAtmosphericPressure() * getSAC() * time;
		}
	}

	public Gas getGas() {
		return gas;
	}

	public void setGas(Gas gas) {
		this.gas = gas;
	}

}
