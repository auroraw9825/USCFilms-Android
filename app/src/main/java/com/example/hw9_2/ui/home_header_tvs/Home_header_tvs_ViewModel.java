package com.example.hw9_2.ui.home_header_tvs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Home_header_tvs_ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public Home_header_tvs_ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Home_header_tvs_ViewModel fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}