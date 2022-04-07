package slaclau.diving.gas;

public class Trimix extends Gas {

	public Trimix(double oxygenFraction, double heliumFraction) throws GasException {
		super(oxygenFraction, 1 - oxygenFraction - heliumFraction, heliumFraction);
	}

}
