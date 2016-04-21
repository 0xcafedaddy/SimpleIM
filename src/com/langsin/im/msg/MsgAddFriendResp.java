package com.langsin.im.msg;

//添加好友应答消息类
public class MsgAddFriendResp extends MsgHead {
	private int friendykNum;// 好友的yk号
	private String friendNikeName;// 好友呢称

	public String toString() {
		// String head=super.toString();
		return "friendykNum:" + friendykNum + " friendNikeName:"
				+ friendNikeName;
	}

	// 以下为getter/seter方法
	public int getFriendykNum() {
		return friendykNum;
	}

	public void setFriendykNum(int friendykNum) {
		this.friendykNum = friendykNum;
	}

	public String getFriendNikeName() {
		return friendNikeName;
	}

	public void setFriendNikeName(String friendNikeName) {
		this.friendNikeName = friendNikeName;
	}
}
