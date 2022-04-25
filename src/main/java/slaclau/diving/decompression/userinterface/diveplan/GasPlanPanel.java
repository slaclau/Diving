package slaclau.diving.decompression.userinterface.diveplan;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import slaclau.diving.gas.Gas;
import slaclau.diving.gas.GasAtDepth;
import slaclau.diving.gas.GasException;
import slaclau.diving.gas.Trimix;

@SuppressWarnings("serial")
public class GasPlanPanel extends JPanel implements ActionListener {
	private ArrayList<GasChoice> gases;
	private BottomGas bottomGas;
	private int numberOfGases;
	private JButton addButton, saveButton;
	private JPanel innerPanel = new JPanel();
	private JPanel outerInnerPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JScrollPane scrollPane = new JScrollPane(outerInnerPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	
	private DivePlanListener divePlanListener;
	
	private boolean init = false;
	
	public GasPlanPanel() {
		super();
		
		outerInnerPanel.add(innerPanel);
		outerInnerPanel.add(Box.createVerticalGlue());
		
		gases = new ArrayList<GasChoice>();
		numberOfGases = 0;
		
		setLayout(new BorderLayout());

		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		
	}
	
	public void addListenerAndInit(DivePlanListener divePlanListener) {
		this.divePlanListener = divePlanListener;
		if ( !init ) init();
	}
	
	private void init() {
		add(scrollPane);
		bottomGas = new BottomGas();
		innerPanel.add(bottomGas);
		
		addButton = new JButton("Add gas");
		addButton.setActionCommand("add");
		addButton.addActionListener(this);
		addButton.addActionListener(divePlanListener);
		saveButton = new JButton("Save gas plan");
		saveButton.setActionCommand("save");
		saveButton.addActionListener(this);
		buttonPanel.add(addButton);
		buttonPanel.add(saveButton);
		add(buttonPanel,BorderLayout.SOUTH);

		setVisible(true);
	}
	
	void addGas() {
		numberOfGases++;
		GasChoice gaschoice = new GasChoice(numberOfGases,this);
		gases.add(gaschoice);
		innerPanel.add(gaschoice);
	}
	
	class BottomGas extends JPanel implements ChangeListener {
		private JButton gasButton;
		private JLabel gasLabel;
		private JSpinner oxygenField, nitrogenField, heliumField;
		private JTextField depthField = new JTextField(5);

		
		private SpinnerNumberModel oxygenModel = new SpinnerNumberModel(21, 0, 100, 1);
		private SpinnerNumberModel nitrogenModel = new SpinnerNumberModel(79, 0, 100, 1);
		private SpinnerNumberModel heliumModel = new SpinnerNumberModel(0, 0, 100, 1);
		
		BottomGas() {
			super(new FlowLayout());
			
			gasLabel = new JLabel("Bottom gas");
			gasLabel.setPreferredSize(new Dimension(200,20));
			add(gasLabel);
			
			oxygenField = new JSpinner(oxygenModel);
			oxygenField.addChangeListener(divePlanListener);
			oxygenField.addChangeListener(this);
			add(oxygenField);
			
			nitrogenField = new JSpinner(nitrogenModel);
			nitrogenField.setEnabled(false);
			add(nitrogenField);
			
			heliumField = new JSpinner(heliumModel);
			heliumField.addChangeListener(divePlanListener);
			heliumField.addChangeListener(this);
			add(heliumField);
			
			depthField.setEnabled(false);
			add(depthField);
						
			gasButton = new JButton("Remove gas");
			gasButton.setEnabled(false);
			gasButton.setActionCommand("Bottom");
			gasButton.addActionListener(GasPlanPanel.this);
			add(gasButton);
		}
		
		int getOxygen() {
			return Integer.valueOf(oxygenField.getValue().toString() );
		}
		void setNitrogen(int nitrogen) {
			nitrogenField.setValue(nitrogen);
		}
		int getHelium() {
			return Integer.valueOf(heliumField.getValue().toString() );
		}
		@Override
		public void stateChanged(ChangeEvent ce) {
			oxygenModel.setMaximum(100 - getHelium() );
			heliumModel.setMaximum(100 - getOxygen() );
						
			int nitrogen = 100 - getOxygen() - getHelium();
			
			nitrogenField.setValue(nitrogen);
		}
	}
	
	class GasChoice extends JPanel implements ChangeListener, ActionListener {
		private GasAtDepth gas;
		private JButton gasButton;
		private JLabel gasLabel;
		private JSpinner oxygenField, nitrogenField, heliumField;
		private JTextField depthField = new JTextField(5);

		private SpinnerNumberModel oxygenModel = new SpinnerNumberModel(100, 0, 100, 1);
		private SpinnerNumberModel nitrogenModel = new SpinnerNumberModel(0, 0, 100, 1);
		private SpinnerNumberModel heliumModel = new SpinnerNumberModel(0, 0, 100, 1);
		int gasChoice;
		
		GasChoice(int i,ActionListener a)  {
			super(new FlowLayout());
			gasChoice = i;
			
			gasLabel = new JLabel("Decompression gas " + gasChoice);
			gasLabel.setPreferredSize(new Dimension(200,20));
			add(gasLabel);
			
			oxygenField = new JSpinner(oxygenModel);
			oxygenField.addChangeListener(this);
			add(oxygenField);
			
			nitrogenField = new JSpinner(nitrogenModel);
			nitrogenField.setEnabled(false);
			add(nitrogenField);
			
			heliumField = new JSpinner(heliumModel);
			heliumField.addChangeListener(this);
			add(heliumField);
			
			depthField.setText("6.0");
			depthField.addActionListener(this);
			add(depthField);
			
			gasButton = new JButton("Remove gas");
			gasButton.setActionCommand(((Integer) gasChoice).toString());
			gasButton.addActionListener(a);
			add(gasButton);
			
			try {
				gas = new GasAtDepth ( new Trimix(getOxygen() / 100d, getHelium() / 100d ), getDepth() );
			} catch (GasException e) {
				e.printStackTrace();
			}
		}
		void setGasNumber(int i) {
			gasChoice = i;
			gasLabel.setText("Decompression gas " + gasChoice);
			gasButton.setActionCommand(((Integer) gasChoice).toString());
		}
		int getOxygen() {
			return Integer.valueOf(oxygenField.getValue().toString() );
		}
		void setNitrogen(int nitrogen) {
			nitrogenField.setValue(nitrogen);
		}
		int getHelium() {
			return Integer.valueOf(heliumField.getValue().toString() );
		}
		double getDepth() {
			return Double.valueOf(depthField.getText());
		}
		GasAtDepth getGasSwitchPoint() {
			return gas;
		}
		@Override
		public void stateChanged(ChangeEvent ce) {
			oxygenModel.setMaximum(100 - getHelium() );
			heliumModel.setMaximum(100 - getOxygen() );
						
			int nitrogen = 100 - getOxygen() - getHelium();
			
			nitrogenField.setValue(nitrogen);
			try {
				depthField.setText( String.valueOf((double) Math.round( new Trimix(getOxygen() / 100d, getHelium() / 100d ).getMOD() ) ) );
			} catch (GasException e) {
				e.printStackTrace();
			}
			try {
				gas = new GasAtDepth ( new Trimix(getOxygen() / 100d, getHelium() / 100d ), getDepth() );
			} catch (GasException e) {
				e.printStackTrace();
			}
			divePlanListener.onUpdate();
		}
		@Override
		public void actionPerformed(ActionEvent ae) {
			gas.setDepth(getDepth());
			divePlanListener.onUpdate();
		}
	}
	
	public int getNumberOfGases() {
		return numberOfGases;
	}
	public Gas getBottomGas() {
		try {
			return new Trimix( bottomGas.getOxygen() / 100d , bottomGas.getHelium() / 100d );
		} catch (GasException e) {
			e.printStackTrace();
		}
		return null;
	}
	public GasAtDepth getGasSwitchPoint(int i) {
		return gases.get(i).getGasSwitchPoint();
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand() == "add") {
			addGas();
		} else if (ae.getActionCommand() == "save" ) {
			System.out.println("save");
		} else {
			int i = Integer.parseInt(ae.getActionCommand());
			innerPanel.remove(gases.get(i-1));
			gases.remove(i-1);
			for ( int j = 0 ; j < gases.size(); j++ ) {
				gases.get(j).setGasNumber( j + 1 );
			}

			numberOfGases--;
		}
		divePlanListener.onUpdate();
		revalidate();
		repaint();
	}
}
