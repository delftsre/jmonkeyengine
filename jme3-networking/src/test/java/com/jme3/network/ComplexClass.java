package com.jme3.network;


public class ComplexClass implements java.io.Serializable {
	public int a = 1;
	public ComplexClass() {};
	public int method() { return 1; }
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + a;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComplexClass other = (ComplexClass) obj;
		if (a != other.a)
			return false;
		return true;
	}
	
}