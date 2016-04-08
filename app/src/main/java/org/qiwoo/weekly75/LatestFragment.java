package org.qiwoo.weekly75;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.ListView;

import org.json.JSONException;
import org.qiwoo.weekly75.moduel.Issue;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * 最新期刊
 * 支持上拉刷新的
 */
public class LatestFragment extends Fragment {

    private int LATEST_ISSUE = 0;
    private int NEXT_ISSUE = 0;

    public static final String LOG_LATEST_FRAGMENT = "LatestFragment";

    private View mRootView;
    private ListView issueListView;
    private IssueListAdapter issueListAdapter;

    private boolean isLoadMore = false;


    public LatestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TAG", "onCreateView");
        // Inflate the layout for this fragment
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_latest, container, false);
            initView(mRootView);
        } else {
            ViewParent parent = mRootView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mRootView);
            }
        }
        // 初始化UI
        return mRootView;
    }

    private void initView(View view) {

        this.issueListView = (ListView) view.findViewById(R.id.issue_list);
        issueListAdapter = new IssueListAdapter(getContext());
        this.issueListView.setAdapter(issueListAdapter);

        // 监听滚动事件
        issueListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //Log.d("ViewPager", totalItemCount+"-----");
                // 我靠,这个地方有 bug 呀
                if (firstVisibleItem + visibleItemCount >= totalItemCount && !isLoadMore && hasMoreData() && totalItemCount > 0) {
                    isLoadMore = true;
                    new FetchIssueTask().execute(NEXT_ISSUE);
                }
            }
        });

        loadIssue();
    }

    @Override
    public void onDetach() {
        Log.d("TAG", "onDetach");
        super.onDetach();
    }

    private boolean hasMoreData() {
        return NEXT_ISSUE >= 1;
    }

    // 获取最新一期的期刊数据
    private void loadIssue() {
        isLoadMore = true;
        new FetchIssueTask().execute(0);
    }


    private class FetchIssueTask extends AsyncTask<Integer, Void, Issue> {

        @Override
        protected Issue doInBackground(Integer... params) {
            try {
                if (params[0] >= 1) {
                    NEXT_ISSUE--;
                    return Weekly75.getIssueById(params[0] + "");
                } else {
                    Issue issue = Weekly75.getLatestIssue();
                    LATEST_ISSUE = issue.getIid();
                    NEXT_ISSUE = LATEST_ISSUE - 1;
                    return issue;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Issue issue) {
            if (issue != null) {
                Log.v(LOG_LATEST_FRAGMENT, issue.getDate());
                Log.v(LOG_LATEST_FRAGMENT, issue.getTopic());

                issueListAdapter.addData(issue);
            }
            isLoadMore = false;
        }
    }


}
