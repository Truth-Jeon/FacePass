package kr.co.hdtel.facepass;

import static kr.co.hdtel.facepass.R.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;

import kr.co.hdtel.htcommonui.view.HTActionBar;

public class MainActivity extends AppCompatActivity {
    private SwipeViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        setRxJavaErrorHandler();

        mViewPager = findViewById(R.id.pager);
        PagerAdapter mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setPagingEnabled(false);

        HTActionBar mHTActionBar = findViewById(R.id.tab_top);
        mHTActionBar.setTitle("Face-pass");
        mHTActionBar.addTab("세대원");
        mHTActionBar.addTab("방문자");
        mHTActionBar.getTabLayout();

        mHTActionBar.setOnTabSelectedListener(new HTActionBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int t = tab.getPosition();
                mViewPager.setCurrentItem(t);
            }
        });
    }

    private static class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private final ArrayList<Fragment> arrayList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            arrayList.add(new TabFragment1());
            arrayList.add(new TabFragment2());
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }
    }

    private void setRxJavaErrorHandler() {
    }
}