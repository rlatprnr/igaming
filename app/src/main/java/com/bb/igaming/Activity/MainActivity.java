package com.bb.igaming.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bb.igaming.Fragment.DealsFragment;
import com.bb.igaming.Fragment.OffersFragment;
import com.bb.igaming.Fragment.Question1Fragment;
import com.bb.igaming.Fragment.Question2Fragment;
import com.bb.igaming.Fragment.Question3Fragment;
import com.bb.igaming.Fragment.Question4Fragment;
import com.bb.igaming.Fragment.Question5Fragment;
import com.bb.igaming.Fragment.Question6Fragment;
import com.bb.igaming.Fragment.WecomeFragment;
import com.bb.igaming.Model.Settings;
import com.bb.igaming.R;
import com.hipmob.android.HipmobCore;

public class MainActivity extends AppCompatActivity {

    private ViewPager _viewPager;
    private ImageView _backButton;
    private ImageView _settingButton;
    private ImageView _liveSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // toobal heght
        findViewById(R.id.llToobar).setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(Settings.scale * 157))
        );

        _viewPager = (ViewPager)findViewById(R.id.viewPager);
        _viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                ((IMyAdapter) _viewPager.getAdapter()).onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // back button
        _backButton = (ImageView)findViewById(R.id.ivBack);
        _backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _viewPager.setCurrentItem(_viewPager.getCurrentItem() - 1);
            }
        });

        // setting button
        _settingButton = (ImageView)findViewById(R.id.ivSetting);
        _settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuestions(false);
            }
        });

        // live support
        _liveSupport = (ImageView)findViewById(R.id.ivLiveSupport);
        _liveSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLiveChat();
            }
        });

        Settings.setString((TextView)findViewById(R.id.tvTitle), "APP_TITLE", R.string.app_title);

        if (Settings.instance.freeSpins == -1) {
            showQuestions(true);
        } else {
            showDeals();
        }
    }

    public void showQuestions(boolean showWelcome) {
        _viewPager.setAdapter(new QestionsAdapter(getSupportFragmentManager()));
        if (!showWelcome) {
            _viewPager.setCurrentItem(1);
        }
    }

    public void showDeals() {
        _viewPager.setAdapter(new DealsAdapter(getSupportFragmentManager()));
    }

    public void gogogo() {
        _viewPager.setCurrentItem(_viewPager.getCurrentItem() + 1);
    }

    private void enableLiveSupport(boolean flag) {
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, flag ? (int)(Settings.scale * 159) : 0);
        _liveSupport.setLayoutParams(param);
    }

    private void showLiveChat() {
        Intent i = new Intent(this, com.hipmob.android.HipmobCore.class);
        i.putExtra(HipmobCore.KEY_APPID, getResources().getString(R.string.hipmob_key));
        i.putExtra(HipmobCore.KEY_USERID, "my-user-identifier-that-is-unique");
        startActivity(i);
    }

    public class QestionsAdapter extends FragmentStatePagerAdapter implements IMyAdapter {

        public QestionsAdapter(FragmentManager fm) {
            super(fm);
            _backButton.setVisibility(View.INVISIBLE);
            _settingButton.setVisibility(View.INVISIBLE);
            enableLiveSupport(false);
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = new WecomeFragment();
                    break;
                case 1:
                    fragment = new Question1Fragment();
                    break;
                case 2:
                    fragment = new Question2Fragment();
                    break;
                case 3:
                    fragment = new Question3Fragment();
                    break;
                case 4:
                    fragment = new Question4Fragment();
                    break;
                case 5:
                    fragment = new Question5Fragment();
                    break;
                case 6:
                    fragment = new Question6Fragment();
                    break;
                default:
                    fragment = new Fragment();
                    break;
            }
            return fragment;
        }

        public void onPageSelected(int position) {
            _backButton.setVisibility(position <= 1 ? View.INVISIBLE : View.VISIBLE);
        }
    }

    public class DealsAdapter extends FragmentStatePagerAdapter implements IMyAdapter {

        private OffersFragment offersFragment;

        public DealsAdapter(FragmentManager fm) {
            super(fm);
            _backButton.setVisibility(View.INVISIBLE);
            _settingButton.setVisibility(View.VISIBLE);
            enableLiveSupport(Settings.instance.isEnableLiveSupport());
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new DealsFragment();
            }
            offersFragment = new OffersFragment();
            return offersFragment;
        }

        public void onPageSelected(int position) {
            _backButton.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
            if (position == 1) {
                offersFragment.initFragment();
            }
        }
    }

    public interface IMyAdapter {
        void onPageSelected(int position);
    }
}
