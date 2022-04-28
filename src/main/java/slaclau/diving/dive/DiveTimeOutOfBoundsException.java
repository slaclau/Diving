package slaclau.diving.dive;

@SuppressWarnings("serial")
public class DiveTimeOutOfBoundsException extends Exception {
	private double time;
	private double duration;
	
	public DiveTimeOutOfBoundsException(double time, double duration) {
		this.time = time;
		this.duration = duration;
	}
	
	public String toString() {
		if (time < 0) return "Time (" + time + ") cannot be negative";
		else if (time > duration) return "Time (" + time + ") cannot exceed duration (" + duration + ")";
		else return "Dive time out of bounds";
	}
}
