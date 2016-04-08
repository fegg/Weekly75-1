package org.qiwoo.weekly75;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.qiwoo.weekly75.moduel.Article;
import org.qiwoo.weekly75.moduel.ArticlePager;
import org.qiwoo.weekly75.moduel.Category;
import org.qiwoo.weekly75.moduel.Issue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hujun-iri on 16/3/26.
 * 该类是 api 接口, 用来获取数据
 *
 */
public class Weekly75 {
    // 获取最新期刊
    public static final String API_LATEST = "http://weeklyapi.75team.com/issue/latest";

    /**
     * 周刊详情
     * 接口格式：/issue/detail/${iid}
     * 参数说明：
     * iid：具体某一期期刊 id。可以从 /issue/list/ 接口中获取；
     * 示例：
     * http://weeklyapi.75team.com/issue/detail/134
     */
    public static final String API_ISSUE_DETAIL = "http://weeklyapi.75team.com/issue/detail/";

    public static final String API_SEARCH = "http://weeklyapi.75team.com/article/search/%1$s/%2$s/hl/0";

    private static int BUFFER_SIZE = 4096;

    // 获取最新的期刊
    public static Issue getLatestIssue() throws IOException, JSONException {
        String result = httpGet(API_LATEST);
        //Issue issue = parseIssue(result);
        return parseIssue(result);
    }

    // 根据期刊编号获取周刊详情
    public static Issue getIssueById(String id) throws IOException, JSONException {
        String result = httpGet(API_ISSUE_DETAIL + id);
        return parseIssue(result);
    }

    public static ArticlePager search(String key, String page) throws IOException, JSONException {
        String url = createSearchUrl(key, page);
        String result = httpGet(url);
        return paseArticle(result);
    }

    private static ArticlePager paseArticle(String result) throws JSONException {
        ArticlePager articles = new ArticlePager();
        JSONObject jo = new JSONObject(result);
        articles.count = jo.getInt("count");
        articles.totalPages = jo.getInt("totalPages");
        articles.numsPerPage = jo.getInt("numsPerPage");
        articles.currentPage = jo.getInt("currentPage");
        List<ArticlePager.Article> data = new ArrayList<>();
        articles.articles = data;
        JSONArray ja = jo.getJSONArray("data");
        for(int i =0 ;i < ja.length(); i++) {
            JSONObject joo = ja.getJSONObject(i);

            // 常规内部类需要通过外部类的实例才能创建对象，与实例变量需要通过对象来访问相似
            ArticlePager.Article aa = articles.new Article();
            aa.aid = joo.getInt("aid");
            aa.iid = joo.getInt("iid");
            aa.title = joo.getString("title");
            aa.url = joo.getString("url");
            aa.description = joo.getString("description");
            aa.tags = joo.getString("tags");
            aa.readability_url = joo.getString("readability_url");

            data.add(aa);
        }
        return articles;
    }

    /**
     * 发送get请求
     * 把请求的结果用一个字符串的形式返回
     */
    private static String httpGet(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        InputStream stream = conn.getInputStream();

        String resultStr = readInputStreamAsString(stream);
        return resultStr;
    }

    private static String readInputStreamAsString(InputStream in) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int count;
        while( (count = in.read(buffer, 0, BUFFER_SIZE)) != -1 ) {
            outputStream.write(buffer, 0, count);
        }
        return new String(outputStream.toByteArray(), "UTF-8");
    }

    private static Issue parseIssue(String str) throws JSONException {
        Issue issue = new Issue();
        JSONObject jsonObject = new JSONObject(str);
        issue.setIid(jsonObject.getInt("iid"));
        issue.setDate(jsonObject.getString("date"));
        issue.setEditor(jsonObject.getString("editor"));
        issue.setTopic(jsonObject.getString("topic"));

        JSONArray categoryObj = jsonObject.getJSONArray("article");
        List<Category> categoryList = new ArrayList<>();
        issue.setCategoryList(categoryList);
        for(int i=  0; i <categoryObj.length() ; i++) {
            JSONObject cateItem = categoryObj.getJSONObject(i);
            Iterator<String> itKeys =  cateItem.keys();
            // 只是获取一级
            String key = itKeys.next();
            Category category = new Category();
            List<Article> articleList = new ArrayList<>();
            category.setTitle(key);
            category.setArticleList(articleList);

            JSONArray articlesArray = cateItem.getJSONArray(key);
            for( int j = 0; j < articlesArray.length(); j++ ) {
                JSONObject tempObj = articlesArray.getJSONObject(j);
                // 创建 Article 对象
                Article article = new Article();
                article.setTitle(tempObj.getString("title"));
                article.setUrl(tempObj.getString("url"));
                article.setReadabilityUrl(tempObj.getString("readability_url"));
                article.setDescription(tempObj.getString("description"));
                article.setProvider(tempObj.getString("provider"));

                // 解析标签
                List<String> tagList = new ArrayList<>();
                JSONArray tagsJSONArray = tempObj.getJSONArray("tags");
                for( int k=0; k< tagsJSONArray.length(); k++){
                    tagList.add(tagsJSONArray.getString(k));
                }
                article.setTagArr(tagList);
                article.setTags(Arrays.toString(tagList.toArray()));

                // 将 article 添加到 List 中
                articleList.add(article);
            }

            categoryList.add(category);
        }

        return issue;
    }

    private static String createSearchUrl(String key, String page) {
        return String.format(API_SEARCH, encodeURIComponent(key), page);
    }


    private static String encodeURIComponent(String s) {
        String result = null;
        try {
            result = URLEncoder.encode(s, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException e) {
            // This exception should never occur.
            result = s;
        }
        return result;
    }



}
