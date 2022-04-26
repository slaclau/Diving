package slaclau.diving.decompression;

import slaclau.diving.decompression.userinterface.UserInterface;

public final class DecompressionPlanner {

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("sun.java2d.uiScale", "2");
		
		Runnable r = () -> new UserInterface();
		javax.swing.SwingUtilities.invokeLater(r);
	}
}
