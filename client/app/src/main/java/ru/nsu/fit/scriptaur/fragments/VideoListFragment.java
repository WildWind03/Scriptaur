package ru.nsu.fit.scriptaur.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import ru.nsu.fit.scriptaur.R;
import ru.nsu.fit.scriptaur.common.DefaultObserver;
import ru.nsu.fit.scriptaur.common.videos.VideosSource;
import ru.nsu.fit.scriptaur.network.ApiHolder;
import ru.nsu.fit.scriptaur.network.YoutubeApi;
import ru.nsu.fit.scriptaur.network.entities.PagesCount;
import ru.nsu.fit.scriptaur.network.entities.Video;


public class VideoListFragment extends Fragment {
    public static final String VIDEOS_SOURCE_KEY = "videos_source";

    private static VideosSource videosSource;
    private static List<Video> videos = new ArrayList<>();
    private static Map<Integer, Bitmap> icons = new TreeMap<>();
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.emptyListHint)
    TextView emptyListHint;
    @BindView(R.id.prevPage)
    Button prevPageButton;
    @BindView(R.id.nextPage)
    Button nextPageButton;
    @BindView(R.id.pageHint)
    TextView pageHint;
    private int currentPage = 0;
    private int maxPage = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videosSource = getArguments().getParcelable(VIDEOS_SOURCE_KEY);
    }

    public void setVideoSource(VideosSource videoSource){
        getArguments().putParcelable(VIDEOS_SOURCE_KEY, videoSource);
        videosSource = videoSource;
        getDataFromVideoSource();
    }

    private void switchToPage(final int page) {

        videosSource.getPage(page)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<Video>>() {
                    @Override
                    public void onNext(List<Video> videosFromPage) {
                        prevPageButton.setEnabled(page > 0);
                        nextPageButton.setEnabled(page < maxPage - 1);

                        currentPage = page;
                        pageHint.setText(String.format("Page %d from %d", currentPage + 1, maxPage));

                        videos = videosFromPage;

                        @SuppressLint("StaticFieldLeak")
                        AsyncTask<Video, Void, Void> loadTask = new AsyncTask<Video, Void, Void>() {
                            @Override
                            protected Void doInBackground(Video[] videos) {

                                final YoutubeApi youtubeApi = ApiHolder.getYoutubeApi();

                                for (final Video video : videos) {
                                    String iconUrl = video.getImageUrl();
                                    youtubeApi.getIcon(iconUrl).subscribe(new DefaultObserver<Response<ResponseBody>>() {
                                        @Override
                                        public void onNextElement(Response<ResponseBody> iconResponce) throws IOException {
                                            if (iconResponce.body() == null) {
                                                Log.e("Incorrect icon url", video.getImageUrl());
                                                return;
                                            }

                                            byte[] bytes = iconResponce.body().bytes();
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                            icons.put(video.getVideoId(), bitmap);
                                            publishProgress();
                                        }
                                    });
                                }

                                return null;
                            }

                            @Override
                            protected void onProgressUpdate(Void... values) {
                                listView.invalidateViews();
                            }
                        };

                        loadTask.execute(videos.toArray(new Video[videos.size()]));

                        listView.invalidateViews();
                        emptyListHint.setVisibility(videos.isEmpty() ? View.VISIBLE : View.INVISIBLE);
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.video_list_fragment, container, false);
        ButterKnife.bind(this, view);

        emptyListHint.setVisibility(videos.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        nextPageButton.setEnabled(false);
        prevPageButton.setEnabled(false);

        final BaseAdapter adapter = new BaseAdapter() {

            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            @Override
            public int getCount() {
                return videos.size();
            }

            @Override
            public Object getItem(int i) {
                return videos.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int position, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = layoutInflater.inflate(R.layout.video_list_element, viewGroup, false);
                }

                Video video = (Video) getItem(position);

                TextView link = ((TextView) view.findViewById(R.id.link));
                TextView duration = ((TextView) view.findViewById(R.id.duration));
                RatingBar rating = ((RatingBar) view.findViewById(R.id.rating));
                ImageView icon = ((ImageView) view.findViewById(R.id.icon));

                link.setText(video.getName());

                if (icons.containsKey(video.getVideoId())) {
                    icon.setImageBitmap(icons.get(video.getVideoId()));
                } else {
                    icon.setImageBitmap(null);
                }

                LocalTime time = new LocalTime(0, 0); // midnight
                time = time.plusSeconds(video.getLength());
                duration.setText(DateTimeFormat.forPattern("HH:mm:ss").print(time));

                rating.setRating(video.getRating());
                return view;
            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), videos.get(position).getVideoUrl(), Toast.LENGTH_LONG).show();
                Fragment fragment = new SingleVideoFragment();
                Bundle bundle = new Bundle();
                bundle.putString(SingleVideoFragment.VIDEO_ID_KEY, videos.get(position).getVideoUrl());
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_drawer, fragment).addToBackStack(null)
                        .commit();
            }
        });

        listView.setAdapter(adapter);

        getDataFromVideoSource();
        return view;
    }

    @OnClick(R.id.prevPage)
    void gotoPrevPage() {
        switchToPage(currentPage - 1);
    }

    @OnClick(R.id.nextPage)
    void gotoNextPage() {
        switchToPage(currentPage + 1);
    }

    private void getDataFromVideoSource(){
        videosSource.pagesCount()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<PagesCount>() {
                    @Override
                    public void onNext(PagesCount pagesCount) {
                        maxPage = pagesCount.getPagesCount();
                        switchToPage(0);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Toast.makeText(getContext(), "Request failed", Toast.LENGTH_LONG).show();
                    }
                });
    };
}
