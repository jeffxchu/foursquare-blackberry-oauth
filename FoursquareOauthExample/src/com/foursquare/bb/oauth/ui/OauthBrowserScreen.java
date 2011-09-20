package com.foursquare.bb.oauth.ui;

import javax.microedition.io.InputConnection;

import net.rim.device.api.browser.field2.BrowserField;
import net.rim.device.api.browser.field2.BrowserFieldConfig;
import net.rim.device.api.browser.field2.BrowserFieldListener;
import net.rim.device.api.browser.field2.BrowserFieldNavigationRequestHandler;
import net.rim.device.api.browser.field2.BrowserFieldRequest;
import net.rim.device.api.browser.field2.BrowserFieldResourceRequestHandler;
import net.rim.device.api.browser.field2.ProtocolController;
import net.rim.device.api.io.transport.ConnectionFactory;
import net.rim.device.api.io.transport.TransportInfo;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import org.w3c.dom.Document;

import com.foursquare.bb.oauth.Log4Device;
import com.foursquare.bb.oauth.OauthTokenChangeListener;

/**
 * 
 * 
 * @author Jeff Hu
 *
 */
public class OauthBrowserScreen extends MainScreen {
	
	protected int[] preferredTransportTypes = { TransportInfo.TRANSPORT_TCP_WIFI, TransportInfo.TRANSPORT_TCP_CELLULAR, TransportInfo.TRANSPORT_WAP2 };
	protected int[] disallowedTransportTypes = { TransportInfo.TRANSPORT_BIS_B, TransportInfo.TRANSPORT_MDS, TransportInfo.TRANSPORT_WAP };
	
	private String                 mUrl;
	private BrowserFieldConfig     mConfig;
	private BrowserField           mBrowserField;
	
	private Field   mLoadingField;
	private OauthTokenChangeListener mTokenListener;
	private String mLoadingText;
	
	public OauthBrowserScreen(String url) {
		this(url, "Connecting to foursquare....");
	}
	
	public OauthBrowserScreen(String url, String loadingText) {
		super(VERTICAL_SCROLL|VERTICAL_SCROLLBAR|HORIZONTAL_SCROLL|HORIZONTAL_SCROLLBAR);
		setTitle("Foursquare Oauth Flow");
		
		mUrl = url;
		mLoadingText = loadingText;
		
		// initialize browser field configuration including browser connection, request handler etc
		initBrowserConfig();
		
		buildUI();
	}
	
	private void buildUI() {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));

		mLoadingField = new AnimationLabelField(mLoadingText);
		add(mLoadingField);
	}
	
	private void initBrowserConfig() {
		mConfig = new BrowserFieldConfig();
		mConfig.setProperty(BrowserFieldConfig.ALLOW_CS_XHR, Boolean.TRUE);
		mConfig.setProperty(BrowserFieldConfig.JAVASCRIPT_ENABLED, Boolean.TRUE);		
		mConfig.setProperty(BrowserFieldConfig.USER_SCALABLE, Boolean.TRUE);
		mConfig.setProperty(BrowserFieldConfig.NAVIGATION_MODE, BrowserFieldConfig.NAVIGATION_MODE_POINTER);
		mConfig.setProperty(BrowserFieldConfig.VIEWPORT_WIDTH, new Integer(Display.getWidth()));
		
		ConnectionFactory conn = new ConnectionFactory();
		conn.setPreferredTransportTypes(preferredTransportTypes);
		conn.setDisallowedTransportTypes(disallowedTransportTypes);
		mConfig.setProperty(BrowserFieldConfig.CONNECTION_FACTORY, conn);
		
		BrowserFieldListener bfListener = new BrowserFieldListener() {
			public void documentLoaded(BrowserField browserField, Document document) throws Exception {
				Log4Device.log("### document loaded: " + document.getDocumentURI());
				if (shouldFetchContent(document.getDocumentURI())) {
					showContent();
				}
			}
		};
		mBrowserField = new BrowserField(mConfig);
		mBrowserField.addListener(bfListener);
		
		OAuthTransportRequestHandler requestHandler = new OAuthTransportRequestHandler(mBrowserField);
		((ProtocolController) mBrowserField.getController()).setNavigationRequestHandler("https", requestHandler);
		((ProtocolController) mBrowserField.getController()).setResourceRequestHandler("https", requestHandler);
	}
	
	protected void onUiEngineAttached(boolean attached) {
		super.onUiEngineAttached(attached);
		if (attached) {
			mBrowserField.requestContent(mUrl);
		}
	}
	
	public void setOauthListener(OauthTokenChangeListener listener) {
		mTokenListener = listener;
	}
	
	protected void showContent() {
		synchronized(UiApplication.getEventLock()) {
			if (mLoadingField.getScreen() != null) {
				delete(mLoadingField);
			}
			if (mBrowserField.getScreen() == null) {
				add(mBrowserField);
			}
		}
	}
	
	protected void showLoading() {
		synchronized(UiApplication.getEventLock()) {
			if (mLoadingField.getScreen() == null) {
				add(mLoadingField);
			}
			if (mBrowserField.getScreen() != null) {
				delete(mBrowserField);
			}
		}
	}
	
	protected boolean onSavePrompt() {
		return false;
	}
	
	private boolean shouldFetchContent(String url) {
		String token = getOAuthToken(url);
		boolean ret = false;
		if (token == null || token.trim().length() == 0) {
			ret = true;
		} else {
			Screen active = UiApplication.getUiApplication().getActiveScreen();
			if (active instanceof OauthBrowserScreen && active.isDisplayed()) {
				// dismiss browser screen, show OAuth Token
				synchronized(UiApplication.getEventLock()) {
					UiApplication.getUiApplication().popScreen(active);
				}
			}
			if (mTokenListener != null) {
				mTokenListener.tokenChanged(token);
			}
			ret = false;
		}
		return ret;
	}
	
	private String getOAuthToken(String url) {
		String token = null;
		if (url != null && url.trim().length() != 0) {
			int startIndex = url.indexOf("#access_token=");
			if (startIndex > -1) {
				startIndex++;
				int endIndex = url.length();
				token = url.substring(url.indexOf('=', startIndex) + 1, endIndex);
			}
		}
		return token;
	}
	
	/**
	 * 
	 * OAuth transport request handler
	 *
	 */
	private class OAuthTransportRequestHandler implements BrowserFieldNavigationRequestHandler, BrowserFieldResourceRequestHandler {

		private BrowserField mBf;
		
		public OAuthTransportRequestHandler(BrowserField bf) {
			mBf = bf;
		}
		
		/*
		 * (non-Javadoc)
		 * @see net.rim.device.api.browser.field2.BrowserFieldResourceRequestHandler#handleResource(net.rim.device.api.browser.field2.BrowserFieldRequest)
		 */
		public InputConnection handleResource(BrowserFieldRequest request) throws Exception {
			Log4Device.log("### handleResource: " + request.getURL());
			InputConnection conn = null;
			if (shouldFetchContent(request.getURL())) {
				conn = mBf.getConnectionManager().makeRequest(request);
			}
			return conn;
		}

		/*
		 * (non-Javadoc)
		 * @see net.rim.device.api.browser.field2.BrowserFieldNavigationRequestHandler#handleNavigation(net.rim.device.api.browser.field2.BrowserFieldRequest)
		 */
		public void handleNavigation(BrowserFieldRequest request) throws Exception {
			Log4Device.log("### navigation: " + request.getURL());
			request.setURL(request.getURL());
			mBf.displayContent(handleResource(request), request.getURL());			
		}
		
	} // Class OAuthTransportRequestHandler 

} // Class OauthBrowserScreen
