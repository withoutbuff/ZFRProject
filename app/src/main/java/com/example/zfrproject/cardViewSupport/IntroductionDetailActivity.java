package com.example.zfrproject.cardViewSupport;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zfrproject.R;

public class IntroductionDetailActivity extends AppCompatActivity {
    public static final String INTRODUCTION_NAME = "introduction_name";

    public static final String INTRODUCTION_IMAGE_ID = "introduction_image_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction_detail);
        Intent intent = getIntent();
        String introductionName = intent.getStringExtra(INTRODUCTION_NAME);
        int introductionImageId = intent.getIntExtra(INTRODUCTION_IMAGE_ID, 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView introductionImageView = (ImageView) findViewById(R.id.introduction_image_view);
        TextView introductionContentText = (TextView) findViewById(R.id.introduction_content_text);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(introductionName);
        Glide.with(this).load(introductionImageId).into(introductionImageView);
        String introductionContent = generateIntroductionContent(introductionName);
        introductionContentText.setText(introductionContent);
    }

    private String generateIntroductionContent(String introductionName) {
        StringBuilder introductionContent = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            introductionContent.append(introductionName);
        }
        return introductionContent.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
