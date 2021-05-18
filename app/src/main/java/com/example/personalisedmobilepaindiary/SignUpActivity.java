package com.example.personalisedmobilepaindiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.personalisedmobilepaindiary.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements TextWatcher {
    private ActivitySignUpBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        auth = FirebaseAuth.getInstance();

        binding.signUpEmail.addTextChangedListener(SignUpActivity.this);
        binding.signUpPwd.addTextChangedListener(SignUpActivity.this);

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.signUpEmail.getText().toString();
                String pwd = binding.signUpPwd.getText().toString();
                if (email.length() > 0 && pwd.length() > 0) {
                    auth.createUserWithEmailAndPassword(email, pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(SignUpActivity.this,
                                    "Sign Up successfully", Toast.LENGTH_LONG).show();
                            Intent intent = getIntent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this, "Unable to create an account, please try again", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(SignUpActivity.this, "Empty field", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public boolean isValidateEmail(String str) {
        if (str.length() == 0 || str == null) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(emailRegex);
        Matcher m = p.matcher(str);
        return m.matches();

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String input = binding.signUpEmail.getText().toString();
        if (!isValidateEmail(input)) {
            binding.signUpEmail.setError("Invalid email!");
        }
        if (binding.signUpPwd.getText().toString().length() < 6){
            binding.signUpPwd.setError("Password must be longer than 6 characters");
        }
    }
}