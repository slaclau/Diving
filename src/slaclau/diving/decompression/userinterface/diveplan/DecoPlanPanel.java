package slaclau.diving.decompression.userinterface.diveplan;

import java.awt.Dimension;

import javax.swing.*;

import slaclau.diving.decompression.DecompressionSchedule;
import slaclau.diving.gas.Gas;
import slaclau.diving.gas.GasAtDepth;

@SuppressWarnings("serial")
public class DecoPlanPanel extends JPanel {
	private JPanel innerPanel = new JPanel();
	
	public DecoPlanPanel() {
		add(innerPanel);
	}
	
	public void setDecoPlan(DecompressionSchedule decompressionSchedule) {
		remove(innerPanel);
		innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		for ( int i = 0 ; i < decompressionSchedule.getNumberOfStops() ; i++ ) {
			double time = decompressionSchedule.getDecoStopTime(i);
			if ( time > 0 ) {
				GasAtDepth gas = decompressionSchedule.getDecoDivePoint(i);
				double depth = gas.getDepth();
				String note = decompressionSchedule.getNote(i);
				addDecoStop(depth, time, gas, note);
			}
		}
		add(innerPanel);
	}
	
	private void addDecoStop(double depth, double time, Gas gas, String note) {
		JPanel decoStopPanel = new JPanel();
		innerPanel.add(decoStopPanel);
		
		JLabel depthLabel, timeLabel, gasLabel, noteLabel;
		
		depthLabel = new JLabel( depth + " msw" );
		depthLabel.setPreferredSize(new Dimension(100,20));
		
		timeLabel = new JLabel( time + ( (time > 1) ? " mins" : " min" ) );
		timeLabel.setPreferredSize(new Dimension(100,20));

		gasLabel = new JLabel( gas.toString() );
		gasLabel.setPreferredSize(new Dimension(100,20));
		
		noteLabel = new JLabel( note );
		noteLabel.setPreferredSize(new Dimension(100, 20));

		decoStopPanel.add(depthLabel);
		decoStopPanel.add(timeLabel);
		decoStopPanel.add(gasLabel);
		decoStopPanel.add(noteLabel);
	}
}
