package slaclau.diving.decompression.userinterface.diveplan;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;

import slaclau.diving.decompression.DecompressionSchedule;
import slaclau.diving.gas.Gas;
import slaclau.diving.gas.GasAtDepth;

@SuppressWarnings("serial")
public class DecoPlanPanel extends JPanel {
	private JPanel innerPanel = new JPanel();
	private JPanel outerInnerPanel = new JPanel();
	private JScrollPane scrollPane = new JScrollPane(outerInnerPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	public DecoPlanPanel() {
		outerInnerPanel.add(innerPanel);
		outerInnerPanel.add(Box.createVerticalGlue());
		setLayout(new BorderLayout());
		add(scrollPane);
	}
	
	public void setDecoPlan(DecompressionSchedule decompressionSchedule) {
		innerPanel.removeAll();
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
