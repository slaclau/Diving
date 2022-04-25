package slaclau.diving.gas;

public class Nitrox extends Gas {
	
	public Nitrox(double oxygenFraction) throws GasException{
		super(oxygenFraction, 1 - oxygenFraction, 0);
	}
	
}
