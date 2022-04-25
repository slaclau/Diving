package slaclau.diving.decompression.model;

@SuppressWarnings("serial")
public class StopLengthException extends Exception {
	public String toString() {
		return "Decompression is taking too long, try a richer decompression gas";
	}
}
