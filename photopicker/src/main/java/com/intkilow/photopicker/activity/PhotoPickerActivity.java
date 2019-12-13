package com.intkilow.photopicker.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.intkilow.photopicker.R;
import com.intkilow.photopicker.adapter.FolderPreviewAdapter;
import com.intkilow.photopicker.adapter.PhotoAdapter;
import com.intkilow.photopicker.datasource.PhotoLoadTask;
import com.intkilow.photopicker.entity.FolderEntity;
import com.intkilow.photopicker.entity.PhotoEntity;
import com.intkilow.photopicker.entity.PhotoWrapperEntity;
import com.intkilow.photopicker.interfaces.Callback;
import com.intkilow.photopicker.utils.DisplayUtil;
import com.intkilow.photopicker.utils.ObjectUtils;
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
    private TextView mPreview, mTitle;
    private int mMaxLen = 9;
    PhotoWrapperEntity mResult;
    private ConstraintLayout banner;
    private LinearLayout chooseFolder;
    private PopupWindow popupWindow;

    private ImageView mDropImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);
        StatusBarUtil.setColor(this, Color.parseColor("#333333"), 0);
        mRecyclerView = findViewById(R.id.recycler_view);
        mComplete = findViewById(R.id.complete);
        chooseFolder = findViewById(R.id.choose_folder);
        banner = findViewById(R.id.banner);
        mPreview = findViewById(R.id.preview);
        mTitle = findViewById(R.id.title);
        mDropImageView = findViewById(R.id.drop_image_view);
        mPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoAdapter.getMap().size() > 0) {
                    for (Map.Entry<Integer, PhotoEntity> integerPhotoEntityEntry : photoAdapter.getMap().entrySet()) {
                        intent(-1);
                        break;
                    }
                }

            }
        });
        chooseFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopWindow();
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                mResult = result;
                photoAdapter = new PhotoAdapter(result.getAllPic());
                photoAdapter.setMaxLen(mMaxLen);
                photoAdapter.setItemClick(new PhotoAdapter.ItemClick() {
                    @Override
                    public void onSelect(int count, boolean enable) {

                        update(count);

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
        LinkedList<PhotoEntity> picList = photoAdapter.getList();
        Intent intent = new Intent(PhotoPickerActivity.this, PhotoPreviewActivity.class);

        LinkedList<PhotoEntity> selectPic = new LinkedList<>();

        int p = 0;
        for (Map.Entry<Integer, PhotoEntity> integerPhotoEntityEntry : photoAdapter.getMap().entrySet()) {
            Integer id = integerPhotoEntityEntry.getKey();
            PhotoEntity value = integerPhotoEntityEntry.getValue();
            value.setId(id);

            if (position >= 0) {
                if (picList.get(position).getId() == id) {
                    value.setSelect(true);
                } else {
                    value.setSelect(false);
                }
            } else {
                value.setSelect(p == 0);
            }
            p++;
            selectPic.add(value);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", position != -1 ? picList : selectPic);
        bundle.putSerializable("selectData", selectPic);
        intent.putExtra("bundle", bundle);
        intent.putExtra("position", position != -1 ? position : 0);
        intent.putExtra("maxLen", mMaxLen);
        startActivityForResult(intent, 100);
    }

    private void update(int count) {

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && null != data) {
            Bundle bundle = data.getBundleExtra("bundle");
            List<PhotoEntity> selectData = (List<PhotoEntity>) bundle.getSerializable("selectData");
            HashMap<Integer, PhotoEntity> map = new LinkedHashMap<>();
            for (PhotoEntity selectDatum : selectData) {
                map.put(selectDatum.getId(), selectDatum);
            }
            photoAdapter.setMap(map);
            photoAdapter.notifyDataSetChanged();
            update(map.size());

        }
    }


    private void initPopWindow() {

        if (null == popupWindow) {


            popupWindow = new PopupWindow(this);
            View inflate = LayoutInflater.from(this).inflate(R.layout.folder_choose, null);
            RecyclerView viewById = inflate.findViewById(R.id.recyclerView);
            List<FolderEntity> list = new LinkedList<>();

            HashMap<String, LinkedList<PhotoEntity>> hashMapPic = mResult.getHashMapPic();


            FolderEntity folderEntity = new FolderEntity();
            folderEntity.setTitle("所有图片");
            folderEntity.setCount(mResult.getAllPic().size());
            folderEntity.setSelect(true);
            folderEntity.setCover(mResult.getAllPic().get(0).getFilePath());
            list.add(folderEntity);


            for (Map.Entry<String, LinkedList<PhotoEntity>> stringLinkedListEntry : hashMapPic.entrySet()) {
                LinkedList<PhotoEntity> value = stringLinkedListEntry.getValue();
                String cover = "";
                if (!ObjectUtils.isEmpty(value)) {
                    cover = value.get(0).getFilePath();
                }
                folderEntity = new FolderEntity();
                folderEntity.setTitle(stringLinkedListEntry.getKey());
                folderEntity.setCount(value.size());
                folderEntity.setSelect(false);
                folderEntity.setCover(cover);
                list.add(folderEntity);
            }
            final FolderPreviewAdapter folderPreviewAdapter = new FolderPreviewAdapter(list);
            folderPreviewAdapter.setItemClick(new FolderPreviewAdapter.ItemClick() {
                @Override
                public void onItemClick(int position) {
                    popupWindow.dismiss();
                    FolderEntity f = folderPreviewAdapter.getList().get(position);

                    mTitle.setText(f.getTitle());
                    LinkedList<PhotoEntity> photoEntities = mResult.getHashMapPic().get(f.getTitle());
                    if (ObjectUtils.isEmpty(photoEntities)) {
                        photoEntities = mResult.getAllPic();
                    }
                    photoAdapter.setList(photoEntities);
                    photoAdapter.notifyDataSetChanged();
                    folderPreviewAdapter.updateSelect(position);
                }
            });
            viewById.setAdapter(folderPreviewAdapter);
            popupWindow.setContentView(inflate);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setAnimationStyle(R.style.popmenu_animation);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());// 这样设置才能铺满屏幕，去掉这句话会出现缝隙
            popupWindow.setFocusable(true);
        }

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                Animation rotateAnimation = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setFillAfter(true);
                rotateAnimation.setDuration(300);
                rotateAnimation.setRepeatCount(0);
                rotateAnimation.setInterpolator(new LinearInterpolator());
                mDropImageView.startAnimation(rotateAnimation);
            }
        });

        if (popupWindow.isShowing()) {
            popupWindow.dismiss();

        } else {
            Animation rotateAnimation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setFillAfter(true);
            rotateAnimation.setDuration(300);
            rotateAnimation.setRepeatCount(0);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            mDropImageView.startAnimation(rotateAnimation);
            popupWindow.showAsDropDown(banner);
        }


    }


}
