package com.loyi.cloud.wechat.platform.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_article_group_rel")
public class ArticleGroupRelEntity {

	@Id
	@Column(length = 50)
	private String id;

	@Column(length = 50)
	private String groupId;

	@Column(length = 50)
	private String articleId;

	private Date created;

	public ArticleGroupRelEntity(String groupId, String articleId) {
		this.groupId = groupId;
		this.articleId = articleId;
		this.created = new Date();
		this.id = UUID.randomUUID().toString();
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public ArticleGroupRelEntity() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}
