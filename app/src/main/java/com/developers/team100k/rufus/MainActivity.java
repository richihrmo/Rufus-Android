package com.developers.team100k.rufus;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  @BindView(R.id.horizontal_recyclerview) RecyclerView mHorizontalRV;
  @BindView(R.id.vertical_recyclerview) RecyclerView mVerticalRV;
  @BindView(R.id.swipe) SwipeRefreshLayout mRefreshLayout;
  @BindView(R.id.toolbar) Toolbar myToolbar;

  private RecyclerView.Adapter mAdapterHor;
  private RecyclerView.Adapter mAdapter;
  private List<String> dummy_data = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(myToolbar);

    mHorizontalRV.setHasFixedSize(true);
    mVerticalRV.setHasFixedSize(true);

    mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
      @Override
      public void onRefresh() {
        Toast.makeText(MainActivity.this, "HAloooo", Toast.LENGTH_LONG).show();
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
}
