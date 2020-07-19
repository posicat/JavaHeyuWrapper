package org.cattech.HeyuWrapper;

public class X10Action {

	public static final String STATE_ON = "on";
	public static final String STATE_OFF = "off";
	public static final String STATE_DIM = "dim";
	public static final String STATE_BRI = "bright";
	public static final String STATE_NONE = "none";
	public static final String SOURCE_RECEIVED = "rcvi";
	public static final String SOURCE_SENT = "sndc";
	public static final String SOURCE_NONE = "none";

	private int deltas[] = {0,1,6,11,17,22,27,32,38,43,48,53,59,64,69,74,80,85,90,95,100,100,100};

	private char houseCode = '_';
	private int unit = 0;
	private int delta = 0;
	private String action = STATE_NONE;
	private String source = SOURCE_NONE;
	
	public void setHousecode(char houseCode) throws HeyuWrapperException {
		if (houseCode < 'A' || houseCode > 'P') {
			throw (new HeyuWrapperException("Housecode is out of range A-P"));
		}
		this.houseCode = houseCode;
	}

	public char getHousecode() {
		return houseCode;
	}

	public void setUnit(int unit) throws HeyuWrapperException {
		if (unit < 1 || unit > 16) {
			throw (new HeyuWrapperException("Unit is out of range 1-16"));
		}
		this.unit = unit;
	}
	
	public int getUnit() {
		return unit;
	}
	
	public String getModule() {
			String module = String.valueOf(houseCode) + String.valueOf(unit);
			return module;
	}
	
	
	public int getPercentDelta() {
		return delta;
	}

	public void setPercentDelta(int delta) {
		this.delta = delta;
	}
	
	public void setScaledDelta(int delta) throws HeyuWrapperException {
		if (delta < 1 || delta > 22) {
			throw (new HeyuWrapperException("Delta is out of range 1-16"));
		}
		this.delta = deltas[delta];
	}

	public int getScaledDelta() {
		int i;
		for(i=0;deltas[i]<delta && i<deltas.length;i++) {}
		return i;
	}

	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public void setSource(String source) {
		this.source=source;
	}
	
	public String getSource() {
		return this.source;
	}

	@Override
	public String toString() {
		String strOut = "X10Action : {housecode="+String.valueOf(this.houseCode)+",unit="+this.unit+",action="+this.action+",delta="+delta+"}";
		return strOut;
	}
}
