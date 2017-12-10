package ru.nsu.fit.scriptaur.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import retrofit2.Response;
import ru.evtushenko.english.R;
import ru.nsu.fit.scriptaur.model.AbstractPlayerListener;
import ru.nsu.fit.scriptaur.model.Caption;
import ru.nsu.fit.scriptaur.network.CaptionsService;
import ru.nsu.fit.scriptaur.network.RetrofitServiceFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class SingleVideoFragment extends Fragment {
    public static final String API_KEY = "AIzaSyB1EKAPqyzYEDcLmTK5ZaqmRLwzgHB8kmc";
    public static final String VIDEO_ID_KEY = "video_id";

    private static final String BASE_URL = "http://video.google.com/";

    private List<Caption> captions;
    private int curCaption = -1;
    private boolean answered = true;
    private Timer timer = new Timer();

    private YouTubePlayer player;

    @BindView(R.id.repeatButton)
    Button repeatButton;
    @BindView(R.id.skipButton)
    Button skipButton;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.editText)
    EditText editText;


    private static String videoId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoId = getArguments().getString(VIDEO_ID_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.api_fragment, container, false);
        ButterKnife.bind(this, view);

        YouTubePlayerSupportFragment youTubePlayer
                = (YouTubePlayerSupportFragment) getChildFragmentManager().findFragmentById(R.id.playerContainer);
        youTubePlayer.initialize(API_KEY, new PlayerInitializedListener());

        CaptionsService service = RetrofitServiceFactory
                .createRetrofitService(CaptionsService.class, BASE_URL);

        service.text(videoId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    XmlPullParser parser = Xml.newPullParser();

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBody) {
                        try {
                            captions = parseResponse(responseBody.body());
                        } catch (XmlPullParserException | IOException e) {
                            Log.e(getClass().getSimpleName(), "Failed to parse captions XML", e);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(getClass().getSimpleName(), "Failed to load captions", e);
                    }

                    @Override
                    public void onComplete() {

                    }

                    private List<Caption> parseResponse(ResponseBody response)
                            throws XmlPullParserException, IOException {
                        List<Caption> result = new ArrayList<>();
                        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                        parser.setInput(new StringReader(response.string()));
                        parser.nextTag();
                        parser.require(XmlPullParser.START_TAG, null, "transcript");
                        while (parser.next() != XmlPullParser.END_TAG) {
                            if (parser.getEventType() != XmlPullParser.START_TAG) {
                                continue;
                            }
                            String name = parser.getName();
                            String text;
                            if (name.equals("text")) {
                                String start = parser.getAttributeValue(null, "start");
                                String duration = parser.getAttributeValue(null, "dur");
                                text = readText(parser);
                                result.add(new Caption(start, duration, text));
                            } else {
                                throw new IOException("Unknown tag");
                            }
                        }
                        return result;
                    }

                    private String readText(XmlPullParser parser)
                            throws IOException, XmlPullParserException {
                        parser.require(XmlPullParser.START_TAG, null, "text");
                        String result = "";
                        if (parser.next() == XmlPullParser.TEXT) {
                            result = parser.getText();
                            parser.nextTag();
                        }
                        parser.require(XmlPullParser.END_TAG, null, "text");
                        return result;
                    }
                });
        return view;
    }

    @OnTextChanged(value = R.id.editText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void onTextChanged(Editable s) {
        String s1 = s.toString().toLowerCase().replaceAll("[\\W]", "");
        String s2 = captions.get(curCaption).getText().toLowerCase().replaceAll("[\\W]", "");
        System.out.println(s1);
        System.out.println(s2);
        if (s1.equals(s2)) {
            answered = true;
            System.out.println("CORRECT");
            editText.post(new Runnable() {
                @Override
                public void run() {
                    editText.setText("");
                }
            });
        }
    }

    @OnClick(R.id.repeatButton)
    public void repeat() {
        player.seekToMillis(captions.get(curCaption).getStart()-50);
        player.play();
    }

    @OnClick(R.id.skipButton)
    public void skip() {
        curCaption++;
        player.seekToMillis(captions.get(curCaption).getStart()-50);
        text.post(new Runnable() {
            @Override
            public void run() {
                text.setText(captions.get(curCaption).getText());
            }
        });
        player.play();
    }




    private class PlayerInitializedListener implements YouTubePlayer.OnInitializedListener {
        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                            final YouTubePlayer player,
                                            boolean restored) {
            SingleVideoFragment.this.player = player;
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
            AbstractPlayerListener listener = new PlayerListener();
            player.setPlayerStateChangeListener(listener);
            player.setPlaybackEventListener(listener);
            if (!restored) {
                player.cueVideo(videoId);
            }
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
            Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_LONG).show();
        }
    }

    private class PlayerListener extends AbstractPlayerListener {
        @Override
        public void onVideoStarted() {
            super.onVideoStarted();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    int curTime = player.getCurrentTimeMillis();
                    if (curCaption + 1 < captions.size()
                            && curTime >= captions.get(curCaption + 1).getStart()) {
                        if (answered) {
                            curCaption++;
                            if (player.getCurrentTimeMillis() > captions.get(curCaption).getStart() && curCaption > 0) {
                                player.seekToMillis(captions.get(curCaption).getStart() - 10);
                            }
                            player.play();
                            answered = false;
                        } else {
                            player.pause();
                        }
                        text.post(new Runnable() {
                            @Override
                            public void run() {
                                text.setText(captions.get(curCaption).getText());
                            }
                        });
                    }

                }
            }, 0, 100);

        }

        @Override
        public void onLoaded(String s) {
            player.play();
        }
    }
}
