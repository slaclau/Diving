package slaclau.diving.decompression.model;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

import slaclau.diving.dive.Dive;
import slaclau.diving.gas.Gas;
import slaclau.diving.gas.GasException;
import slaclau.diving.gas.Nitrox;

public abstract class Model implements Cloneable {
	protected Dive dive;
	private static final int STOP_INTERVAL = 3;
	private static final int LAST_STOP = 6;
	private static final double STOP_LENGTH_UNIT = 1;
	private static final double DECO_ASCENT_RATE = 10;
	private static final boolean ACCOUNT_FOR_DECO_ASCENT_TIME = false;
	
	private Map<Double,Gas> gasSwitchPoints;
	public NavigableSet<Double> depths;
	
	public Model() {
		gasSwitchPoints = new HashMap<Double,Gas>();
		depths = new TreeSet<Double>();
		try {
			Gas EAN50 = new Nitrox(.5);
			Gas EAN40 = new Nitrox(.4);
			Gas oxygen = new Nitrox(1);
		//addGasSwitchPoint(6.0, oxygen);
		//addGasSwitchPoint(24.0, EAN40);
		//addGasSwitchPoint(21.0, EAN50);
		} catch (GasException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setGasSwitchPoints(Map<Double,Gas> gasSwitchPoints) {
		this.gasSwitchPoints = gasSwitchPoints;
		depths = new TreeSet<Double>( gasSwitchPoints.keySet() );
	}

	public void addGasSwitchPoint(double depth, Gas gas) {
		gasSwitchPoints.put(depth,  gas);
		depths.add(depth);
		System.out.println(depths);
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
	public abstract Model clone();
	public abstract void descend(double depth, double rate);
	public abstract void stay(double time);
	public abstract void ascend(double depth, double rate);
	
	// default implementations are defined here and will usually not be overridden
	private Gas getCorrectGas(double depth) {
		System.out.print(depths);
		Gas gas = null;
		Double d = depths.ceiling(depth);		
		if (d != null) gas = gasSwitchPoints.get(d);
		return gas;
	}
	
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
		Gas gas = getCorrectGas( currentStop );
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
