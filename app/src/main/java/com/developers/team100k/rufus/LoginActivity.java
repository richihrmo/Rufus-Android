package com.developers.team100k.rufus;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {

  @BindView(R.id.facebook_login) LoginButton mFacebookButton;
  @BindView(R.id.google_login) SignInButton mGoogleButton;
  private GoogleSignInClient googleSignInClient;
  private FirebaseAuth mAuth;
  private ProgressDialog progressDialog;
  private CallbackManager callbackManager;

  private static final String EMAIL = "email";
  private static final int RC_SIGN_IN = 1;
  private Intent mainActivity;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);
    mAuth = FirebaseAuth.getInstance();
    FacebookSdk.sdkInitialize(getApplicationContext());
    mainActivity = new Intent(LoginActivity.this, MainActivity.class);

    callbackManager = CallbackManager.Factory.create();
    mFacebookButton.setReadPermissions(Arrays.asList(EMAIL));
    mFacebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
      @Override
      public void onSuccess(LoginResult loginResult) {
        progressDialog = ProgressDialog.show(LoginActivity.this, "", "Loading", true);
        startActivity(mainActivity);
        finish();
      }

      @Override
      public void onCancel() {
        Toast.makeText(LoginActivity.this, "Facebook login was cancelled", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onError(FacebookException error) {
//        Toast.makeText(LoginActivity.this, "Could not login with Facebook, try again later.", Toast.LENGTH_SHORT).show();
        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
        Log.e("Login Error" ,error.toString());
      }
    });

    mGoogleButton.setSize(SignInButton.SIZE_WIDE);
    mGoogleButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        progressDialog = ProgressDialog.show(LoginActivity.this, "", "Loading", true);
        signIn();
      }
    });

    GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build();
    googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
  }

  @Override
  protected void onStart() {
    super.onStart();
    Intent intent = getIntent();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    System.out.println(currentUser);
    if (intent.getAction() != null && intent.getAction().equals("logout"))
      googleSignInClient.signOut();
    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    if (account == null && accessToken == null){
      mFacebookButton.setVisibility(View.VISIBLE);
      mGoogleButton.setVisibility(View.VISIBLE);
    } else {
      progressDialog = ProgressDialog.show(LoginActivity.this, "", "Loading", true);
      mGoogleButton.setVisibility(View.GONE);
      mFacebookButton.setVisibility(View.GONE);
      startActivity(mainActivity);
      finish();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (progressDialog != null) {
      progressDialog.dismiss();
    }
  }

  private void signIn(){
    progressDialog.dismiss();
    Intent signInIntent = googleSignInClient.getSignInIntent();
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    callbackManager.onActivityResult(requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == RC_SIGN_IN){
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      handleSignInResult(task);
    }
  }

  private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
    Log.d("Firebase", "firebaseAuthWithGoogle:" + acct.getId());

    AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
    mAuth.signInWithCredential(credential)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
              // Sign in success, update UI with the signed-in user's information
              Log.d("Firebase", "signInWithCredential:success");
              FirebaseUser user = mAuth.getCurrentUser();
//              updateUI(user);
            } else {
              // If sign in fails, display a message to the user.
              Log.w("Firebase", "signInWithCredential:failure", task.getException());
              System.out.println("Authentication Failed.");
//              updateUI(null);
            }

            // ...
          }
        });
  }


  private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
    try {
      GoogleSignInAccount account = completedTask.getResult(ApiException.class);
      firebaseAuthWithGoogle(account);
      startActivity(mainActivity);
      finish();
      //update UI
    } catch (ApiException e){
      Log.w("Sign-in", "failed code: " + e.getStatusCode());
      //update UI null
    }
  }
}