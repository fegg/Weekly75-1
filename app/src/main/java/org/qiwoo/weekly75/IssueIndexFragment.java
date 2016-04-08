package org.qiwoo.weekly75;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONException;
import org.qiwoo.weekly75.R;
import org.qiwoo.weekly75.moduel.Issue;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class IssueIndexFragment extends Fragment {

    private static int LATEST_ISSUE = 0;

    private ListView listView;
    private IssueIndexAdapter issueListIndexAdapter;

    public IssueIndexFragment() {
        // Required empty public constructor
    }

    public void updateLatestIssue(int id) {
        issueListIndexAdapter.setIusse(id);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_issue_index, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        this.listView = (ListView) view.findViewById(R.id.issue_index_list);
        issueListIndexAdapter = new IssueIndexAdapter(getContext());
        listView.setAdapter(issueListIndexAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 设置最新的期刊,用来更新列表

        if(IssueIndexFragment.LATEST_ISSUE > 0) {
            issueListIndexAdapter.setIusse(IssueIndexFragment.LATEST_ISSUE);
            //isInitList = true;
        } else {
            (new AsyncTask<String, Void, Issue>(){

                @Override
                protected Issue doInBackground(String... params) {
                    try {
                        return Weekly75.getLatestIssue();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Issue issue) {
                    if(issue != null ) {
                        IssueIndexFragment.LATEST_ISSUE = issue.getIid();
                        issueListIndexAdapter.setIusse(IssueIndexFragment.LATEST_ISSUE);
                    }
                }
            }).execute();
        }
        //Log.d("TAG", LatestFragment.LATEST_ISSUE + "期刊");
    }



}
