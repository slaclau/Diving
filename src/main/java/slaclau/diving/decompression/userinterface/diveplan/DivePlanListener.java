package slaclau.diving.decompression.userinterface.diveplan;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import slaclau.diving.decompression.DecoGasPlan;
import slaclau.diving.decompression.DecompressionSchedule;
import slaclau.diving.decompression.model.ModelledDive;
import slaclau.diving.decompression.model.StopLengthException;
import slaclau.diving.decompression.model.buhlmann.BuhlmannModelWithGF;
import slaclau.diving.decompression.model.buhlmann.constants.ZHL16B;
import slaclau.diving.decompression.userinterface.UserInterface;
import slaclau.diving.decompression.userinterface.chart.DiveChartPanel;
import slaclau.diving.dive.Dive;
import slaclau.diving.dive.SimpleDive;
import slaclau.diving.gas.Gas;
import slaclau.diving.gas.GasAtDepth;

public class DivePlanListener implements ActionListener, ChangeListener {
	private ModelledDive dive;
	private DecoGasPlan decoGasPlan = new DecoGasPlan();
	
	private DivePlanPanel divePlanPanel;
	private GasPlanPanel gasPlanPanel;
	private DecoPlanPanel decoPlanPanel;
	private ExtraInfoPanel extraInfoPanel;
	private DiveChartPanel diveChartPanel;
	
	private UserInterface userInterface;
	
	public DivePlanListener(DivePlanMainPanel divePlanMainPanel) {
		divePlanPanel = divePlanMainPanel.getDivePlanPanel();
		gasPlanPanel = divePlanMainPanel.getGasPlanPanel();
		decoPlanPanel = divePlanMainPanel.getDecoPlanPanel();
		extraInfoPanel = divePlanMainPanel.getExtraInfoPanel();
		
		userInterface = divePlanMainPanel.getUserInterface();
		
		diveChartPanel = userInterface.getDiveChartPanel();
	}
	
	private Thread updateThread = new Thread("updateThread");
	
	public void onUpdate() {
		decoGasPlan = new DecoGasPlan();
		Gas bottomGas = gasPlanPanel.getBottomGas();
		double depth = bottomGas.getMOD();
		decoGasPlan.addDecoGas(depth, bottomGas);
		
		GasAtDepth gas;
		
		for ( int i = 0 ; i < gasPlanPanel.getNumberOfGases() ; i++ ) {
			gas = gasPlanPanel.getGasSwitchPoint(i);
			depth = gas.getDepth();
			decoGasPlan.addDecoGas(depth, gas);
		}
		
		Dive simpleDive = new SimpleDive(bottomGas);
		dive = new BuhlmannModelWithGF(simpleDive, new ZHL16B() );
		dive.setDecoGasPlan(decoGasPlan);
		
		depth = 0;
		
		for ( int i = 0 ; i < divePlanPanel.getNumberOfLevels() ; i++ ) {
			double newDepth = divePlanPanel.getDepthOfLevel(i);
			if (newDepth >= depth) dive.descend(newDepth, dive.getDescentRate() );
			else dive.ascend(newDepth, dive.getAscentRate() );
			dive.stay(divePlanPanel.getTimeOfLevel(i) );
		}
		Runnable r = () -> {
			DecompressionSchedule decompressionSchedule;
			try {
				decompressionSchedule = dive.decompress();
				decoPlanPanel.setDecoPlan(decompressionSchedule);
				decoPlanPanel.getParent().repaint();
				userInterface.println(decompressionSchedule.toString());
			
				extraInfoPanel.setInfo(dive.getfExtraInfo() );
				extraInfoPanel.getParent().repaint();

				diveChartPanel.plotDive(dive);
			} catch (StopLengthException e) {
				userInterface.println(e.toString());
			}
		};
		updateThread = new Thread(r,"updateThread");
		updateThread.start();
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
