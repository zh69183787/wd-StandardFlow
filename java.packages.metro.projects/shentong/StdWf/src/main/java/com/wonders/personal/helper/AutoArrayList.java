package com.wonders.personal.helper;
import java.util.ArrayList;

import org.apache.commons.beanutils.BeanUtils;

import com.wonders.personal.entity.StdApply;

public class AutoArrayList<T> extends ArrayList<T> {  
      
    private Class<T> itemClass;  
    private StdApply stdApply;
      
    public AutoArrayList(Class<T> itemClass, StdApply stdApply) {  
        this.itemClass = itemClass;
        this.stdApply = stdApply;
    }  
      
    public T get(int index) {  
        try {  
            while (index >= size()) {  
            	T item = itemClass.newInstance();
            	BeanUtils.copyProperty(item, "stdApply", stdApply);
                add(item);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return super.get(index);  
    }  
}  