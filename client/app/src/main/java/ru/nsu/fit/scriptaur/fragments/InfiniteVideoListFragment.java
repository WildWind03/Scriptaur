package ru.nsu.fit.scriptaur.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import ru.nsu.fit.scriptaur.R;
import ru.nsu.fit.scriptaur.common.DefaultObserver;
import ru.nsu.fit.scriptaur.common.EndlessScrollListener;
import ru.nsu.fit.scriptaur.common.videos.VideosSource;
import ru.nsu.fit.scriptaur.network.entities.PagesCount;
import ru.nsu.fit.scriptaur.network.entities.Video;

import java.util.ArrayList;
import java.util.List;


public class InfiniteVideoListFragment extends Fragment {
    public static final String VIDEOS_SOURCE_KEY = "videos_source";

    private static VideosSource videosSource;
    private static ArrayList<Video> videos = new ArrayList<>();
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.emptyListHint)
    TextView emptyListHint;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    private int currentPage = 0;
    private int maxPage;

    private BaseAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videosSource = getArguments().getParcelable(VIDEOS_SOURCE_KEY);
        adapter = new BaseAdapter() {
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

                Glide.with(getContext()).load(video.getImageUrl()).into(icon);

                LocalTime time = new LocalTime(0, 0); // midnight
                time = time.plusSeconds(video.getLength());
                duration.setText(DateTimeFormat.forPattern("HH:mm:ss").print(time));

                rating.setRating(video.getRating());
                return view;
            }
        };
        loadFirstPage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_list_fragment_2, container, false);
        ButterKnife.bind(this, view);

        emptyListHint.setVisibility(videos.isEmpty() ? View.VISIBLE : View.INVISIBLE);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFirstPage();
            }
        });

        listView.setOnScrollListener(new EndlessScrollListener() {
            public boolean onLoadMore() {
                if (currentPage < maxPage) {
                    videosSource.getPage(currentPage++)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DefaultObserver<List<Video>>() {
                                @Override
                                public void onNext(List<Video> videos) {
                                    InfiniteVideoListFragment.videos.addAll(videos);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                    return true;
                } else {
                    return false;
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new SingleVideoFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(SingleVideoFragment.VIDEO_KEY, videos.get(position));
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_drawer, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }


    private void loadFirstPage() {
        currentPage = 0;
        maxPage = 0;
        videos.clear();
        videosSource.pagesCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        videosSource.getPage(currentPage++)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DefaultObserver<List<Video>>() {
                                    @Override
                                    public void onNext(List<Video> videos) {
                                        InfiniteVideoListFragment.videos.addAll(videos);
                                        adapter.notifyDataSetChanged();
                                        if (emptyListHint != null) {
                                            emptyListHint.setVisibility(videos.isEmpty() ? View.VISIBLE : View.INVISIBLE);
                                        }
                                        if (swipeContainer != null) {
                                            swipeContainer.setRefreshing(false);
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        super.onError(e);
                                    }
                                });


                    }
                })
                .subscribe(new DefaultObserver<PagesCount>() {
                    @Override
                    public void onNext(PagesCount pagesCount) {
                        maxPage = pagesCount.getPagesCount();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Toast.makeText(getActivity(), "Request failed", Toast.LENGTH_LONG).show();
                    }
                });

    }
}
