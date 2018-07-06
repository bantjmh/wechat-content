package com.loyi.cloud.stone.content.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 附件信息
 *
 */
@Entity
@Table(name = "t_attach", indexes = {})
public class AttachEntity {

	@Id
	@Column(length = 40)
	private String id;

	@Column(length = 100)
	private String filename;

	private Date uploadtime = new Date();

	@Column(length = 40)
	private String createrid;

	private Integer type = 1;

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

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Date getUploadtime() {
		return uploadtime;
	}

	public void setUploadtime(Date uploadtime) {
		this.uploadtime = uploadtime;
	}

	public String getCreaterid() {
		return createrid;
	}

	public void setCreaterid(String createrid) {
		this.createrid = createrid;
	}

}
