package eg.com.iti.mshwar.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;

import eg.com.iti.mshwar.R;


public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";

    //firebase
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;

    //widgets
    private EditText mEmail, mCurrentPassword, mName;
    private Button mSave, changeImage;
    private ProgressBar mProgressBar;
    private TextView mResetPasswordLink;
    private ImageView mImage;

    private static final int PICK_IMAGE = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Log.d(TAG, "onCreate: started.");

        user = FirebaseAuth.getInstance().getCurrentUser();

        //mCurrentPassword = (EditText) findViewById(R.id.input_password);
        mEmail             = (EditText) findViewById(R.id.input_email);
        mName              = (EditText) findViewById(R.id.input_name);
        mSave              = (Button) findViewById(R.id.btn_save);
        mProgressBar       = (ProgressBar) findViewById(R.id.progressBar);
        mResetPasswordLink = (TextView) findViewById(R.id.change_password);
        changeImage        = findViewById(R.id.change_image);
        mImage             = findViewById(R.id.input_image);

        mEmail.setEnabled(false);

        // Setup upper toolbar with title and back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_activity_settings));

        setupFirebaseAuth();

        setCurrentUserData();

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to save settings.");
                String uName = mName.getText().toString();
                if ( uName .length() > 0){

                    mSave.setClickable(false);
                    UserProfileChangeRequest profileUpdates;

                    if (imageUri != null){
                        profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(uName)
                                .setPhotoUri(Uri.parse(imageUri.toString()))
                                .build();
                    } else {
                        profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(uName)
                                .build();
                    }

                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mSave.setClickable(true);
                                Toast.makeText(SettingsActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                mSave.setClickable(true);
                                Toast.makeText(SettingsActivity.this, "An error occurred. Please try again later!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(SettingsActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        mResetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResetPasswordLink();
            }
        });
    }

    @Override // For action bar
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void sendResetPasswordLink(){
        FirebaseAuth.getInstance().sendPasswordResetEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SettingsActivity.this, "Password reset link was sent to your email",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SettingsActivity.this, "No user associated with that email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setCurrentUserData(){
        Log.d(TAG, "setCurrentEmail: setting current email to EditText field");
        if(user != null){
            Log.d(TAG, "setCurrentEmail: user is NOT null.");
            mEmail.setText(user.getEmail());
            mName.setText(user.getDisplayName());
            mImage.setImageURI(user.getPhotoUrl());
            if (mImage.getDrawable() == null) mImage.setImageResource(R.drawable.nav_user);
        }
    }

    private boolean isEmpty(String string){
        return string.equals("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();

    }

    private void checkAuthenticationState(){
        Log.d(TAG, "checkAuthenticationState: checking authentication state.");

        if(user == null){
            Log.d(TAG, "checkAuthenticationState: user is null, navigating back to login screen.");

            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
            Log.d(TAG, "checkAuthenticationState: user is authenticated.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && data != null){
            imageUri = data.getData();
            mImage.setImageURI(imageUri);
        }
    }

    /*----------------------------- Firebase setup ---------------------------------*/
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: started.");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Toast.makeText(SettingsActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }
}