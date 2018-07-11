package com.loyi.cloud.stone.content.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 文章分组
 *
 */
@Entity
@Table(name = "t_group")
public class GroupEntity {

	@Id
	@Column(length = 50)
	private String id;

	@Deprecated
	@Column(columnDefinition = "tinyint")
	private int type;

	@Column(length = 100)
	private String name;

	private Date created;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
