package HeyuWrapper;

import HeyuWrapper.X10Action;

public class TestHeyuWrapper {

	private static class WrapperCallback implements HeyuWrapperCallback { 
		@Override
		public void heyuEventReceiver(X10Action receiveEvent) {
			ReceiveEvent(receiveEvent);
		};
	}
	
	public static void main(String args[]) {
		HeyuWrapperCallback cb = new WrapperCallback();
		HeyuWrapper.registerListener(cb);

	}

	public static void ReceiveEvent(X10Action receivedEvent) {
		System.out.println(receivedEvent.toString()); 		
	}
	
}
