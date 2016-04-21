package com.langsin.im.msg;
//添加好友请求消息类
public class MsgAddFriend extends MsgHead{
	private int  friendykNum;//好友的yk号
	//以下为getter/seter/toString方法
	public String toString(){
		String head=super.toString();
		return head+" friendykNum:"+ friendykNum;
	}
	//以下为getter/seter方法
    public int getFriendykNum() {
		return friendykNum;
	}


	public void setFriendykNum(int friendykNum) {
		this.friendykNum = friendykNum;
	}
	 
	 
}
