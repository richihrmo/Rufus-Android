package com.developers.team100k.rufus;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener;
import com.afollestad.materialdialogs.MaterialDialog;
import com.developers.team100k.rufus.adapter.TabLayoutPagerAdapter;
import com.developers.team100k.rufus.entity.Headline;
import com.developers.team100k.rufus.processing.ArticlesParser;
import com.developers.team100k.rufus.processing.CategoriesParser;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.google.android.material.tabs.TabLayout.Tab;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.SnapHelper;
import androidx.appcompat.widget.Toolbar;
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
import com.developers.team100k.rufus.processing.JsonParser;
import com.developers.team100k.rufus.profile.UserProfile;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.observers.DefaultObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.menu_layout)
  DrawerLayout mDrawerLayout;
  @BindView(R.id.tabs)
  TabLayout tabLayout;
  @BindView(R.id.nav_view)
  NavigationView mNavigationView;
  @BindView(R.id.toolbar)
  Toolbar myToolbar;
  @BindView(R.id.viewpager)
  ViewPager mViewPager;

  private UserProfile user;
  private boolean facebook = false;
  private boolean google = false;
  private RecyclerView.Adapter mAdapterHor;
  private RecyclerView.Adapter mAdapter;
  private List<String> random;
  private FirebaseAuth mAuth;
  private FirebaseUser mFirebaseUser;
  private DatabaseReference mDatabase;
  private List<Headline> listHeadline;
  private TabLayoutPagerAdapter mTabLayoutPagerAdapter;
  private EventBus mEventBus = EventBus.getDefault();
  private MaterialDialog mMaterialDialog;

  private Intent loginActivity;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(myToolbar);

    if (mDatabase != null){
      FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    mAuth = FirebaseAuth.getInstance();
    mDatabase = FirebaseDatabase.getInstance().getReference();

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setHomeAsUpIndicator(R.drawable.com_facebook_button_icon);
    }

    mFirebaseUser = mAuth.getCurrentUser();
    System.out.println(mFirebaseUser.getDisplayName());

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

    ArticlesParser articlesParser = new ArticlesParser(mDatabase);
    articlesParser.call();

    mMaterialDialog = new MaterialDialog.Builder(this)
        .content("Loading content...")
        .build();
    mMaterialDialog.show();

    io.reactivex.Observer<Object> articleObserver = new DefaultObserver<Object>() {
      @Override
      public void onNext(Object o) {
        listHeadline = (List<Headline>) o;
        String string = mTabLayoutPagerAdapter.getPageTitle(mViewPager.getCurrentItem()).toString();
        Log.e("article", string);
        mEventBus.postSticky(listHeadline);
        mMaterialDialog.dismiss();
        Log.e("articleObserver", "onNext");
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
    articlesParser.getData().subscribe(articleObserver);

    CategoriesParser categoriesParser = new CategoriesParser(mDatabase);
    categoriesParser.call();

    io.reactivex.Observer<Object> categoryObserver = new DefaultObserver<Object>() {
      @Override
      public void onNext(Object o) {
        random = (List<String>) o;
        mTabLayoutPagerAdapter = new TabLayoutPagerAdapter(getSupportFragmentManager(), random);
        mViewPager.setAdapter(mTabLayoutPagerAdapter);
        Log.e("categoryObserver", "onNext");
        tabLayout.addTab(tabLayout.newTab().setText(""));
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

    tabLayout.setupWithViewPager(mViewPager);
    tabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
      @Override
      public void onTabSelected(Tab tab) {
        if (mEventBus != null && listHeadline != null){
          Log.e("tab", tab.getText().toString());
          List<Headline> list = new ArrayList<>();
          if (String.valueOf(tab.getText()).equals("All")){
            list = listHeadline;
          } else if (listHeadline != null){
            for (Headline headline : listHeadline ){
              if (headline.getCategory() != null && headline.getCategory().equals(String.valueOf(tab.getText()))){
                list.add(headline);
              }
            }
          }
          mEventBus.postSticky(list);
        }
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

//    mHorizontalRV.setHasFixedSize(true);

//    mVerticalRV.addOnScrollListener(new OnScrollObserver() {
//      @Override
//      public void show() {
//        myToolbar.animate()
//            .translationY(0)
//            .setInterpolator(new DecelerateInterpolator(1))
//            .setStartDelay(0)
//            .start();
//        mHorizontalRV.animate()
//            .translationY(0)
//            .setInterpolator(new DecelerateInterpolator(1))
//            .setStartDelay(0)
//            .start();
//        welcome.animate().translationY(0)
//            .setInterpolator(new DecelerateInterpolator(1))
//            .setListener(new AnimatorListener() {
//              @Override
//              public void onAnimationStart(Animator animator) {
//                mHorizontalRV.setVisibility(View.VISIBLE);
//                myToolbar.setVisibility(View.VISIBLE);
//                welcome.setVisibility(View.VISIBLE);
//              }
//              @Override
//              public void onAnimationEnd(Animator animator) {
//              }
//              @Override
//              public void onAnimationCancel(Animator animator) {
//                mHorizontalRV.setVisibility(View.VISIBLE);
//                myToolbar.setVisibility(View.VISIBLE);
//                welcome.setVisibility(View.VISIBLE);
//              }
//              @Override
//              public void onAnimationRepeat(Animator animator) {
//                mHorizontalRV.setVisibility(View.VISIBLE);
//                myToolbar.setVisibility(View.VISIBLE);
//                welcome.setVisibility(View.VISIBLE);
//              }
//            })
//            .setStartDelay(0)
//            .start();
//      }
//      @Override
//      public void hide() {
//        myToolbar.animate()
//            .translationY(-myToolbar.getHeight())
//            .setInterpolator(new AccelerateInterpolator(1))
//            .setStartDelay(450)
//            .start();
//        mHorizontalRV.animate()
//            .translationY(-(myToolbar.getHeight() + mHorizontalRV.getHeight() + welcome.getHeight() + MARGIN_TOP))
//            .setInterpolator(new AccelerateInterpolator(1))
//            .setStartDelay(150)
//            .start();
//        welcome.animate().translationY(-(myToolbar.getHeight() + welcome.getHeight() + MARGIN_TOP))
//            .setInterpolator(new AccelerateInterpolator(1))
//            .setListener(new AnimatorListener() {
//              @Override
//              public void onAnimationStart(Animator animator) {
//                mHorizontalRV.setVisibility(View.GONE);
//                myToolbar.setVisibility(View.GONE);
//                welcome.setVisibility(View.GONE);
//                mVerticalRV.setNestedScrollingEnabled(false);
//              }
//              @Override
//              public void onAnimationEnd(Animator animator) {
//                mVerticalRV.setVerticalScrollbarPosition(2);
//
////                mVerticalRV.smoothScrollToPosition(2);
//              }
//              @Override
//              public void onAnimationCancel(Animator animator) {
//                mHorizontalRV.setVisibility(View.GONE);
//                myToolbar.setVisibility(View.GONE);
//                welcome.setVisibility(View.GONE);
//              }
//              @Override
//              public void onAnimationRepeat(Animator animator) {
//                mHorizontalRV.setVisibility(View.GONE);
//                myToolbar.setVisibility(View.GONE);
//                welcome.setVisibility(View.GONE);
//              }
//            })
//            .setStartDelay(450)
//            .start();
//      }
//    });

//    mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//      @Override
//      public void onRefresh() {
//        Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_LONG).show();
//        mRefreshLayout.setRefreshing(false);
//      }
//    });

//    SnapHelper snapHelper = new PagerSnapHelper();
//    snapHelper.attachToRecyclerView(mHorizontalRV);
//    LayoutManager mHorizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//    mHorizontalRV.setLayoutManager(mHorizontalLayoutManager);
//    mAdapterHor = new RecyclerAdapter(dummy_data);
//    mHorizontalRV.setAdapter(mAdapterHor);

//    LayoutManager mLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
//    mVerticalRV.setLayoutManager(mLayoutManager);
//    mAdapter = new RecyclerAdapter(dummy_data);
//    mVerticalRV.setAdapter(mAdapter);
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

  public void itemClick(View view){
    Log.e("click", mViewPager.getAdapter().getPageTitle(mViewPager.getCurrentItem()).toString());
    Log.e("click", "" + ((TextView)((ConstraintLayout) view).getChildAt(6)).getText());
    Intent intent = new Intent(this, ShowActivity.class);
    intent.putExtra("article_id", ((TextView)((ConstraintLayout) view).getChildAt(6)).getText());
    startActivity(intent);
  }

}
