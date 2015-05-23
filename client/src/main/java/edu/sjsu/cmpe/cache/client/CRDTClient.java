package edu.sjsu.cmpe.cache.client;

import java.util.HashMap;
import java.util.Map;

public class CRDTClient {
    public static void readOnRepair(CacheServiceInterface p1, CacheServiceInterface p2, 
    		CacheServiceInterface p3) throws Exception {
    	CacheServiceInterface csi1  = p1;
    	CacheServiceInterface csi2  = p2;
    	CacheServiceInterface csi3  = p3;
    	
        long key = 1;
        String value = "a";
        
        csi1.put(key, value);
        csi2.put(key, value);
        csi3.put(key, value);
        
        System.out.println("Step : Setting the value of a");
        Thread.sleep(30000);
        
        csi1.get(1);
	    csi2.get(1);
	    csi3.get(1);
	        
	    System.out.println("Step : Setting the value of a");
	    Thread.sleep(1000);
	    
	    System.out.println("Result from the Server A is: " + csi1.getValue());
	    System.out.println("Result from the Server B is: " + csi2.getValue());
	    System.out.println("Result from the Server C is: " + csi3.getValue());
        
        value = "b";
        csi1.put(key, value);
        csi2.put(key, value);
        csi3.put(key, value);
        
        System.out.println("Step : Setting the value of b");
        Thread.sleep(30000);
	        
	    csi1.get(1);
	    csi2.get(1);
	    csi3.get(1);
	        
	    System.out.println("Step : Getting the value of b");
	    Thread.sleep(1000);
	    
	    System.out.println("Result from the Server A is: " + csi1.getValue());
	    System.out.println("Result from the Server B is: " + csi2.getValue());
	    System.out.println("Result from the Server C is: " + csi3.getValue());
	        
	    String[] values = {csi1.getValue(), csi2.getValue(), csi3.getValue()};
	    
	    Map<String, Integer> hm = new HashMap<String, Integer>();
	    String mj = null;
	    for (String val : values) {
	        Integer count = hm.get(val);	        
	        hm.put(val, count != null ? count+1 : 1);
	        if (hm.get(val) > values.length / 2) {
	        	mj = val;
	        	break;
	        }	
	    }
	    
	    csi1.put(key, mj);
        csi2.put(key, mj);
        csi3.put(key, mj);
        
        System.out.println("Step : Correcting the value of b ");
	    Thread.sleep(1000);
	    
	    csi1.get(key);
        csi2.get(key);
        csi3.get(key);
        
        System.out.println("Step : Getting the value of b again ");
	    Thread.sleep(1000);
	    
	    System.out.println("Result from the Server A is: " + csi1.getValue());
	    System.out.println("Result from the Server B is: " + csi2.getValue());
	    System.out.println("Result from the Server C is: " + csi3.getValue());
    }
}
