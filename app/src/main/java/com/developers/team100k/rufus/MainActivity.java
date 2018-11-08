package com.developers.team100k.rufus;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Intent;
import android.net.Uri;
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
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.developers.team100k.rufus.adapter.OnScrollObserver;
import com.developers.team100k.rufus.adapter.RecyclerAdapter;
import com.developers.team100k.rufus.entity.Page;
import com.developers.team100k.rufus.processing.CategoriesParser;
import com.developers.team100k.rufus.processing.JsonParser;
import com.developers.team100k.rufus.processing.PagesParser;
import com.developers.team100k.rufus.profile.UserProfile;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.observers.DefaultObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.menu_layout) DrawerLayout mDrawerLayout;
  @BindView(R.id.tabs) TabLayout tabLayout;
  @BindView(R.id.nav_view) NavigationView mNavigationView;
  @BindView(R.id.horizontal_recyclerview) RecyclerView mHorizontalRV;
  @BindView(R.id.vertical_recyclerview) RecyclerView mVerticalRV;
  @BindView(R.id.swipe) SwipeRefreshLayout mRefreshLayout;
  @BindView(R.id.toolbar) Toolbar myToolbar;
  @BindView(R.id.welcome) TextView welcome;

  private UserProfile user;
  private boolean facebook = false;
  private boolean google = false;
  private RecyclerView.Adapter mAdapterHor;
  private RecyclerView.Adapter mAdapter;
  private static final int MARGIN_TOP = 56;
  private List<String> dummy_data = new ArrayList<>();
  private FirebaseAuth mAuth;
  private FirebaseUser mFirebaseUser;
  private DatabaseReference mDatabase;
  private Map<String, String> random;

  private Intent loginActivity;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(myToolbar);
    mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    mDatabase = FirebaseDatabase.getInstance().getReference();

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
    }

    mFirebaseUser = mAuth.getCurrentUser();
    System.out.println(mFirebaseUser.getDisplayName());

//    mDatabase.child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
//      @Override
//      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//        randomJson = dataSnapshot.getValue(Object.class);
//        System.out.println(randomJson);
//      }
//
//      @Override
//      public void onCancelled(@NonNull DatabaseError databaseError) {
//        System.out.println("Fail to read.");
//      }
//    });


    View header = mNavigationView.getHeaderView(0);
    TextView loggedUsing = header.findViewById(R.id.loggedwith);
    TextView profileName = header.findViewById(R.id.profile_name);
    TextView profileEmail = header.findViewById(R.id.profile_email);
    CircleImageView profileImage = header.findViewById(R.id.profile_picture);

    GoogleSignInAccount GAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
    Profile FAccount = Profile.getCurrentProfile();
    if (GAccount != null) {
      user = new UserProfile(GAccount.getId(), GAccount.getDisplayName(), GAccount.getEmail(), GAccount.getPhotoUrl());
      loggedUsing.setText("Logged with Google Account");
      google = true;
      profileName.setText(user.getPersonName());
      profileEmail.setText(user.getPersonEmail());
      Uri photo = user.getPersonPhoto();
      if (photo != null) {
        Glide.with(MainActivity.this)
            .load(photo)
            .into(profileImage);
      }
    } else if (FAccount != null) {
      user = new UserProfile(FAccount.getId(), FAccount.getName(), FAccount.getId(), FAccount.getProfilePictureUri(64,64));
      loggedUsing.setText("Logged with Facebook Account");
      facebook = true;
      profileName.setText(user.getPersonName());
      profileEmail.setText(user.getPersonEmail());
      Uri photo = user.getPersonPhoto();
      if (photo != null) {
        Glide.with(MainActivity.this)
            .load(photo)
            .into(profileImage);
      }
    }

    JsonParser dataParser = new JsonParser(this, "");
    dataParser.jsonToCollection(dataParser.getJson());

    CategoriesParser categoriesParser = new CategoriesParser(mDatabase);
    categoriesParser.databaseCall();

//    PagesParser pagesParser = new PagesParser(mDatabase);
//    pagesParser.callDatabase();

    io.reactivex.Observer<Object> categoryObserver = new DefaultObserver<Object>() {
      @Override
      public void onNext(Object o) {
        random = (Map<String, String>) o;
        Log.e("categoryObserver", "onNext");
        tabLayout.addTab(tabLayout.newTab().setText(random.get("name")));
      }

      @Override
      public void onError(Throwable e) {
        Log.e("Observer", "onError");
      }

      @Override
      public void onComplete() {
        Log.e("Observer", "onComplete");
      }
    };
    categoriesParser.getData().subscribe(categoryObserver);

//    io.reactivex.Observer<Object> pageObserver = new DefaultObserver<Object>() {
//      @Override
//      public void onNext(Object o) {
//        Page random = (Page) o;
////        Log.e("pageObserver", "onNext");
//        Log.e("pageObserver", random.toString());
//      }
//
//      @Override
//      public void onError(Throwable e) {
//        Log.e("Observer", "onError");
//      }
//
//      @Override
//      public void onComplete() {
//        Log.e("Observer", "onComplete");
//      }
//    };
//    pagesParser.getData().subscribe(pageObserver);


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
        switch (item.getItemId()){
          case R.id.staticpage:
            Intent intent = new Intent(MainActivity.this, ShowActivity.class);
            startActivity(intent);
            break;
          case R.id.logout:
            loginActivity = new Intent(MainActivity.this, LoginActivity.class);
            if (facebook) LoginManager.getInstance().logOut();
            if (google) loginActivity.setAction("logout");
              startActivity(loginActivity);
              finish();
              break;
            default:
              break;
        }
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
