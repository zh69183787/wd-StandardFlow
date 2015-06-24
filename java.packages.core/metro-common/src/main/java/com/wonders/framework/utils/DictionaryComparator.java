package com.wonders.framework.utils;

import java.util.Comparator;

import com.wonders.framework.auth.entity.Dictionary;

public class DictionaryComparator implements Comparator<Dictionary>{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public int compare(Dictionary f1, Dictionary f2) {
		if (f1.getOrdernum() > f2.getOrdernum())
		  {
		   return 1;
		  }
		  else if (f1.getOrdernum() == f2.getOrdernum())
		  {
		   return 0;
		  }
		  else
		  {
		   return -1;
		  }
	}

}
