package com.loyi.cloud.wechat.platform.filter;

public class GroupFilter {
	private String name;
	/**
	 * 1=公众号分组；2=文章分组
	 */
	private Integer type;

	private String articleId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}


	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

}
