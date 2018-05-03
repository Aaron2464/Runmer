package com.example.aaron.runmer.Map;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.aaron.runmer.Base.BaseActivity;
import com.example.aaron.runmer.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class MapPage  extends BaseActivity implements MapContract.View{

    private MapContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    }

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}