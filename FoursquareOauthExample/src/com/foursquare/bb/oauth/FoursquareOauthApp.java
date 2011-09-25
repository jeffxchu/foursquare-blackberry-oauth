package com.foursquare.bb.oauth;

import net.rim.device.api.ui.UiApplication;

import com.foursquare.bb.oauth.ui.OauthDisplayScreen;

/**
 * https://developer.foursquare.com/docs/oauth.html
 * https://foursquare.com/oauth/
 * 
 * @author Jeff Hu (jeff4sq@gmail.com)
 */
public class FoursquareOauthApp extends UiApplication {

    /*
     * Default client id for the testing purpose
     * Your can register your application client id at https://foursquare.com/oauth/register 
     */
    public static final String TEST_CLIENT_ID    = "CTHTLACET0NMF4BKB1KHXY11BNWVJZ0UIGQFKXW4LWPJ3LXE";
        
    /*
     * Default callback (redirect) url for the testing purpose. 
     * This should be your application url.
     */
    public static final String TEST_CALLBACK_URL = "http://www.google.com";
        
        
    /**
     * Entry point for demo application
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        FoursquareOauthApp application = new FoursquareOauthApp();
        application.enterEventDispatcher();
    }
        
    public FoursquareOauthApp() {
        pushScreen(new OauthDisplayScreen(TEST_CLIENT_ID, TEST_CALLBACK_URL));
    }
        
}
