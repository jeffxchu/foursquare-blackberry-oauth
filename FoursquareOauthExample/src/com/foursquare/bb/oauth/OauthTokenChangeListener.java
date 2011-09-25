package com.foursquare.bb.oauth;

/**
 * OAuth token change listener invoked when the token is obtained.
 * 
 * @author Jeff Hu (jeff4sq@gmail.com)
 */
public interface OauthTokenChangeListener {
        
    public void tokenChanged(String token);
        
}
