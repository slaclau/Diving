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
	public static double getPressure(double depth) {
		return depth / 10 + getAtmosphericPressure();
	}
	
	
	public double getPressure() {
		return getPressure(depth);
	}
	
	public double getEND() {
		return ( depth + 10 ) * ( 1 - getHeliumFraction() ) - 10;
	}
	
	public double getOxygenPartialPressure() {
		return getOxygenFraction() * getPressure();
	}
	
	public double getNitrogenPartialPressure() {
		return getNitrogenFraction() * getPressure();
	}
	
	public double getHeliumPartialPressure() {
		return getHeliumFraction() * getPressure();
	}
}
