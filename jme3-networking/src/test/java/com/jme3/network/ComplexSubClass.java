package com.jme3.network;


public class ComplexSubClass extends ComplexClass implements java.io.Serializable {
	public int a = 2;
	public ComplexSubClass() {};
	public int method() { return 2; }
	public int ownMethod() { return 1; }
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + a;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComplexSubClass other = (ComplexSubClass) obj;
		if (a != other.a)
			return false;
		return true;
	}
	
}