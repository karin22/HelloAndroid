package com.karin.helloandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.karin.helloandroid.MESSAGE";
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testFirestore();
    }
    public void openListpage(View view){

        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);

    }


//    public void sendMessage(View view){
//        Intent intent = new Intent(this, DisplayNameActivity.class);
//        EditText editText =  findViewById(R.id.editText);
//        String message = editText.getText().toString();
//
//        Log.d("editText",message);
//
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
//        Toast.makeText(getApplicationContext(),"Send Message Clicked",Toast.LENGTH_SHORT).show();
//
//    }
    public void testFirestore(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String TAG= "testFirestore";
        db.collection("recommanded")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public void signInClick(View view){
        final String TAG = "signInClick";
        String email = ((TextView)findViewById(R.id.editText)).getText().toString();
        String password = ((TextView)findViewById(R.id.editText2)).getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    public void sigoutClick(View view){
        final String TAG = "sigoutClick";
        Log.w(TAG, "sign out");
        mAuth.signOut();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            updateUI(currentUser);
        }
    }

    public void updateUI(FirebaseUser currentUser){
        final String TAG = "updateUI";
        TextView loginName = findViewById(R.id.textView);
        if(currentUser != null){
            Log.d(TAG, currentUser.getEmail());

            loginName.setText(currentUser.getEmail());
        }
        else{
            loginName.setText("");
        }
    }
}
