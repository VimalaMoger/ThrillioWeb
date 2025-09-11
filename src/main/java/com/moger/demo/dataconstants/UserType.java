package com.moger.demo.dataconstants;

public enum UserType {
	
	User("user"),
	EDITOR("editor"),
	CHIEF_EDITOR("chiefEditor");

	private UserType(String type) {
		this.type =type;
	}
	private String type;
	
	public String getType() {
		return type;
	}

}
