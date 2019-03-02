package d.tonyandfriends.thirdtimesthecharm;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText registerEmailText;
    private EditText registerPasswordText;
    private EditText registerConfirmPasswordText;

    Button registerButton;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        registerEmailText = findViewById(R.id.register_email);
        registerPasswordText = findViewById(R.id.register_password);
        registerConfirmPasswordText = findViewById(R.id.register_confirm_password);
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
        String emailInput = registerEmailText.getText().toString();
        String passwordInput = registerPasswordText.getText().toString();
        String confirmPasswordInput = registerConfirmPasswordText.getText().toString();

        // If either email or passwords are empty...
        if(TextUtils.isEmpty(emailInput) || TextUtils.isEmpty(passwordInput)
            || TextUtils.isEmpty(confirmPasswordInput))
        {
            Toast.makeText(RegisterActivity.this, "ERROR: E-mail and/or passwords " +
                            "are empty", Toast.LENGTH_SHORT).show();
        }
        // If the passwords aren't equal...
        else if(!TextUtils.equals(passwordInput, confirmPasswordInput))
        {
            Toast.makeText(RegisterActivity.this, "ERROR: Passwords don't match",
                    Toast.LENGTH_SHORT).show();
        }
        else
        {

            // Stores the register result.
            Task<AuthResult> registerResult;
            registerResult = firebaseAuth.createUserWithEmailAndPassword(emailInput, passwordInput);


            Toast.makeText(RegisterActivity.this,
                    "Hello!", Toast.LENGTH_SHORT)
                    .show();

            registerResult.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    // If registration fails...
                    if(!task.isSuccessful())
                    {
                        Toast.makeText(RegisterActivity.this,
                                "ERROR: Registration failure...", Toast.LENGTH_SHORT)
                                .show();
                    }
                    else
                    {
                        startActivity(new Intent(RegisterActivity.this,
                                MenuActivity.class));
                    }
                }
            });
        }
    }
}
