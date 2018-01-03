package ru.nsu.fit.scriptaur.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.gson.Gson;
import ru.nsu.fit.scriptaur.R;
import ru.nsu.fit.scriptaur.common.DefaultObserver;
import ru.nsu.fit.scriptaur.network.Api;
import ru.nsu.fit.scriptaur.network.ApiHolder;
import ru.nsu.fit.scriptaur.network.entities.SignUpData;
import ru.nsu.fit.scriptaur.network.entities.User;
import ru.nsu.fit.scriptaur.network.entities.UserToken;

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
        ButterKnife.bind(this);
    }

    @OnClick(R.id.confirm_registration_button)
    void signUp() {
        Api api = ApiHolder.getBackendApi();
        if (password.getText().toString().equals(repeatedPassword.getText().toString())) {
            api.signUp(new SignUpData(login.getText().toString(),
                    password.getText().toString(),
                    name.getText().toString())).subscribe(new DefaultObserver<UserToken>() {
                @Override
                public void onNextElement(UserToken userToken) throws Throwable {
                    Toast.makeText(RegistrationActivity.this, "Sign up", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onError(Throwable e) {
                    //todo real sign up error handle
                    Toast.makeText(RegistrationActivity.this, "Failed to sign up", Toast.LENGTH_LONG).show();
                    SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                    User user = new User(11, "name", 1.2f, "01/10/2007");
                    sharedPreferences.edit().putString("user_info", new Gson().toJson(user)).apply();
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
