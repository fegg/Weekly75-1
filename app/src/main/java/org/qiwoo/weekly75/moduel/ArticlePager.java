package org.qiwoo.weekly75.moduel;

import java.util.List;

/**
 * Created by hujun-iri on 16/3/27.
 */
public class ArticlePager {
    public int count;
    public int totalPages;
    public int numsPerPage;
    public int currentPage;

    public List<Article> articles;

    public class Article {
        public int aid;
        public int iid;
        public String title;
        public String url;
        public String description;
        public String tags;
        public String provider;
        public String readability_url;
    }
}
