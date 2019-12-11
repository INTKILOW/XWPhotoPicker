package com.intkilow.photopicker.entity;

import java.util.HashMap;
import java.util.LinkedList;


public class PhotoWrapperEntity {
    private HashMap<String, LinkedList<PhotoEntity>> hashMapPic;
    private LinkedList<PhotoEntity> allPic;

    public HashMap<String, LinkedList<PhotoEntity>> getHashMapPic() {
        return hashMapPic;
    }

    public void setHashMapPic(HashMap<String, LinkedList<PhotoEntity>> hashMapPic) {
        this.hashMapPic = hashMapPic;
    }

    public LinkedList<PhotoEntity> getAllPic() {
        return allPic;
    }

    public void setAllPic(LinkedList<PhotoEntity> allPic) {
        this.allPic = allPic;
    }
}
