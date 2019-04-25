package d.tonyandfriends.thirdtimesthecharm;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import com.google.android.gms.common.api.ApiException;
import android.content.Intent;
import com.google.android.gms.tasks.Task;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // Create variables for the email and password text boxes.
    public EditText signInEmailText;
    public EditText signInPasswordText;

    // Create a variable for the sign in button.
    Button signInButton;
    Button registerButton;

    //a constant for detecting the login intent result
    private static final int RC_SIGN_IN = 234;

    //Tag for the logs optional
    private static final String TAG = "poopBoy";

    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;

    //And also a Firebase Auth object
    FirebaseAuth mAuth;
    boolean isCreated = false;
    boolean isEmpty = false;
    boolean isBadCombo = false;
    int poop = 0;
    boolean iGetHere = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //first we intialized the FirebaseAuth object
        mAuth = FirebaseAuth.getInstance();
        isCreated = true;

        // Set the text and button variables to the desired objects defined in activity_login.xml
        signInEmailText = findViewById(R.id.sign_in_email_text);
        signInPasswordText = findViewById(R.id.sign_in_password_text);
        signInButton = findViewById(R.id.sign_in_button);
        registerButton = findViewById(R.id.register_now_button);

        // Whenever the sign in button is pressed...
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start sign in process.
                signIn();
            }
        });

        // If the register button is pressed...
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the RegisterActivity page.
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        //Then we need a GoogleSignInOptions object
        //And we need to build it as below
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("970846969532-lqb3jcf2g3e46madqhubqjh4a9umoa2e.apps.googleusercontent.com")
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Now we will attach a click listener to the sign_in_button
        //and inside onClick() method we are calling the signInWithGoogle() method that will open
        //google sign in intent
        findViewById(R.id.google_sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // If we are already signed in, take it directly to profile page
        // we will change profile page to our actual Scanner page
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MenuActivity.class));
            finish();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Sign In with FireBase (mAuth)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(LoginActivity.this, "User Signed In", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this   , MenuActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }


    //this method is called on click
    private void signInWithGoogle() {
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signIn() {
        // Store what the user entered as email and password.
        String emailInput = signInEmailText.getText().toString();
        String passwordInput = signInPasswordText.getText().toString();
        Log.d("myTest",emailInput);
        Log.d("myTest",passwordInput);
        //iGetHere = true;

        // If either email or password are empty...
        if(TextUtils.isEmpty(emailInput) || TextUtils.isEmpty(passwordInput))
        {
            poop = 3;
            isBadCombo = true;
            isEmpty = true;
            //Toast.makeText(LoginActivity.this, "ERROR: E-mail and/or password are empty",
                   // Toast.LENGTH_SHORT).show();
        }
        else
        {
            // Stores the sign in result.
            Task<AuthResult> signInResult;
            signInResult = mAuth.signInWithEmailAndPassword(emailInput, passwordInput);
            Log.d("mySign",signInResult.toString());

            iGetHere = true;
            signInResult.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d("myActuallygethere","ehe");
                    // If sign in fails...
                    if(!task.isSuccessful())
                    {
                        Log.d("myShoydl","really should get here");
                        poop = 1;
                        isBadCombo = true;
                        Toast.makeText(LoginActivity.this,
                               "ERROR: E-mail and password do not match", Toast.LENGTH_SHORT)
                                .show();
                    }
                    else
                    {
                        Log.d("myShoydl","really shouldnt get here");
                        poop = 2;
                        isBadCombo = false;
                        startActivity(new Intent(LoginActivity.this,
                                MenuActivity.class));
                        finish();
                    }
                }
            });
        }
    }

}

