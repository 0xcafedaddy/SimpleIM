package com.langsin.im.client;

import com.langsin.im.msg.MsgHead;
 
/**
 *��ʱͨ��ϵͳ 
 *ͨ��ģ�����Ϣ����������ӿڶ���
 */
public interface IClientMsgListener {

	/**
	 * ������յ���һ����Ϣ
	 * @param msg:���յ�����Ϣ����
	 */
	public void fireMsg(MsgHead msg);
	
}
