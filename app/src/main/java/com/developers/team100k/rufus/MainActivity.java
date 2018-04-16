package com.developers.team100k.rufus;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.menu_layout) DrawerLayout mDrawerLayout;
  @BindView(R.id.tabs) TabLayout tabLayout;
  @BindView(R.id.nav_view) NavigationView mNavigationView;
  @BindView(R.id.horizontal_recyclerview) RecyclerView mHorizontalRV;
  @BindView(R.id.vertical_recyclerview) RecyclerView mVerticalRV;
  @BindView(R.id.swipe) SwipeRefreshLayout mRefreshLayout;
  @BindView(R.id.toolbar) Toolbar myToolbar;
  @BindView(R.id.welcome) TextView welcome;

  private RecyclerView.Adapter mAdapterHor;
  private RecyclerView.Adapter mAdapter;
  private static final int MARGIN_TOP = 56;
  private List<String> dummy_data = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(myToolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
    }

    tabLayout.addTab(tabLayout.newTab().setText("Politics"));
    tabLayout.addTab(tabLayout.newTab().setText("Sports"));
    tabLayout.addTab(tabLayout.newTab().setText("Peto"));
    tabLayout.addTab(tabLayout.newTab().setText("je"));
    tabLayout.addTab(tabLayout.newTab().setText("Noob"));
    tabLayout.addTab(tabLayout.newTab().setText("Peto"));
    tabLayout.addTab(tabLayout.newTab().setText("je"));
    tabLayout.addTab(tabLayout.newTab().setText("Noob"));
    tabLayout.addTab(tabLayout.newTab().setText("Peto"));
    tabLayout.addTab(tabLayout.newTab().setText("je"));
    tabLayout.addTab(tabLayout.newTab().setText("Noob"));
    tabLayout.addTab(tabLayout.newTab().setText("Peto"));
    tabLayout.addTab(tabLayout.newTab().setText("je"));
    tabLayout.addTab(tabLayout.newTab().setText("Noob"));

    tabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
      @Override
      public void onTabSelected(Tab tab) {
        //TODO
      }

      @Override
      public void onTabUnselected(Tab tab) {

      }

      @Override
      public void onTabReselected(Tab tab) {

      }
    });

    mNavigationView.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        mDrawerLayout.closeDrawers();
        //update UI according to item selected
        // TODO
        return true;
      }
    });

    mRefreshLayout.setEnabled(true);
    mHorizontalRV.setHasFixedSize(true);

    mVerticalRV.addOnScrollListener(new OnScrollObserver() {
      @Override
      public void show() {
        myToolbar.animate()
            .translationY(0)
            .setInterpolator(new DecelerateInterpolator(1))
            .setStartDelay(0)
            .start();
        mHorizontalRV.animate()
            .translationY(0)
            .setInterpolator(new DecelerateInterpolator(1))
            .setStartDelay(0)
            .start();
        welcome.animate().translationY(0)
            .setInterpolator(new DecelerateInterpolator(1))
            .setListener(new AnimatorListener() {
              @Override
              public void onAnimationStart(Animator animator) {
                mHorizontalRV.setVisibility(View.VISIBLE);
                myToolbar.setVisibility(View.VISIBLE);
                welcome.setVisibility(View.VISIBLE);
              }
              @Override
              public void onAnimationEnd(Animator animator) {
              }
              @Override
              public void onAnimationCancel(Animator animator) {
                mHorizontalRV.setVisibility(View.VISIBLE);
                myToolbar.setVisibility(View.VISIBLE);
                welcome.setVisibility(View.VISIBLE);
              }
              @Override
              public void onAnimationRepeat(Animator animator) {
                mHorizontalRV.setVisibility(View.VISIBLE);
                myToolbar.setVisibility(View.VISIBLE);
                welcome.setVisibility(View.VISIBLE);
              }
            })
            .setStartDelay(0)
            .start();
      }
      @Override
      public void hide() {
        myToolbar.animate()
            .translationY(-myToolbar.getHeight())
            .setInterpolator(new AccelerateInterpolator(1))
            .setStartDelay(450)
            .start();
        mHorizontalRV.animate()
            .translationY(-(myToolbar.getHeight() + mHorizontalRV.getHeight() + welcome.getHeight() + MARGIN_TOP))
            .setInterpolator(new AccelerateInterpolator(1))
            .setStartDelay(150)
            .start();
        welcome.animate().translationY(-(myToolbar.getHeight() + welcome.getHeight() + MARGIN_TOP))
            .setInterpolator(new AccelerateInterpolator(1))
            .setListener(new AnimatorListener() {
              @Override
              public void onAnimationStart(Animator animator) {
                mHorizontalRV.setVisibility(View.GONE);
                myToolbar.setVisibility(View.GONE);
                welcome.setVisibility(View.GONE);
                mVerticalRV.setNestedScrollingEnabled(false);
              }
              @Override
              public void onAnimationEnd(Animator animator) {
                mVerticalRV.setVerticalScrollbarPosition(2);

//                mVerticalRV.smoothScrollToPosition(2);
              }
              @Override
              public void onAnimationCancel(Animator animator) {
                mHorizontalRV.setVisibility(View.GONE);
                myToolbar.setVisibility(View.GONE);
                welcome.setVisibility(View.GONE);
              }
              @Override
              public void onAnimationRepeat(Animator animator) {
                mHorizontalRV.setVisibility(View.GONE);
                myToolbar.setVisibility(View.GONE);
                welcome.setVisibility(View.GONE);
              }
            })
            .setStartDelay(450)
            .start();
      }
    });

    mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
      @Override
      public void onRefresh() {
        Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_LONG).show();
        mRefreshLayout.setRefreshing(false);
      }
    });

    SnapHelper snapHelper = new PagerSnapHelper();
    snapHelper.attachToRecyclerView(mHorizontalRV);
    LayoutManager mHorizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    mHorizontalRV.setLayoutManager(mHorizontalLayoutManager);
    mAdapterHor = new RecyclerAdapter(dummy_data);
    mHorizontalRV.setAdapter(mAdapterHor);

    LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    mVerticalRV.setLayoutManager(mLayoutManager);
    mAdapter = new RecyclerAdapter(dummy_data);
    mVerticalRV.setAdapter(mAdapter);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        mDrawerLayout.openDrawer(GravityCompat.START);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
