package com.bshuai.content.model.vo;

public class DataCubeVo {
	private Integer newUserCount;
	private Integer summerUserCount;

	public Integer getNewUserCount() {
		return newUserCount;
	}

	public void setNewUserCount(Integer newUserCount) {
		this.newUserCount = newUserCount;
	}

	public Integer getSummerUserCount() {
		return summerUserCount;
	}

	public void setSummerUserCount(Integer summerUserCount) {
		this.summerUserCount = summerUserCount;
	}

	@Override
	public String toString() {
		return "DataCubeVo{" + "newUserCount=" + newUserCount + ", summerUserCount=" + summerUserCount + '}';
	}
}
