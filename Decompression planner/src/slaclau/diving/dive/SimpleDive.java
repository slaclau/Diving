package slaclau.diving.dive;

import java.util.ArrayList;

import slaclau.diving.gas.Gas;
import slaclau.diving.gas.GasAtDepth;

public class SimpleDive implements Dive {
	private ArrayList<Double> times = new ArrayList<Double>();
	private ArrayList<GasAtDepth> divePoints = new ArrayList<GasAtDepth>();
	public ArrayList<Double> getTimes() {
		return times;
	}

	public void setTimes(ArrayList<Double> times) {
		this.times = times;
	}

	public ArrayList<GasAtDepth> getDivePoints() {
		return divePoints;
	}

	public void setDivePoints(ArrayList<GasAtDepth> divePoints) {
		this.divePoints = divePoints;
	}
	
	private double duration = 0;

	public SimpleDive(ArrayList<Double> times, ArrayList<GasAtDepth> divePoints) {
		setTimes(times);
		setDivePoints(divePoints);
	}
	
	public SimpleDive(double time, GasAtDepth gas) {
		times.add(time);
		divePoints.add(gas);
	}
	
	public SimpleDive(double time, Gas gas) {
		this(time, gas.atDepth(0));
	}
	
	public SimpleDive(Gas gas) {
		this(0, gas);
	}
	
	@SuppressWarnings("unchecked")
	public SimpleDive clone() {
		return new SimpleDive( (ArrayList<Double>) getTimes().clone() , (ArrayList<GasAtDepth>) getDivePoints().clone() );
	}
	
	@Override
	public GasAtDepth getPoint(double time) throws DiveTimeOutOfBoundsException {
		if (time < 0 || time > duration) throw new DiveTimeOutOfBoundsException(time, duration);
		int index = 0;
		double oldTime;
		double newTime = times.get(index);
		do {
			oldTime = newTime;
			index++;
			newTime = times.get(index);
		} while (time > newTime);
		
		GasAtDepth oldGAD = divePoints.get(index - 1);
		GasAtDepth newGAD = divePoints.get(index);
		
		Gas gas = newGAD;
		
		double depth = oldGAD.getDepth() + (time - oldTime) / (newTime - oldTime) * (newGAD.getDepth() - oldGAD.getDepth());
		return gas.atDepth(depth);
	}

	private void addPoint(double time, GasAtDepth gasAtDepth) {
		duration = time;
		times.add(time);
		divePoints.add(gasAtDepth);
	}
	
	@Override
	public void descend(double depth, double rate) {
		int index = times.size() - 1;
		Gas gas = divePoints.get(index);
		double oldDepth = divePoints.get(index).getDepth();
		double time = times.get(index) + (depth - oldDepth) / rate;
		addPoint(time,gas.atDepth(depth));
	}

	@Override
	public void stay(double time) {
		int index = times.size() - 1;
		GasAtDepth gasAtDepth = divePoints.get(index);
		double newTime = times.get(index) + time;
		addPoint(newTime,gasAtDepth);
	}

	@Override
	public void ascend(double depth, double rate) {
		int index = times.size() - 1;
		Gas gas = divePoints.get(index);
		double oldDepth = divePoints.get(index).getDepth();
		double time = times.get(index) + (oldDepth - depth) / rate;
		addPoint(time,gas.atDepth(depth));
	}

	@Override
	public void switchGas(Gas gas) {
		int index = times.size() - 1;
		double depth = divePoints.get(index).getDepth();
		double time = times.get(index);
		addPoint(time,gas.atDepth(depth));
	}

	@Override
	public GasAtDepth getCurrentPoint() {
		int index = times.size() - 1;
		return divePoints.get(index);
	}
	
	@Override
	public double getTime() {
		return duration;
	}
}
