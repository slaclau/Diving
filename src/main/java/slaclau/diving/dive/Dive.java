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

import slaclau.diving.gas.Gas;
import slaclau.diving.gas.GasAtDepth;

public interface Dive extends Cloneable {
	public GasAtDepth getPoint(double time) throws DiveTimeOutOfBoundsException;
	public void descend(double depth, double rate);
	public void stay(double time);
	public void ascend(double depth, double rate);
	public void switchGas(Gas gas);
	public GasAtDepth getCurrentPoint();
	public double getTime();
	public Dive clone();
	public Dive cloneAndReset();
	public GasAtDepth getPoint(int i);
	public double getTime(int i);
	public int getNumberOfPoints();
}
