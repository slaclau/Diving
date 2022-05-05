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
package slaclau.diving.dive;

import java.util.ArrayList;

import slaclau.diving.gas.Gas;
import slaclau.diving.gas.GasAtDepth;

public class SimpleDive implements Dive {
	private ArrayList<Double> times = new ArrayList<Double>();
	private ArrayList<GasAtDepth> divePoints = new ArrayList<GasAtDepth>();
	private final double initialTime;
	private final GasAtDepth initialPoint;
	
	public ArrayList<Double> getTimes() {
		return times;
	}

	public void setTimes(ArrayList<Double> times) {
		this.times = times;
		duration = times.get(times.size() - 1);
	}

	public ArrayList<GasAtDepth> getDivePoints() {
		return divePoints;
	}

	public void setDivePoints(ArrayList<GasAtDepth> divePoints) {
		this.divePoints = divePoints;
	}
	
	private double duration = 0;

	private SimpleDive(ArrayList<Double> times, ArrayList<GasAtDepth> divePoints) {
		initialTime = times.get(0);
		initialPoint = divePoints.get(0);
		setTimes(times);
		setDivePoints(divePoints);
	}
	
	public SimpleDive(double time, GasAtDepth gas) {
		initialTime = time;
		duration = time;
		initialPoint = gas;
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

	@Override
	public Dive cloneAndReset() {
		return new SimpleDive(initialTime, initialPoint);
	}

	@Override
	public GasAtDepth getPoint(int i) {
		return divePoints.get(i);
	}

	@Override
	public double getTime(int i) {
		return times.get(i);
	}

	@Override
	public int getNumberOfPoints() {
		return divePoints.size();
	}

	double getInitialTime() {
		return initialTime;
	}

	GasAtDepth getInitialPoint() {
		return initialPoint;
	}
}
