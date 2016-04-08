package org.qiwoo.weekly75;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.qiwoo.weekly75.R;
import org.qiwoo.weekly75.moduel.ArticlePager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private ListView listView;
    private EditText editText;
    private TextView submitText;
    private SearchListAdapter searchListAdapter;

    private int currentPage = 0;
    private int totalPages = 0;

    private boolean isLoadMore = false;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        initView(view);
        return view;
    }

    private void initView(View root) {
        editText = (EditText) root.findViewById(R.id.edit_text);
        submitText = (TextView) root.findViewById(R.id.submit_text);

        listView = (ListView) root.findViewById(R.id.result_list);
        searchListAdapter = new SearchListAdapter(getContext());
        listView.setAdapter(searchListAdapter);

        submitText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = editText.getText().toString();
                searchListAdapter.clearData();
                if ( !key.trim().equals("") ) {
                    new SearchTask(SearchTask.TYPE_FIRST_SEARCH).execute(key, 1 + "");
                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount && !isLoadMore && hasMoreData()) {
                    isLoadMore = true;
                    new SearchTask(SearchTask.TYPE_PAGE_SEARCH).execute(editText.getText().toString(), (currentPage + 1) + "");
                }
            }
        });
    }

    private boolean hasMoreData() {
        return currentPage < totalPages;
    }

    class SearchTask extends AsyncTask<String, Void, ArticlePager> {

        public static final int TYPE_FIRST_SEARCH = 1;
        public static final int TYPE_PAGE_SEARCH = 2;

        private int freshType;

        public SearchTask(int freshType) {
            this.freshType = freshType;
        }

        @Override
        protected ArticlePager doInBackground(String... params) {
            try {
                return Weekly75.search(params[0], params[1]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArticlePager articlePager) {
            if(articlePager != null) {
                if(freshType == TYPE_FIRST_SEARCH) {
                    searchListAdapter.updateData(articlePager.articles);
                } else {
                    searchListAdapter.addData(articlePager.articles);
                }

                currentPage = articlePager.currentPage;
                totalPages = articlePager.totalPages;
            }
            isLoadMore = false;
        }
    }


    class SearchListAdapter extends BaseAdapter{

        Context context;

        List<ArticlePager.Article> articles = new ArrayList<>();

        public SearchListAdapter(Context context) {
            this.context = context;
        }

        // 添加数据
        public void addData(List<ArticlePager.Article> articles) {
            this.articles.addAll(articles);
            notifyDataSetChanged();
        }

        // 更新数据
        public void updateData(List<ArticlePager.Article> articles) {
            articles.clear();
            addData(articles);
        }

        public void clearData() {
            articles.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return articles.size();
        }

        @Override
        public ArticlePager.Article getItem(int position) {
            return articles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ArticleViewHolder articleViewHolder;
            if(convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.article_item, parent, false);

                articleViewHolder = new ArticleViewHolder();
                articleViewHolder.mTitle = findView(convertView, R.id.lv_title);
                articleViewHolder.mDescription = findView(convertView, R.id.lv_description);
                articleViewHolder.mTags = findView(convertView, R.id.lv_tags);
                articleViewHolder.mProvider = findView(convertView, R.id.lv_provider);

                convertView.setTag(articleViewHolder);
            } else {
                articleViewHolder = (ArticleViewHolder) convertView.getTag();
            }

            final ArticlePager.Article aa = articles.get(position);
            articleViewHolder.mTitle.setText(aa.title);
            articleViewHolder.mDescription.setText(aa.description);
            articleViewHolder.mTags.setText(aa.tags);
            articleViewHolder.mProvider.setText(aa.provider);

            if(strIsNUll(aa.provider)) {
                articleViewHolder.mProvider.setVisibility(View.GONE);
            } else {
                articleViewHolder.mProvider.setVisibility(View.VISIBLE);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowArticleActivity.class);
                    intent.putExtra(IssueListAdapter.ARTICLE_TITLE, aa.title);
                    intent.putExtra(IssueListAdapter.ARTICLE_URL, aa.url);
                    context.startActivity(intent);
                }
            });

            return convertView;
        }

        private boolean strIsNUll(String str) {
            return str == null || str.equals("null") || str.trim().equals("");
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

        class ArticleViewHolder {
            TextView mTitle;
            TextView mDescription;
            TextView mTags;
            TextView mProvider;
        }

    }


}
