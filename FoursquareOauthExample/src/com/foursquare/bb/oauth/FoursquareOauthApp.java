package com.foursquare.bb.oauth;

import com.foursquare.bb.oauth.ui.OauthDisplayScreen;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.util.StringUtilities;

/**
 * 
 * 
 * @author Jeff Hu (jeff4sq@gmail.com)
 *
 */
public class FoursquareOauthApp extends UiApplication {

	public static final String CLIENT_ID    = "";
	public static final String CALLBACK_URL = "";
	
	public static final long GUID = StringUtilities.stringHashToLong(FoursquareOauthApp.class.getName() + ".GUID");
	
	/**
	 * Entry point for demo application
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		FoursquareOauthApp application = new FoursquareOauthApp();
		application.enterEventDispatcher();
	}
	
	public FoursquareOauthApp() {
		pushScreen(new OauthDisplayScreen());
	}
	
}
