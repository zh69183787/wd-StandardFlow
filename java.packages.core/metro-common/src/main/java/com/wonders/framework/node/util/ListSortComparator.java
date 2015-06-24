package com.wonders.framework.node.util;

import java.util.Comparator;

import com.wonders.framework.node.entity.NodeAttr;
import com.wonders.framework.node.entity.NodeBase;

public class ListSortComparator implements Comparator<Object>{
	
	public static int ASC = 1;
	public static int DESC = -1;
	
	protected int oderValue;
	
	public ListSortComparator(){
		oderValue = ListSortComparator.ASC;
	}
	
	public ListSortComparator(int value){
		oderValue = value;
	}

	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		if(o1 != null && o2 != null){
			if(o1 instanceof NodeBase && o2 instanceof NodeBase){
				return (((NodeBase)o1).getOrderNum()-((NodeBase)o2).getOrderNum())*oderValue;
			}
			if(o1 instanceof NodeAttr && o2 instanceof NodeAttr){
				return (((NodeAttr)o1).getOrderNum()-((NodeAttr)o2).getOrderNum())*oderValue;
			}
			return 0;
		}else{
			if(o1 == null && o2 != null){
				return -1;
			}else{
				if(o1 != null && o2 == null){
					return 1;
				}
			}
		}
		
		return 0;
	}

}
