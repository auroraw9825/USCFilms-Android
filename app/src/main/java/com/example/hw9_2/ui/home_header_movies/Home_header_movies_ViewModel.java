package com.example.hw9_2.ui.home_header_movies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Home_header_movies_ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public Home_header_movies_ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}