package com.intkilow.photopicker.utils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;


public class ObjectUtils {

    /**
     * 转list
     *
     * @param list
     * @return
     */
    public static List<File> stringToFiles(List<String> list) {

        List<File> files = new LinkedList<>();
        for (String s : list) {
            files.add(new File(s));
        }
        return files;
    }

    /**
     * 判断数组是否是空
     *
     * @param list
     * @return true空
     */
    public static boolean isEmpty(List list) {
        return null == list || list.size() <= 0;
    }
}
