package com.meiji.toutiao.module.media.wip.tab;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.meiji.toutiao.Register;
import com.meiji.toutiao.adapter.DiffCallback;
import com.meiji.toutiao.bean.FooterBean;
import com.meiji.toutiao.module.base.BaseListFragment;
import com.meiji.toutiao.utils.OnLoadMoreListener;

import java.util.List;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

import static com.meiji.toutiao.module.media.wip.tab.MediaTabPresenter.TYPE_VIDEO;

/**
 * Created by Meiji on 2017/6/29.
 */

public class MediaVideoFragment extends BaseListFragment<IMediaProfile.Presenter> implements IMediaProfile.View {

    private static final String TAG = "MediaVideoFragment";
    private String mediaId;

    public static MediaVideoFragment newInstance(String mediaId) {
        Bundle args = new Bundle();
        args.putString(TAG, mediaId);
        MediaVideoFragment fragment = new MediaVideoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setPresenter(IMediaProfile.Presenter presenter) {
        if (null == presenter) {
            this.presenter = new MediaTabPresenter(this);
        }
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        adapter = new MultiTypeAdapter(oldItems);
        Register.registerMediaArticleItem(adapter);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (canLoadMore) {
                    canLoadMore = false;
                    presenter.doLoadMoreData(TYPE_VIDEO);
                }
            }
        });
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mediaId = bundle.getString(TAG);
        if (TextUtils.isEmpty(mediaId)) {
            onShowNetError();
        }
    }

    @Override
    public void onLoadData() {
        onShowLoading();
        presenter.doLoadArticle(mediaId);
    }

    @Override
    public void onRefresh() {
        presenter.doRefresh();
    }

    @Override
    public void onSetAdapter(List<?> list) {
        Items newItems = new Items(list);
        newItems.add(new FooterBean());
        DiffCallback.notifyDataSetChanged(oldItems, newItems, DiffCallback.MUlTI_MEDIA, adapter);
        oldItems.clear();
        oldItems.addAll(newItems);
        canLoadMore = true;
    }

    @Override
    public void fetchData() {
        onLoadData();
    }
}
