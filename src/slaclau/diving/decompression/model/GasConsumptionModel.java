package slaclau.diving.decompression.model;

import slaclau.diving.dive.Dive;
import slaclau.diving.gas.Gas;
import slaclau.diving.gas.GasAtDepth;

public class GasConsumptionModel implements AccessoryModel<Double> {
	private Dive dive;
	private Gas gas;
	private double gasConsumed;
	
	private double getSAC() {
		return 20;
	}
	
	public GasConsumptionModel(Dive dive, Gas gas) {
		this.dive = dive;
		this.gas = gas;
	}

	@Override
	public Double get() {
		return gasConsumed;
	}

	@Override
	public void descend(double depth, double rate) {
		GasAtDepth gas = dive.getCurrentPoint();
		if( this.gas.isEqual(gas) ) {
			double oldDepth = gas.getDepth();
			double time = ( depth - oldDepth ) /rate;
			gasConsumed += GasAtDepth.getPressure( ( depth + oldDepth ) / 2 ) / GasAtDepth.getAtmosphericPressure() * getSAC() * time;
		}
	}

	@Override
	public void stay(double time) {
		GasAtDepth gas = dive.getCurrentPoint();
		if( this.gas.isEqual(gas) ) gasConsumed += gas.getPressure() / GasAtDepth.getAtmosphericPressure() * getSAC() * time;
	}

	@Override
	public void ascend(double depth, double rate) {
		GasAtDepth gas = dive.getCurrentPoint();
		if( this.gas.isEqual(gas) ) {
			double oldDepth = gas.getDepth();
			double time = ( oldDepth - depth ) /rate;
			gasConsumed += GasAtDepth.getPressure( ( depth + oldDepth ) / 2 ) / GasAtDepth.getAtmosphericPressure() * getSAC() * time;
		}
	}

	public Gas getGas() {
		return gas;
	}

	public void setGas(Gas gas) {
		this.gas = gas;
	}

}
