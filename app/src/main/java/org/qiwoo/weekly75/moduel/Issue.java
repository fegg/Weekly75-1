package org.qiwoo.weekly75.moduel;

import java.util.List;

/**
 * Created by hujun-iri on 16/3/25.
 * 期刊
 * 每期的数据结构
 */
public class Issue {
    private int iid;
    private String date;
    private String editor;
    private String topic;
    private List<Category> categoryList;

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
