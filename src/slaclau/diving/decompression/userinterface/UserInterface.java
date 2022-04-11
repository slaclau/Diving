package slaclau.diving.decompression.userinterface;

import java.awt.Dimension;

import javax.swing.*;

public class UserInterface {
	private JFrame jframe;
	private JSplitPane mainSplitPane, verticalSplitPane, chartSplitPane;
	
	private DivePlanPanel divePlanPanel = new DivePlanPanel();
	private DiveChartPanel diveChartPanel = new DiveChartPanel();
	private DiveChartControlPanel diveChartControlPanel = new DiveChartControlPanel(diveChartPanel);
	private ConsolePanel consolePanel = new ConsolePanel();
	
	private JTabbedPane rightTabbedPane = new JTabbedPane();
	private JTabbedPane leftTabbedPane = new JTabbedPane();
	private JTabbedPane bottomTabbedPane = new JTabbedPane();

	
	public UserInterface() {
		jframe = new JFrame();
		jframe.setSize(1000, 1000);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		rightTabbedPane.addTab("Chart options", diveChartControlPanel);
		
		leftTabbedPane.addTab("Dive plan", divePlanPanel);
		
		bottomTabbedPane.addTab("Console", consolePanel);
		
		chartSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true, diveChartPanel, rightTabbedPane);
		chartSplitPane.setOneTouchExpandable(true);
		chartSplitPane.setResizeWeight(1);
		
		verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,chartSplitPane, bottomTabbedPane);
		verticalSplitPane.setOneTouchExpandable(true);
		
		mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,true,leftTabbedPane,verticalSplitPane);
		mainSplitPane.setOneTouchExpandable(true);
		jframe.add(mainSplitPane);
		jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
		

		diveChartPanel.addDepthPoint(0,0);
		diveChartPanel.addDepthPoint(40,2);
		diveChartPanel.addDepthPoint(40,90);

		jframe.pack();
		
		jframe.setVisible(true);
	}
	
	public void println(String string) {
		consolePanel.println(string);
	}
}
