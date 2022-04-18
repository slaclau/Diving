package slaclau.diving.decompression.userinterface.chart;

import javax.swing.*;

@SuppressWarnings("serial")
public class DiveChartControlPanel extends JPanel {
	private JPanel innerPanel = new JPanel();
	public DiveChartControlPanel(DiveChartPanel diveChartPanel) {
		super();
		add(innerPanel);
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		add(Box.createVerticalGlue());
		
		JCheckBox depthCheckBox = new JCheckBox("Show depth",true);
		JCheckBox ceilingCheckBox = new JCheckBox("Show ceiling",true);
		
		JCheckBox pulmonaryCheckBox = new JCheckBox("Show OTUs",false);
		
		JCheckBox cnsCheckBox = new JCheckBox("Show CNS load",false);
		JCheckBox gfCheckBox = new JCheckBox("Show GF",false);
		JCheckBox currentGradientCheckBox = new JCheckBox("Show current gradient",false);
		JCheckBox surfaceGradientCheckBox = new JCheckBox("Show surface gradient",false);
		
		JCheckBox oxygenCheckBox = new JCheckBox("Show oxygen partial pressure",false);
		JCheckBox nitrogenCheckBox = new JCheckBox("Show nitrogen partial pressure",false);
		JCheckBox heliumCheckBox = new JCheckBox("Show helium partial pressure",false);

		
		JCheckBox[] checkBoxes = {depthCheckBox, ceilingCheckBox, 
				pulmonaryCheckBox,
				cnsCheckBox, gfCheckBox, currentGradientCheckBox, surfaceGradientCheckBox,
				oxygenCheckBox, nitrogenCheckBox, heliumCheckBox};

		for ( JCheckBox checkBox : checkBoxes ) {
			checkBox.addItemListener(diveChartPanel);
			innerPanel.add(checkBox);
		}
	}
}
