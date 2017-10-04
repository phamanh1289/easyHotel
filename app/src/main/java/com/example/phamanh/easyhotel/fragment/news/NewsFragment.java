package com.example.phamanh.easyhotel.fragment.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.phamanh.easyhotel.R;
import com.example.phamanh.easyhotel.adapter.NewsAdapter;
import com.example.phamanh.easyhotel.base.BaseFragment;
import com.example.phamanh.easyhotel.interfaces.ItemListener;
import com.example.phamanh.easyhotel.model.NewsModel;
import com.example.phamanh.easyhotel.utils.AppUtils;
import com.example.phamanh.easyhotel.utils.KeyboardUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class NewsFragment extends BaseFragment implements ItemListener, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.actionbar_imgBack)
    FrameLayout ivBack;
    Unbinder unbinder;
    @BindView(R.id.fragNews_rvNew)
    RecyclerView rvNew;
    @BindView(R.id.fragNews_srlRefresh)
    SwipeRefreshLayout srlRefresh;
    private List<NewsModel> mData = new ArrayList<>();
    private NewsAdapter mAdapter;
    private String mImage, mDescription, mTitle, mDate, mLink = "";
    private boolean isLoading = true;
    private int visibleThreshold = 5, visibleItemCount, firstVisibleItem, totalItemCount;
    private int previousTotal = 0, page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        KeyboardUtils.setupUI(view, getActivity());
        setActionBar(view, getString(R.string.page_news));
        unbinder = ButterKnife.bind(this, view);
        ivBack.setVisibility(View.GONE);
        init();
        getData();
        return view;
    }

    private void init() {
        mAdapter = new NewsAdapter(mData);
        mAdapter.setClickLister(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvNew.setLayoutManager(layoutManager);
        rvNew.setAdapter(mAdapter);
        srlRefresh.setOnRefreshListener(this);
        rvNew.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                visibleItemCount = rvNew.getChildCount();
                if (isLoading) {
                    if (totalItemCount > previousTotal + 1) {
                        isLoading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    page++;
                    getData();
                    isLoading = true;
                }
            }
        });
    }

    public void getData() {
        srlRefresh.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://news.zing.vn/du-lich" + "/trang" + page + ".html", response -> {
            Document document = Jsoup.parse(response);
            if (document != null) {
                Element element = document.select("section.cate_content").first();
                Elements elements = element.select("article.picture");
                for (Element news : elements) {
                    NewsModel item = new NewsModel();
                    Element title = news.select("p.title").first();
                    mTitle = title.text();
                    Element imageNews = news.select("div.cover").first();
                    Element image = imageNews.getElementsByTag("img").first();
                    if (image != null) {
                        mImage = image.attr("src");
                    }
                    Element time = news.getElementsByTag("time").first();
                    if (time != null) {
                        mDate = AppUtils.parseDate(time.attr("datetime"));
                    }
                    Element descriptionShort = news.select("p.summary").first();
                    if (descriptionShort != null) {
                        mDescription = descriptionShort.text();
                    }
                    Element link = title.getElementsByTag("a").first();
                    if (link != null) {
                        mLink = link.attr("href");
                    }
                    item.setTitle(mTitle);
                    item.setDate(mDate);
                    item.setDescription(mDescription);
                    item.setLink(mLink);
                    item.setLinkImage(mImage);
                    mData.add(item);
                }
            }
            mAdapter.notifyDataSetChanged();
            srlRefresh.setRefreshing(false);
        }, error -> srlRefresh.setRefreshing(false));
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        mData.clear();
        mAdapter.notifyDataSetChanged();
        isLoading = false;
        getData();
    }

    @Override
    public void onItemClicked(int pos) {
        addFragment(NewDetailFragment.newInstance("https://news.zing.vn" + mData.get(pos).getLink()), true);
    }

    private String toChangeTime(String time) {
        String[] arrSecond = time.substring(0,time.length()-5).split(" ");
        return arrSecond[1] + " " + arrSecond[0];
    }
}
