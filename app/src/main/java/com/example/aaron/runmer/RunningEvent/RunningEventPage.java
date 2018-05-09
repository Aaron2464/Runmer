package com.example.aaron.runmer.RunningEvent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aaron.runmer.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class RunningEventPage extends Fragment implements RunningEventContract.View {

    private RunningEventContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        return view;
    }

    @Override
    public void setPresenter(RunningEventContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
