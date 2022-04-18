package slaclau.diving.decompression.model;

import slaclau.diving.dive.Dive;
import slaclau.diving.gas.GasAtDepth;

public class CNSOxygenToxicityModel implements AccessoryModel<Double> {
	private Dive dive;
	private double CNSfraction = 0;
	
	private static final double[] PO2_LOW = {0.5, 0.6, 0.7, 0.8, 0.9, 1.1, 1.5};
	private static final double[] PO2_HIGH = {0.6, 0.7, 0.8, 0.9, 1.1, 1.5, 1.7};
	private static final double[] CNS_M = {-1800, -1500, -1200, -900, -600, -300, -750};
	private static final double[] CNS_C = {1800, 1620, 1410, 1170, 900, 570, 1245};
	
	public CNSOxygenToxicityModel(Dive dive) {
		this.dive = dive;
	}
	
	public Double get() {
		return CNSfraction;
	}
	
	public void descend(double depth, double rate) {
		GasAtDepth gas = dive.getCurrentPoint();
		double oldDepth = gas.getDepth();
		double oxygenFraction = gas.getOxygenFraction();
		double k = oxygenFraction * rate / 10d;
		
		double segmentTime = ( depth - oldDepth ) / rate;

		double minPO2 = oxygenFraction * ( oldDepth / 10 + 1);
		double maxPO2 = oxygenFraction * ( depth / 10 + 1);
				
		for ( int i = 0 ; i < 7 ; i++ ) {
			double lowPO2 = PO2_LOW[i];
			double highPO2 = PO2_HIGH[i];
			double m = CNS_M[i];
			double mk = m * k;
			double c = CNS_C[i];
			
			
			if ( maxPO2 > lowPO2 && minPO2 < highPO2 ) {
				if ( minPO2 > lowPO2 ) lowPO2 = minPO2;
				if ( maxPO2 < highPO2 ) highPO2 = maxPO2;
				
				double exposureTime = segmentTime * ( highPO2 - lowPO2 ) / ( maxPO2 - minPO2 );
				
				double limitTime = m * lowPO2 + c;
				
				double temp = 100d / mk * ( Math.log( Math.abs( limitTime + mk * exposureTime ) ) - Math.log( Math.abs( limitTime ) ) );
				CNSfraction += temp;
			}
		}
	}
	
	public void ascend(double depth, double rate) {
		GasAtDepth gas = dive.getCurrentPoint();
		double oldDepth = gas.getDepth();
		double oxygenFraction = gas.getOxygenFraction();
		double k = - oxygenFraction * rate / 10d;
		
		double segmentTime = ( oldDepth - depth ) / rate;

		double minPO2 = oxygenFraction * ( depth / 10 + 1);
		double maxPO2 = oxygenFraction * ( oldDepth / 10 + 1);
		
		for ( int i = 0 ; i < 7 ; i++ ) {
			double lowPO2 = PO2_LOW[i];
			double highPO2 = PO2_HIGH[i];
			double m = CNS_M[i];
			double mk = m * k;
			double c = CNS_C[i];
			
			
			if ( maxPO2 > lowPO2 && minPO2 < highPO2 ) {
				if ( minPO2 > lowPO2 ) lowPO2 = minPO2;
				if ( maxPO2 < highPO2 ) highPO2 = maxPO2;
				
				double exposureTime = segmentTime * ( highPO2 - lowPO2 ) / ( maxPO2 - minPO2 );
				
				double limitTime = m * highPO2 + c;
				
				double temp = 100d / mk * ( Math.log( Math.abs( limitTime + mk * exposureTime ) ) - Math.log( Math.abs( limitTime ) ) );
				CNSfraction += temp;
			}
		}
	}
	
	public void stay(double time) {
		GasAtDepth gas = dive.getCurrentPoint();
		double pO2 = gas.getOxygenPartialPressure();
		
		for ( int i = 0 ; i < 7 ; i++ ) {
			double lowPO2 = PO2_LOW[i];
			double highPO2 = PO2_HIGH[i];
			
			if (pO2 >= lowPO2 && pO2 < highPO2) {
				double m = CNS_M[i];
				double c = CNS_C[i];
				
				double limitTime = m * pO2 + c;
				CNSfraction += 100d * time / limitTime;
				break;
			}
		}
	}
}
