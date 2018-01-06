package ru.nsu.fit.scriptaur.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import ru.nsu.fit.scriptaur.R;
import ru.nsu.fit.scriptaur.activity.LoginActivity;
import ru.nsu.fit.scriptaur.common.DefaultObserver;
import ru.nsu.fit.scriptaur.common.PreferencesUtils;
import ru.nsu.fit.scriptaur.network.Api;
import ru.nsu.fit.scriptaur.network.ApiHolder;
import ru.nsu.fit.scriptaur.network.entities.SignUpData;


public class ProfileFragment extends Fragment {

    @BindView(R.id.save)
    Button save;
    @BindView(R.id.signOut)
    Button signOut;

    @BindView(R.id.new_password)
    EditText password;
    @BindView(R.id.new_password_repeat)
    EditText repeatedPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.account_fragment, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.save)
    public void save() {
        Api api = ApiHolder.getBackendApi();
        String passwordText = password.getText().toString();
        String passwordRepeatText = repeatedPassword.getText().toString();
        if (passwordText.equals(passwordRepeatText)) {
            api.changePassword(PreferencesUtils.getToken(getActivity()), new SignUpData("", passwordText, ""))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DefaultObserver<ResponseBody>() {
                        @Override
                        public void onNextElement(ResponseBody responseBody) throws Throwable {
                            Toast.makeText(getActivity(), "Password successfully changed", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getActivity(), "Failed to change password", Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "Passwords are not equals", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.signOut)
    public void signOut() {
        Api api = ApiHolder.getBackendApi();
        api.signOut(PreferencesUtils.getToken(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<ResponseBody>(){
                    @Override
                    public void onError(Throwable e) {
                        PreferencesUtils.setToken(getActivity(), null);
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }

                    @Override
                    public void onComplete() {
                        PreferencesUtils.setToken(getActivity(), null);
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                });
    }

}
