package com.langsin.im.msg;

//��Ӻ���Ӧ����Ϣ��
public class MsgAddFriendResp extends MsgHead {
	private int friendykNum;// ���ѵ�yk��
	private String friendNikeName;// �����س�

	public String toString() {
		// String head=super.toString();
		return "friendykNum:" + friendykNum + " friendNikeName:"
				+ friendNikeName;
	}

	// ����Ϊgetter/seter����
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
