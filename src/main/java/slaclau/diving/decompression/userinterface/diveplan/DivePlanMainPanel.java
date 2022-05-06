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
package slaclau.diving.decompression.userinterface.diveplan;

import java.awt.Dimension;

import javax.swing.*;

import slaclau.diving.decompression.userinterface.UserInterface;

@SuppressWarnings("serial")
public class DivePlanMainPanel extends JSplitPane {
	private DivePlanListener listener;
	
	private UserInterface userInterface;
	
	private DivePlanPanel divePlanPanel = new DivePlanPanel();
	private GasPlanPanel gasPlanPanel = new GasPlanPanel();
	private DecoPlanPanel decoPlanPanel = new DecoPlanPanel();
	private ExtraInfoPanel extraInfoPanel = new ExtraInfoPanel();
	
	private JTabbedPane upperTabbedPane = new JTabbedPane();
	private JTabbedPane lowerTabbedPane = new JTabbedPane();
	
	public DivePlanMainPanel(UserInterface userInterface) {
		super(JSplitPane.VERTICAL_SPLIT,true);
		this.setTopComponent(upperTabbedPane);
		this.setBottomComponent(lowerTabbedPane);
		
		this.userInterface = userInterface;
		
		divePlanPanel.addListenerAndInit(listener);
		gasPlanPanel.addListenerAndInit(listener);
		
		upperTabbedPane.addTab("Dive plan", divePlanPanel);
		upperTabbedPane.addTab("Gas plan", gasPlanPanel);
		lowerTabbedPane.addTab("Decompression plan", decoPlanPanel);
		lowerTabbedPane.addTab("Additional information", extraInfoPanel);
		
		upperTabbedPane.setPreferredSize(new Dimension( (int) upperTabbedPane.getPreferredSize().getWidth(), 500 ) );

		listener.onUpdate();
	}

	public DivePlanPanel getDivePlanPanel() {
		return divePlanPanel;
	}

	public GasPlanPanel getGasPlanPanel() {
		return gasPlanPanel;
	}

	public DecoPlanPanel getDecoPlanPanel() {
		return decoPlanPanel;
	}

	public ExtraInfoPanel getExtraInfoPanel() {
		return extraInfoPanel;
	}

	public UserInterface getUserInterface() {
		return userInterface;
	}
}
