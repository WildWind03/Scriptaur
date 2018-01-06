package ru.nsu.fit.scriptaur.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.nsu.fit.scriptaur.R;
import ru.nsu.fit.scriptaur.common.DefaultObserver;
import ru.nsu.fit.scriptaur.common.PreferencesUtils;
import ru.nsu.fit.scriptaur.network.Api;
import ru.nsu.fit.scriptaur.network.ApiHolder;
import ru.nsu.fit.scriptaur.network.entities.SignInData;
import ru.nsu.fit.scriptaur.network.entities.User;
import ru.nsu.fit.scriptaur.network.entities.UserToken;


public class LoginActivity extends AppCompatActivity {
    public final static String USERNAME_KEY = "USER_NAME";
    private final static int REGISTRATION_ACTIVITY_CODE = 1;

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
        String token = PreferencesUtils.getToken(this);
        if (token != null) {
            ApiHolder.getBackendApi().getUser(token)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<User>() {
                                   @Override
                                   public void onNext(User user) {
                                       Intent intent = new Intent(LoginActivity.this,
                                               DrawerActivity.class);
                                       intent.putExtra(USERNAME_KEY, user.getUserName());
                                       startActivity(intent);
                                       finish();
                                   }

                                   @Override
                                   public void onError(Throwable e) {
                                       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                               WindowManager.LayoutParams.FLAG_FULLSCREEN);
                                       setContentView(R.layout.login_activity_layout);
                                       ButterKnife.bind(LoginActivity.this);
                                   }
                               }
                    );
        }
    }

    @OnClick(R.id.signUpButton)
    void startSignUpActivity() {
        startActivityForResult(new Intent(this, RegistrationActivity.class),
                REGISTRATION_ACTIVITY_CODE);
    }

    @OnClick(R.id.goButton)
    void signIn() {
        Api api = ApiHolder.getBackendApi();
        api.signIn(new SignInData(loginField.getText().toString(),
                passwordField.getText().toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<UserToken>() {
                    @Override
                    public void onNextElement(UserToken userToken) throws Throwable {
                        PreferencesUtils.setToken(LoginActivity.this, userToken.getToken());
                        startActivity(new Intent(LoginActivity.this, DrawerActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(LoginActivity.this, "Failed to sign in", Toast.LENGTH_LONG).show();
                        Log.e("LOGIN", "fail", e);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTRATION_ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {
                startActivity(new Intent(this, DrawerActivity.class));
                finish();
            }
        }
    }
}
