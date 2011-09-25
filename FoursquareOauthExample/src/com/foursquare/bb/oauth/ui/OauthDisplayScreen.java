package com.foursquare.bb.oauth.ui;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.FlowFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.foursquare.bb.oauth.OauthTokenChangeListener;

/**
 * Display screen
 * @author Jeff Hu (jeff4sq@gmail.com)
 */
public class OauthDisplayScreen extends MainScreen implements FieldChangeListener, OauthTokenChangeListener {
        
    private String        mClientId;
    private String        mCallbackUrl;
    private Manager       mLayout;
    private EditField     mClientIdEditField;
    private EditField     mCallbackEditField;
    private EditField     mOauthUrlField;
    private ButtonField   mCreateButton;
    private ButtonField   mClearButton;
    private Field         mConnectButton;
    private CheckboxField mDebugCheckbox;

    public OauthDisplayScreen(String clientId, String callbackUrl) {
        super(USE_ALL_WIDTH|USE_ALL_HEIGHT|NO_VERTICAL_SCROLL|NO_HORIZONTAL_SCROLL);
        setTitle("Foursquare Oauth2 Demo App");
                
        mClientId    = clientId;
        mCallbackUrl = callbackUrl;
                
        buildUI();
    }
        
    private void buildUI() {
        mLayout = new VerticalFieldManager(VERTICAL_SCROLL|VERTICAL_SCROLLBAR);
                
        // Client id
        mClientIdEditField = new OauthEditField("", mClientId);
        mClientIdEditField.setChangeListener(this);
        Manager clientIdManager = createSectionManager("Edit Client Id", mClientIdEditField);
        mLayout.add(clientIdManager);
                
        // Callback url
        mCallbackEditField = new OauthEditField("", mCallbackUrl);
        mCallbackEditField.setChangeListener(this);
        Manager callbackManager = createSectionManager("Edit Callback Site", mCallbackEditField);
        mLayout.add(callbackManager);
                
        // Create request url button
        mCreateButton = new ButtonField("Create", ButtonField.HCENTER);         
        mCreateButton.setChangeListener(this);
                
        // Clear button to clear all editable fields
        mClearButton = new ButtonField("Clear", ButtonField.HCENTER);
        mClearButton.setChangeListener(this);
        Manager ffm = new FlowFieldManager(Manager.USE_ALL_WIDTH | Field.FIELD_HCENTER);
        ffm.add(mCreateButton);
        ffm.add(mClearButton);
        mLayout.add(ffm);
                
        // full oauth2 request url display field
        mOauthUrlField = new OauthEditField("", "", EditField.NON_FOCUSABLE|EditField.READONLY) {
            public void paint(Graphics graphics) {
                int oldColor = graphics.getColor();
                graphics.setColor(Color.GRAY);
                super.paint(graphics);
                graphics.setColor(oldColor);
            }
        };
        Manager oauthManager = createSectionManager("Your OAuth2 url looks like", mOauthUrlField);
        mLayout.add(oauthManager);
                
        mDebugCheckbox = new CheckboxField("enable browser debug window", false);
        mDebugCheckbox.setPadding(5, 8, 5, 8);
        mLayout.add(mDebugCheckbox);
                
        // Connect to foursquare button
        mConnectButton = new ConnectButton(0L);
        mConnectButton.setChangeListener(this);
        Manager ffm2 = new FlowFieldManager(Manager.USE_ALL_WIDTH | Field.FIELD_HCENTER);
        ffm2.add(mConnectButton);
        mLayout.add(ffm2);
                
        add(mLayout);
    }
        
    private Manager createSectionManager(String title, Field field) {
        Manager vfm = new VerticalFieldManager();
        LabelField titleField = new LabelField(title);
        titleField.setPadding(5, 0, 5, 0);
        vfm.add(titleField);
        vfm.add(field);
        vfm.setPadding(8, 8, 8, 8);
        return vfm;
    }
        
    private String getOauth2AuthenticateUrl() {
        return "https://foursquare.com/oauth2/authenticate" + 
               "?client_id=" + mClientIdEditField.getText() + 
               "&response_type=token" +
               "&redirect_uri=" + mCallbackEditField.getText();
    }

    public void fieldChanged(Field field, int context) {
        if (field == mClientIdEditField || field == mCallbackEditField) {
            // clean up oauth field
            mOauthUrlField.setText("");
            invalidate();
        }
        else if (field == mCreateButton) {
            mOauthUrlField.setText(getOauth2AuthenticateUrl());
            invalidate();
        }
        else if (field == mClearButton) {
            mClientIdEditField.setText("");
            mCallbackEditField.setText("");
            mOauthUrlField.setText("");
            invalidate();
        }
        else if (field == mConnectButton) {
            // push browser screen
            OauthBrowserScreen screen = new OauthBrowserScreen(getOauth2AuthenticateUrl());
            screen.setOauthListener(this);
            UiApplication.getUiApplication().pushScreen(screen);
        }
    }       
        
    /*
     * (non-Javadoc)
     * @see com.foursquare.bb.oauth.OauthTokenChangeListener#tokenChanged(java.lang.String)
     * 
     * Once the oauth token is found, we simply show it.
     */
    public void tokenChanged(String token) {
        synchronized(UiApplication.getEventLock()) {
            String message = "OAuth Token found! {" + token + "}";
            Dialog.alert(message);
        }
    }
        
    /*
     * (non-Javadoc)
     * @see net.rim.device.api.ui.Screen#isDirty()
     */
    public boolean isDirty() {
        return false;
    }
}
