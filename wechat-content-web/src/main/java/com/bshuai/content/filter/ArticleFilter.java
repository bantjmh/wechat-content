package com.bshuai.content.filter;

public class ArticleFilter {

	private String keyword;

	private String groupId;

	private int checkStatus;

	private boolean mine;

	private String uid;

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

	public boolean isMine() {
		return mine;
	}

	public void setMine(boolean mine) {
		this.mine = mine;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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
