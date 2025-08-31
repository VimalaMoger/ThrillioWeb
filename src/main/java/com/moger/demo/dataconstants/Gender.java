package com.moger.demo.dataConstants;

public enum Gender {	

	MALE(0),
	FEMALE(1),
	UNKNOWN(2);

	private Gender(int i) {
		this.i =i;
	}

	private int i;

	public int getI() {
		return i;
	}
}
