package com.bshuai.content.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 手机端文章草稿实体
 */
@Entity
@Table(name = "t_draft")
public class DraftEntity {

	@Id
	@Column(length = 50)
	private String uid;

	@Column(length = 80)
	private String title;

	@Column(columnDefinition = "longtext")
	private String contentModel;

	/**
	 * 封面图片上传到微信服务器后返回的Id
	 */
	@Column(length = 50)
	private String mediaId;

	/**
	 * 封面图片地址
	 */
	@Column(columnDefinition = "text")
	private String thumbUrl;

	@Column(length = 50)
	private String author;

	private Date created;

	private Date updated;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContentModel() {
		return contentModel;
	}

	public void setContentModel(String contentModel) {
		this.contentModel = contentModel;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}

	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
}
