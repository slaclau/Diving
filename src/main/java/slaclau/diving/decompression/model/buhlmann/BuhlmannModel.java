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
package slaclau.diving.decompression.model.buhlmann;

import slaclau.diving.dive.Dive;

import slaclau.diving.gas.Gas;
import slaclau.diving.gas.GasAtDepth;
import slaclau.diving.gas.GasException;

import static slaclau.diving.gas.Gas.getAtmosphericPressure;
import static slaclau.diving.gas.Gas.getWaterVapourPressure;

import slaclau.diving.decompression.model.ModelledDive;
import slaclau.diving.decompression.model.buhlmann.constants.BuhlmannConstants;

public class BuhlmannModel extends slaclau.diving.decompression.model.ModelledDive {
	protected BuhlmannConstants constants;
	protected double[] nitrogenK;
	protected double[] nitrogenA;
	protected double[] nitrogenB;
	
	protected double[] heliumK;
	protected double[] heliumA;
	protected double[] heliumB;
	
	protected double[] nitrogenLoading = new double[16];
	protected double[] heliumLoading = new double[16];
	private Gas surface;
	
	public double[] getNitrogenLoading() {
		return nitrogenLoading;
	}

	public void setNitrogenLoading(double[] nitrogenLoading) {
		this.nitrogenLoading = nitrogenLoading;
	}

	public double[] getHeliumLoading() {
		return heliumLoading;
	}

	public void setHeliumLoading(double[] heliumLoading) {
		this.heliumLoading = heliumLoading;
	}

	public BuhlmannModel(Dive dive, BuhlmannConstants constants) {
		super(dive);
		this.constants = constants;
		int numberOfCompartments = constants.getNumberOfCompartments();
		
		nitrogenK = new double[numberOfCompartments];
		nitrogenA = new double[numberOfCompartments];
		nitrogenB = new double[numberOfCompartments];
		
		heliumK = new double[numberOfCompartments];
		heliumA = new double[numberOfCompartments];
		heliumB = new double[numberOfCompartments];
		
		for(int i = 0 ; i < numberOfCompartments ; i++) {
			try {
				nitrogenK[i] = Math.log(2) / constants.getNitrogenHalfTime(i);
			} catch (CompartmentOutOfRangeException e) {
				e.printStackTrace();
			}
			try {
				nitrogenA[i] = constants.getNitrogenA(i);
			} catch (CompartmentOutOfRangeException e) {
				e.printStackTrace();
			}
			try {
				nitrogenB[i] = constants.getNitrogenB(i);
			} catch (CompartmentOutOfRangeException e) {
				e.printStackTrace();
			}
			try {
				heliumK[i] = Math.log(2) / constants.getHeliumHalfTime(i);
			} catch (CompartmentOutOfRangeException e) {
				e.printStackTrace();
			}
			try {
				heliumA[i] = constants.getHeliumA(i);
			} catch (CompartmentOutOfRangeException e) {
				e.printStackTrace();
			}
			try {
				heliumB[i] = constants.getHeliumB(i);
			} catch (CompartmentOutOfRangeException e) {
				e.printStackTrace();
			}
		}
		
		
		this.dive = dive;
		try {
			surface = new Gas();
		} catch (GasException e) {
			e.printStackTrace();
		}
		
		double nitrogenFraction = surface.getNitrogenFraction();
		double heliumFraction = surface.getHeliumFraction();
		
		for(int i = 0 ; i < 16 ; i++) {
			nitrogenLoading[i] = nitrogenFraction * (getAtmosphericPressure() - getWaterVapourPressure());
			heliumLoading[i] = heliumFraction * (getAtmosphericPressure() - getWaterVapourPressure());
		}
	}
	
	public BuhlmannModel clone() {
		BuhlmannModel clone = new BuhlmannModel(dive.clone(), constants);
		clone.setDecoGasPlan(getDecoGasPlan().clone());
		clone.setNitrogenLoading(this.getNitrogenLoading().clone());
		clone.setHeliumLoading(this.getHeliumLoading().clone());
		return clone;
	}
	
	private double schreiner(double inspiredPressure, double gasRate, double timeInterval, double k, double initialCompartmentPressure) {
		return inspiredPressure + gasRate * (timeInterval - 1 / k) - (inspiredPressure - initialCompartmentPressure - gasRate / k) * Math.exp(-k * timeInterval);
	}
	
	@Override
	public void descend(double depth, double rate) {
		super.descend(depth, rate);
		GasAtDepth gas = dive.getCurrentPoint();
		double oldDepth = gas.getDepth();
		dive.descend(depth, rate);
		
		double nitrogenFraction = gas.getNitrogenFraction();
		double heliumFraction = gas.getHeliumFraction();
		
		double nitrogenRate = rate / 10 * nitrogenFraction;
		double heliumRate = rate / 10 * heliumFraction;
		
		double timeInterval = ( depth - oldDepth ) / rate;
		
		double nitrogenInspiredPressure = nitrogenFraction * (1 + oldDepth / 10 - getWaterVapourPressure());
		double heliumInspiredPressure = heliumFraction * (1 + oldDepth / 10 - getWaterVapourPressure());
		
		double oldNitrogenLoading;
		double oldHeliumLoading;
		
		for(int i = 0 ; i < 16 ; i++) {
			oldNitrogenLoading = nitrogenLoading[i];
			oldHeliumLoading = heliumLoading[i];
			
			nitrogenLoading[i] = schreiner(nitrogenInspiredPressure, nitrogenRate, timeInterval, nitrogenK[i], oldNitrogenLoading);
			heliumLoading[i] = schreiner(heliumInspiredPressure, heliumRate, timeInterval, heliumK[i], oldHeliumLoading);
		}
	}

	@Override
	public void stay(double timeInterval) {
		super.stay(timeInterval);
		GasAtDepth gas = dive.getCurrentPoint();
		double depth = gas.getDepth();
		dive.stay(timeInterval);
		
		double nitrogenFraction = gas.getNitrogenFraction();
		double heliumFraction = gas.getHeliumFraction();
		
		double nitrogenInspiredPressure = nitrogenFraction * (getAtmosphericPressure() + depth / 10 - getWaterVapourPressure());
		double heliumInspiredPressure = heliumFraction * (getAtmosphericPressure() + depth / 10 - getWaterVapourPressure());
		
		double oldNitrogenLoading;
		double oldHeliumLoading;
		
		for(int i = 0 ; i < 16 ; i++) {
			oldNitrogenLoading = nitrogenLoading[i];
			oldHeliumLoading = heliumLoading[i];
			
			nitrogenLoading[i] = schreiner(nitrogenInspiredPressure, 0, timeInterval, nitrogenK[i], oldNitrogenLoading);
			heliumLoading[i] = schreiner(heliumInspiredPressure, 0, timeInterval, heliumK[i], oldHeliumLoading);
		}

	}

	@Override
	public void ascend(double depth, double rate) {
		super.ascend(depth, rate);
		GasAtDepth gas = dive.getCurrentPoint();
		double oldDepth = gas.getDepth();
		dive.ascend(depth, rate);
		
		double nitrogenFraction = gas.getNitrogenFraction();
		double heliumFraction = gas.getHeliumFraction();
		
		double nitrogenRate = - rate / 10 * nitrogenFraction;
		double heliumRate = - rate / 10 * heliumFraction;
		
		double timeInterval = ( oldDepth - depth ) / rate;
		
		double nitrogenInspiredPressure = nitrogenFraction * (getAtmosphericPressure() + oldDepth / 10 - getWaterVapourPressure());
		double heliumInspiredPressure = heliumFraction * (getAtmosphericPressure() + oldDepth / 10 - getWaterVapourPressure());
		
		double oldNitrogenLoading;
		double oldHeliumLoading;
		
		for(int i = 0 ; i < 16 ; i++) {
			oldNitrogenLoading = nitrogenLoading[i];
			oldHeliumLoading = heliumLoading[i];
			
			nitrogenLoading[i] = schreiner(nitrogenInspiredPressure, nitrogenRate, timeInterval, nitrogenK[i], oldNitrogenLoading);
			heliumLoading[i] = schreiner(heliumInspiredPressure, heliumRate, timeInterval, heliumK[i], oldHeliumLoading);
		}
	}

	@Override
	public void switchGas(Gas gas) {
		dive.switchGas(gas);
	}

	@Override
	public double getCeiling() {
		double compartmentCeiling[] = new double[16];
		double a;
		double b;
		for (int i = 0 ; i < 16 ; i++) {
			a = ( nitrogenA[i] * nitrogenLoading[i] + heliumA[i] * heliumLoading[i] ) / ( nitrogenLoading[i] + heliumLoading[i] );
			b = ( nitrogenB[i] * nitrogenLoading[i] + heliumB[i] * heliumLoading[i] ) / ( nitrogenLoading[i] + heliumLoading[i] );

			compartmentCeiling[i] = 10 * ( ( nitrogenLoading[i] + heliumLoading[i] - a ) * b - 1 );
		}
		
		double ceiling = compartmentCeiling[0];
		for (int i = 0 ; i < 16 ; i++) {
			if ( compartmentCeiling[i] > ceiling ) ceiling = compartmentCeiling[i];
		}
		return ceiling;
	}

	@Override
	public ModelledDive cloneAndReset() {
		BuhlmannModel clone = new BuhlmannModel(dive.cloneAndReset(), constants);
		clone.setDecoGasPlan(getDecoGasPlan().clone());
		return clone;
	}

	@Override
	public double getCurrentGradient() {
		double gradient = getCurrentGradient(0);
		for (int i = 0 ; i < 16 ; i++) {
			if ( getCurrentGradient(i) > gradient ) gradient = getCurrentGradient(i);
		}
		return gradient;
	}
	
	private double getCurrentGradient(int i) {
		double ambientPressure = dive.getCurrentPoint().getPressure();
		double a = ( nitrogenA[i] * nitrogenLoading[i] + heliumA[i] * heliumLoading[i] ) / ( nitrogenLoading[i] + heliumLoading[i] );
		double b = ( nitrogenB[i] * nitrogenLoading[i] + heliumB[i] * heliumLoading[i] ) / ( nitrogenLoading[i] + heliumLoading[i] );
		double mValue = ambientPressure / b + a;
		return ( nitrogenLoading[i] + heliumLoading[i] - ambientPressure ) / ( mValue - ambientPressure );
	}
	
	public double getSurfaceGradient() {
		double gradient = getSurfaceGradient(0);
		for (int i = 0 ; i < 16 ; i++) {
			if ( getSurfaceGradient(i) > gradient ) gradient = getSurfaceGradient(i);
		}
		return gradient;
	}
	
	private double getSurfaceGradient(int i) {
		double surfacePressure = Gas.getAtmosphericPressure();
		double a = ( nitrogenA[i] * nitrogenLoading[i] + heliumA[i] * heliumLoading[i] ) / ( nitrogenLoading[i] + heliumLoading[i] );
		double b = ( nitrogenB[i] * nitrogenLoading[i] + heliumB[i] * heliumLoading[i] ) / ( nitrogenLoading[i] + heliumLoading[i] );
		double mValue = surfacePressure / b + a;
		return ( nitrogenLoading[i] + heliumLoading[i] - surfacePressure ) / ( mValue - surfacePressure );
	}
}
