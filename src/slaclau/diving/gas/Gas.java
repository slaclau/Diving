package slaclau.diving.gas;

public class Gas {
	private static final double ATMOSPHERIC_PRESSURE = 1;
	private static final double WATER_VAPOUR_PRESSURE = 0.0627;
	
	public static double getAtmosphericPressure() {
		return ATMOSPHERIC_PRESSURE;
	}
	public static double getWaterVapourPressure() {
		return WATER_VAPOUR_PRESSURE;
	}
	
	private static final double MAX_PO2 = 1.60;
	private static final double MIN_PO2 = 0.16;
	
	private double oxygenFraction;
	private double nitrogenFraction;
	private double heliumFraction;
	
	public Gas () throws GasException {
		this(0.21,0.79,0);
	}
	
	public Gas (Gas gas) {
		setOxygenFraction(gas.getOxygenFraction());
		setNitrogenFraction(gas.getNitrogenFraction());
		setHeliumFraction(gas.getHeliumFraction());
	}
	
	public Gas(double oxygenFraction, double nitrogenFraction, double heliumFraction) throws GasException {
		if (0 > oxygenFraction || oxygenFraction > 1 || 0 > nitrogenFraction || nitrogenFraction > 1 || 0 > heliumFraction || heliumFraction > 1) {
			throw new GasException(oxygenFraction, nitrogenFraction, heliumFraction);
		}
		if (oxygenFraction + nitrogenFraction + heliumFraction != 1) {
			throw new GasException(oxygenFraction, nitrogenFraction, heliumFraction);
		}
		setOxygenFraction(oxygenFraction);
		setNitrogenFraction(nitrogenFraction);
		setHeliumFraction(heliumFraction);
	}
	
	public double getOxygenFraction() {
		return oxygenFraction;
	}
	public double getNitrogenFraction() {
		return nitrogenFraction;
	}
	public double getHeliumFraction() {
		return heliumFraction;
	}
	
	private void setOxygenFraction(double oxygenFraction) {
		this.oxygenFraction = oxygenFraction;
	}
	private void setNitrogenFraction(double nitrogenFraction) {
		this.nitrogenFraction = nitrogenFraction;
	}
	private void setHeliumFraction(double heliumFraction) {
		this.heliumFraction = heliumFraction;
	}
	
	public double getMOD() {
		return 10 * ( MAX_PO2 / oxygenFraction - 1 );
	}
	
	public double getMSD() {
		double msd = 10 * ( MIN_PO2 / oxygenFraction - 1 );
		if (msd > 0) return msd;
		else return 0;
	}
	
	public GasAtDepth atDepth(double depth) {
		return new GasAtDepth(this, depth);
	}
	
	public boolean isEqual(Gas gas) {
		if(getOxygenFraction() == gas.getOxygenFraction() && getNitrogenFraction() == gas.getNitrogenFraction()) return true;
		else return false;
	}
	
	public String toString() {
		if ( heliumFraction == 0 ) {
			if (oxygenFraction == 1.0 ) return "oxygen";
			else if ( oxygenFraction == 0.21 ) return "air";
			else return "EAN" + (int) (100 * oxygenFraction);
		} else {
			return "Trimix " + (int) (100 * oxygenFraction) + "/" + (int) (100 * heliumFraction);
		}
		//return "Oxygen: " + 100*oxygenFraction + "% Nitrogen: " + 100*nitrogenFraction + "% Helium: " + 100*heliumFraction + "%";
	}
}
