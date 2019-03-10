package com.example.zfrproject.fragmentUser;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zfrproject.R;
import com.example.zfrproject.cardViewSupport.Introduction;
import com.example.zfrproject.cardViewSupport.IntroductionAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//改为从服务器获取数据
public class NewFragment extends BaseFragment implements View.OnClickListener {

    private Introduction[] introductions = {new Introduction("海口站开业", R.drawable.haikou), new Introduction("东方站开业", R.drawable.dongfang),
            new Introduction("澄迈站开业", R.drawable.chengmai), new Introduction("五指山站开业", R.drawable.wuzhishan),
            new Introduction("琼海站开业", R.drawable.qionghai), new Introduction("三沙站开业", R.drawable.sansha),
            new Introduction("三亚站开业", R.drawable.sanya), new Introduction("儋州站开业", R.drawable.danzhou),
            new Introduction("文昌站开业", R.drawable.wenchang), new Introduction("万宁站开业", R.drawable.wanning)};
    private List<Introduction> introductionList = new ArrayList<>();
    private IntroductionAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mContent = getContext();
        View view=inflater.inflate(R.layout.activity_introduction,container,false);
        initChairs();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(mContent, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new IntroductionAdapter(introductionList);
        recyclerView.setAdapter(adapter);
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshChairs();
            }
        });

        return view;
    }

    private void refreshChairs() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initChairs();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void initChairs() {
        introductionList.clear();
        for (int i = 0; i < 50; i++) {
            Random random = new Random();
            int index = random.nextInt(introductions.length);
            introductionList.add(introductions[index]);
        }
    }

    @Override
    protected void loadData() {
        //Toast.makeText(mContent,"Fragment头条加载数据",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected View initView() {
        TextView mView = new TextView(mContent);
//        mView.setText("Fragment头条");
//        mView.setGravity(Gravity.CENTER);
//        mView.setTextSize(18);
//        mView.setTextColor(Color.BLACK);
        return mView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initFruits();


    }
    @Override
    public void onClick(View v){

    }
}
