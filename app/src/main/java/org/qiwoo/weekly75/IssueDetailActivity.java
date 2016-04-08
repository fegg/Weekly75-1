package org.qiwoo.weekly75;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import org.json.JSONException;
import org.qiwoo.weekly75.R;
import org.qiwoo.weekly75.moduel.Issue;

import java.io.IOException;

public class IssueDetailActivity extends AppCompatActivity {

    private ListView listView;
    private IssueListAdapter issueListAdapter;
    private String issueId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.GONE);

        // 显示返回导航按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IssueDetailActivity.this.finish();
            }
        });


        // 获取传递过来的参数
        Intent intent = getIntent();
        issueId = intent.getStringExtra(IssueIndexAdapter.ISSUE_ID);
        getSupportActionBar().setTitle("第"+ issueId +"期");

        initView();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.issue_list);
        issueListAdapter = new IssueListAdapter(this, true);
        listView.setAdapter(issueListAdapter);

        // 获取数据
        (new AsyncTask<String, Void, Issue>() {
            @Override
            protected Issue doInBackground(String... params) {
                try {
                    return Weekly75.getIssueById(params[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Issue issue) {
                if(issue != null) {
                    issueListAdapter.addData(issue);
                }
            }
        }).execute(issueId);

    }

}
