package slaclau.diving.decompression.userinterface.diveplan;

import java.awt.Dimension;

import javax.swing.*;

import slaclau.diving.decompression.userinterface.UserInterface;

@SuppressWarnings("serial")
public class DivePlanMainPanel extends JSplitPane {
	private DivePlanListener listener;
	
	private DivePlanPanel divePlanPanel = new DivePlanPanel();
	private GasPlanPanel gasPlanPanel = new GasPlanPanel();
	private DecoPlanPanel decoPlanPanel = new DecoPlanPanel();
	
	private JTabbedPane upperTabbedPane = new JTabbedPane();
	private JTabbedPane lowerTabbedPane = new JTabbedPane();
	
	public DivePlanMainPanel(UserInterface userInterface) {
		super(JSplitPane.VERTICAL_SPLIT,true);
		this.setTopComponent(upperTabbedPane);
		this.setBottomComponent(lowerTabbedPane);
		
		listener = new DivePlanListener(divePlanPanel, gasPlanPanel, decoPlanPanel, userInterface);
		divePlanPanel.addListenerAndInit(listener);
		gasPlanPanel.addListenerAndInit(listener);
		
		upperTabbedPane.addTab("Dive plan", divePlanPanel);
		upperTabbedPane.addTab("Gas plan", gasPlanPanel);
		lowerTabbedPane.addTab("Decompression plan", decoPlanPanel);
		
		upperTabbedPane.setPreferredSize(new Dimension( (int) upperTabbedPane.getPreferredSize().getWidth(), 500 ) );

		listener.onUpdate();
	}
}
