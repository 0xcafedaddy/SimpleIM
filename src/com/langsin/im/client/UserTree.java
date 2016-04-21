package com.langsin.im.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.langsin.im.model.TeamInfo;
import com.langsin.im.model.UserInfo;
import com.langsin.im.msg.IMsgConstance;
import com.langsin.im.msg.MsgAddFriendResp;
import com.langsin.im.msg.MsgChatText;
import com.langsin.im.msg.MsgHead;
import com.langsin.im.msg.MsgTeamList;
import com.langsin.im.utils.LogTools;

/**
 * ��ʱͨ��ϵͳ ������������ʾ����/�����б�����νṹ �����������Ϣ
 */
public class UserTree extends javax.swing.JTree {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DefaultMutableTreeNode root;// ���ϵĸ��ڵ�
	private int ykNum;// �û�yk��
	// ȡ���������ĵ�ʵ������
	private ClientConnection conn = ClientConnection.getIns();

	// ������ʱ������yk��
	public UserTree(int ykNum) {
		this.ykNum = ykNum;
		root = new DefaultMutableTreeNode("�ҵĺ���");
		DefaultTreeModel tm = new DefaultTreeModel(root);
		this.setModel(tm);
		// ��������Mouse�¼���������˫������������û�����Ϣ
		this.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {// ˫���¼�
					showSendFrame();// ����������Ϣ��
				}
			}
		});
	}

	/**
	 * ����ͻ��˽��յ�һ����Ϣʱ�Ķ���,�������洰�������� 
	 * 1.��½�ɹ��󣬽��յ����ѷ��� 
	 * 2.���һ�����ѳɹ� 
	 * 3.�ӵ�������Ϣ
	 * 4.�ӵ��ļ�������Ϣ
	 * 
	 * @param m
	 * ���յ�����Ϣ����
	 */
	public void onMsgRecive(MsgHead m) {
		if (m.getType() == IMsgConstance.command_teamList) {// �����б�
			MsgTeamList ms = (MsgTeamList) m;
			List<TeamInfo> teams = ms.getTeamLists();
			for (TeamInfo team : teams) {
				List<UserInfo> users = team.getBudyList();
				for (UserInfo user : users) {
					DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(
							user);
					root.add(newNode);// ���½��Ľڵ���ص�����
				}
			}
		}// ��Ӻ��ѵ�Ӧ��
		else if (m.getType() == IMsgConstance.command_addFriend_Resp) {
			MsgAddFriendResp mf = (MsgAddFriendResp) m;
			UserInfo budy = new UserInfo(mf.getFriendykNum());
			budy.setNikeName(mf.getFriendNikeName());
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(budy);
			root.add(newNode);// ���½��Ľڵ���ص�����
		} else if (m.getType() == IMsgConstance.command_chatText) {
			MsgChatText mt = (MsgChatText) m;
			javax.swing.JOptionPane.showMessageDialog(this, mt.getSrc() + "˵:"
					+ mt.getMsgContent());
		} else if (m.getType() == IMsgConstance.command_chatFile) {
			javax.swing.JOptionPane.showMessageDialog(this, "�յ��ļ�,����ô��?");
		} else if (m.getType() == IMsgConstance.command_onLine) {
			// �˴�Ӧ���������ϵ�ͼ�����
			javax.swing.JOptionPane
					.showMessageDialog(this, m.getSrc() + "������!");
		} else if (m.getType() == IMsgConstance.command_offLine) {
			// �˴�Ӧ���������ϵ�ͼ����
			javax.swing.JOptionPane
					.showMessageDialog(this, m.getSrc() + "������!");
		} else { // ����Ϣ�����װΪ�½��Ľڵ�
			javax.swing.JOptionPane.showMessageDialog(this,
					"ʲô��Ϣ?����:" + m.getType());
		}
		javax.swing.SwingUtilities.updateComponentTreeUI(this);// ˢ�½���
	}

	/* ˫�����ϵĺ��ѽڵ�ʱ��������Ϣ���Ϳ� */
	private void showSendFrame() {
		// �õ�����ѡ�еĽڵ�:
		javax.swing.tree.TreePath tp = this.getSelectionPath();
		if (tp == null) {// δѡ�����ڵ�
			return;
		}
		Object obj = tp.getLastPathComponent();// ȡ��ѡ�еĽڵ�
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
		Object userO = node.getUserObject();// ȡ�ýڵ��ڵĶ���
		if (userO instanceof UserInfo) {// ѡ�е���һ���û��ڵ����
			final UserInfo destUser = (UserInfo) userO;
			// ����������Ϣ��
			final javax.swing.JDialog jda = new javax.swing.JDialog();
			String s = "���:" + destUser.getNikeName() + "("
					+ destUser.getykNum() + ")˵:";
			jda.setTitle(s);
			jda.setSize(200, 150);
			java.awt.FlowLayout fl = new java.awt.FlowLayout();
			jda.setLayout(fl);
			javax.swing.JLabel la = new javax.swing.JLabel("������Ҫ������Ϣ(���س�����):");
			jda.add(la);
			final javax.swing.JTextField jta = new javax.swing.JTextField(15);
			jda.add(jta);
			jda.setVisible(true);
			// ��������а��»س���������:�����¼�������ʵ��
			jta.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String msg = jta.getText();
					MsgChatText mct = new MsgChatText();
					mct.setType(IMsgConstance.command_chatText);
					mct.setTotalLen(4 + 1 + 4 + 4 + msg.getBytes().length);
					mct.setDest(destUser.getykNum());
					mct.setSrc(ykNum);
					mct.setMsgContent(msg);
					try {
						conn.sendMsg(mct);
					} catch (Exception ef) {
						LogTools.ERROR(this.getClass(), "���ͳ���:" + ef);
					}
					jda.dispose();
				}
			});
		}
	}

}
