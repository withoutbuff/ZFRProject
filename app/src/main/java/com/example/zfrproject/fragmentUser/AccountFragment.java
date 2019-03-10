package com.example.zfrproject.fragmentUser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.zfrproject.R;
import com.example.zfrproject.login.LoginActivity;

public class AccountFragment extends BaseFragment{
    private SharedPreferences.Editor editor;
    private Button exitButton;
    private SharedPreferences pref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mContent = getContext();
        View view=inflater.inflate(R.layout.fragment_account,container,false);
        exitButton=view.findViewById(R.id.exit);


        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref = PreferenceManager.getDefaultSharedPreferences(mContent);
                editor=pref.edit();
                editor.clear();
                editor.apply();
                Intent intent=new Intent(mContent,LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }
    @Override
    protected void loadData() {
        //Toast.makeText(mContent,"Fragment头条加载数据",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected View initView() {
        TextView mView = new TextView(mContent);
        return mView;
    }
}
