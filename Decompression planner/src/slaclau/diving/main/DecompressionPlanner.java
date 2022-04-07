package slaclau.diving.main;

import slaclau.diving.gas.Gas;
import slaclau.diving.decompression.Model;
import slaclau.diving.decompression.buhlmann.BuhlmannModel;
import slaclau.diving.decompression.buhlmann.BuhlmannModelWithGF;
import slaclau.diving.decompression.buhlmann.constants.ZHL16B;
import slaclau.diving.dive.Dive;
import slaclau.diving.dive.ModelledDive;
import slaclau.diving.dive.SimpleDive;
import slaclau.diving.gas.GasException;

public final class DecompressionPlanner {

	public static void main(String[] args) throws InterruptedException {
		try {
			Dive simpleDive = new SimpleDive(new Gas());
			Model model = new BuhlmannModelWithGF(simpleDive, new ZHL16B() );
			ModelledDive dive = new ModelledDive(simpleDive, model);
			dive.descend(40, 20);
			dive.stay(20);
			dive.getDecompressionSchedule();
		} catch (GasException e) {
			e.printStackTrace();
		}
	}
	
}
