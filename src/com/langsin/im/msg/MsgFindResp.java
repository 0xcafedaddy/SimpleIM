package com.langsin.im.msg;

import java.util.ArrayList;
import java.util.List;

import com.langsin.im.model.UserInfo;

//���Һ���������Ϣ��
public class MsgFindResp extends MsgHead {
	// ��ź��ѷ���Ķ���
	private List<UserInfo> users = new ArrayList<UserInfo>();

	// ��һ���û�����
	public void addUserInfo(UserInfo bu) {
		this.users.add(bu);
	}

	// ȡ�����к����б�
	public List<UserInfo> getUsers() {
		return this.users;
	}

	public String toString() {
		return super.toString() + this.users.toString();
	}

}
