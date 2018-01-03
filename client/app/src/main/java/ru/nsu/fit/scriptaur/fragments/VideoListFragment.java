package ru.nsu.fit.scriptaur.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Response;
import ru.nsu.fit.scriptaur.R;
import ru.nsu.fit.scriptaur.common.DefaultObserver;
import ru.nsu.fit.scriptaur.network.RetrofitServiceFactory;
import ru.nsu.fit.scriptaur.network.YoutubeApi;
import ru.nsu.fit.scriptaur.network.entities.Video;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class VideoListFragment extends Fragment {
    public static final String API_KEY = "AIzaSyB1EKAPqyzYEDcLmTK5ZaqmRLwzgHB8kmc";
    public static final String VIDEOS_LIST_KEY = "videos_list";
    private static final String BASE_URL = "https://www.googleapis.com/youtube/v3/";
    private static List<Video> videos;
    private static Map<Video, Bitmap> icons = new TreeMap<>();
    private static Map<Video, String> names = new TreeMap<>();
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.emptyListHint)
    TextView emptyListHint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videos = getArguments().getParcelableArrayList(VIDEOS_LIST_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.video_list_fragment, container, false);
        ButterKnife.bind(this, view);

        emptyListHint.setVisibility(videos.isEmpty() ? View.VISIBLE : View.INVISIBLE);

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
                TextView rating = ((TextView) view.findViewById(R.id.rating));
                ImageView icon = ((ImageView) view.findViewById(R.id.icon));

                if (names.containsKey(video)) {
                    link.setText(names.get(video));
                }

                rating.setText(String.format("Рейтинг %.2f", video.getRating()));

                if (icons.containsKey(video)) {
                    icon.setImageBitmap(icons.get(video));
                }

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


        @SuppressLint("StaticFieldLeak")
        AsyncTask<Video, Void, Void> loadTask = new AsyncTask<Video, Void, Void>() {
            @Override
            protected Void doInBackground(Video[] videos) {

                final YoutubeApi youtubeApi = RetrofitServiceFactory
                        .createRetrofitService(YoutubeApi.class, BASE_URL);

                for (final Video video : videos) {
                    youtubeApi.getMetaInfo(API_KEY, "snippet", video.getVideoUrl()).subscribe(new DefaultObserver<Response<ResponseBody>>() {
                        @Override
                        public void onNextElement(final Response<ResponseBody> response) throws JSONException, IOException {
                            String body = response.body().string();
                            JSONObject jsonObject = new JSONObject(body);
                            String title = jsonObject.getJSONArray("items").getJSONObject(0).getJSONObject("snippet").getString("title");

                            names.put(video, title);

                            String iconUrl = jsonObject.getJSONArray("items")
                                    .getJSONObject(0)
                                    .getJSONObject("snippet")
                                    .getJSONObject("thumbnails")
                                    .getJSONObject("medium").getString("url");

                            youtubeApi.getIcon(iconUrl).subscribe(new DefaultObserver<Response<ResponseBody>>() {
                                @Override
                                public void onNextElement(Response<ResponseBody> iconResponce) throws IOException {
                                    byte[] bytes = iconResponce.body().bytes();
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    icons.put(video, bitmap);
                                    publishProgress();
                                }
                            });
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
        return view;
    }
}
