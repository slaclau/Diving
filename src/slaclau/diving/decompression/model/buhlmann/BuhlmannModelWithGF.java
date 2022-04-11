package slaclau.diving.decompression.model.buhlmann;

import slaclau.diving.decompression.model.buhlmann.constants.BuhlmannConstants;
import slaclau.diving.dive.Dive;
import slaclau.diving.gas.Gas;

public class BuhlmannModelWithGF extends BuhlmannModel {
	private static final double LOW_GF = 0.2;
	private static final double HIGH_GF = 0.8;
	private double gradientFactorSlope;
	
	private static double getLowGF() {
		return LOW_GF;
	}
	private static double getHighGF() {
		return HIGH_GF;
	}
	
	private double gradientFactor;
	private double firstStop;

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
	public String decompress() {
		String string = "Start of decompression\n";
		double nextStop;
		double stopLength;
		double decoAscentRate = getDecoAscentRate();
		double oldGradientFactor = gradientFactor;
		
		firstStop = nextStop = getNextStop();
		gradientFactorSlope = ( getLowGF() - getHighGF() ) / firstStop;
		while ( nextStop >= getLastStop() ) {
			ascend(nextStop, decoAscentRate);
			dive.ascend(nextStop, decoAscentRate);
			oldGradientFactor = gradientFactor;
			if ( nextStop == getLastStop() ) gradientFactor = getHighGF();
			else gradientFactor = gradientFactorSlope * ( nextStop - getStopInterval() ) + getHighGF();

			stopLength = getStopLength();
			if ( stopLength > 0 ) { 
				string += (nextStop + " msw for " + stopLength + " minutes on " + (Gas) dive.getCurrentPoint() 
				+ ", GF is " + Math.round( 1000 * oldGradientFactor ) / 1000d 
				+ ", next GF is " + Math.round( 1000 * gradientFactor ) / 1000d + "\n");
			}
			nextStop = getNextStop();
		}
		string += "End of decompression";
		return string;
	}
	
	@Override
	public double getCeiling() {
		double compartmentCeiling[] = new double[16];
		double a;
		double b;
		for (int i = 0 ; i < 16 ; i++) {
			a = ( nitrogenA[i] * nitrogenLoading[i] + heliumA[i] * heliumLoading[i] ) / ( nitrogenLoading[i] + heliumLoading[i] );
			b = ( nitrogenB[i] * nitrogenLoading[i] + heliumB[i] * heliumLoading[i] ) / ( nitrogenLoading[i] + heliumLoading[i] );

			compartmentCeiling[i] = 10 * ( ( nitrogenLoading[i] + heliumLoading[i] - gradientFactor * a ) / ( gradientFactor / b - gradientFactor + 1 ) - 1 );
		}
		
		double ceiling = compartmentCeiling[0];
		for (int i = 0 ; i < 16 ; i++) {
			if ( compartmentCeiling[i] > ceiling ) ceiling = compartmentCeiling[i];
		}
		//System.out.println(ceiling);
		return ceiling;
	}
}
