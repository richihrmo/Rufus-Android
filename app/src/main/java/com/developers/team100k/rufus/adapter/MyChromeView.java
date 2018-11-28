package com.developers.team100k.rufus.adapter;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;

/**
 * Created by Richard Hrmo.
 */
public class MyChromeView extends WebChromeClient{

  private View mCustomView;
  private Window mWindow;
  private WebChromeClient.CustomViewCallback mCustomViewCallback;
  protected FrameLayout mFullscreenContainer;
  private int mOriginalOrientation;
  private int mOriginalSystemUiVisibility;

  public MyChromeView(Window window) {
    this.mWindow = window;
  }

  public Bitmap getDefaultVideoPoster()
  {
    if (mCustomView == null) {
      return null;
    }
    return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
  }

  public void onHideCustomView()
  {
    ((FrameLayout) mWindow.getDecorView()).removeView(this.mCustomView);
    this.mCustomView = null;
    mWindow.getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
    mWindow.setTransitionBackgroundFadeDuration(500);
    this.mCustomViewCallback.onCustomViewHidden();
    this.mCustomViewCallback = null;
  }

  public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
  {
    if (this.mCustomView != null) {
      onHideCustomView();
      return;
    }
    this.mCustomView = paramView;
    this.mOriginalSystemUiVisibility = mWindow.getDecorView().getSystemUiVisibility();
    this.mCustomViewCallback = paramCustomViewCallback;
    ((FrameLayout) mWindow.getDecorView()).addView(this.mCustomView, new FrameLayout . LayoutParams (-1, -1));
    mWindow.getDecorView().setSystemUiVisibility(3846);
  }

}
