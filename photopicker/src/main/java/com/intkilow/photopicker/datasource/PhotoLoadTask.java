package com.intkilow.photopicker.datasource;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.intkilow.photopicker.entity.PhotoEntity;
import com.intkilow.photopicker.entity.PhotoWrapperEntity;
import com.intkilow.photopicker.interfaces.Callback;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;

public class PhotoLoadTask extends AsyncTask<Void, Void, PhotoWrapperEntity> {


    private WeakReference<Context> mContextReference;
    private HashMap<String, LinkedList<PhotoEntity>> mHashMapPic = new HashMap<>();
    private LinkedList<PhotoEntity> mAllPic = new LinkedList<>();

    private Callback<PhotoWrapperEntity> mCallback;

    public PhotoLoadTask(Context context) {
        mContextReference = new WeakReference<>(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (null != mCallback) {
            mCallback.onPreExecute();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (null != mCallback) {
            mCallback.onTaskCancelled();
        }
    }

    @Override
    protected void onPostExecute(PhotoWrapperEntity photoWrapperEntity) {
        super.onPostExecute(photoWrapperEntity);
        if (null != mCallback) {
            mCallback.onPostExecute(photoWrapperEntity);
        }
    }

    @Override
    protected PhotoWrapperEntity doInBackground(Void... voids) {
        PhotoWrapperEntity photoWrapperEntity = new PhotoWrapperEntity();
        Context context = mContextReference.get();
        if (null == context) {
            return photoWrapperEntity;
        }
        try (Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.SIZE, MediaStore.Images.Media.MIME_TYPE},
                MediaStore.Images.Media.MIME_TYPE + "=? or " +
                        MediaStore.Images.Media.MIME_TYPE + "=? or " +
                        MediaStore.Images.Media.MIME_TYPE + "=? or " +
                        MediaStore.Images.Media.MIME_TYPE + "=? ",
                new String[]{"image/jpeg", "image/png", "image/jpg", "image/gif"},
                MediaStore.Images.Media.DATE_ADDED + " DESC"
        )) {
            if (cursor == null || cursor.getCount() <= 0) {
                //没有图片
                return photoWrapperEntity;
            } else {
                int id = 0;
                while (cursor.moveToNext()) {

                    String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                    String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
                    String folder = "";

                    File file = new File(imagePath);
                    String parent = file.getParent();
                    if (!TextUtils.isEmpty(parent)) {

                        int index = parent.lastIndexOf(File.separator);
                        if (-1 != index) {
                            folder = parent.substring(index);
                            folder = folder.replace(File.separator, "");
                        }
                    }
                    PhotoEntity photoEntity = new PhotoEntity();
                    photoEntity.setFilePath(imagePath);
                    photoEntity.setSize(size);
                    photoEntity.setFolder(folder);
                    photoEntity.setMimeType(mimeType);
                    photoEntity.setId(id);
                    if (mHashMapPic.containsKey(folder)) {
                        LinkedList<PhotoEntity> photoEntities = mHashMapPic.get(folder);
                        if (null != photoEntities) {
                            photoEntities.add(photoEntity);
                        } else {
                            photoEntities = new LinkedList<>();
                            photoEntities.add(photoEntity);
                        }

                    } else {
                        LinkedList<PhotoEntity> photoEntities = new LinkedList<>();
                        photoEntities.add(photoEntity);
                        mHashMapPic.put(folder, photoEntities);
                    }

                    mAllPic.add(photoEntity);
                    id++;

                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        photoWrapperEntity.setAllPic(mAllPic);
        photoWrapperEntity.setHashMapPic(mHashMapPic);
        return photoWrapperEntity;
    }


    public void setCallback(Callback<PhotoWrapperEntity> mCallback) {
        this.mCallback = mCallback;
    }
}
