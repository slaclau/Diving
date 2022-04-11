package slaclau.diving.decompression.userinterface;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.*;

import slaclau.diving.decompression.userinterface.DivePlanPanel.Level;

@SuppressWarnings("serial")
public class DiveChartControlPanel extends JPanel {
	private JPanel innerPanel = new JPanel();
	DiveChartControlPanel(DiveChartPanel diveChartPanel) {
		super();
		add(innerPanel);
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		add(Box.createVerticalGlue());
		
		JCheckBox depthCheckBox = new JCheckBox("Show depth",true);
		JCheckBox ceilingCheckBox = new JCheckBox("Show ceiling",true);
		JCheckBox pulmonaryCheckBox = new JCheckBox("Show OTUs",false);
		JCheckBox cnsCheckBox = new JCheckBox("Show CNS%",false);

		depthCheckBox.addItemListener(diveChartPanel);
		ceilingCheckBox.addItemListener(diveChartPanel);
		pulmonaryCheckBox.addItemListener(diveChartPanel);
		cnsCheckBox.addItemListener(diveChartPanel);

		innerPanel.add(depthCheckBox);
		innerPanel.add(ceilingCheckBox);
		innerPanel.add(pulmonaryCheckBox);
		innerPanel.add(cnsCheckBox);
	}
}
