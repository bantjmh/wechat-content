package com.bshuai.content.model.param;

import javax.validation.constraints.NotNull;

public class DraftParam {

	private String title;

	@NotNull(message = "内容不能为空")
	private String contentModel;

	/**
	 * 封面图片上传到微信服务器后返回的Id
	 */
	@NotNull(message = "封面图片的id不能为空")
	private String mediaId;

	/**
	 * 封面图片地址
	 */
	private String thumbUrl;

	private String author;

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

}
