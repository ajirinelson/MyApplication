package warri.crownfmng.com.napic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverLoginRegisterActivity extends AppCompatActivity {

    private Button DriverLoginButton;
    private Button DriverRegisterButton;
    private TextView DriverStatus;
    private TextView DriverRegisterLink;
    private EditText DriverEmail;
    private EditText DriverPassword;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference DriverDatabaseRef;
    private String onlineDriverID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login_register);

        mAuth = FirebaseAuth.getInstance();

        DriverLoginButton = (Button) findViewById(R.id.driver_login_btn);
        DriverRegisterButton = (Button) findViewById(R.id.driver_register_btn);
        DriverStatus = (TextView) findViewById(R.id.driver_status);
        DriverRegisterLink = (TextView) findViewById(R.id.driver_register_link);
        DriverEmail = (EditText) findViewById(R.id.email_driver);
        DriverPassword = (EditText) findViewById(R.id.password_driver);

        loadingBar = new ProgressDialog(this);


        DriverRegisterButton.setVisibility(View.INVISIBLE);
        DriverRegisterButton.setEnabled(false);

        DriverRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DriverLoginButton.setVisibility(View.INVISIBLE);
                DriverRegisterLink.setVisibility(View.INVISIBLE);

                DriverStatus.setText("Register Driver");

                DriverRegisterButton.setVisibility(View.VISIBLE);
                DriverRegisterButton.setEnabled(true);
            }
        });

        DriverRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = DriverEmail.getText().toString();
                String password = DriverPassword.getText().toString();

                RegisterDriver(email, password);
            }
        });

        DriverLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = DriverEmail.getText().toString();
                String password = DriverPassword.getText().toString();

                SignInDriver(email, password);

            }
        });
    }

    private void SignInDriver(String email, String password) {

        if (TextUtils.isEmpty(email)){

            Toast.makeText(DriverLoginRegisterActivity.this, "Please write an email...", Toast.LENGTH_SHORT).show();

            Intent driverIntent = new Intent(DriverLoginRegisterActivity.this, DriversMapActivity.class);
            startActivity(driverIntent);
        }

        if (TextUtils.isEmpty(password)){

            Toast.makeText(DriverLoginRegisterActivity.this, "Please write a password...", Toast.LENGTH_SHORT).show();
        }

        else{

            loadingBar.setTitle("Driver Login...");
            loadingBar.setMessage("Please wait, Logging In...");
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {

                        Intent driverIntent = new Intent(DriverLoginRegisterActivity.this, DriverMapActivity.class);
                        startActivity(driverIntent);

                        Toast.makeText(DriverLoginRegisterActivity.this, "Login Successful...", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();


                    }

                    else
                    {
                        Toast.makeText(DriverLoginRegisterActivity.this, "Login Unsuccessful,Please Try Again...", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                }
            });
        }
    }

    private void RegisterDriver(final String email, String password) {
        if (TextUtils.isEmpty(email)){

            Toast.makeText(DriverLoginRegisterActivity.this, "Please write a email...", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(password)){

            Toast.makeText(DriverLoginRegisterActivity.this, "Please write a password...", Toast.LENGTH_SHORT).show();
        }

        else{

            loadingBar.setTitle("Driver Registration...");
            loadingBar.setMessage("Please wait, registering data...");
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {

                        onlineDriverID = mAuth.getCurrentUser().getUid();
                        DriverDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(onlineDriverID).child("name");
                        DriverDatabaseRef.setValue(email);


                        Intent driverIntent = new Intent(DriverLoginRegisterActivity.this, DriverMapActivity.class);
                        startActivity(driverIntent);

                        Toast.makeText(DriverLoginRegisterActivity.this, "Successful Registration...", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                    else
                    {
                        Toast.makeText(DriverLoginRegisterActivity.this, "Registration Unsuccessful,Please Try Again...", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                }
            });
        }

    }
}
