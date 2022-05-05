/* Copyright (C) 2022 Sebastien Laclau
   
   This file belongs to the Diving project.
   
   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
   
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   
   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>. */
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
