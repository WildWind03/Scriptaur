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
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import ru.nsu.fit.scriptaur.R;
import ru.nsu.fit.scriptaur.activity.DrawerActivity;
import ru.nsu.fit.scriptaur.activity.LoginActivity;
import ru.nsu.fit.scriptaur.common.DefaultObserver;
import ru.nsu.fit.scriptaur.common.PreferencesUtils;
import ru.nsu.fit.scriptaur.network.Api;
import ru.nsu.fit.scriptaur.network.ApiHolder;


public class ProfileFragment extends Fragment {

    @BindView(R.id.save)
    Button save;
    @BindView(R.id.signOut)
    Button signOut;

    @BindView(R.id.new_password)
    EditText text;
    @BindView(R.id.new_password_repeat)
    EditText editText;

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

    }

    @OnClick(R.id.signOut)
    public void signOut() {
        Api api = ApiHolder.getBackendApi();
        api.signOut(PreferencesUtils.getToken(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<ResponseBody>() {
                    @Override
                    public void onNextElement(ResponseBody userToken) throws Throwable {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "Failed to sign out", Toast.LENGTH_LONG).show();
                        //todo for degug only
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                });
    }

}
