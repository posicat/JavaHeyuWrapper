package org.cattech.HeyuWrapperManualTest;

import org.cattech.HeyuWrapper.HeyuWrapper;
import org.cattech.HeyuWrapper.HeyuWrapperCallback;
import org.cattech.HeyuWrapper.X10Action;

public class TestHeyuWrapper {

	public static class WrapperCallback implements HeyuWrapperCallback, Runnable{ 
		public void heyuEventReceiver(X10Action receiveEvent) {
			ReceiveEvent(receiveEvent);
		}

		@Override
		public void run() {
			HeyuWrapper.registerListener(this);
		};
	}
	
	public static void main(String args[]) {
		
		Thread wrapperThread = new Thread(new WrapperCallback(),"Wrapper Callback");
		wrapperThread.start();
		
		System.out.println("Done with start");
	}

	public static void ReceiveEvent(X10Action receivedEvent) {
		System.out.println(receivedEvent.toString()); 		
	}
	
}
