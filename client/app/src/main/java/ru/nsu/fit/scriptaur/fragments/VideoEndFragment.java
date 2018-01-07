package ru.nsu.fit.scriptaur.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.nsu.fit.scriptaur.R;
import ru.nsu.fit.scriptaur.common.DefaultObserver;
import ru.nsu.fit.scriptaur.common.PreferencesUtils;
import ru.nsu.fit.scriptaur.network.ApiHolder;
import ru.nsu.fit.scriptaur.network.entities.MarkData;
import ru.nsu.fit.scriptaur.network.entities.Video;

public class VideoEndFragment extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.rate_dialog, null);
        return builder.setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.new_rating);
                        int rating = (int) ratingBar.getRating();
                        if (rating > 0) {
                            int videoId = getArguments().getInt(SingleVideoFragment.VIDEO_ID_KEY);
                            MarkData markData = new MarkData(videoId, rating);
                            ApiHolder.getBackendApi().addMark(PreferencesUtils.getToken(getContext()), markData)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new DefaultObserver<Video>());
                        }
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        VideoEndFragment.this.getDialog().cancel();
                    }
                }).create();
    }
}

