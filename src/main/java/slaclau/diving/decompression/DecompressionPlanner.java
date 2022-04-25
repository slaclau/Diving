package slaclau.diving.decompression;

import slaclau.diving.decompression.userinterface.UserInterface;

public final class DecompressionPlanner {

	public static void main(String[] args) throws InterruptedException {
		Runnable r = () -> new UserInterface();
		javax.swing.SwingUtilities.invokeLater(r);
	}
}
