package com.loyi.cloud.wechat.platform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by fq on 2018/7/4.
 */
@Entity
@Table(name = "t_image")
public class ImageEntity {

    @Id
    @Column(length = 50)
    private String mediaId;

    @Column(length = 50)
    private String uid;

    /**
     * 图片上传时间
     */
    private Date updateTime;

    /**
     * 宽度
     */
    private Integer width;

    /**
     * 高度
     */
    private Integer height;

    /**
     * 光圈
     */
    @Column(length = 50)
    private String aperture;

    /**
     * 焦距
     */
    private Double focusLength;

    /**
     * 媒体类型
     */
    @Column(length = 50)
    private String mimeType;

    /**
     * 感光度
     */
    private Integer iso;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 是否使用闪光灯
     */
    private boolean flash;

    /**
     * 压缩比
     */
    @Column(length = 50)
    private String compression;

    /**
     * 设备型号
     */
    @Column(length = 50)
    private String model;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getAperture() {
        return aperture;
    }

    public void setAperture(String aperture) {
        this.aperture = aperture;
    }

    public Double getFocusLength() {
        return focusLength;
    }

    public void setFocusLength(Double focusLength) {
        this.focusLength = focusLength;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Integer getIso() {
        return iso;
    }

    public void setIso(Integer iso) {
        this.iso = iso;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public boolean isFlash() {
        return flash;
    }

    public void setFlash(boolean flash) {
        this.flash = flash;
    }

    public String getCompression() {
        return compression;
    }

    public void setCompression(String compression) {
        this.compression = compression;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
