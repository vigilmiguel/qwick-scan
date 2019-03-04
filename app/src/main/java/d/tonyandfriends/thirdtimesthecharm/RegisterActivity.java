package d.tonyandfriends.thirdtimesthecharm;


import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class RegisterActivity extends AppCompatActivity {

    private EditText registerEmailText;
    private EditText registerPasswordText;
    private EditText registerConfirmPasswordText;

    private TextView registerErrorText;

    Button registerButton;

    FirebaseAuth firebaseAuth;

    //DatabaseReference databaseUsers;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        // Refers to the users "table"
        //databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        registerEmailText = findViewById(R.id.register_email);
        registerPasswordText = findViewById(R.id.register_password);
        registerConfirmPasswordText = findViewById(R.id.register_confirm_password);
        registerErrorText = findViewById(R.id.text_error);
        registerButton = findViewById(R.id.register_button);



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }


    public void register() {
        // Store what the user entered as email and password.
        final String emailInput = registerEmailText.getText().toString();
        String passwordInput = registerPasswordText.getText().toString();
        String confirmPasswordInput = registerConfirmPasswordText.getText().toString();




        // If either email or passwords are empty...
        if(TextUtils.isEmpty(emailInput) || TextUtils.isEmpty(passwordInput)
            || TextUtils.isEmpty(confirmPasswordInput)) {

            showError("ERROR: One or more fields are empty...");
        }
        // If the passwords aren't equal...
        else if(!TextUtils.equals(passwordInput, confirmPasswordInput)) {
            showError("ERROR: The passwords do not match!");
        }
        else if(passwordInput.length() < 6) {
            showError("ERROR: Password must be at least 6 characters in length...");
        }
        else {

            // Stores the register result.
            Task<AuthResult> registerResult;
            registerResult = firebaseAuth.createUserWithEmailAndPassword(emailInput, passwordInput);


            registerResult.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    // If registration fails...
                    if(!task.isSuccessful())
                    {
                        showError("ERROR: Registration failed!");
                        //Toast.makeText(RegisterActivity.this,
                        //        "ERROR: Registration failure...", Toast.LENGTH_SHORT)
                        //        .show();
                    }
                    else
                    {

                        //FirebaseUser fbUser = firebaseAuth.getCurrentUser();
                        //User user = new User(fbUser.getUid(), emailInput);

                        //databaseUsers.child(fbUser.getUid()).setValue(user);

                        Intent menuActivity = new Intent(RegisterActivity.this,
                                MenuActivity.class);

                        /*
                            Set flags to clear activities on the stack.
                            This prevents the back button from going back
                            to the register and login activities.
                         */
                        menuActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(menuActivity);
                    }
                }
            });
        }
    }

    private void showError(String message){
        registerErrorText.setText(message);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                registerErrorText.setText("");
            }
        }, 5000);
    }


}
