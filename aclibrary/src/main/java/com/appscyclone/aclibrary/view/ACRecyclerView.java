package com.appscyclone.aclibrary.view;
/*
 * Created by HoangDong on 07/09/2017.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;

import com.appscyclone.aclibrary.R;
import com.appscyclone.aclibrary.view.adapter.ACBaseAdapter;
import com.appscyclone.aclibrary.view.adapter.ACExpandBaseAdapter;
import com.appscyclone.aclibrary.view.decoration.ACSpaceDecoration;
import com.appscyclone.aclibrary.view.decoration.ACStickyHeaderDecoration;
import com.appscyclone.aclibrary.view.model.ACBaseGroupModel;
import com.appscyclone.aclibrary.view.model.ACBaseTextImageModel;
import com.appscyclone.aclibrary.view.model.ACBaseTextModel;
import com.appscyclone.aclibrary.view.viewholder.ACTextImageViewHolder;
import com.appscyclone.aclibrary.view.viewholder.ACTextViewHolder;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class ACRecyclerView extends FrameLayout {

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnItemListener {
        void onItemClicked(View view, int position);
    }

    public interface OnGroupClickListener {
        boolean onGroupClick(int GroupPos);
    }

    public interface GroupExpandCollapseListener {
        void onGroupExpanded(ACBaseGroupModel group);

        void onGroupCollapsed(ACBaseGroupModel group);
    }

    private OnLoadMoreListener onMoreListener;
    private OnItemListener onItemListener;
    private SwipeRefreshLayout swipeRefresh;
    private FrameLayout frmEmptyView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ACBaseAdapter mAdapter;

    private int mEmptyViewId, mLoadMoreId;
    private int mScrollbar;
    private int mPage = 1, mMaxPage = 1;
    private int mThresholdLoadMore = 3;
    private int mScrollbarStyle;
    private int mSpace, mSpaceItem, mSpaceStart, mSpaceFooter, mSpaceEdge, mColorSwipeRefresh, mColorLoadMore;
    private boolean mHasFixedSize;

    public ACRecyclerView(Context context) {
        super(context);
    }

    public ACRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initView();
    }

    public ACRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(attrs);
        initView();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ACRecyclerView);
        try {
            mScrollbar = a.getInteger(R.styleable.ACRecyclerView_rv_scrollbars, -1);
            mScrollbarStyle = a.getInteger(R.styleable.ACRecyclerView_rv_scrollbarStyle, -1);
            mHasFixedSize = a.getBoolean(R.styleable.ACRecyclerView_rv_hasFixedSize, true);
            mEmptyViewId = a.getResourceId(R.styleable.ACRecyclerView_layout_empty, 0);
            mLoadMoreId = a.getResourceId(R.styleable.ACRecyclerView_layout_loadMore, 0);
            mColorSwipeRefresh = a.getResourceId(R.styleable.ACRecyclerView_color_swipeRefresh, -1);
            mColorLoadMore = a.getResourceId(R.styleable.ACRecyclerView_color_loadMore, -1);
            mSpace = (int) a.getDimension(R.styleable.ACRecyclerView_rv_space, -1);
            mSpaceItem = (int) a.getDimension(R.styleable.ACRecyclerView_rv_spaceHeight, 0);
            mSpaceStart = (int) a.getDimension(R.styleable.ACRecyclerView_rv_spaceStart, 0);
            mSpaceFooter = (int) a.getDimension(R.styleable.ACRecyclerView_rv_spaceFooter, 0);
            mSpaceEdge = (int) a.getDimension(R.styleable.ACRecyclerView_rv_spaceEdge, 0);
        } finally {
            a.recycle();
        }
    }

    private void initView() {
        if (isInEditMode()) {
            return;
        }
        View view = LayoutInflater.from(getContext()).inflate(R.layout.acview_recylerview, this);
        swipeRefresh = view.findViewById(R.id.acViewRecyclerView_swipeRefresh);
        swipeRefresh.setEnabled(false);
        frmEmptyView = view.findViewById(R.id.acViewRecyclerView_frmEmpty);
        if (mEmptyViewId != 0)
            LayoutInflater.from(getContext()).inflate(mEmptyViewId, frmEmptyView);
        if (mColorSwipeRefresh != -1)
            setColorRefreshing(mColorSwipeRefresh);
        initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.acViewRecyclerView_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (mScrollbarStyle != -1) {
            recyclerView.setScrollBarStyle(mScrollbarStyle);
        }

        if (recyclerView != null) {
            setHasFixedSize(mHasFixedSize);
            switch (mScrollbar) {
                case 0:
                    recyclerView.setVerticalScrollBarEnabled(false);
                    break;
                case 1:
                    recyclerView.setHorizontalScrollBarEnabled(false);
                    break;
                case 2:
                    recyclerView.setVerticalScrollBarEnabled(false);
                    recyclerView.setHorizontalScrollBarEnabled(false);
                    break;
            }
        }
        ACSpaceDecoration spaceDecoration;
        if (mSpace != -1) {
            spaceDecoration = new ACSpaceDecoration(mSpace, mSpace, mSpace, mSpace);

        } else {
            spaceDecoration = new ACSpaceDecoration(mSpaceItem, mSpaceStart, mSpaceFooter, mSpaceEdge);
        }
        recyclerView.addItemDecoration(spaceDecoration);

    }

    public void setHasFixedSize(boolean isFixed) {
        recyclerView.setHasFixedSize(isFixed);

    }

    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        if (manager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(obtainGridSpanSizeLookUp(gridLayoutManager.getSpanCount()));
        }
        recyclerView.setLayoutManager(manager);
    }

    private class GridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
        private int mMaxCount;

        GridSpanSizeLookup(int maxCount) {
            this.mMaxCount = maxCount;
        }

        @Override
        public int getSpanSize(int position) {
            if (onMoreListener != null) {
                switch (mAdapter.getItemViewType(position)) {
                    case ACBaseAdapter.TYPE_ITEM:
                        return 1;
                    case ACBaseAdapter.TYPE_LOADING:
                        return this.mMaxCount;
                    default:
                        return -1;
                }
            } else return -1;
        }
    }

    public GridSpanSizeLookup obtainGridSpanSizeLookUp(int maxCount) {
        return new GridSpanSizeLookup(maxCount);
    }

    public void setPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        if(recyclerView != null)
            recyclerView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    public void setRefresh(SwipeRefreshLayout.OnRefreshListener refreshListener) {
        swipeRefresh.setEnabled(true);
        swipeRefresh.setOnRefreshListener(() -> {
            setPage(1);
            refreshListener.onRefresh();
        });
    }

    public void setRefreshing(boolean isShow) {
        swipeRefresh.setRefreshing(isShow);
    }

    public boolean isRefreshing() {
        return swipeRefresh.isRefreshing();
    }

    public void setColorRefreshing(@ColorInt int color) {
        swipeRefresh.setColorSchemeColors(color);
    }

    public SwipeRefreshLayout getRefreshLayout() {
        return swipeRefresh;
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        recyclerView.addItemDecoration(decor);
    }

    public void addStickyHeaderDecoration(ACStickyHeaderDecoration.StickyHeaderAdapter headerAdapter) {
        recyclerView.addItemDecoration(new ACStickyHeaderDecoration(headerAdapter));
    }

    public void setLoadMore(ACRecyclerView.OnLoadMoreListener moreListener) {
        this.onMoreListener = moreListener;
        mLayoutManager = recyclerView.getLayoutManager();
        mAdapter = (ACBaseAdapter) getRecyclerView().getAdapter();
        setLayoutLoadMore(mAdapter);
        setColorLoadMore(mAdapter);
        recyclerView.addOnScrollListener(onScrollListener);
    }

    private void setLayoutLoadMore(ACBaseAdapter adapter) {
        if (adapter != null && mLoadMoreId != 0) {
            View view = LayoutInflater.from(this.getContext()).inflate(mLoadMoreId, recyclerView, false);
            adapter.setViewLoadMore(view);
        }
    }

    private void setColorLoadMore(ACBaseAdapter adapter) {
        if (adapter != null && mColorLoadMore != -1) {
            adapter.setColorLoadMore(mColorLoadMore);
        }
    }

    public void setOnItemListener(OnItemListener itemListener) {
        this.onItemListener = itemListener;
        mAdapter.setParentClick(true);
        mAdapter.setOnItemListener(onItemListener);
    }

    public void setOnItemListener(OnItemListener itemListener, @ArrayRes Integer[] arrIdRes, boolean isParentClick) {
        this.onItemListener = itemListener;
        mAdapter.setParentClick(isParentClick);
        mAdapter.setOnItemListener(onItemListener, arrIdRes);
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        int lastVisibleItems, totalItemCount;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            totalItemCount = mLayoutManager.getItemCount();
            if (mLayoutManager instanceof LinearLayoutManager)
                lastVisibleItems = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            else if (mLayoutManager instanceof StaggeredGridLayoutManager)
                lastVisibleItems = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null)[0];
            if (totalItemCount <= (lastVisibleItems + mThresholdLoadMore)) {
                if (mAdapter != null && !mAdapter.getLoading() && onMoreListener != null && (mPage < mMaxPage)) {
                    mPage++;
                    mAdapter.getData().add(null);
                    mAdapter.notifyItemInserted(mAdapter.getData().size() - 1);
                    onMoreListener.onLoadMore();
                    mAdapter.setLoading(true);
                }
            }
        }
    };

    public void setAdapter(ACBaseAdapter adapter) {
        setLayoutLoadMore(adapter);
        setColorLoadMore(mAdapter);
        recyclerView.setAdapter(adapter);
        showRecycler();
    }

    public void setAdapter(ACExpandBaseAdapter adapter) {
        recyclerView.setAdapter(adapter);
        showRecycler();
    }

    public void setAdapter(Class viewHolderParentClass, Class viewHolderChildClass, List<? extends ACBaseGroupModel> data) {
        recyclerView.setAdapter(new ACExpandBaseAdapter(data) {
            @Override
            public ACParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
                return createConstructorViewHolder(viewHolderParentClass, parent, viewType);
            }

            @Override
            public ACChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
                return createConstructorViewHolder(viewHolderChildClass, parent, viewType);
            }

            @Override
            public void onBindChildViewHolder(ACChildViewHolder holder, int flatPosition, ACBaseGroupModel group, int childIndex) {

                holder.bindData(group.getItems().get(childIndex));
            }

            @Override
            public void onBindGroupViewHolder(ACParentViewHolder holder, int flatPosition, ACBaseGroupModel group) {
                holder.bindData(group);

            }
        });
        showRecycler();
    }

    public void setAdapter(Class viewHolderClass, List<?> data) {
        recyclerView.setAdapter(new ACBaseAdapter(data) {
            @Override
            public ACBaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
                return createConstructorViewHolder(viewHolderClass, parent, viewType);
            }

        });
        mAdapter = (ACBaseAdapter) recyclerView.getAdapter();
        setLayoutLoadMore(mAdapter);
        setColorLoadMore(mAdapter);
    }


    public void setAdapter(@LayoutRes int res, List<String> data) {
        recyclerView.setAdapter(new ACBaseAdapter(data) {
            @Override
            public ACBaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
                ACTextViewHolder viewHolder = new ACTextViewHolder(parent, res, R.id.acTvTitle);
                return viewHolder;
            }

        });
        mAdapter = (ACBaseAdapter) recyclerView.getAdapter();
        setLayoutLoadMore(mAdapter);
        setColorLoadMore(mAdapter);
    }

    public void setAdapter(@LayoutRes int res, int[] idTextView, List<? extends ACBaseTextModel> data) {
        recyclerView.setAdapter(new ACBaseAdapter(data) {
            @Override
            public ACBaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
                ACTextViewHolder viewHolder = new ACTextViewHolder(parent, res, idTextView);
                return viewHolder;
            }

        });
        mAdapter = (ACBaseAdapter) recyclerView.getAdapter();
        setLayoutLoadMore(mAdapter);
        setColorLoadMore(mAdapter);
    }

    public void setAdapter(@LayoutRes int res, int[] idTextView, int[] idImageView, List<? extends ACBaseTextImageModel> data) {
        recyclerView.setAdapter(new ACBaseAdapter(data) {
            @Override
            public ACBaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
                ACTextImageViewHolder viewHolder = new ACTextImageViewHolder(parent, res, idTextView, idImageView);
                return viewHolder;
            }

        });
        mAdapter = (ACBaseAdapter) recyclerView.getAdapter();
        setLayoutLoadMore(mAdapter);
        setColorLoadMore(mAdapter);
    }

    private <T> T createConstructorViewHolder(Class viewHolderClass, ViewGroup parent, int viewType) {
        try {
            return (T) viewHolderClass.getConstructors()[0].newInstance(parent, viewType);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void showEmpty() {
        hideAll();
        if (frmEmptyView.getChildCount() > 0) {
            frmEmptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void clear() {
        recyclerView.setAdapter(null);
    }

    public void handleDataResponse(int maxPage) {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter instanceof ACBaseAdapter) {
                ((ACBaseAdapter) adapter).getData().clear();
                adapter.notifyDataSetChanged();
            }
        }
        if (mAdapter.getLoading() && mAdapter.getData().get(mAdapter.getData().size() - 1) == null) {
            mAdapter.getData().remove(mAdapter.getData().size() - 1);
            mAdapter.setLoading(false);
            mAdapter.notifyItemRemoved(mAdapter.getData().size());
        }
        setMaxPage(maxPage);
    }


    public ACBaseAdapter getAdapter() {
        return mAdapter;
    }

    public void showRecycler() {
        hideAll();
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void hideAll() {
        frmEmptyView.setVisibility(View.GONE);
        swipeRefresh.setRefreshing(false);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public int getPage() {
        return mPage;
    }

    public void setPage(int page) {
        this.mPage = page;
    }

    public int getMaxPage() {
        return mMaxPage;
    }

    public void setMaxPage(int maxPage) {
        this.mMaxPage = maxPage;
    }

    public void setThresholdLoadMore(int threshold) {
        this.mThresholdLoadMore = threshold;
    }

    abstract static public class ACBaseViewHolder<T> extends RecyclerView.ViewHolder {
        public ACBaseViewHolder(ViewGroup parent, @LayoutRes int res) {
            super(LayoutInflater.from(parent.getContext()).inflate(res, parent, false));
        }

        public ACBaseViewHolder(View view) {
            super(view);
        }

        abstract public void bindData(T data);

        abstract public void onCreatedView(View view);

    }

    abstract static public class ACChildViewHolder<T> extends RecyclerView.ViewHolder {
        public ACChildViewHolder(ViewGroup parent, @LayoutRes int res) {
            super(LayoutInflater.from(parent.getContext()).inflate(res, parent, false));
        }

        public ACChildViewHolder(View itemView) {
            super(itemView);
        }

        abstract public void bindData(T data);
    }

    abstract static public class ACParentViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ACRecyclerView.OnGroupClickListener listener;
        private View view;

        public ACParentViewHolder(ViewGroup parent, @LayoutRes int res) {
            super(LayoutInflater.from(parent.getContext()).inflate(res, parent, false));

        }

        public ACParentViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (view == null)
                view = getViewRotate();
            if (listener != null) {
                if (listener.onGroupClick(getAdapterPosition())) {
                    collapse();
                    rotateView(false);
                } else {
                    expand();
                    rotateView(true);
                }
            }
        }

        private void rotateView(boolean expand) {
            if (view == null)
                return;
            RotateAnimation rotate =
                    new RotateAnimation(expand ? 360 : 180, expand ? 180 : 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            view.startAnimation(rotate);
        }

        public void setOnGroupClickListener(ACRecyclerView.OnGroupClickListener listener) {
            this.listener = listener;
        }

        public void handleItemListener(View view) {
            view.setOnClickListener(this);
        }

        public void expand() {
        }

        public void collapse() {
        }

        abstract public void bindData(T data);

        abstract public View getViewRotate();

    }

    public void notifyDataSetChanged() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void notifyItemChanged(int position) {
        recyclerView.getAdapter().notifyItemChanged(position);
    }

    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        recyclerView.getAdapter().notifyItemRangeChanged(positionStart, itemCount);
    }

    public void notifyItemInserted(int position) {
        recyclerView.getAdapter().notifyItemInserted(position);
    }

    public void notifyItemRangeInserted(int positionStart, int itemCount) {
        recyclerView.getAdapter().notifyItemRangeInserted(positionStart, itemCount);
    }

    public void notifyItemRemoved(int position) {
        recyclerView.getAdapter().notifyItemRemoved(position);
    }

    public void notifyItemRangeRemoved(int positionStart, int itemCount) {
        recyclerView.getAdapter().notifyItemRangeRemoved(positionStart, itemCount);
    }

}
