package org.qiwoo.weekly75;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.qiwoo.weekly75.R;
import org.qiwoo.weekly75.moduel.Article;
import org.qiwoo.weekly75.moduel.Category;
import org.qiwoo.weekly75.moduel.Issue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hujun-iri on 16/3/26.
 *
 */
public class IssueListAdapter extends BaseAdapter{
    public static final String ARTICLE_URL="org.qiwoo.weekly75.articleUrl";
    public static final String ARTICLE_TITLE="org.qiwoo.weekly75.articleTitle";

    private static final int ITEM_TYPE_ISSUE = 0;
    private static final int ITEM_TYPE_CATEGORY = 1;
    private static final int ITEM_TYPE_ARTICLE = 2;

    private static final int VIEW_TYPE_COUNT = 3;

    private Context mContext;
    private List<WeeklyItem> mDatas = new ArrayList();

    // 是否为查看单个 Issue
    private boolean isSingleIssue = false;

    public IssueListAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public IssueListAdapter(Context mContext, boolean isSingleIssue) {
        this.mContext = mContext;
        this.isSingleIssue = isSingleIssue;
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public WeeklyItem getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case ITEM_TYPE_ISSUE:
                IssueViewHolder issueViewHolder;
                if(convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.issue_item, parent, false);
                    issueViewHolder = new IssueViewHolder();
                    issueViewHolder.mTitle = findView(convertView, R.id.tv_title);
                    issueViewHolder.mDate = findView(convertView, R.id.tv_date);
                    issueViewHolder.mEditor = findView(convertView, R.id.tv_editor);
                    issueViewHolder.mTopic = findView(convertView, R.id.tv_topic);
                    convertView.setTag(issueViewHolder);
                } else {
                    issueViewHolder = (IssueViewHolder) convertView.getTag();
                }
                // 设置值
                Issue issue = mDatas.get(position).issue;
                issueViewHolder.mTitle.setText("第"+ issue.getIid() +"期");
                issueViewHolder.mTopic.setText(issue.getTopic());
                issueViewHolder.mEditor.setText(issue.getEditor());
                issueViewHolder.mDate.setText(issue.getDate());

                if(strIsNUll(issue.getTopic())) {
                    issueViewHolder.mTopic.setVisibility(View.GONE);
                } else {
                    issueViewHolder.mTopic.setVisibility(View.VISIBLE);
                }

                break;
            case ITEM_TYPE_CATEGORY:
                CategoryViewHolder categoryViewHolder;
                if(convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.category_item, parent, false);
                    categoryViewHolder = new CategoryViewHolder();
                    categoryViewHolder.mTitle = findView(convertView, R.id.title);
                    convertView.setTag(categoryViewHolder);
                } else {
                    categoryViewHolder = (CategoryViewHolder) convertView.getTag();
                }

                // 设置值
                Category category = mDatas.get(position).category;
                categoryViewHolder.mTitle.setText(category.getTitle());
                break;
            case ITEM_TYPE_ARTICLE:
                ArticleViewHolder articleViewHolder;
                if(convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.article_item, parent, false);
                    articleViewHolder = new ArticleViewHolder();
                    articleViewHolder.mTitle = findView(convertView, R.id.lv_title);
                    articleViewHolder.mDescription = findView(convertView, R.id.lv_description);
                    articleViewHolder.mTags = findView(convertView, R.id.lv_tags);
                    articleViewHolder.mProvider = findView(convertView, R.id.lv_provider);

                    convertView.setTag(articleViewHolder);
                } else {
                    articleViewHolder = (ArticleViewHolder) convertView.getTag();
                }

                // 设置值
                final Article article = mDatas.get(position).article;
                articleViewHolder.mTitle.setText(article.getTitle());
                articleViewHolder.mDescription.setText(article.getDescription());
                articleViewHolder.mTags.setText(article.getTags());
                articleViewHolder.mProvider.setText(article.getProvider());

                if(strIsNUll(article.getProvider())) {
                    articleViewHolder.mProvider.setVisibility(View.GONE);
                } else {
                    articleViewHolder.mProvider.setVisibility(View.VISIBLE);
                }

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, ShowArticleActivity.class);
                        intent.putExtra(ARTICLE_TITLE, article.getTitle());
                        intent.putExtra(ARTICLE_URL, article.getUrl());
                        mContext.startActivity(intent);
                    }
                });

                break;
        }
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).mType;
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

    private boolean strIsNUll(String str) {
        return str == null || str.equals("null") || str.trim().equals("");
    }

    private void dealIssue(Issue issue, boolean clear) {
        if(clear) {
            mDatas.clear();
        }
        if(issue != null) {
            mDatas.add(new WeeklyItem(issue,ITEM_TYPE_ISSUE));
            for(Category category: issue.getCategoryList()) {
                mDatas.add(new WeeklyItem(category, ITEM_TYPE_CATEGORY));
                for (Article article: category.getArticleList()) {
                    mDatas.add(new WeeklyItem(article, ITEM_TYPE_ARTICLE));
                }
            }
        }
    }

    public void addData(Issue issue) {
        dealIssue(issue, false);
        notifyDataSetChanged();
    }

     public void updateData(Issue issue) {
         dealIssue(issue, true);
         notifyDataSetChanged();
     }

    class IssueViewHolder {
        TextView mTitle;
        TextView mTopic;
        TextView mDate;
        TextView mEditor;
    }

    class CategoryViewHolder {
        TextView mTitle;
    }

    class ArticleViewHolder {
        TextView mTitle;
        TextView mDescription;
        TextView mTags;
        TextView mProvider;
    }

    class WeeklyItem {
        int mType;
        Issue issue;
        Category category;
        Article article;

        public WeeklyItem(Issue issue, int mType) {
            this.issue = issue;
            this.mType = mType;
        }

        public WeeklyItem(Category category, int mType) {
            this.category = category;
            this.mType = mType;
        }

        public WeeklyItem(Article article, int mType) {
            this.mType = mType;
            this.article = article;
        }
    }


}
