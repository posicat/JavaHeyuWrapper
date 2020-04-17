package HeyuWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class HeyuWrapper {
	static Logger log = Logger.getLogger(HeyuWrapper.class.getName());

	private static String heyuPath = "/usr/local/bin/heyu";

	private static HeyuWrapperCallback callbackHandler;

	public void setHeyuPath(String heyuPath) {
		HeyuWrapper.heyuPath = heyuPath;
	}

	public static void registerListener(HeyuWrapperCallback callback) {
		callbackHandler = callback;
		String heyuCommandline = heyuPath + " monitor";

		log.info("Executing monitor : " + heyuCommandline);
		Runtime rt = Runtime.getRuntime();
		Process pr;
		X10Action receivedEvent = new X10Action();
		
		Pattern patAddr=Pattern.compile("(sndc|rcvi) addr unit\\s+(\\d+) : hu ([A-P])");
		Pattern patFunc=Pattern.compile("(sndc|rcvi) func\\s+(\\w+) : hc ([A-P])");
		Pattern patBriDim=Pattern.compile("(bright|dim) \\%(\\d+)");		
		try {
			pr = rt.exec(heyuCommandline);

			BufferedReader stdIn = new BufferedReader(new InputStreamReader(pr.getInputStream()));

			String s = null;
			while ((s = stdIn.readLine()) != null) {
				log.debug("STDOUT monitor : " + s);
				System.out.println(s);

				Matcher mat;
				mat = patAddr.matcher(s);
				if (mat.find()) {
					try {
						receivedEvent.setSource(mat.group(1));
						receivedEvent.setUnit(Integer.parseInt(mat.group(2)));
						receivedEvent.setHousecode(mat.group(3).charAt(0));
					} catch (HeyuWrapperException|NumberFormatException e) {
						log.error("Error processing X10 data : "+s,e);
					}
				}

				mat = patFunc.matcher(s);
				if (mat.find()) {
					try {
						receivedEvent.setSource(mat.group(1));
						receivedEvent.setAction(mat.group(2).toLowerCase());
						receivedEvent.setHousecode(mat.group(3).charAt(0));
						
						mat = patBriDim.matcher(s);
						if (mat.find()) {
							receivedEvent.setPercentDelta(Integer.parseInt(mat.group(2)));
						}
						callbackHandler.heyuEventReceiver(receivedEvent);
						receivedEvent = new X10Action();
					} catch (HeyuWrapperException e) {
						log.error("Error processing X10 data : "+s,e);
					}
				}
				
			}
		} catch (IOException e) {
			log.error("Heyu monitor exited", e);
			e.printStackTrace();
		}
	}

	public static void executeX10HeyuAction(X10Action sendEvent) throws HeyuWrapperException {
		String heyuCommandline = heyuPath + " turn " + sendEvent.getModule();

		switch (sendEvent.getAction()) {
		case X10Action.STATE_ON:
			heyuCommandline += " on";
			break;
		case X10Action.STATE_OFF:
			heyuCommandline += " off";
			break;
		case X10Action.STATE_DIM:
			heyuCommandline += " dim" + sendEvent.getScaledDelta();
			break;
		case X10Action.STATE_BRI:
			heyuCommandline += " off" + sendEvent.getScaledDelta();
			break;
		}

		log.info("Executing : " + heyuCommandline);
		Runtime rt = Runtime.getRuntime();
		Process pr;
		int retVal = 0;
		try {
			pr = rt.exec(heyuCommandline);
			retVal = pr.waitFor();
			pr.destroy();
		} catch (IOException | InterruptedException e) {
			log.error("Command failed to execute", e);
			throw new HeyuWrapperException("Could not execute heyu command");
		}
		if (retVal != 0) {
			log.error("Command exited with :" + retVal);
			throw new HeyuWrapperException("Heyu command exited with error : " + retVal);
		}
	}

}
