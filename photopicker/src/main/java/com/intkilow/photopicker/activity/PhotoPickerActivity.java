package com.intkilow.photopicker.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.intkilow.photopicker.R;
import com.intkilow.photopicker.adapter.PhotoAdapter;
import com.intkilow.photopicker.datasource.PhotoLoadTask;
import com.intkilow.photopicker.entity.PhotoEntity;
import com.intkilow.photopicker.entity.PhotoWrapperEntity;
import com.intkilow.photopicker.interfaces.Callback;
import com.intkilow.photopicker.utils.DisplayUtil;
import com.intkilow.photopicker.utils.SpaceItemDecoration;
import com.intkilow.photopicker.utils.StatusBarUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PhotoPickerActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    PhotoAdapter photoAdapter;
    private Button mComplete;
    private TextView mPreview;
    private int mMaxLen = 9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);
        StatusBarUtil.setColor(this, Color.parseColor("#4E4D4B"), 0);
        mRecyclerView = findViewById(R.id.recycler_view);
        mComplete = findViewById(R.id.complete);
        mPreview = findViewById(R.id.preview);
        mPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoAdapter.getMap().size() > 0) {
                    for (Map.Entry<Integer, PhotoEntity> integerPhotoEntityEntry : photoAdapter.getMap().entrySet()) {
                        intent(integerPhotoEntityEntry.getKey());
                        break;
                    }
                }

            }
        });
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
            public void onPostExecute(final PhotoWrapperEntity result) {
                photoAdapter = new PhotoAdapter(result.getAllPic());
                photoAdapter.setMaxLen(mMaxLen);
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

                        intent(position);
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


    public void intent(int position) {
        LinkedList<PhotoEntity> allPic = photoAdapter.getList();
        Intent intent = new Intent(PhotoPickerActivity.this, PhotoPreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", allPic);
        LinkedList<PhotoEntity> selectPic = new LinkedList<>();
        for (Map.Entry<Integer, PhotoEntity> integerPhotoEntityEntry : photoAdapter.getMap().entrySet()) {
            Integer key = integerPhotoEntityEntry.getKey();
            PhotoEntity value = integerPhotoEntityEntry.getValue();
            value.setPosition(key);
            if (position == key) {
                value.setSelect(true);
            } else {
                value.setSelect(false);
            }
            selectPic.add(value);
        }

        bundle.putSerializable("selectData", selectPic);
        intent.putExtra("bundle", bundle);
        intent.putExtra("position", position);
        intent.putExtra("maxLen", mMaxLen);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && null != data) {
            Bundle bundle = data.getBundleExtra("bundle");
            List<PhotoEntity> selectData = (List<PhotoEntity>) bundle.getSerializable("selectData");
            HashMap<Integer, PhotoEntity> map = new LinkedHashMap<>();
            for (PhotoEntity selectDatum : selectData) {
                map.put((int) selectDatum.getPosition(), selectDatum);
            }
            photoAdapter.setMap(map);
            photoAdapter.notifyDataSetChanged();

        }


    }
}
