package com.loyi.cloud.stone.content.filter;

public class ArticleFilter {

	private String keyword;

	private String groupId;

	private int checkStatus;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(int checkStatus) {
		this.checkStatus = checkStatus;
	}

	@Override
	public String toString() {
		return "ArticleFilter{" +
				"keyword='" + keyword + '\'' +
				", groupId='" + groupId + '\'' +
				", checkStatus=" + checkStatus +
				'}';
	}
}
