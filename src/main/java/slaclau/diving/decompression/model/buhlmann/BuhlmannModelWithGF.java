package slaclau.diving.decompression.model.buhlmann;

import slaclau.diving.decompression.DecompressionSchedule;
import slaclau.diving.decompression.model.ModelledDive;
import slaclau.diving.decompression.model.StopLengthException;
import slaclau.diving.decompression.model.buhlmann.constants.BuhlmannConstants;
import slaclau.diving.dive.Dive;

public class BuhlmannModelWithGF extends BuhlmannModel {
	private static final double LOW_GF = 0.2;
	private static final double HIGH_GF = 0.8;
	private double gradientFactorSlope;
	
	public double getLowGF() {
		return LOW_GF;
	}
	public double getHighGF() {
		return HIGH_GF;
	}
	
	private double gradientFactor;
	private double firstStop;
	private double firstStopTime = 0;

	public BuhlmannModelWithGF(Dive dive, BuhlmannConstants constants) {
		super(dive, constants);
		gradientFactor = getLowGF();
	}
	
	public BuhlmannModelWithGF clone() {
		BuhlmannModelWithGF clone = new BuhlmannModelWithGF(dive.clone(), constants);
		clone.setDecoGasPlan(getDecoGasPlan().clone());
		clone.setNitrogenLoading(this.getNitrogenLoading().clone());
		clone.setHeliumLoading(this.getHeliumLoading().clone());
		return clone;
	}
	@Override
	public ModelledDive cloneAndReset() {
		BuhlmannModelWithGF clone = new BuhlmannModelWithGF(dive.cloneAndReset(), constants);
		clone.setDecoGasPlan(getDecoGasPlan().clone());
		return clone;
	}
	
	@Override
	public DecompressionSchedule decompress() throws StopLengthException {
		DecompressionSchedule schedule = new DecompressionSchedule();
		double nextStop;
		double stopLength;
		double decoAscentRate = getDecoAscentRate();
		double oldGradientFactor = gradientFactor;
		
		firstStop = nextStop = getNextStop();
		gradientFactorSlope = ( getLowGF() - getHighGF() ) / firstStop;
		ascend(nextStop, decoAscentRate);
		firstStopTime = getTime();
		while ( nextStop >= getLastStop() ) {
			if(nextStop != firstStop) ascend(nextStop, decoAscentRate);
			dive.ascend(nextStop, decoAscentRate);
			oldGradientFactor = gradientFactor;
			if ( nextStop == getLastStop() ) gradientFactor = getHighGF();
			else gradientFactor = gradientFactorSlope * ( nextStop - getStopInterval() ) + getHighGF();

			stopLength = getStopLength();
			schedule.addStop(stopLength, dive.getCurrentPoint(), "GF is " + Math.round( 1000 * oldGradientFactor ) / 1000d );

			nextStop = getNextStop();
		}
		ascend(0, decoAscentRate);
		return schedule;
	}
	
	@Override
	public double getCeiling() {
		double compartmentCeiling[] = new double[16];
		double a;
		double b;
		for (int i = 0 ; i < 16 ; i++) {
			a = ( nitrogenA[i] * nitrogenLoading[i] + heliumA[i] * heliumLoading[i] ) / ( nitrogenLoading[i] + heliumLoading[i] );
			b = ( nitrogenB[i] * nitrogenLoading[i] + heliumB[i] * heliumLoading[i] ) / ( nitrogenLoading[i] + heliumLoading[i] );

			compartmentCeiling[i] = 10 * ( ( ( nitrogenLoading[i] + heliumLoading[i] - gradientFactor * a ) / ( gradientFactor / b - gradientFactor + 1 ) ) - 1 );
		}
		
		double ceiling = compartmentCeiling[0];
		for (int i = 0 ; i < 16 ; i++) {
			if ( compartmentCeiling[i] > ceiling ) ceiling = compartmentCeiling[i];
		}
		//System.out.println(ceiling);
		return ceiling;
	}
	public double getFirstStopTime() {
		return firstStopTime;
	}
	public double getGradientFactor() {
		double depth = dive.getCurrentPoint().getDepth();
		if ( depth > firstStop ) return getLowGF();
		else return gradientFactorSlope * depth + getHighGF();
	}
	public double getActualCeiling() {
		double compartmentCeiling[] = new double[16];
		double a;
		double b;
		for (int i = 0 ; i < 16 ; i++) {
			a = ( nitrogenA[i] * nitrogenLoading[i] + heliumA[i] * heliumLoading[i] ) / ( nitrogenLoading[i] + heliumLoading[i] );
			b = ( nitrogenB[i] * nitrogenLoading[i] + heliumB[i] * heliumLoading[i] ) / ( nitrogenLoading[i] + heliumLoading[i] );

			compartmentCeiling[i] = 10 * ( ( ( nitrogenLoading[i] + heliumLoading[i] - getGradientFactor() * a ) / ( getGradientFactor() / b - getGradientFactor() + 1 ) ) - 1 );
		}
		
		double ceiling = compartmentCeiling[0];
		for (int i = 0 ; i < 16 ; i++) {
			if ( compartmentCeiling[i] > ceiling ) ceiling = compartmentCeiling[i];
		}
		//System.out.println(ceiling);
		return ceiling;
	}
	public double getFirstStop() {
		return firstStop;
	}
	public void setFirstStop(double firstStop) {
		this.firstStop = firstStop;
		gradientFactorSlope = ( getLowGF() - getHighGF() ) / firstStop;
	}
}
