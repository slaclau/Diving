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
