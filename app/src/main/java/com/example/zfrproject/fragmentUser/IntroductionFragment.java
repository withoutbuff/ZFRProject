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


public class IntroductionFragment extends BaseFragment implements View.OnClickListener {

    private Introduction[] introductions = {new Introduction("按摩椅A1型", R.drawable.chair_a1), new Introduction("按摩椅A2型", R.drawable.chair_a2),
            new Introduction("按摩椅B1型", R.drawable.chair_b1), new Introduction("按摩椅B2型", R.drawable.chair_b2),
            new Introduction("按摩椅C1型", R.drawable.chair_c1), new Introduction("按摩椅C2型", R.drawable.chair_c2),
            new Introduction("按摩椅D1型", R.drawable.chair_d1), new Introduction("按摩椅D2型", R.drawable.chair_d2),
            new Introduction("按摩椅E1型", R.drawable.chair_e1), new Introduction("按摩椅E2型", R.drawable.chair_e2)};
    private List<Introduction> introductionList = new ArrayList<>();
    private IntroductionAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
//相当于activity中的onCreate
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
