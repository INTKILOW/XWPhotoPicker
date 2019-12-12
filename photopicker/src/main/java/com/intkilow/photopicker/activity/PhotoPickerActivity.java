package com.intkilow.photopicker.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.intkilow.photopicker.R;
import com.intkilow.photopicker.adapter.PhotoAdapter;
import com.intkilow.photopicker.datasource.PhotoLoadTask;
import com.intkilow.photopicker.entity.PhotoWrapperEntity;
import com.intkilow.photopicker.interfaces.Callback;
import com.intkilow.photopicker.utils.DisplayUtil;
import com.intkilow.photopicker.utils.SpaceItemDecoration;

public class PhotoPickerActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    PhotoAdapter photoAdapter;
    private Button mComplete;
    private TextView mPreview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);
        mRecyclerView = findViewById(R.id.recycler_view);
        mComplete = findViewById(R.id.complete);
        mPreview = findViewById(R.id.preview);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mComplete.getLayoutParams();
        layoutParams.width = DisplayUtil.dpToPx(62);
        mComplete.setLayoutParams(layoutParams);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration());
        mRecyclerView.setHasFixedSize(true);


        PhotoLoadTask photoLoadTask = new PhotoLoadTask(this);
        photoLoadTask.setCallback(new Callback<PhotoWrapperEntity>() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(PhotoWrapperEntity result) {
                Log.e("TAG", "");
                photoAdapter = new PhotoAdapter(result.getAllPic());
                photoAdapter.setItemClick(new PhotoAdapter.ItemClick() {
                    @Override
                    public void onSelect(int count, boolean enable) {
                        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mComplete.getLayoutParams();
                        if (count > 0) {
                            String tip = String.format(getResources().getString(R.string.complete), String.valueOf(count));
                            mComplete.setText(tip);
                            mComplete.setTextColor(Color.WHITE);
                            mComplete.setBackgroundResource(R.drawable.complete_enable);
                            mPreview.setTextColor(Color.WHITE);
                            layoutParams.width = DisplayUtil.dpToPx(82);

                        } else {
                            mPreview.setTextColor(Color.parseColor("#7E7E7E"));
                            mComplete.setText(R.string.complete_name);
                            mComplete.setTextColor(Color.parseColor("#6d6d6d"));
                            mComplete.setBackgroundResource(R.drawable.complete_disable);
                            layoutParams.width = DisplayUtil.dpToPx(62);
                        }
                        mComplete.setLayoutParams(layoutParams);


                    }

                    @Override
                    public void onImageClick(int position) {

                    }
                });
                mRecyclerView.setAdapter(photoAdapter);

            }

            @Override
            public void onTaskCancelled() {

            }
        });
        photoLoadTask.execute();
    }
}
