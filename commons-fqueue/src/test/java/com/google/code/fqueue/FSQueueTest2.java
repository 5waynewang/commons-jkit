package com.google.code.fqueue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;

public class FSQueueTest2 {

	public static void main(String[] args) throws Exception {
		final FQueue queue = new FQueue("C:\\Users\\lucifer\\Downloads\\db", 1024);
        final String message = "1234567890";
        final byte[] bytes = message.getBytes();
        final int counter = 1000000;
        final CountDownLatch cdl = new CountDownLatch(counter);
        new Thread(new Runnable() {
			@Override
			public void run() {
		        for (int i = 0; i < counter; i++) {
		            queue.add(bytes);
		            
		            try {
						TimeUnit.MILLISECONDS.sleep(200);
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
		        }
			}
		}).start();
        
        
        new Thread(new Runnable() {
			@Override
			public void run() {
		        while (true) {
		            final byte[] data = queue.poll();
		            if (data == null) {
		            	continue;
		            }
		            
		            Assert.assertEquals(new String(data), message);
		            
		            cdl.countDown();
		        }
			}
		}).start();
        
        cdl.await();
	}

}
