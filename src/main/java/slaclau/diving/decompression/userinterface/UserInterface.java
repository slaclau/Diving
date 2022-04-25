package slaclau.diving.decompression.userinterface;

import javax.swing.*;

import slaclau.diving.decompression.userinterface.chart.DiveChartControlPanel;
import slaclau.diving.decompression.userinterface.chart.DiveChartPanel;
import slaclau.diving.decompression.userinterface.diveplan.*;

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
}
