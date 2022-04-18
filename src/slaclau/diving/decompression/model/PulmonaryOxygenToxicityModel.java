package slaclau.diving.decompression.model;

import slaclau.diving.dive.Dive;
import slaclau.diving.gas.GasAtDepth;

public class PulmonaryOxygenToxicityModel implements AccessoryModel<Double>{
	private Dive dive;
	private double OTUs = 0;
	
	public PulmonaryOxygenToxicityModel(Dive dive) {
		this.dive = dive;
	}
	
	public Double get() {
		return OTUs;
	}
	
	public void descend(double depth, double rate) {
		GasAtDepth gas = dive.getCurrentPoint();
		double oldDepth = gas.getDepth();
		double oxygenFraction = gas.getOxygenFraction();
		
		double segmentTime = ( depth - oldDepth ) / rate;
		double exposureTime = segmentTime;

		double minPO2 = oxygenFraction * ( oldDepth / 10 + 1);
		double maxPO2 = oxygenFraction * ( depth / 10 + 1);
		double lowPO2 = minPO2;
		
		if ( maxPO2 >= 0.5 ) {
			if (minPO2 < 0.5 ) lowPO2 = 0.5;
			exposureTime = segmentTime * ( maxPO2 - lowPO2 ) / ( maxPO2 - minPO2 );
			OTUs += ( 3d * exposureTime / 11d ) / ( maxPO2 - lowPO2 ) * ( Math.pow( ( maxPO2 - 0.5 ) / 0.5 , 11d / 6d ) - Math.pow( ( lowPO2 - 0.5 ) / 0.5 , 11d / 6d ) );
		}
	}
	
	public void ascend(double depth, double rate) {
		GasAtDepth gas = dive.getCurrentPoint();
		double oldDepth = gas.getDepth();
		double oxygenFraction = gas.getOxygenFraction();
		
		double segmentTime = ( oldDepth - depth ) / rate;
		double exposureTime = segmentTime;

		double minPO2 = oxygenFraction * ( depth / 10 + 1);
		double maxPO2 = oxygenFraction * ( oldDepth / 10 + 1);
		double lowPO2 = minPO2;
		
		if ( maxPO2 >= 0.5 ) {
			if (minPO2 < 0.5 ) lowPO2 = 0.5;
			exposureTime = segmentTime * ( maxPO2 - lowPO2 ) / ( maxPO2 - minPO2 );
			OTUs += ( 3d * exposureTime / 11d ) / ( maxPO2 - lowPO2 ) * ( Math.pow( ( maxPO2 - 0.5 ) / 0.5 , 11d / 6d ) - Math.pow( ( lowPO2 - 0.5 ) / 0.5 , 11d / 6d ) );
		}
	}
	
	public void stay(double time) {
		GasAtDepth gas = dive.getCurrentPoint();
		double pO2 = gas.getOxygenPartialPressure();
		
		if ( pO2 > 0.5 ) {
			OTUs += time * Math.pow( 0.5 / ( pO2 - 0.5 ) , -5d / 6d );
		}
	}
}
