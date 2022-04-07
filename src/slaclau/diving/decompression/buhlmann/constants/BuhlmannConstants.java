package slaclau.diving.decompression.buhlmann.constants;

import slaclau.diving.decompression.buhlmann.CompartmentOutOfRangeException;

public abstract class BuhlmannConstants {
	public abstract int getNumberOfCompartments();
	
	public abstract double getNitrogenHalfTime(int i) throws CompartmentOutOfRangeException;
	public abstract double getNitrogenA(int i) throws CompartmentOutOfRangeException;
	public abstract double getNitrogenB(int i) throws CompartmentOutOfRangeException;
	public abstract double getHeliumHalfTime(int i) throws CompartmentOutOfRangeException;
	public abstract double getHeliumA(int i) throws CompartmentOutOfRangeException;
	public abstract double getHeliumB(int i) throws CompartmentOutOfRangeException;
}
