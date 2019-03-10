package com.example.zfrproject.cardViewSupport;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zfrproject.R;

import java.util.List;

public class IntroductionAdapter extends RecyclerView.Adapter<IntroductionAdapter.ViewHolder>{

    private static final String TAG = "IntroductionAdapter";

    private Context mContext;

    private List<Introduction> mIntroductionList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView introductionImage;
        TextView introductionName;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            introductionImage = (ImageView) view.findViewById(R.id.introduction_image);
            introductionName = (TextView) view.findViewById(R.id.introduction_name);
        }
    }

    public IntroductionAdapter(List<Introduction> introductionList) {
        mIntroductionList = introductionList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.introduction_detail_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Introduction introduction = mIntroductionList.get(position);
                Intent intent = new Intent(mContext, IntroductionDetailActivity.class);
                intent.putExtra(IntroductionDetailActivity.INTRODUCTION_NAME, introduction.getName());
                intent.putExtra(IntroductionDetailActivity.INTRODUCTION_IMAGE_ID, introduction.getImageId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Introduction introduction = mIntroductionList.get(position);
        holder.introductionName.setText(introduction.getName());
        Glide.with(mContext).load(introduction.getImageId()).into(holder.introductionImage);
    }

    @Override
    public int getItemCount() {
        return mIntroductionList.size();
    }

}
