package com.langsin.im.msg;
//��Ӻ���������Ϣ��
public class MsgAddFriend extends MsgHead{
	private int  friendykNum;//���ѵ�yk��
	//����Ϊgetter/seter/toString����
	public String toString(){
		String head=super.toString();
		return head+" friendykNum:"+ friendykNum;
	}
	//����Ϊgetter/seter����
    public int getFriendykNum() {
		return friendykNum;
	}


	public void setFriendykNum(int friendykNum) {
		this.friendykNum = friendykNum;
	}
	 
	 
}
