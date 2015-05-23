package edu.sjsu.cmpe.cache.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

public class Client {
	private static CacheServiceInterface c1 = null;
	private static CacheServiceInterface c2 = null;
	private static CacheServiceInterface c3 = null;
	
    public static void main(String[] args) {
    	try {
    		System.out.println("Starting the Cache Client...");
    		
            c1 = new DistributedCacheService("http://localhost:3000");
            c2 = new DistributedCacheService("http://localhost:3001");
            c3 = new DistributedCacheService("http://localhost:3002");
            
	    	if (args.length > 0) {
	    		if (args[0].equals("write")) {
	    			write();
	    		} else if (args[0].equals("read")) {
	    			CRDTClient.readOnRepair(c1, c2, c3);
	    		}
	    	}
	    	
	    	System.out.println("Exiting the Cache Client...");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}        
    }
    
    public static void write() throws Exception {       
        long key = 1;
        String value = "a";
        
        Future<HttpResponse<JsonNode>> futRes1 = c1.put(key, value);        
        Future<HttpResponse<JsonNode>> futRes2 = c2.put(key, value);
        Future<HttpResponse<JsonNode>> futRes3 = c3.put(key, value);
        
        final CountDownLatch countDownLatch = new CountDownLatch(3);
        
        try {
        	futRes1.get();
        } catch (Exception e) {
        	//e.printStackTrace();
        } finally {
        	countDownLatch.countDown();
        }
        
        try {
        	futRes2.get();
        } catch (Exception e) {
        	//e.printStackTrace();
        } finally {
        	countDownLatch.countDown();
        }
        
        try {
        	futRes3.get();
        } catch (Exception e) {
        	//e.printStackTrace();
        } finally {
        	countDownLatch.countDown();
        }

        countDownLatch.await();
        
        if (DistributedCacheService.successCount.intValue() < 2) {	        	
        	c1.delete(key);
        	c2.delete(key);
        	c3.delete(key);
        } else {
        	c1.get(key);
        	c2.get(key);
        	c3.get(key);
        	Thread.sleep(1000);
        	System.out.println("Result from the Server A is: " + c1.getValue());
    	    System.out.println("Result from the Server B is: " + c2.getValue());
    	    System.out.println("Result from the Server C is: " + c3.getValue());
        }
        DistributedCacheService.successCount = new AtomicInteger();
    }
}
