package slaclau.diving.decompression;

import slaclau.diving.gas.Gas;
import slaclau.diving.decompression.model.ModelledDive;
import slaclau.diving.decompression.model.buhlmann.BuhlmannModelWithGF;
import slaclau.diving.decompression.model.buhlmann.constants.ZHL16B;
import slaclau.diving.decompression.userinterface.UserInterface;
import slaclau.diving.dive.Dive;
import slaclau.diving.dive.SimpleDive;
import slaclau.diving.gas.GasException;
import slaclau.diving.gas.Nitrox;

public final class DecompressionPlanner {

	@SuppressWarnings("unused")
	public static void main(String[] args) throws InterruptedException {
		UserInterface userInterface = new UserInterface();
		
		try {
			Gas air = new Gas();
			Gas EAN28 = new Nitrox(.28);
			Gas EAN40 = new Nitrox(.4);
			Gas oxygen = new Nitrox(1);
			Dive simpleDive = new SimpleDive(EAN28);
			ModelledDive dive = new BuhlmannModelWithGF(simpleDive, new ZHL16B() );
			
			DecoGasPlan plan = new DecoGasPlan();
			plan.addDecoGas(24.0, EAN40);
			
			dive.setDecoGasPlan(plan);
			
			dive.descend(40, 20);
			dive.stay(88);
			userInterface.println(dive.decompress());
			userInterface.println("OTUs: " + Math.round( dive.getOTUs() ) + ", CNS: " + Math.round( 100 * dive.getCNS() ) + "%");
		} catch (GasException e) {
			e.printStackTrace();
		}
	}
	
}
