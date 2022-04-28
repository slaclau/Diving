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
	public void v();
}
