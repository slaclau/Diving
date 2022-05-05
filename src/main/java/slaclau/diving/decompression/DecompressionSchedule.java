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
package slaclau.diving.decompression;

import java.util.ArrayList;

import slaclau.diving.gas.Gas;
import slaclau.diving.gas.GasAtDepth;

public class DecompressionSchedule {
	private ArrayList<Double> times = new ArrayList<Double>();
	private ArrayList<GasAtDepth> divePoints = new ArrayList<GasAtDepth>();
	private ArrayList<String> notes = new ArrayList<String>();
	private int numberOfStops = 0;

	public void addStop(double time, GasAtDepth gasAtDepth) {
		times.add(time);
		divePoints.add(gasAtDepth);
		notes.add("");
		numberOfStops++;
	}
	public void addStop(double time, GasAtDepth gasAtDepth, String note) {
		times.add(time);
		divePoints.add(gasAtDepth);
		notes.add(note);
		numberOfStops++;
	}
	
	public String toString() {
		String string = "Start of decompression:\n";
		for ( int i = 0 ; i < times.size() ; i++ ) {
			if( times.get(i) > 0 ) {
				string += (divePoints.get(i).getDepth() + " msw for " + times.get(i) + " minutes on " + (Gas) divePoints.get(i) + ( notes.get(i).equals("") ? "" : ", " + notes.get(i)) + "\n");
			}
		}
		string += "End of decompression";
		return string;
	}
	
	public double getDecoStopTime(int i) {
		return times.get(i);
	}
	public GasAtDepth getDecoDivePoint(int i) {
		return divePoints.get(i);
	}
	public int getNumberOfStops() {
		return numberOfStops;
	}
	public String getNote(int i) {
		return notes.get(i);
	}
}
