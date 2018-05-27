package com.aaron.runmer.ViewPagerMain;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.aaron.runmer.RunningEvent.RunningEventPage;
import com.aaron.runmer.Base.BaseActivity;
import com.aaron.runmer.UserProfile.UserProfilePage;
import com.aaron.runmer.FriendsList.FriendsListPage;
import com.aaron.runmer.Map.MapPage;
import com.aaron.runmer.R;
import com.aaron.runmer.util.Constants;

import static com.google.common.base.Preconditions.checkNotNull;

public class ViewPagerActivity extends BaseActivity implements ViewPagerMainContract.View
        , ViewPager.OnPageChangeListener
        , TabLayout.OnTabSelectedListener
        , View.OnClickListener {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Button mBtnBackToMap;
    private UserProfilePage mUserProfilePage;
    private FriendsListPage mFriendsListPage;
    private RunningEventPage mRunningEventPage;

    private ViewPagerMainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpage);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mViewPager = findViewById(R.id.viewpager_main);
        mTabLayout = findViewById(R.id.tablayout_main);
        mBtnBackToMap = findViewById(R.id.back_to_map);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mBtnBackToMap.setOnClickListener(this);

        mViewPager.setOffscreenPageLimit(2);            //懶載入
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        mUserProfilePage = new UserProfilePage();
                        Log.d(Constants.TAG, "mUserProfilePage ViewPager : " + position);
                        return mUserProfilePage;
                    case 1:
                        mRunningEventPage = new RunningEventPage();
                        Log.d(Constants.TAG, "mRunningEventPage ViewPager : " + position);
                        return mRunningEventPage;
                    case 2:
                        mFriendsListPage = new FriendsListPage();
                        Log.d(Constants.TAG, "mFriendsListPage ViewPager : " + position);
                        return mFriendsListPage;
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //TabLayout裡的TabItem被選中的時候觸發
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //viewPager滑動之後顯示觸發
        mTabLayout.getTabAt(position).select();
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void setPresenter(ViewPagerMainContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(ViewPagerActivity.this, MapPage.class);
        ViewPagerActivity.this.finish();
    }
}
