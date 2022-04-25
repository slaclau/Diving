package slaclau.diving.decompression.model.buhlmann;

@SuppressWarnings("serial")
public class CompartmentOutOfRangeException extends Exception {
	private int compartmentNumber;
	private int numberOfCompartments;
	
	public CompartmentOutOfRangeException(int compartmentNumber, int numberOfCompartments) {
		this.compartmentNumber = compartmentNumber;
		this.numberOfCompartments = numberOfCompartments;
	}
	
	public String toString() {
		return "compartment out of range exception: compartment " + compartmentNumber + " of " + numberOfCompartments;
	}
}
