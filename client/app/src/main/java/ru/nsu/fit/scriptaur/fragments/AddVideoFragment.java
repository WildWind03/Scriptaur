package ru.nsu.fit.scriptaur.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.nsu.fit.scriptaur.R;
import ru.nsu.fit.scriptaur.common.DefaultObserver;
import ru.nsu.fit.scriptaur.common.PreferencesUtils;
import ru.nsu.fit.scriptaur.network.ApiHolder;
import ru.nsu.fit.scriptaur.network.entities.Video;
import ru.nsu.fit.scriptaur.network.entities.VideoUrl;

import java.net.MalformedURLException;
import java.net.URL;

public class AddVideoFragment extends AppCompatDialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.add_video_layout, null);
        builder.setView(view)
                .setPositiveButton("Send", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddVideoFragment.this.getDialog().cancel();
                    }
                });
        Dialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText) view.findViewById(R.id.add_video_url);
                        String videoUrl = editText.getText().toString();

                        if(!videoUrl.startsWith("https://youtu.be/") && !videoUrl.startsWith("http://youtu.be/")){
                            videoUrl = "https://youtu.be/" + videoUrl;
                        }

                        try {
                            URL test = new URL(videoUrl);
                        } catch (MalformedURLException e) {
                            Toast.makeText(getContext(), "Incorrect URL", Toast.LENGTH_LONG).show();
                            return;
                        }
                        ApiHolder.getBackendApi()
                                .addVideo(PreferencesUtils.getToken(getActivity()), new VideoUrl(videoUrl))
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DefaultObserver<Video>()
                                {
                                    @Override
                                    public void onNext(Video v) {
                                        Fragment fragment = new SingleVideoFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString(SingleVideoFragment.VIDEO_ID_KEY, v.getVideoUrl());
                                        fragment.setArguments(bundle);
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.content_drawer, fragment).addToBackStack(null)
                                                .commit();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(getActivity(), "Video not added", Toast.LENGTH_LONG).show();
                                        super.onError(e);
                                    }
                                });
                        dialog.dismiss();
                    }
                });

            }
        });
        return dialog;
    }
}
