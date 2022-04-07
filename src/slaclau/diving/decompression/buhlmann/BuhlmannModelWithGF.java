package slaclau.diving.decompression.buhlmann;

import slaclau.diving.decompression.buhlmann.constants.BuhlmannConstants;
import slaclau.diving.dive.Dive;

public class BuhlmannModelWithGF extends BuhlmannModel {
	private static final double LOW_GF = 0.8;
	private static final double HIGH_GF = 0.8;
	
	private static double getLowGF() {
		return LOW_GF;
	}
	@SuppressWarnings("unused")
	private static double getHighGF() {
		return HIGH_GF;
	}
	
	private double gradientFactor;

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
		return ceiling;
	}
}
