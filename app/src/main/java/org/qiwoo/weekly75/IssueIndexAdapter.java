package org.qiwoo.weekly75;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.qiwoo.weekly75.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hujun-iri on 16/3/27.
 * 期刊列表 adapter
 */
public class IssueIndexAdapter extends BaseAdapter {

    public static final String ISSUE_ID = "org.qiwoo.weekly75.issueId";

    List<IssueIndex> issueIndexes = new ArrayList<>();

    private Context mContext;

    public IssueIndexAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return issueIndexes.size();
    }

    @Override
    public IssueIndex getItem(int position) {
        return issueIndexes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IssueIndexListViewHolder issueIndexListViewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.issue_index_item, parent, false);

            issueIndexListViewHolder = new IssueIndexListViewHolder();
            issueIndexListViewHolder.mTitle = findView(convertView, R.id.title);

            convertView.setTag(issueIndexListViewHolder);
        } else {
            issueIndexListViewHolder = (IssueIndexListViewHolder) convertView.getTag();
        }

        final IssueIndex issueIndex = issueIndexes.get(position);
        issueIndexListViewHolder.mTitle.setText(issueIndex.title);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, IssueDetailActivity.class);
                intent.putExtra(ISSUE_ID, issueIndex.id + "");
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    private <T> T findView(View rootView, int resId) {
        if(rootView == null) {
            return null;
        }
        View v = rootView.findViewById(resId);
        if(v == null) {
            return null;
        }
        return (T) v;
    }

    public void setIusse(int iid) {
        while (iid >= 1) {
            issueIndexes.add(new IssueIndex("第"+ iid +"期", iid));
            iid = iid-1;
        }
        notifyDataSetChanged();
    }

    class IssueIndexListViewHolder {
        TextView mTitle;
    }

    class IssueIndex {
        String title;
        int id;

        public IssueIndex(String title, int id) {
            this.title = title;
            this.id = id;
        }
    }
}
