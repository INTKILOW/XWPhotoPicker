package com.intkilow.photopicker.entity;


import java.io.Serializable;

public class PhotoEntity implements Serializable {
    private boolean isSelect = false;
    private boolean iGif = false;
    private boolean isVideo = false;
    private int count = 1;
    private boolean canSelect = true;


    private String filePath;
    private long size;
    private String mimeType;
    private String folder;

    private long position;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        if (mimeType.equals("image/gif")) {
            setiGif(true);
        }
        this.mimeType = mimeType;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }


    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isiGif() {
        return iGif;
    }

    public void setiGif(boolean iGif) {
        this.iGif = iGif;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public boolean isCanSelect() {
        return canSelect;
    }

    public void setCanSelect(boolean canSelect) {
        this.canSelect = canSelect;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }
}
