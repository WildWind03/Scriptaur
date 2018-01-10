package ru.nsu.fit.scriptaur.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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

    private static final String VIDEO_ID_KEY = "video_id_key";
    private static final String MARK_KEY = "mark_key";

    @NonNull
    public static AppCompatDialogFragment newInstance(int videoId, int userMark){
        AppCompatDialogFragment dialog = new VideoEndFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(VIDEO_ID_KEY, videoId);
        bundle.putInt(MARK_KEY, userMark);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    public static AppCompatDialogFragment newInstance(int videoId){
        AppCompatDialogFragment dialog = new VideoEndFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(VIDEO_ID_KEY, videoId);
        dialog.setArguments(bundle);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.rate_dialog, null);
        final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.new_rating);
        if (getArguments().containsKey(MARK_KEY)){
            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(
                    ContextCompat.getColor(getContext(), R.color.primaryDarkColor),
                    PorterDuff.Mode.SRC_ATOP);
            ratingBar.setRating(getArguments().getInt(MARK_KEY));
        }
        return builder.setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

