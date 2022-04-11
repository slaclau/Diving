package slaclau.diving.decompression.model;

import slaclau.diving.decompression.DecoGasPlan;
import slaclau.diving.dive.Dive;
import slaclau.diving.gas.Gas;

public abstract class ModelledDive implements Cloneable {
	protected Dive dive;
	private static final int STOP_INTERVAL = 3;
	private static final int LAST_STOP = 6;
	private static final double STOP_LENGTH_UNIT = 1;
	private static final double DECO_ASCENT_RATE = 10;
	private static final boolean ACCOUNT_FOR_DECO_ASCENT_TIME = false;
	
	private DecoGasPlan decoGasPlan;
	private slaclau.diving.decompression.model.PulmonaryOxygenToxicityModel pulmonaryModel;
	private slaclau.diving.decompression.model.CNSOxygenToxicityModel cnsModel;

	
	public ModelledDive(Dive dive) {
		this.dive = dive;
		decoGasPlan = new DecoGasPlan();
		pulmonaryModel = new slaclau.diving.decompression.model.PulmonaryOxygenToxicityModel(dive);
		cnsModel = new slaclau.diving.decompression.model.CNSOxygenToxicityModel(dive);
	}
	
	public void setDecoGasPlan(DecoGasPlan decoGasPlan) {
		this.decoGasPlan = decoGasPlan;
	}
	public DecoGasPlan getDecoGasPlan() {
		return decoGasPlan;
	}
	
	// accessor methods for model independent constants
	public static int getStopInterval() {
		return STOP_INTERVAL;
	}
	public static int getLastStop() {
		return LAST_STOP;
	}
	public static double getStopLengthUnit() {
		return STOP_LENGTH_UNIT;
	}
	public static double getDecoAscentRate() {
		return DECO_ASCENT_RATE;
	}
	public static boolean accountForDecoAscentTime() {
		return ACCOUNT_FOR_DECO_ASCENT_TIME;
	}
	
	// abstract methods to be implemented
	public abstract double getCeiling();
	public abstract ModelledDive clone();
	public void descend(double depth, double rate) {
		pulmonaryModel.descend(depth, rate);
		cnsModel.descend(depth, rate);
	}
;	public void stay(double time) {
		pulmonaryModel.stay(time);
		cnsModel.stay(time);
	}
	public void ascend(double depth, double rate) {
		pulmonaryModel.ascend(depth, rate);
		cnsModel.ascend(depth, rate);
	}
	
	// default implementations are defined here and will usually not be overridden
	public void switchGas(Gas gas) {
		dive.switchGas(gas);
	}

	public double getNextStop() {
		double nextStop = getStopInterval() * Math.ceil( getCeiling() / getStopInterval() );
		return nextStop;
	}
	public double getStopLength() {
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
				dive.stay( getStopLengthUnit() );
				stopLength++;
			}
		} else {
			while ( getNextStop() > 0 ) {
				stay( getStopLengthUnit() );
				dive.stay( getStopLengthUnit() );
				stopLength++;
			}
		}
		if ( accountForDecoAscentTime() && stopLength == 0 ) {
			stay( getStopInterval() / getDecoAscentRate() );
			dive.stay( getStopInterval() / getDecoAscentRate() );
		}
		return stopLength * getStopLengthUnit();
	}
	
	public String decompress() {
		String string = "Start of decompression\n";
		double nextStop;
		double stopLength;
		double decoAscentRate = getDecoAscentRate();
		nextStop = getNextStop();
		while ( nextStop >= getLastStop() ) {
			ascend(nextStop, decoAscentRate);
			dive.ascend(nextStop, decoAscentRate);
			stopLength = getStopLength();
			if ( stopLength > 0 ) string += (nextStop + " msw for " + stopLength + " minutes on " + (Gas) dive.getCurrentPoint() + "\n");
			nextStop = getNextStop();
		}
		string += "End of decompression";
		return string;
	}
	
	public String getDecompressionSchedule() {
		ModelledDive clone = (ModelledDive) this.clone();
		return clone.decompress();
	}
	
	public double getOTUs() {
		return pulmonaryModel.getOTUs();
	}
	public double getCNS() {
		return cnsModel.getCNS();
	}
}
