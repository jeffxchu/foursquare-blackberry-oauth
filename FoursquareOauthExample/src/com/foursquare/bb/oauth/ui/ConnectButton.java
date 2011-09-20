/**
 * 
 */
package com.foursquare.bb.oauth.ui;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

/**
 * 
 * A simple connect to foursquare button
 * 
 * @author Jeff Hu (jeff4sq@gmail.com)
 *
 */
public class ConnectButton extends Field {

	private static final Bitmap NORMAL_IMAGE = Bitmap.getBitmapResource("connect_to_fs_white.png");
	private static final Bitmap FOCUS_IMAGE  = Bitmap.getBitmapResource("connect_to_fs_blue.png");
	
	public ConnectButton(long style) {
		super(Field.FOCUSABLE | style);		
	}
	
	public int getPreferredWidth() {
		return NORMAL_IMAGE.getWidth();
	}
	
	public int getPreferredHeight() {
		return NORMAL_IMAGE.getHeight();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rim.device.api.ui.Field#layout(int, int)
	 */
	protected void layout(int width, int height) {
		setExtent(getPreferredWidth(), getPreferredHeight());
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rim.device.api.ui.Field#paintBackground(net.rim.device.api.ui.Graphics)
	 */
	protected void paintBackground(Graphics graphics) {
		// do nothing
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rim.device.api.ui.Field#keyChar(char, int, int)
	 */
	protected boolean keyChar(char ch, int status, int time) {
		if (ch == Characters.ENTER) {
			fieldChangeNotify(0);
			return true;
		}
		return super.keyChar(ch, status, time);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rim.device.api.ui.Field#navigationUnclick(int, int)
	 */
	protected boolean navigationUnclick(int status, int time) {
		fieldChangeNotify(0);
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rim.device.api.ui.Field#invokeAction(int)
	 */
	protected boolean invokeAction(int action) {
		switch(action) {
			case ACTION_INVOKE: {
				fieldChangeNotify(0);
				return true;
			}
		}
		return super.invokeAction(action);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rim.device.api.ui.Field#setDirty(boolean)
	 */
	public void setDirty(boolean dirty) {
		// do nothing, never dirty
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rim.device.api.ui.Field#setMuddy(boolean)
	 */
	public void setMuddy(boolean muddy) {
		// do nothing, never muddy
	}

	/*
	 * (non-Javadoc)
	 * @see net.rim.device.api.ui.Field#paint(net.rim.device.api.ui.Graphics)
	 */
	protected void paint(Graphics graphics) {
		int width = NORMAL_IMAGE.getWidth();
		int height = NORMAL_IMAGE.getHeight();		
		int oldColor = graphics.getColor();
		graphics.setBackgroundColor(Color.WHITE);
		graphics.clear();
		Bitmap bitmap = graphics.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS)? FOCUS_IMAGE : NORMAL_IMAGE;
		graphics.drawBitmap(0, 0, width, height, bitmap, 0, 0);
		graphics.setColor(oldColor);
	}
}
