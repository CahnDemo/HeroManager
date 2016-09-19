package com.yujie.heromanager.bean;

public class SuperUser {
	private String userName;
	private String passWord;
	private String userRemark;
	public SuperUser() {
		super();
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getUserRemark() {
		return userRemark;
	}
	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}
	public SuperUser(String userName, String passWord, String userRemark) {
		super();
		this.userName = userName;
		this.passWord = passWord;
		this.userRemark = userRemark;
	}
	@Override
	public String toString() {
		return "SuperUser [userName=" + userName + ", passWord=" + passWord
				+ ", userRemark=" + userRemark + "]";
	}
}
