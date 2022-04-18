package slaclau.diving.decompression.userinterface.diveplan;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import slaclau.diving.decompression.DecoGasPlan;
import slaclau.diving.decompression.DecompressionSchedule;
import slaclau.diving.decompression.model.ModelledDive;
import slaclau.diving.decompression.model.buhlmann.BuhlmannModelWithGF;
import slaclau.diving.decompression.model.buhlmann.constants.ZHL16B;
import slaclau.diving.decompression.userinterface.UserInterface;
import slaclau.diving.decompression.userinterface.chart.DiveChartPanel;
import slaclau.diving.dive.Dive;
import slaclau.diving.dive.SimpleDive;
import slaclau.diving.gas.Gas;
import slaclau.diving.gas.GasException;
import slaclau.diving.gas.Nitrox;

public class DivePlanListener implements ActionListener, ChangeListener {
	private ModelledDive dive;
	private DecoGasPlan decoGasPlan = new DecoGasPlan();
	
	private DivePlanPanel divePlanPanel;
	private GasPlanPanel gasPlanPanel;
	private DecoPlanPanel decoPlanPanel;
	private DiveChartPanel diveChartPanel;
	
	private UserInterface userInterface;
	
	public DivePlanListener(DivePlanPanel divePlanPanel, GasPlanPanel gasPlanPanel, DecoPlanPanel decoPlanPanel, UserInterface userInterface) {
		this.divePlanPanel = divePlanPanel;
		this.gasPlanPanel = gasPlanPanel;
		this.decoPlanPanel = decoPlanPanel;
		this.userInterface = userInterface;
		
		this.diveChartPanel = userInterface.getDiveChartPanel();
	}
	
	public void onUpdate() {
		decoGasPlan = new DecoGasPlan();
		Gas gas = gasPlanPanel.getBottomGas();
		double depth = gas.getMOD();
		decoGasPlan.addDecoGas(depth, gas);
		
		for ( int i = 0 ; i < gasPlanPanel.getNumberOfGases() ; i++ ) {
			gas = gasPlanPanel.getGas(i);
			depth = gas.getMOD();
			decoGasPlan.addDecoGas(depth, gas);
		}
		
		Gas BottomGas = gasPlanPanel.getBottomGas();
		Dive simpleDive = new SimpleDive(BottomGas);
		dive = new BuhlmannModelWithGF(simpleDive, new ZHL16B() );
		dive.setDecoGasPlan(decoGasPlan);
		
		depth = 0;
		
		for ( int i = 0 ; i < divePlanPanel.getNumberOfLevels() ; i++ ) {
			double newDepth = divePlanPanel.getDepthOfLevel(i);
			if (newDepth >= depth) dive.descend(newDepth, dive.getDescentRate() );
			else dive.ascend(newDepth, dive.getAscentRate() );
			dive.stay(divePlanPanel.getTimeOfLevel(i) );
		}
		DecompressionSchedule decompressionSchedule = dive.decompress();
		decoPlanPanel.setDecoPlan(decompressionSchedule);
		decoPlanPanel.getParent().repaint();
		userInterface.println(decompressionSchedule.toString());
		try {
			userInterface.println("CNS% is " + dive.getCNS() + ", OTUs are " + dive.getOTUs() +", gas used is " + dive.getGasConsumed(gasPlanPanel.getBottomGas()) + ", EAN40 used is " + dive.getGasConsumed(new Nitrox(.4)));
		} catch (GasException e) {
			e.printStackTrace();
		}
		
		diveChartPanel.plotDive(dive);
	}

	@Override
	public void stateChanged(ChangeEvent ce) {
		onUpdate();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		onUpdate();
	}

}
