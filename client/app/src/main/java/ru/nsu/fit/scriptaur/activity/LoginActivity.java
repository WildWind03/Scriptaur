package ru.nsu.fit.scriptaur.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.evtushenko.english.R;

public class LoginActivity extends AppCompatActivity {
    final static int REGISTRATION_ACTIVITY_CODE = 1;


    @BindView(R.id.login)
    EditText loginField;
    @BindView(R.id.password)
    EditText passwordField;
    @BindView(R.id.goButton)
    Button goButton;
    @BindView(R.id.signUpButton)
    Button signUpButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_activity_layout);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.signUpButton)
    void startSignUpActivity() {
        startActivityForResult(new Intent(this, RegistrationActivity.class),
                REGISTRATION_ACTIVITY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTRATION_ACTIVITY_CODE){
            Toast.makeText(this, resultCode+"", Toast.LENGTH_LONG).show();
        }
    }
}
