package com.wonders.framework.entity;

public enum SearchType {
	EQ(1,"eq"),LIKE(2,"like"),GT(3,"gt"),LT(4,"lt"),GE(5,"ge"),
	LE(6,"le"),NE(7,"ne"),OR(8,"or"),NNULL(9,"nnull"),NB(10,"nb"),
	ISNULL(11,"isNull"),ISEMPTY(12,"isEmpty");
	
	private final int key;
	private final String value;
	
	SearchType(int key,String value){
		this.key = key;
		this.value = value;
	}

	public int getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
	
	public static SearchType parseValue(String value){
		SearchType[] sts = SearchType.values();
		for(SearchType st:sts){
			if(st.getValue().equalsIgnoreCase(value)) return st;
		}
		return null;
	}
	
	public static SearchType parseKey(int key){
		SearchType[] sts = SearchType.values();
		for(SearchType st:sts){
			if(st.getKey() == key) return st;
		}
		return null;
	}
	
}
