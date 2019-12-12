package com.intkilow.photopicker.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.intkilow.photopicker.R;
import com.intkilow.photopicker.adapter.PhotoPreviewItemAdapter;
import com.intkilow.photopicker.entity.PhotoEntity;
import com.intkilow.photopicker.utils.DisplayUtil;
import com.intkilow.photopicker.utils.StatusBarUtil;
import com.intkilow.photopicker.view.CountView;

import java.util.List;

public class PhotoPreviewActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;

    private PhotoPreviewItemAdapter mPhotoPreviewItemAdapter;

    private TextView mPreviewText;

    private int mAllSize = 0;

    private CountView countView;
    private ViewPager viewPager;

    private Button mComplete;

    private SamplePagerAdapter mSamplePagerAdapter;

    private int mMaxSelect = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_preview);

        StatusBarUtil.setColor(this, Color.parseColor("#4E4D4B"), 0);
        mRecyclerView = findViewById(R.id.recyclerView);
        mPreviewText = findViewById(R.id.previewText);
        viewPager = findViewById(R.id.view_pager);
        mComplete = findViewById(R.id.complete);
        countView = findViewById(R.id.countView);
        countView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isSelect = countView.isIsSelect();
                List<PhotoEntity> list = mPhotoPreviewItemAdapter.getList();
                if (isSelect) {
                    //当前选中 取消选择当前

                    int index = countView.getCount() - 1;
                    list.remove(index);
                    mPhotoPreviewItemAdapter.notifyItemRemoved(index);
                    countView.setSelect(false, 0, true);
                } else {
                    if (mMaxSelect > list.size()) {
                        PhotoEntity photoEntity = mSamplePagerAdapter.getAllPic().get(viewPager.getCurrentItem());
                        photoEntity.setPosition(viewPager.getCurrentItem());
                        photoEntity.setSelect(true);
                        list.add(photoEntity);
                        mRecyclerView.smoothScrollToPosition(list.size());
                        mPhotoPreviewItemAdapter.notifyItemInserted(list.size());
                        countView.setSelect(true, list.size(), true);
                    }


                }

                setCompleteWH(mComplete, list.size());
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);


        if (null != getIntent()) {
            Bundle bundle = getIntent().getBundleExtra("bundle");
            int position = getIntent().getIntExtra("position", 0);
            List<PhotoEntity> allPic = (List<PhotoEntity>) bundle.getSerializable("data");
            List<PhotoEntity> selectData = (List<PhotoEntity>) bundle.getSerializable("selectData");

            setCompleteWH(mComplete, selectData.size());
            mAllSize = allPic.size();
            mPreviewText.setText((position + 1) + "/" + mAllSize);
            mPhotoPreviewItemAdapter = new PhotoPreviewItemAdapter(selectData);
            boolean select = false;
            int i = 1, p = 0;
            for (PhotoEntity selectDatum : selectData) {
                if (selectDatum.isSelect() && selectDatum.getPosition() == position) {
                    select = true;
                    p = i;
                    break;
                }
                i++;
            }
            countView.setSelect(select, i, false);
            mRecyclerView.smoothScrollToPosition(p);
            mPhotoPreviewItemAdapter.setItemClick(new PhotoPreviewItemAdapter.ItemClick() {
                @Override
                public void onItemClick(int position) {
                    viewPager.setCurrentItem((int) mPhotoPreviewItemAdapter.getList().get(position).getPosition(), false);
                }
            });
            mRecyclerView.setAdapter(mPhotoPreviewItemAdapter);
            mSamplePagerAdapter = new SamplePagerAdapter(allPic);
            viewPager.setAdapter(mSamplePagerAdapter);
            viewPager.setCurrentItem(position);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mPreviewText.setText((position + 1) + "/" + mAllSize);
                    List<PhotoEntity> list = mPhotoPreviewItemAdapter.getList();
                    int i = 0, p = -1;
                    boolean select = false;
                    for (PhotoEntity entity : list) {
                        if (entity.getPosition() == position) {
                            select = true;
                            entity.setSelect(true);
                            p = i;

                        } else {
                            entity.setSelect(false);
                        }
                        i++;
                    }
                    if (p >= 0) {
                        mRecyclerView.smoothScrollToPosition(p);
                    }
                    countView.setSelect(select, p + 1, true);
                    mPhotoPreviewItemAdapter.notifyDataSetChanged();
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }

    }

    private void setCompleteWH(Button button, int count) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) button.getLayoutParams();
        if (count > 0) {
            button.setBackgroundResource(R.drawable.complete_enable);
            layoutParams.width = DisplayUtil.dpToPx(82);
            String tip = String.format(getResources().getString(R.string.complete), String.valueOf(count));
            mComplete.setText(tip);
        } else {
            layoutParams.width = DisplayUtil.dpToPx(62);
            mComplete.setText(R.string.complete_name);
        }
        button.setLayoutParams(layoutParams);
    }

    class SamplePagerAdapter extends PagerAdapter {
        List<PhotoEntity> allPic;

        SamplePagerAdapter(List<PhotoEntity> allPic) {
            this.allPic = allPic;
        }

        public List<PhotoEntity> getAllPic() {
            return allPic;
        }

        public void setAllPic(List<PhotoEntity> allPic) {
            this.allPic = allPic;
        }

        @Override
        public int getCount() {
            return allPic.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());

            PhotoEntity photoEntity = allPic.get(position);

            Glide.with(PhotoPreviewActivity.this)
                    .asBitmap()
                    .load(photoEntity.getFilePath())
//                    .placeholder(new ColorDrawable(color))
//                    .error(new ColorDrawable(color))
                    .dontAnimate()
                    .into(photoView);
//            photoView.setImageResource(allPic[position]);
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
