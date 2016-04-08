package org.qiwoo.weekly75;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.qiwoo.weekly75.R;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    IndexViewPagerAdapter myAdapter;
    ViewPager pager;
    List<BottomNav> bottomNavs;

    private int currPage=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        myAdapter = new IndexViewPagerAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(myAdapter);

//        btnPrev = (Button) findViewById(R.id.goto_prev);
//        btnNext = (Button) findViewById(R.id.goto_next);
//
//        btnPrev.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pager.setCurrentItem(0, false);
//            }
//        });
//
//        btnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pager.setCurrentItem(NUM_ITEMS - 1, false);
//            }
//        });
        initView();
    }

    private void initView() {

        bottomNavs = new ArrayList<>();
        bottomNavs.add(generateBtn(R.id.bottom_group1, R.id.bottom_group1_img, R.id.bottom_group1_text, 0));
        bottomNavs.add(generateBtn(R.id.bottom_group2, R.id.bottom_group2_img, R.id.bottom_group2_text, 1));
        bottomNavs.add(generateBtn(R.id.bottom_group3, R.id.bottom_group3_img, R.id.bottom_group3_text, 2));
        bottomNavs.add(generateBtn(R.id.bottom_group4, R.id.bottom_group4_img, R.id.bottom_group4_text, 3));
        bottomNavs.get(currPage).fous();


        // 监听ViewPager的滑动事件
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavs.get(position).fous();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private BottomNav generateBtn(int btnId, int ivId, int tvId, int index) {
        ViewGroup group = (ViewGroup) findViewById(btnId);
        ImageView iv = (ImageView) findViewById(ivId);
        TextView tv = (TextView) findViewById(tvId);
       return new BottomNav(group, iv, tv, index);
    }

    class BottomNav {
        int order;
        ViewGroup g;
        ImageView img;
        TextView t;

        public BottomNav(ViewGroup g, ImageView img, TextView t, final int order) {
            this.g = g;
            this.img = img;
            this.t = t;
            this.order = order;

            // 初始化事件处理
            g.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomNav.this.fous();
                }
            });
        }

        public void fous(){
            pager.setCurrentItem(order, false);
            // 先 blur 上一个item
            bottomNavs.get(currPage).blur();
            // 把当前 item focus
            this.t.setTextColor(getResources().getColor(R.color.material_teal_600));
            currPage = this.order;
        }

        public void blur(){
            this.t.setTextColor(getResources().getColor(R.color.bottomPrimaryColor));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // 这里暂时先注释掉了这个菜单
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public class IndexViewPagerAdapter extends FragmentPagerAdapter {

        // 设置首页一个有多少个tab
        private static final int NUM_ITEMS = 4;

        public IndexViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //return null;
            //Log.v("Verbose", "position:*****" + position);

            if( position == 0) {
                // 首页,获取最新期刊
                return new LatestFragment();
            } else if (position == 1 ) {
                // 获取期刊列表
                return new IssueIndexFragment();
            } else if(position == 2){
                // 搜索
                return new SearchFragment();
            } else {
                return new SettingFragment();
            }

            //return ArrayListFragment.newInstance(position);

        }

        @Override
        public int getCount() {
            //Log.v("Verbose", "getCount:-------" + NUM_ITEMS);
            return NUM_ITEMS;
        }
    }
}
