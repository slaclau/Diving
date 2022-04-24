package slaclau.diving.decompression.userinterface.diveplan;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.*;

import slaclau.diving.decompression.model.ModelledDive.DataRecord;

@SuppressWarnings("serial")
public class ExtraInfoPanel extends JPanel {
	private JPanel innerPanel = new JPanel();
		
	public ExtraInfoPanel() {
		add(innerPanel);
	}
	
	public void setInfo(ArrayList<DataRecord<String>> list) {
		innerPanel.removeAll();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		
		for ( DataRecord<?> record : list ) {			
			addLine(record.getLabel(), record.getValue() );
		}
	}
	
	private <T> void addLine(String label, T value) {
		JPanel line = new JPanel();
		innerPanel.add(line);
		
		JLabel jlabel, valueLabel;
		
		jlabel = new JLabel( label );
		jlabel.setPreferredSize(new Dimension(400,20));
		
		valueLabel = new JLabel( value.toString() );
		valueLabel.setPreferredSize(new Dimension(100,20));

		line.add(jlabel);
		line.add(valueLabel);
	}
}
