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
package slaclau.diving.decompression.userinterface;

import javax.swing.*;

import slaclau.diving.decompression.userinterface.chart.DiveChartControlPanel;
import slaclau.diving.decompression.userinterface.chart.DiveChartPanel;
import slaclau.diving.decompression.userinterface.diveplan.*;
import slaclau.diving.decompression.userinterface.menu.MainMenuBar;

public class UserInterface {
	private JFrame jframe;
	private JSplitPane mainSplitPane, verticalSplitPane, chartSplitPane;	
	
	private DiveChartPanel diveChartPanel = new DiveChartPanel();
	private DiveChartControlPanel diveChartControlPanel = new DiveChartControlPanel(diveChartPanel);
	private ConsolePanel consolePanel = new ConsolePanel();
	private DivePlanMainPanel divePlanMainPanel = new DivePlanMainPanel(this);
	
	private JTabbedPane rightTabbedPane = new JTabbedPane();
	private JTabbedPane bottomTabbedPane = new JTabbedPane();
	
	public UserInterface() {
		jframe = new JFrame();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setJMenuBar(new MainMenuBar(this));
		
		rightTabbedPane.addTab("Chart options", diveChartControlPanel);
		
		bottomTabbedPane.addTab("Console", consolePanel);
		
		chartSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true, diveChartPanel, rightTabbedPane);
		chartSplitPane.setOneTouchExpandable(true);
		chartSplitPane.setResizeWeight(1);
		
		verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,chartSplitPane, bottomTabbedPane);
		verticalSplitPane.setOneTouchExpandable(true);
		verticalSplitPane.setResizeWeight(1);
		
		mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,divePlanMainPanel,verticalSplitPane);
		mainSplitPane.setOneTouchExpandable(true);
		jframe.add(mainSplitPane);
		jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);

		jframe.pack();
		
		jframe.setVisible(true);
	}
	
	public void println(String string) {
		consolePanel.println(string);
	}
	
	public DiveChartPanel getDiveChartPanel() {
		return diveChartPanel;
	}
	
	public void dispose() {
		jframe.dispose();
	}

	public JFrame getJframe() {
		return jframe;
	}
}
