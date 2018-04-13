package com.example.zzh.channelmanagedemo_kotlin;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ChannelAdapter.onItemRangeChangeListener {

    private RecyclerView mRecyclerView;
    private List<ChannelBean> mList;
    private ChannelAdapter mAdapter;
    private String select[] = {"要闻", "体育", "新时代", "汽车", "时尚", "国际", "电影", "财经", "游戏", "科技", "房产", "政务", "图片", "独家"};
    private String recommend[] = {"娱乐", "军事", "文化", "视频", "股票", "动漫", "理财", "电竞", "数码", "星座", "教育", "美容", "旅游"};
    private String city[] = {"重庆", "深圳", "汕头", "东莞", "佛山", "江门", "湛江", "惠州", "中山", "揭阳", "韶关", "茂名", "肇庆", "梅州", "汕尾", "河源", "云浮", "四川"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerView);

        //列表中所有的数据集合
        mList = new ArrayList<>();

        //创建布局管理器
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int spanSize = mList.get(position).getSpanSize();
                Log.d("Indicator---", "创建适配器后  " + spanSize);
                return spanSize;
            }
        });
        mRecyclerView.setLayoutManager(manager);

        DefaultItemAnimator animator = new DefaultItemAnimator();
        //设置动画时间
        animator.setMoveDuration(300);
        animator.setRemoveDuration(0);
        //设置Item动画
        mRecyclerView.setItemAnimator(animator);

        //添加Title
        ChannelBean title = new ChannelBean();
        title.setLayoutId(R.layout.adapter_title);
        title.setSpanSize(4);
        mList.add(title);

        //添加默认选中的
        for (String bean : select) {
            mList.add(new ChannelBean(bean, 1, R.layout.adapter_channel, true));
        }

        //添加Table
        ChannelBean tabBean = new ChannelBean();
        tabBean.setLayoutId(R.layout.adapter_tab);
        tabBean.setSpanSize(4);
        mList.add(tabBean);

        //添加推荐频道
        List<ChannelBean> recommendList = new ArrayList<>();
        for (String bean : recommend) {
            recommendList.add(new ChannelBean(bean, 1, R.layout.adapter_channel, true));
        }
        mList.addAll(recommendList);

        //添加地方新闻
        List<ChannelBean> cityList = new ArrayList<>();
        for (String bean : city) {
            cityList.add(new ChannelBean(bean, 1, R.layout.adapter_channel, false));
        }

        //将更多频道  添加   cityList的集合中
        ChannelBean moreBean = new ChannelBean();
        moreBean.setLayoutId(R.layout.adapter_more_channel);
        moreBean.setSpanSize(4);
        cityList.add(moreBean);

        //创建是适配器
        mAdapter = new ChannelAdapter(this, mList, recommendList, cityList);
        mAdapter.setFixSize(1);
        mAdapter.setSelectedSize(select.length);
        mAdapter.setRecommend(true);
        mAdapter.setOnItemRangeChangeListener(this);
        mRecyclerView.setAdapter(mAdapter);

        //添加分割线
        WindowManager m = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (m != null) {
            m.getDefaultDisplay().getMetrics(displayMetrics);
            int spacing = (displayMetrics.widthPixels - dip2px(this, 70) * 4) / 5;
            mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4, spacing, true));
        }

        //设置拖拽回调
        ItemDragCallback callback = new ItemDragCallback(mAdapter, 2);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void refreshItemDecoration() {
        //如果设置了itemDecoration，必须调用recyclerView.invalidateItemDecorations(),否则间距会不对
        mRecyclerView.invalidateItemDecorations();
    }
}
