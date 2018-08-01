package com.loyi.stone.content.api.domain;

import java.util.Date;

/**
 * Created by fq on 2018/7/6.
 */
public class Article {
    private String id;

    private String title;

    private String content;

    /**
     * 是否显示封面，0为false，即不显示，1为true，即显示
     */
    private int showCoverPic = 1;

    /**
     * 封面图片地址
     */
    private String thumbUrl;

    /**
     * 图文消息的封面图片素材id
     */
    private String thumbMediaId;

    private String author;

    /**
     * 图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空
     */
    private String digest = "";

    /**
     * 图文页的URL(高级群发不可用外链).
     */
    private String url = "";

    /**
     * 图文消息的原文地址，即点击“阅读原文”后的URL
     */
    private String contentSourceUrl = "";

    /**
     * 是否打开评论，0不打开，1打开
     */
    private int need_open_comment = 1;

    /**
     * 是否粉丝才可评论，0所有人可评论，1粉丝才可评论
     */
    private Integer only_fans_can_comment = 0;

    private String createrId;

    private String creater;

    private String mediaId;

    private Date updated;

    private Date created;

    private int checkStatus;

    private double longtitude;

    private double latitude;

    private String contentModel;

    private String thumbImageFileName;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getShowCoverPic() {
        return showCoverPic;
    }

    public void setShowCoverPic(int showCoverPic) {
        this.showCoverPic = showCoverPic;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentSourceUrl() {
        return contentSourceUrl;
    }

    public void setContentSourceUrl(String contentSourceUrl) {
        this.contentSourceUrl = contentSourceUrl;
    }

    public int getNeed_open_comment() {
        return need_open_comment;
    }

    public void setNeed_open_comment(int need_open_comment) {
        this.need_open_comment = need_open_comment;
    }

    public Integer getOnly_fans_can_comment() {
        return only_fans_can_comment;
    }

    public void setOnly_fans_can_comment(Integer only_fans_can_comment) {
        this.only_fans_can_comment = only_fans_can_comment;
    }

    public String getCreaterId() {
        return createrId;
    }

    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(int checkStatus) {
        this.checkStatus = checkStatus;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getContentModel() {
        return contentModel;
    }

    public void setContentModel(String contentModel) {
        this.contentModel = contentModel;
    }

    public String getThumbImageFileName() {
        return thumbImageFileName;
    }

    public void setThumbImageFileName(String thumbImageFileName) {
        this.thumbImageFileName = thumbImageFileName;
    }
}
