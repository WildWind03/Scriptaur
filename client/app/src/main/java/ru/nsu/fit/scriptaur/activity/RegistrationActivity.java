package ru.nsu.fit.scriptaur.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import ru.nsu.fit.scriptaur.R;
import ru.nsu.fit.scriptaur.network.Api;
import ru.nsu.fit.scriptaur.network.RetrofitServiceFactory;

public class RegistrationActivity extends AppCompatActivity {

    @BindView(R.id.new_login)
    EditText login;

    @BindView(R.id.new_name)
    EditText name;

    @BindView(R.id.new_password)
    EditText password;

    @BindView(R.id.new_password_repeat)
    EditText repeatedPassword;

    @BindView(R.id.confirm_registration_button)
    Button confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registraion_activity);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.confirm_registration_button)
    void signUp() {
//        RetrofitServiceFactory.createRetrofitService(Api.class, "localhost:1111");
        //TODO check password equals, sign up and save token to shared preferences
        setResult(RESULT_OK);
        finish();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
