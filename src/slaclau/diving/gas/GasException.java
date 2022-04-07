package slaclau.diving.gas;

@SuppressWarnings("serial")
public class GasException extends Exception {
	private double oxygenFraction;
	private double nitrogenFraction;
	private double heliumFraction;

	public GasException(double oxygenFraction, double nitrogenFraction, double heliumFraction) {
		this.oxygenFraction = oxygenFraction;
		this.nitrogenFraction = nitrogenFraction;
		this.heliumFraction = heliumFraction;
	}
	
	public String toString() {
		String string = "gas exception:";
		if (oxygenFraction < 0) string += " oxygen component is too small,";
		if (nitrogenFraction < 0) string += " nitrogen component is too small,";
		if (heliumFraction < 0) string += " helium component is too small,";
		
		if (oxygenFraction > 1) string += " oxygen component is too large,";
		if (nitrogenFraction > 1) string += " nitrogen component is too large,";
		if (heliumFraction > 1) string += " helium component is too large,";
		
		if (oxygenFraction + nitrogenFraction + heliumFraction != 1) string += " components do not sum to 1";
		return string;
	}
}
