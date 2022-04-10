package slaclau.diving.decompression;

import slaclau.diving.gas.Gas;
import slaclau.diving.decompression.model.Model;
import slaclau.diving.decompression.model.buhlmann.BuhlmannModelWithGF;
import slaclau.diving.decompression.model.buhlmann.constants.ZHL16B;
import slaclau.diving.decompression.userinterface.UserInterface;
import slaclau.diving.dive.Dive;
import slaclau.diving.dive.ModelledDive;
import slaclau.diving.dive.SimpleDive;
import slaclau.diving.gas.GasException;
import slaclau.diving.gas.Nitrox;

public final class DecompressionPlanner {

	public static void main(String[] args) throws InterruptedException {
		new UserInterface();
		
		try {
			Gas air = new Gas();
			Gas EAN50 = new Nitrox(.5);
			Gas EAN40 = new Nitrox(.4);
			Gas oxygen = new Nitrox(1);
			Dive simpleDive = new SimpleDive(air);
			Model model = new BuhlmannModelWithGF(simpleDive, new ZHL16B() );
			model.addGasSwitchPoint(6.0, oxygen);
			model.addGasSwitchPoint(24.0, EAN40);
			model.addGasSwitchPoint(21.0, EAN50);
						
			ModelledDive dive = new ModelledDive(simpleDive, model);
			dive.descend(40, 20);
			dive.stay(40);
			dive.getDecompressionSchedule();
		} catch (GasException e) {
			e.printStackTrace();
		}
	}
	
}
