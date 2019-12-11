package com.intkilow.photopicker.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.intkilow.photopicker.R;
import com.intkilow.photopicker.adapter.PhotoAdapter;
import com.intkilow.photopicker.datasource.PhotoLoadTask;
import com.intkilow.photopicker.entity.PhotoWrapperEntity;
import com.intkilow.photopicker.interfaces.Callback;
import com.intkilow.photopicker.utils.SpaceItemDecoration;

public class PhotoPickerActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);
        mRecyclerView = findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration());

        PhotoLoadTask photoLoadTask = new PhotoLoadTask(this);
        photoLoadTask.setCallback(new Callback<PhotoWrapperEntity>() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(PhotoWrapperEntity result) {
                Log.e("TAG", "");

                mRecyclerView.setAdapter(new PhotoAdapter(result.getAllPic()));

            }

            @Override
            public void onTaskCancelled() {

            }
        });
        photoLoadTask.execute();
    }
}
