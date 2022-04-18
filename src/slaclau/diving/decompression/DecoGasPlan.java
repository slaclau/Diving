package slaclau.diving.decompression;

import java.util.HashMap;
import java.util.TreeSet;

import slaclau.diving.gas.Gas;

public class DecoGasPlan implements Cloneable {
	private HashMap<Double,Gas> gasSwitchPoints;
	private TreeSet<Double> depths;
	
	public void setGasSwitchPoints(HashMap<Double, Gas> gasSwitchPoints) {
		this.gasSwitchPoints = gasSwitchPoints;
	}

	public void setDepths(TreeSet<Double> depths) {
		this.depths = depths;
	}
	public HashMap<Double, Gas> getGasSwitchPoints() {
		return gasSwitchPoints;
	}

	public TreeSet<Double> getDepths() {
		return depths;
	}
	public DecoGasPlan() {
		gasSwitchPoints = new HashMap<Double,Gas>();
		depths = new TreeSet<Double>();
	}
	
	@SuppressWarnings("unchecked")
	public DecoGasPlan clone() {
		DecoGasPlan clone = new DecoGasPlan();
		clone.setGasSwitchPoints((HashMap<Double, Gas>) getGasSwitchPoints().clone());
		clone.setDepths((TreeSet<Double>) getDepths().clone());
		return clone;
	}
	
	public void addDecoGas(double depth, Gas gas) {
		gasSwitchPoints.put(depth, gas);
		depths.add(depth);
	}
	
	public Gas getCorrectGas(double depth) {
		Gas gas = null;
		Double d = depths.ceiling(depth);
		if ( d != null) gas = gasSwitchPoints.get(d);
		return gas;
	}
	public Gas[] getDecoGases() {
		Gas[] gases = new Gas[gasSwitchPoints.size()];
		int i = 0;
		for ( Gas gas : gasSwitchPoints.values() ) {
			gases[i] = gas;
			i++;
			
		}
		return gases;
	}
}
