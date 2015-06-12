package com.google.code.fqueue;

import java.security.SecureRandom;
import java.util.concurrent.CountDownLatch;

import org.junit.Assert;

public class FSQueueTest2 {

	public static void main(String[] args) throws Exception {
		final FQueue queue = new FQueue("C:\\Users\\lucifer\\Downloads\\db", 1024);
		final int len = 247;
        final StringBuilder message = new StringBuilder(len);
        for (int i = 0;i < len; i++) {
        	message.append(new SecureRandom().nextInt(9));
        }
        final byte[] bytes = message.toString().getBytes();
        final int counter = 1000000;
        final CountDownLatch cdl = new CountDownLatch(counter);
//        new Thread(new Runnable() {
//			@Override
//			public void run() {
//		        for (int i = 0; i < counter; i++) {
//		            queue.add(bytes);
//		        }
//			}
//		}).start();
        
        
        new Thread(new Runnable() {
			@Override
			public void run() {
		        while (true) {
		            final byte[] data = queue.poll();
		            if (data == null) {
		            	System.err.println("**************** poll() null *******************");
		            	continue;
		            }
		            
		            Assert.assertEquals(new String(data), "0304215485540872728364052433527215710772735487616770574081415328534555048442786220382616545284667481646611415685446713577746550552008455028403056682266871187653121858182682110554185737455684315862721252660065058420833831187010361345232161355525344");
		            
		            cdl.countDown();
		        }
			}
		}).start();
        
        cdl.await();
	}

}
