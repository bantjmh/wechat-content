package com.loyi.cloud.stone.content.model;

import java.util.Date;

/**
 * 文章分组
 *
 */
public class Group {
	private String id;

	private String name;

	private Date created;

	@Deprecated
	private Integer type;

	public String getTypeStr() {

		switch (this.type) {
		case 1:
			return "公众号";
		default:
			return "文章";
		}

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}
