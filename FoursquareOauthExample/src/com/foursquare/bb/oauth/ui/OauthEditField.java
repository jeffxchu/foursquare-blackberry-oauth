package com.foursquare.bb.oauth.ui;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.EditField;

/**
 * Simple customized edit field
 * @author Jeff Hu (jeff4sq@gmail.com)
 */
public class OauthEditField extends EditField {
        
    private int mMinSize;
        
    public OauthEditField(String label, String initialValue) {
        this(label, initialValue, 0, 0L);
    }
        
    public OauthEditField(String label, String initialValue, long style) {
        this(label, initialValue, 0, style);
    }
        
    public OauthEditField(String label, String initialValue, int minSize, long style) {
        super(label, initialValue, Integer.MAX_VALUE, style);
        mMinSize = minSize;
        setPadding(4, 4, 4, 4);
    }
        
    public int getPreferredWidth() {
        return Math.min(getFont().getBounds('x') * mMinSize, 10);
    }
        
    protected void paintBackground(Graphics graphics) {
        graphics.setBackgroundColor(Color.WHITE);
        graphics.clear();
        int oldColor = graphics.getColor();
        graphics.setColor(0xf5f5f5); // white smoke
        graphics.fillRect(0, 0, getWidth(), getHeight());               
        graphics.setColor(0x999999); // gray
        graphics.drawRect(0, 0, getWidth(), getHeight());               
        graphics.setColor(oldColor);
    }
        
}
