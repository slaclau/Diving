package slaclau.diving.decompression.model;

public interface AccessoryModel<T> {
	public T get();
	public String getf();
	
	public void descend(double depth, double rate);
	public void stay(double time) throws ModelException;
	public void ascend(double depth, double rate);
}
