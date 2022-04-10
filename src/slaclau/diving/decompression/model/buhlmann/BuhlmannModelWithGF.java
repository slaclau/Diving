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
		clone.setNitrogenLoading(this.getNitrogenLoading().clone());
		clone.setHeliumLoading(this.getHeliumLoading().clone());
		return clone;
	}
	
	@Override
	public void decompress() {
		double nextStop;
		double stopLength;
		double decoAscentRate = getDecoAscentRate();
		double oldGradientFactor = gradientFactor;
		
		firstStop = nextStop = getNextStop();
		gradientFactorSlope = ( getLowGF() - getHighGF() ) / firstStop;
		
		System.out.println("Start of decompression");
		while ( nextStop >= getLastStop() ) {
			ascend(nextStop, decoAscentRate);
			dive.ascend(nextStop, decoAscentRate);
			oldGradientFactor = gradientFactor;
			if ( nextStop == getLastStop() ) gradientFactor = getHighGF();
			else gradientFactor = gradientFactorSlope * ( nextStop - getStopInterval() ) + getHighGF();

			stopLength = getStopLength();
			if ( stopLength > 0 ) { 
				System.out.println(nextStop + " msw for " + stopLength + " minutes on " + (Gas) dive.getCurrentPoint() + ", GF is " + oldGradientFactor + ", next GF is " + gradientFactor);
			}
			nextStop = getNextStop();
		}
		System.out.println("End of decompression");
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
