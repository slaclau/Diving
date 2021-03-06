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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import slaclau.diving.decompression.DecoGasPlan;
import slaclau.diving.decompression.DecompressionSchedule;
import slaclau.diving.dive.Dive;
import slaclau.diving.dive.DiveTimeOutOfBoundsException;
import slaclau.diving.gas.Gas;
import slaclau.diving.gas.GasAtDepth;

public abstract class ModelledDive implements Dive, Cloneable {
	protected Dive dive;
	private static final int STOP_INTERVAL = 3;
	private static final int LAST_STOP = 6;
	private static final double STOP_LENGTH_UNIT = 1;
	private static final double DESCENT_RATE = 20;
	private static final double ASCENT_RATE = 10;
	private static final double DECO_ASCENT_RATE = 10;
	private static final boolean ACCOUNT_FOR_DECO_ASCENT_TIME = false;
	private static final int MAX_STOP_LENGTH = 1000;
	
	private DecoGasPlan decoGasPlan;
	private PulmonaryOxygenToxicityModel pulmonaryModel;
	private CNSOxygenToxicityModel cnsModel;
	private List<Gas> decoGases;
	
	private AccessoryModel<?>[] accessoryModels;
	
	public ModelledDive(Dive dive) {
		this.dive = dive;
		pulmonaryModel = new PulmonaryOxygenToxicityModel(dive);
		cnsModel = new CNSOxygenToxicityModel(dive);
		setDecoGasPlan(new DecoGasPlan());
	}
	
	public void setDecoGasPlan(DecoGasPlan decoGasPlan) {
		this.decoGasPlan = decoGasPlan;
		
		decoGases = Arrays.asList( getDecoGasPlan().getDecoGases() );
		accessoryModels = new AccessoryModel<?>[decoGases.size() + 2];
		int i = 0;
		for ( Gas gas : decoGases ) {
			accessoryModels[i] = new GasConsumptionModel(dive, gas);
			i++;
		}
		accessoryModels[i] = pulmonaryModel;
		accessoryModels[i+1] = cnsModel;
	}
	public DecoGasPlan getDecoGasPlan() {
		return decoGasPlan;
	}
	
	// accessor methods for constants
	public static int getStopInterval() {
		return STOP_INTERVAL;
	}
	public static int getLastStop() {
		return LAST_STOP;
	}
	public static double getStopLengthUnit() {
		return STOP_LENGTH_UNIT;
	}
	public double getDescentRate() {
		return DESCENT_RATE;
	}
	public double getAscentRate() {
		return ASCENT_RATE;
	}
	public static double getDecoAscentRate() {
		return DECO_ASCENT_RATE;
	}
	public static boolean accountForDecoAscentTime() {
		return ACCOUNT_FOR_DECO_ASCENT_TIME;
	}
	
	// abstract methods to be implemented
	public abstract ModelledDive cloneAndReset();
	public abstract double getCeiling();
	public abstract ModelledDive clone();
	public void descend(double depth, double rate) {
		for (AccessoryModel<?> m : accessoryModels ) m.descend(depth, rate);
	}
;	public void stay(double time) {
	for (AccessoryModel<?> m : accessoryModels )
		try {
			m.stay(time);
		} catch (ModelException e) {
			// TODO
			e.printStackTrace();
		}

	}
	public void ascend(double depth, double rate) {
		for (AccessoryModel<?> m : accessoryModels ) m.ascend(depth, rate);

	}
	
	// default implementations are defined here and will usually not be overridden
	public void switchGas(Gas gas) {
		dive.switchGas(gas);
	}

	public double getNextStop() {
		double nextStop = getStopInterval() * Math.ceil( getCeiling() / getStopInterval() );
		return nextStop;
	}
	public double getStopLength() throws StopLengthException {
		int stopLength = 0;
		double currentStop = dive.getCurrentPoint().getDepth();
		Gas gas = decoGasPlan.getCorrectGas( currentStop );
		if ( gas != null ) {
			switchGas( gas );
		}
		
		if ( accountForDecoAscentTime() ) {
			stay( - getStopInterval() / getDecoAscentRate() );
			dive.stay( - getStopInterval() / getDecoAscentRate() );
		}
		if ( currentStop > getLastStop() ) {
			while ( getNextStop() == currentStop ) {
				stay( getStopLengthUnit() );
				stopLength++;
				if ( stopLength > getMaxStopLength() ) throw new StopLengthException();
			}
		} else {
			while ( getNextStop() > 0 ) {
				stay( getStopLengthUnit() );
				stopLength++;
				if ( stopLength > getMaxStopLength() ) throw new StopLengthException();
			}
		}
		if ( accountForDecoAscentTime() && stopLength == 0 ) {
			stay( getStopInterval() / getDecoAscentRate() );
		}
		return stopLength * getStopLengthUnit();
	}
	
	public DecompressionSchedule decompress() throws StopLengthException {
		DecompressionSchedule schedule = new DecompressionSchedule();
		double nextStop;
		double stopLength;
		double decoAscentRate = getDecoAscentRate();
		nextStop = getNextStop();
		while ( nextStop >= getLastStop() ) {
			ascend(nextStop, decoAscentRate);
			dive.ascend(nextStop, decoAscentRate);
			stopLength = getStopLength();
			schedule.addStop(stopLength, dive.getCurrentPoint());
		}
		ascend(0, decoAscentRate);
		return schedule;
	}
	
	public DecompressionSchedule getDecompressionSchedule() throws StopLengthException {
		ModelledDive clone = (ModelledDive) this.clone();
		return clone.decompress();
	}
	
	public double getOTUs() {
		return pulmonaryModel.get();
	}
	public double getCNS() {
		return cnsModel.get();
	}
	public String getfOTUs() {
		return pulmonaryModel.getf();
	}
	public String getfCNS() {
		return cnsModel.getf();
	}
	public double getGasConsumed(Gas gas) {
		int i = -1 ,j = 0;
		for (Gas decoGas : decoGases ) {
			if ( decoGas.isEqual(gas) ) i = j;
			j++;
		}
		if ( i < 0 ) return 0;
		else return (double) accessoryModels[i].get();
	}
	public String getfGasConsumed(Gas gas) {
		int i = -1 ,j = 0;
		for (Gas decoGas : decoGases ) {
			if ( decoGas.isEqual(gas) ) i = j;
			j++;
		}
		if ( i < 0 ) return "0 l";
		else return accessoryModels[i].getf();
	}
	public class DataRecord<T> {
		String label;
		T value;
		
		DataRecord(String label, T value) {
			this.label = label;
			this.value = value;
		}

		public String getLabel() {
			return label;
		}

		public T getValue() {
			return value;
		}
	}
	
	public ArrayList<DataRecord<Double>> getGasConsumed() {
		ArrayList<DataRecord<Double>> list = new ArrayList<DataRecord<Double>>();
		
		for (Gas decoGas : decoGases ) {
			list.add(new DataRecord<Double>(decoGas.toString() + " consumed is", getGasConsumed(decoGas) ) );
		}
		return list;
	}
	public ArrayList<DataRecord<String>> getfGasConsumed() {
		ArrayList<DataRecord<String>> list = new ArrayList<DataRecord<String>>();
		
		for (Gas decoGas : decoGases ) {
			list.add(new DataRecord<String>(decoGas.toString() + " consumed is", getfGasConsumed(decoGas) ) );
		}
		return list;
	}
	public ArrayList<DataRecord<Double>> getExtraInfo() {
		ArrayList<DataRecord<Double>> list = new ArrayList<DataRecord<Double>>();
		
		list.add(new DataRecord<Double>("CPTD is", getOTUs() ) );
		list.add(new DataRecord<Double>("CNS toxicity (%) is", getCNS() ) );
		list.addAll(getGasConsumed() );
		return list;
	}
	public ArrayList<DataRecord<String>> getfExtraInfo() {
		ArrayList<DataRecord<String>> list = new ArrayList<DataRecord<String>>();
		
		list.add(new DataRecord<String>("CPTD is", getfOTUs() ) );
		list.add(new DataRecord<String>("CNS toxicity (%) is", getfCNS() ) );
		list.addAll(getfGasConsumed() );
		return list;
	}

	public double getTime() {
		return dive.getTime();
	}
	public GasAtDepth getCurrentPoint() {
		return dive.getCurrentPoint();
	}
	public GasAtDepth getPoint(double time) throws DiveTimeOutOfBoundsException {
		return dive.getPoint(time);
	}
	public void goTo(double time, GasAtDepth gas) {
		double currentTime = getTime();
		GasAtDepth currentGas = dive.getCurrentPoint();
		double currentDepth = currentGas.getDepth();
		double depth = gas.getDepth();
		
		double timeChange = time - currentTime;
		if ( timeChange < 0 ) throw new IllegalArgumentException();
		
		if ( !gas.isEqual(currentGas) ) switchGas(gas);
		double rate = Math.abs( ( depth - currentDepth ) / timeChange );
		if ( depth == currentDepth ) stay(timeChange);
		else if ( depth > currentDepth ) descend(depth, rate);
		else ascend(depth, rate);
	}
	public int getNumberOfPoints() {
		return dive.getNumberOfPoints();
	}
	public GasAtDepth getPoint(int i) {
		return dive.getPoint(i);
	}
	public double getTime(int i) {
		return dive.getTime(i);
	}
	public abstract double getCurrentGradient();
	public double getActualCeiling() {
		return getCeiling();
	}

	public static int getMaxStopLength() {
		return MAX_STOP_LENGTH;
	}
}
