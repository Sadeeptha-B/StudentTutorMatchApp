package com.example.studenttutormatchapp.view;

import android.view.View;

public interface GenericView {
    View getRootView();
    void initViews();
    void bindDataToView();
}
