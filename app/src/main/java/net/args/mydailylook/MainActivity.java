package net.args.mydailylook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.yalantis.ucrop.UCrop;

import net.args.mydailylook.fragment.HomeFragment;
import net.args.mydailylook.fragment.InfoFragment;
import net.args.mydailylook.fragment.MessageFragment;
import net.args.mydailylook.fragment.SearchFragment;

public class MainActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private FragmentPagerItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.title_home, HomeFragment.class)
                .add(R.string.title_search, SearchFragment.class)
                .add(R.string.title_message, MessageFragment.class)
                .add(R.string.title_info, InfoFragment.class)
                .create());

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mAdapter);

        final LayoutInflater inflater = LayoutInflater.from(this);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.stl_activity_main_tab);
        viewPagerTab.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                ImageView icon = (ImageView) inflater.inflate(R.layout.fragment_home_tab, container,
                        false);
                switch (position) {
                    case 0:
                        icon.setImageResource(R.drawable.ic_account_balance);
                        break;
                    case 1:
                        icon.setImageResource(R.drawable.ic_search);
                        break;
                    case 2:
                        icon.setImageResource(R.drawable.ic_message);
                        break;
                    case 3:
                        icon.setImageResource(R.drawable.ic_supervisor_account);
                        break;
                    default:
                        throw new IllegalStateException("Invalid position: " + position);
                }
                return icon;
            }
        });

        viewPagerTab.setViewPager(mViewPager);

//        Intent intent = new Intent(this, InfoActivity.class);
//        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == UCrop.REQUEST_CROP) {
                InfoFragment fragment = (InfoFragment) mAdapter.getPage(3);
                if (fragment != null) {
                    fragment.onActivityResult(UCrop.REQUEST_CROP, RESULT_OK, data);
                }
            }
        }
    }

}
