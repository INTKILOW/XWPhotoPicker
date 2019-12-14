package com.intkilow.xwphotopicker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.intkilow.photopicker.activity.PhotoPickerActivity;
import com.intkilow.photopicker.interfaces.ChooseImageDelegate;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    A a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        a = new A(new LinkedList<String>());
        recyclerView.setAdapter(a);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PhotoPickerActivity.class));
                PhotoPickerActivity.setChooseImageDelegate(new ChooseImageDelegate() {
                    @Override
                    public void chooseResult(List<String> photo) {
                        Log.e("TAG", "size" + photo.size());
                        a.setNewData(photo);
                    }
                });
            }
        });


    }

    class A extends BaseQuickAdapter<String, BaseViewHolder> {

        public A(@Nullable List<String> data) {
            super(R.layout.photo_items, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            ImageView image = helper.getView(R.id.image);
            Glide.with(mContext)
                    .asBitmap()
                    .load(item)
                    .dontAnimate()
                    .into(image);

        }
    }
}
