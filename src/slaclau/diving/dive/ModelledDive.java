package slaclau.diving.dive;

import slaclau.diving.gas.Gas;
import slaclau.diving.gas.GasAtDepth;

public class ModelledDive implements Dive {
	private Dive dive;
	private slaclau.diving.decompression.model.Model decompressionModel;
	
	public ModelledDive(Dive dive, slaclau.diving.decompression.model.Model decompressionModel) {
		this.dive = dive;
		this.decompressionModel = decompressionModel;
	}
	
	public ModelledDive clone() {
		return null;
	}

	@Override
	public GasAtDepth getPoint(double time) throws DiveTimeOutOfBoundsException {
		return dive.getPoint(time);
	}

	@Override
	public void descend(double depth, double rate) {
		dive.descend(depth, rate);
		decompressionModel.descend(depth, rate);
	}

	@Override
	public void stay(double time) {
		dive.stay(time);
		decompressionModel.stay(time);
	}

	@Override
	public void ascend(double depth, double rate) {
		dive.ascend(depth, rate);
		decompressionModel.ascend(depth, rate);
	}

	@Override
	public void switchGas(Gas gas) {
		dive.switchGas(gas);
		decompressionModel.switchGas(gas);
	}

	@Override
	public GasAtDepth getCurrentPoint() {
		return dive.getCurrentPoint();
	}

	@Override
	public double getTime() {
		return dive.getTime();
	}
	
	public void decompress() {
		decompressionModel.decompress();

	}
	
	public void getDecompressionSchedule() {
		decompressionModel.getDecompressionSchedule();
	}
}
