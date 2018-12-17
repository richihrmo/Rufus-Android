package com.developers.team100k.rufus;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener;
import com.afollestad.materialdialogs.MaterialDialog;
import com.developers.team100k.rufus.adapter.TabLayoutPagerAdapter;
import com.developers.team100k.rufus.entity.Dialog;
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
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.menu_layout)
  DrawerLayout mDrawerLayout;
  @BindView(R.id.tabs)
  TabLayout tabLayout;
  @BindView(R.id.nav_view)
  NavigationView mNavigationView;
  @BindView(R.id.toolbar)
  Toolbar myToolbar;
  @BindView(R.id.credits)
  Button mButton;
  @BindView(R.id.viewpager)
  ViewPager mViewPager;

  private UserProfile user;
  private boolean facebook = false;
  private boolean google = false;
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

    mButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
        startActivity(intent);
      }
    });

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

    mMaterialDialog = new MaterialDialog.Builder(this)
        .content("Loading content...")
        .build();

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

  @Override
  protected void onStart() {
    super.onStart();
    mEventBus.register(this);
    Log.e("MainActivity", "start");
  }

  @Override
  protected void onStop() {
    super.onStop();
    mEventBus.unregister(this);
    Log.e("MainActivity", "stop");
  }

  @Subscribe(sticky = true)
  public void onEvent(Dialog string){
    switch (string){
      case SHOW:
        mMaterialDialog.show();
        break;
      case DISMISS:
        mMaterialDialog.dismiss();
        break;
      default:
        break;
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.e("MainActivity", "destroy");
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    Log.e("MainActivity", "restart");
  }

  public void itemClick(View view){

    Log.e("click", "cool");

  }

}
