package com.wonders.framework.attachment.entity;

public enum AttachType {
	INVITE(1,"招标"),COSTS(2,"造价"),CONTRACT(3,"合同"),LOG(4,"日志");
	
	private final int k;
	private final String v;
	private AttachType(int k,String v){
		this.k= k;
		this.v = v;
	}
	public int getK() {
		return k;
	}
	public String getV() {
		return v;
	}
	
	public static AttachType parseValue(String v){
		for(AttachType at:AttachType.values())
			if(at.getV().equals(v)) return at;
		return null;
	}
	
	public static AttachType parseKeyt(int k){
		for(AttachType at:AttachType.values())
			if(at.getK()==k) return at;
		return null;
	}
}
