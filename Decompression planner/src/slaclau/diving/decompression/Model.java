package slaclau.diving.decompression;

import slaclau.diving.dive.Dive;
import slaclau.diving.gas.Gas;

public abstract class Model implements Cloneable {
	protected Dive dive;
	public static final int STOP_INTERVAL = 3;
	public static final int LAST_STOP = 6;
	public static final double STOP_LENGTH_UNIT = 1;
	public static final double DECO_ASCENT_RATE = 10;
	public static final boolean ACCOUNT_FOR_DECO_ASCENT_TIME = false;
	
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
	
	public abstract double getCeiling();
	public abstract Model clone();
	public abstract void descend(double depth, double rate);
	public abstract void stay(double time);
	public abstract void ascend(double depth, double rate);
	public abstract void switchGas(Gas gas);
	
	// default implementations are defined here and will usually not be overridden
	public double getNextStop() {
		double nextStop = getStopInterval() * Math.ceil( getCeiling() / getStopInterval() );
		return nextStop;
	}
	public double getStopLength() {
		int stopLength = 0;
		if ( accountForDecoAscentTime() ) {
			stay( - getStopInterval() / getDecoAscentRate() );
			dive.stay( - getStopInterval() / getDecoAscentRate() );
		}
		
		double currentStop = dive.getCurrentPoint().getDepth();
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
	
	public void decompress() {
		double nextStop;
		double stopLength;
		double decoAscentRate = getDecoAscentRate();
		nextStop = getNextStop();
		System.out.println("Start of decompression");
		while ( nextStop >= getLastStop() ) {
			ascend(nextStop, decoAscentRate);
			dive.ascend(nextStop, decoAscentRate);
			stopLength = getStopLength();
			if ( stopLength > 0 ) System.out.println(nextStop + " msw for " + stopLength + " minutes on " + (Gas) dive.getCurrentPoint() );
			nextStop = getNextStop();
		}
		System.out.println("End of decompression");
	}
	
	public void getDecompressionSchedule() {
		Model clone = (Model) this.clone();
		clone.decompress();
		clone = null;
	}
}
