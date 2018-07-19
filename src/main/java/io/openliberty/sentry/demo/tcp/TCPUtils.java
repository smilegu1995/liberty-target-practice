package io.openliberty.sentry.demo.tcp;

public class TCPUtils {

	public static final String TARGETS = "targets";
	public static final String SHIP = "ship";
	
	public static TCPCommand convertRequestCmdStringToTCPCommand(String device, String cmd) {
		if (device.equals(TARGETS)) {
	        switch(cmd)
	        {
	            case "allup":
	                return TCPCommand.T_ALLUP;
	            case "alldown":
	            	return TCPCommand.T_ALLDOWN;
	            case "cycle":
	            	return TCPCommand.T_CYCLETARGETS;
	            case "target1":
	            	return TCPCommand.T_TARGET1;
	            case "target2":
	            	return TCPCommand.T_TARGET2;
	            case "target3":
	            	return TCPCommand.T_TARGET3;
	            case "target4":
	            	return TCPCommand.T_TARGET4;
	            case "target5":
	            	return TCPCommand.T_TARGET5;
	            default:
	                System.out.println("invalid request cmd, not TCPCommand is matched.");
	        }
		}

		return null;
	}
	
	public static String convertTCPCommandToString(TCPCommand tcpcmd) {
		switch(tcpcmd) {
			case T_GAMESTART:
				return "GSTR";
			case T_GAMESTOP:
				return "GG";
			case T_TXTEST:
				return "T_X";
			case T_TARGET1:
				return "T_T1";
			case T_TARGET2:
				return "T_T2";
			case T_TARGET3:
				return "T_T3";
			case T_TARGET4:
				return "T_T4";
			case T_TARGET5:
				return "T_T5";
			case T_ALLUP:
				return "T_AU";
			case T_ALLDOWN:
				return "T_AD";
			case T_CYCLETARGETS:
				return "T_C";
			case T_UPDATELASER:
			case T_UPDATEPIEZO:
			default:
				System.out.println("Should not reach here");
		}
		return null;
	}
}
