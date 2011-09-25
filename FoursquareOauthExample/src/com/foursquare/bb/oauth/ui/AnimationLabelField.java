package com.foursquare.bb.oauth.ui;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

/**
 * Progress animation label field for loading embedded browser page
 * 
 * @author Jeff Hu (jeff4sq@gmail.com)
 *
 */
public class AnimationLabelField extends VerticalFieldManager {
        
    protected static final int VPADDING = 5;
    protected static final int HPADDING = 5;
        
    private LabelField mLabelField;
    private Bitmap     mImage;
        
    public AnimationLabelField(String labelText) {
        super(Manager.FIELD_HCENTER);
        mImage = getAnimationImage();
        Field progressAnimatedField = createAnimatedImageField(mImage);
        Field progressLabelField = new LabelField(labelText, FIELD_VCENTER) {
            protected void paint(Graphics graphics) {
                Font font = graphics.getFont();
                int alpha = graphics.getGlobalAlpha();
                graphics.setGlobalAlpha(195);
                graphics.setFont(Font.getDefault().derive(Font.PLAIN, Font.getDefault().getHeight() * 2/3));
                super.paint(graphics);
                graphics.setFont(font);
                graphics.setGlobalAlpha(alpha);
            }
        };
                
        int imageHeight = mImage.getHeight();
        int fontHeight = progressLabelField.getFont().getHeight();
        if (imageHeight > fontHeight) {
            int diffPadding = (imageHeight - fontHeight) >> 1;
            progressAnimatedField.setPadding(VPADDING, HPADDING, 0, HPADDING);
            progressLabelField.setPadding(VPADDING + diffPadding, HPADDING, diffPadding, HPADDING);
        } else {
            int diffPadding = (fontHeight - imageHeight) >> 1;
            progressLabelField.setPadding(VPADDING, HPADDING, 0, HPADDING);
            progressAnimatedField.setPadding(VPADDING + diffPadding, HPADDING, diffPadding, HPADDING);
        }
                
        HorizontalFieldManager hfm = new HorizontalFieldManager(Manager.FIELD_VCENTER|Manager.FIELD_HCENTER);
        hfm.add(progressAnimatedField);
        hfm.add(progressLabelField);
                
        add(hfm);
                
        setPadding(2, 2, 2, 2);
    }
        
    protected Bitmap getAnimationImage() {
        return Bitmap.getBitmapResource("progress.png");
    }
                
    protected void applyTheme(Graphics graphics, boolean arg) {
        // do nothing
    }
         
    protected Field createAnimatedImageField(Bitmap image) {     
        return new AnimatedImageField(image, NON_FOCUSABLE|FIELD_VCENTER|FIELD_HCENTER);
    }
         
    public void updateText(String text) {
        mLabelField.setText(text);
    }
         
    private static class AnimatedImageField extends Field implements Runnable {
        private Bitmap mImage;
        private int mCurrentFrame = 0;
        private int mInvokeId     = -1;
        private int mFrameWidth;
        private int mFrameHeight;
        private int mFrames;
                 
        public AnimatedImageField(Bitmap image, long style) {
            super(style | Field.NON_FOCUSABLE);
            mImage = image;
            mFrameHeight = mFrameWidth = mImage.getHeight();
            mFrames = mImage.getWidth() / mFrameHeight;
        }
                 
        public void run() {
            if (isVisible()) {
                invalidate();
            }
        }
                 
        protected void onDisplay() {
            super.onDisplay();
            if (mInvokeId == -1) {
                mInvokeId = UiApplication.getUiApplication().invokeLater(this, 275, true);
            }
        }
                 
        protected void onUndisplay() {
            super.onUndisplay();
            if (mInvokeId != -1) {
                UiApplication.getUiApplication().cancelInvokeLater(mInvokeId);
                mInvokeId = -1;
            }
        }
                 
        protected void layout(int width, int height) {
            setExtent(Math.min(width, mFrameWidth), Math.min(height, mFrameHeight));
        }
                 
        protected void paint(Graphics graphics) {
            graphics.drawBitmap(0, 0, mFrameWidth, mFrameHeight, mImage, mFrameWidth * mCurrentFrame, 0);
            ++mCurrentFrame;
            if (mCurrentFrame >= mFrames) {
                mCurrentFrame = 0;
            }       
        }
    } // Class AnimatedImageField
} // Class AnimationLabelField
