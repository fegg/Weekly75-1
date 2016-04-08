package org.qiwoo.weekly75.moduel;

import java.util.List;

/**
 * Created by hujun-iri on 16/3/25.
 */
public class Category {

    // 分类名称
    private String title;

    // 文章列表
    private List<Article> articleList;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }
}
