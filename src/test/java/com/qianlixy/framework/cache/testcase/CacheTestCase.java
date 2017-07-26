package com.qianlixy.framework.cache.testcase;

import java.io.Serializable;

public class CacheTestCase implements Serializable {

	private static final long serialVersionUID = 2850239297916887128L;
	
	private int intVal = 1;
	private long longVal = 1L;
	private float floatVal = 1.1F;
	private double doubleVal = 1.1;
	private String strVal = "abcd";

	public int getIntVal() {
		return intVal;
	}

	public void setIntVal(int intVal) {
		this.intVal = intVal;
	}

	public long getLongVal() {
		return longVal;
	}

	public void setLongVal(long longVal) {
		this.longVal = longVal;
	}

	public float getFloatVal() {
		return floatVal;
	}

	public void setFloatVal(float floatVal) {
		this.floatVal = floatVal;
	}

	public double getDoubleVal() {
		return doubleVal;
	}

	public void setDoubleVal(double doubleVal) {
		this.doubleVal = doubleVal;
	}

	public String getStrVal() {
		return strVal;
	}

	public void setStrVal(String strVal) {
		this.strVal = strVal;
	}
	
}
