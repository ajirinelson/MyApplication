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

public class CustomerLoginRegisterActivity extends AppCompatActivity {

    private Button CustomerLoginButton;
    private Button CustomerRegisterButton;
    private TextView CustomerStatus;
    private TextView CustomerRegisterLink;
    private EditText CustomerEmail;
    private EditText CustomerPassword;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference CustomerDatabaseRef;
    private String onlineCustomerID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login_register);

        mAuth = FirebaseAuth.getInstance();

        CustomerLoginButton = (Button) findViewById(R.id.customer_login_btn);
        CustomerRegisterButton = (Button) findViewById(R.id.customer_register_btn);
        CustomerStatus = (TextView) findViewById(R.id.customer_status);
        CustomerRegisterLink = (TextView) findViewById(R.id.register_customer_link);
        CustomerEmail = (EditText) findViewById(R.id.email_customer);
        CustomerPassword = (EditText) findViewById(R.id.password_customer);

        loadingBar = new ProgressDialog(this);


        CustomerRegisterButton.setVisibility(View.INVISIBLE);
        CustomerRegisterButton.setEnabled(false);

        CustomerRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerLoginButton.setVisibility(View.INVISIBLE);
                CustomerRegisterLink.setVisibility(View.INVISIBLE);

                CustomerStatus.setText("Register Customer");

                CustomerRegisterButton.setVisibility(View.VISIBLE);
                CustomerRegisterButton.setEnabled(true);
            }
        });

        CustomerRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = CustomerEmail.getText().toString();
                String password = CustomerPassword.getText().toString();

                RegisterDriver(email, password);
            }
        });

        CustomerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = CustomerEmail.getText().toString();
                String password = CustomerPassword.getText().toString();
                SignInDriver(email, password);

            }
        });
    }

    private void SignInDriver(String email, String password) {

        if (TextUtils.isEmpty(email)){

            Toast.makeText(CustomerLoginRegisterActivity.this, "Please write an email...", Toast.LENGTH_SHORT).show();

            Intent driverIntent = new Intent(CustomerLoginRegisterActivity.this, CustomerMapActivity.class);
            startActivity(driverIntent);
        }

        if (TextUtils.isEmpty(password)){

            Toast.makeText(CustomerLoginRegisterActivity.this, "Please write a password...", Toast.LENGTH_SHORT).show();
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

                        Intent driverIntent = new Intent(CustomerLoginRegisterActivity.this, CustomerMapActivity.class);
                        startActivity(driverIntent);

                        Toast.makeText(CustomerLoginRegisterActivity.this, "Login Successful...", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();


                    }

                    else
                    {
                        Toast.makeText(CustomerLoginRegisterActivity.this, "Login Unsuccessful,Please Try Again...", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                }
            });
        }
    }

    private void RegisterDriver(final String email, String password) {
        if (TextUtils.isEmpty(email)){

            Toast.makeText(CustomerLoginRegisterActivity.this, "Please write a email...", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(password)){

            Toast.makeText(CustomerLoginRegisterActivity.this, "Please write a password...", Toast.LENGTH_SHORT).show();
        }

        else{

            loadingBar.setTitle("Customer Registration...");
            loadingBar.setMessage("Please wait, registering data...");
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {

                        onlineCustomerID = mAuth.getCurrentUser().getUid();
                        CustomerDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(onlineCustomerID).child("name");
                        CustomerDatabaseRef.setValue(email);


                        Intent driverIntent = new Intent(CustomerLoginRegisterActivity.this, DriverMapActivity.class);
                        startActivity(driverIntent);

                        Toast.makeText(CustomerLoginRegisterActivity.this, "Successful Registration...", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                    else
                    {
                        Toast.makeText(CustomerLoginRegisterActivity.this, "Registration Unsuccessful,Please Try Again...", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                }
            });
        }

    }
}
