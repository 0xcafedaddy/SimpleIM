package com.langsin.im.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.langsin.im.model.ToolsCreateMsg;
import com.langsin.im.model.ToolsParseMsg;
import com.langsin.im.msg.IMsgConstance;
import com.langsin.im.msg.MsgHead;
import com.langsin.im.msg.MsgLogin;
import com.langsin.im.msg.MsgLoginResp;
import com.langsin.im.msg.MsgReg;
import com.langsin.im.msg.MsgRegResp;
import com.langsin.im.utils.LogTools;

/**
 * ��ʱͨ��ϵͳ �ͻ��˵�ͨ��ģ��,�ṩ�� 1.��½��ע��ӿڵ��ã� 2.�ڶ����߳��н��շ�������Ϣ 3.�����յ�����Ϣ�ַ�������������
 */
public class ClientConnection extends Thread {

	private static ClientConnection ins;// ���൥ʵ������
	private Socket client; // ����������������
	private java.io.DataOutputStream dous;// ���������
	private java.io.DataInputStream dins;// ����������

	private List<IClientMsgListener> listeners = new ArrayList<IClientMsgListener>();

	/** ����Ҫ��������,���Թ�����˽�� */
	private ClientConnection() {
	}

	// ��ʵ��������ʷ���
	public static ClientConnection getIns() {
		if (null == ins) {
			ins = new ClientConnection();
		}
		return ins;
	}

	/** �����Ϸ�����,�Ƿ�����ɹ� */
	public boolean conn2Server() {
		try {
			// 1.����һ�����������˵�Socket����
			client = new Socket(IMsgConstance.serverIP,
					IMsgConstance.serverPort);
			// 2.�õ��������������
			// 3.��װΪ�ɶ�дԭʼ�������͵����������
			this.dous = new DataOutputStream(client.getOutputStream());
			this.dins = new DataInputStream(client.getInputStream());
			return true;
		} catch (Exception ef) {
			ef.printStackTrace();
		}
		return false;
	}

	/**
	 * 1.��½������
	 * 
	 * @param nikeName
	 *            :�û��س�
	 * @param pwd
	 *            :����
	 * @return: ע���� -1:ʧ�� ����:ע�ᵽ��yk��
	 */
	public int regServer(String nikeName, String pwd) {
		try {
			MsgReg mrg = new MsgReg();
			mrg.setTotalLen(4 + 1 + 4 + 4 + 10 + 10);
			mrg.setType(IMsgConstance.command_reg);
			mrg.setDest(IMsgConstance.Server_yk_NUMBER);
			mrg.setSrc(0);
			mrg.setNikeName(nikeName);
			mrg.setPwd(pwd);
			this.sendMsg(mrg);
			// �����˵�½ע������֮��,�������һ��Ӧ�����Ϣ
			MsgHead loginResp = readFromServer();
			MsgRegResp mr = (MsgRegResp) loginResp;
			if (mr.getState() == 0) {// ע��ɹ�!
				return mr.getDest();
			}
		} catch (Exception ef) {
			ef.printStackTrace();
		}
		return -1;
	}

	/**
	 * ��½������
	 * 
	 * @param ykNum
	 *            :�û�ykNum
	 * @param pwd
	 *            :����
	 * @return: �Ƿ��½�ɹ�
	 */
	public boolean loginServer(int ykNum, String pwd) {
		try {
			MsgLogin ml = new MsgLogin();
			ml.setTotalLen(4 + 1 + 4 + 4 + 10);
			ml.setType(IMsgConstance.command_login);
			ml.setDest(IMsgConstance.Server_yk_NUMBER);
			ml.setSrc(ykNum);
			ml.setPwd(pwd);
			this.sendMsg(ml);
			// �����˵�½����֮��,�������һ��Ӧ�����Ϣ
			MsgHead loginResp = readFromServer();
			MsgLoginResp mlr = (MsgLoginResp) loginResp;
			return mlr.getState() == 0;
		} catch (Exception ef) {
			ef.printStackTrace();
		}
		return false;
	}

	// �߳����ж�ȡ��������������Ϣ�����ַ���������
	public void run() {
		while (true) {
			try {
				// ����һ����Ϣ
				MsgHead m = readFromServer();
				// ����Ϣ�ַ���������ȥ����
				for (IClientMsgListener lis : listeners) {
					lis.fireMsg(m);
				}
			} catch (Exception ef) {
				ef.printStackTrace();
				break; // �����ȡ����,���˳�
			}
		}
		LogTools.INFO(this.getClass(), "�ͻ��˽����̼߳��˳�!");

	}

	/**
	 * ���������϶�ȡһ���������˷�������Ϣ ��������������������ڶ����߳���
	 * 
	 * @return:��ȡ������Ϣ����
	 */
	public MsgHead readFromServer() throws Exception {
		int totalLen = dins.readInt();
		LogTools.INFO(this.getClass(), "�ͻ��˶�����Ϣ�ܳ�Ϊ:" + totalLen);
		byte[] data = new byte[totalLen - 4];
		dins.readFully(data); // ��ȡ���ݿ�
		MsgHead msg = ToolsParseMsg.parseMsg(data);// ���Ϊ��Ϣ����
		LogTools.INFO(this.getClass(), "�ͻ����յ���Ϣ:" + msg);
		return msg;
	}

	/** ����һ����Ϣ���������ķ��� */
	public void sendMsg(MsgHead msg) throws Exception {
		LogTools.INFO(this.getClass(), "�ͻ��˷�����Ϣ:" + msg);
		byte[] data = ToolsCreateMsg.packMsg(msg);// �������Ϊ���ݿ�
		this.dous.write(data);// ����
		this.dous.flush();
	}

	/**
	 * Ϊ����������һ����Ϣ��������������
	 * 
	 * @param l
	 *            :��Ϣ��������������
	 */
	public void addMsgListener(IClientMsgListener l) {
		this.listeners.add(l);
	}

	// �ر���һ���ͻ���������
	public void closeMe() {
		try {
			this.client.close();
		} catch (Exception ef) {
		}
	}
}