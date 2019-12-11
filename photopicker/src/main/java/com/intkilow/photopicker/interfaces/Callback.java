package com.intkilow.photopicker.interfaces;

public interface Callback<T> {

    void onPreExecute();


    void onPostExecute(T result);


    void onTaskCancelled();
}