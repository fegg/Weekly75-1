package org.qiwoo.weekly75.moduel;

import java.util.List;

/**
 * Created by hujun-iri on 16/3/25.
 */
public class Article {

    private String title;
    private String url;
    private String readabilityUrl;
    private String description;
    private String tags;
    private String provider;
    private List<String> tagArr;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReadabilityUrl() {
        return readabilityUrl;
    }

    public void setReadabilityUrl(String readabilityUrl) {
        this.readabilityUrl = readabilityUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public List<String> getTagArr() {
        return tagArr;
    }

    public void setTagArr(List<String> tagArr) {
        this.tagArr = tagArr;
    }
}
