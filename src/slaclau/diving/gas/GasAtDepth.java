package slaclau.diving.gas;

public class GasAtDepth extends Gas {
	private double depth;
	
	public GasAtDepth() throws GasException {
		super();
		setDepth(0);
	}
	public GasAtDepth (Gas gas, double depth) {
		super(gas);
		setDepth(depth);
	}
	
	public double getDepth() {
		return depth;
	}

	public void setDepth(double depth) {
		this.depth = depth;
	}
	
	public double getEND() {
		return ( depth + 10 ) * ( 1 - getHeliumFraction() ) - 10;
	}
	
	public double getOxygenPartialPressure() {
		return getOxygenFraction() * ( getDepth() / 10 + 1);
	}
	
	public double getNitrogenPartialPressure() {
		return getNitrogenFraction() * ( getDepth() / 10 + 1);
	}
	
	public double getHeliumPartialPressure() {
		return getHeliumFraction() * ( getDepth() / 10 + 1);
	}
}
