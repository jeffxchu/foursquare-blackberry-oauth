package com.foursquare.bb.oauth;

import java.io.UnsupportedEncodingException;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.EventLogger;
import net.rim.device.api.util.StringUtilities;

/**
 * Event logger for the sample app
 * 
 * @author Jeff Hu (jeff4sq@gmail.com)
 */
public class Log4Device {

    private static long GUID = 0L;
        
    private static final String APPNAME = "4sqOauth[3party]";
        
    static {
        String classname = Log4Device.class.getName();
        String guidstr   = classname + ".GUID";
        GUID             = StringUtilities.stringHashToLong(guidstr);
                
        // register event logger
        EventLogger.register(GUID, APPNAME, EventLogger.ALWAYS_LOG);
    }
        
    private Log4Device() { /* make class singleton */ }

    public static void log(String message) {
        byte[] data = message.getBytes();
        if (DeviceInfo.isSimulator()) {
            try {
                System.out.println(new String(data, "UTF-8"));
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else {
            EventLogger.logEvent(GUID, data, EventLogger.ALWAYS_LOG);
        }
    }
}
