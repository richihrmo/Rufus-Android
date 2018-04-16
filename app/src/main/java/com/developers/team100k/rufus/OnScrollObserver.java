package com.developers.team100k.rufus;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

/**
 * Created by Richard Hrmo.
 */
public abstract class OnScrollObserver extends RecyclerView.OnScrollListener {

  private static final float MIN = 25;

  private int scrollDistance = 0;
  private boolean isVisible = true;

  public abstract void show();
  public abstract void hide();

  @Override
  public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);
    if (isVisible && scrollDistance > MIN){
      scrollDistance = 0;
      isVisible = false;
    } else if ((!recyclerView.canScrollVertically(-1))){
      show();
      scrollDistance = 0;
      isVisible = true;
    }

    if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
      if (scrollDistance > MIN) isVisible = false;
      else scrollDistance += dy;
    }
  }

  @Override
  public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    if (newState == SCROLL_STATE_SETTLING && !isVisible) hide();
    super.onScrollStateChanged(recyclerView, newState);
  }
}
