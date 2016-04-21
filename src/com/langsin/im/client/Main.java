package com.langsin.im.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

/**
 * yk��ʱͨ��ϵͳ �ͻ����������� 1.�ͻ����������࣬��ʾ��½���� 2.�����½�ɹ�����ʾ������ 3.�ṩע�Ṧ�ܣ����ע��ɹ������ص�½����
 */
public class Main {
	private JFrame jf_login;// ��½����
	// ��½�����ϵ�yk����������������
	private JFormattedTextField jta_ykNum;
	private javax.swing.JTextField jta_pwd;
	// ȡ���������ĵ�ʵ������
	private ClientConnection conn = ClientConnection.getIns();

	// ��ʾ��½����:
	public void showLoginUI() throws Exception {
		jf_login = new javax.swing.JFrame("���½:");
		java.awt.FlowLayout fl = new java.awt.FlowLayout();
		jf_login.setLayout(fl);
		jf_login.setSize(200, 160);
		// yk�������,��ʽ��Ϊֻ����������
		MaskFormatter mfName = new MaskFormatter("##########");
		jta_ykNum = new JFormattedTextField();
		mfName.install(jta_ykNum);
		jta_ykNum.setColumns(10);
		jta_pwd = new javax.swing.JPasswordField(12);
		jta_pwd.setColumns(10);
		// �û�������ı�ǩ
		JLabel la_name = new JLabel("ID ��:");
		JLabel la_pwd = new JLabel("��  ��:");

		jf_login.add(la_name);
		jf_login.add(jta_ykNum);
		jf_login.add(la_pwd);
		jf_login.add(jta_pwd);

		javax.swing.JButton bu_login = new javax.swing.JButton("Login");
		bu_login.setActionCommand("login");
		javax.swing.JButton bu_Register = new javax.swing.JButton("Register");
		bu_Register.setActionCommand("reg");
		// ��½��ע����¼�������
		ActionListener buttonAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String command = e.getActionCommand();
				if (command.equals("login")) {
					loginAction(); // ִ�е�½����
				}
				if (command.equals("reg")) {
					showRegForm();// ����ע�����
				}
			}
		};
		bu_login.addActionListener(buttonAction);
		bu_Register.addActionListener(buttonAction);

		jf_login.add(bu_login);
		jf_login.add(bu_Register);
		jf_login.setLocationRelativeTo(null);// ����
		jf_login.setVisible(true);
	}

	// ע���¼�����
	private void showRegForm() {
		final JFrame jf_reg = new JFrame("��ע��:");
		java.awt.FlowLayout fl = new java.awt.FlowLayout();
		jf_reg.setLayout(fl);
		jf_reg.setSize(200, 160);
		final JTextField jta_regNikeName = new JTextField(12);
		final JTextField jta_regPwd = new JTextField(12);
		JLabel la_regName = new JLabel("�� ��:");
		JLabel la_regPwd = new JLabel("��  ��:");
		jf_reg.add(la_regName);
		jf_reg.add(jta_regNikeName);
		jf_reg.add(la_regPwd);
		jf_reg.add(jta_regPwd);
		JButton bu_reg = new JButton("Register User");
		jf_reg.add(bu_reg);
		jf_reg.setLocationRelativeTo(null);// ����
		jf_reg.setVisible(true);
		// ע�ᰴť�¼�����������
		ActionListener buttonAction = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ȡ��ע����سƺ�����
				String nikeName = jta_regNikeName.getText().trim();
				String pwd = jta_regPwd.getText().trim();
				String s = "����������ʧ��!";
				if (ClientConnection.getIns().conn2Server()) {// ����������Ϸ�����
					int ykNum = conn.regServer(nikeName, pwd);
					s = "ע��ʧ��,��ʶ��:" + ykNum;
					if (ykNum != -1) {
						s = "ע��ɹ�,���ID��:" + ykNum;
					}
				}
				javax.swing.JOptionPane.showMessageDialog(jf_reg, s);
				conn.closeMe();
				jf_reg.dispose();
			}
		};
		bu_reg.addActionListener(buttonAction);
	}

	// ��½�¼�����
	private void loginAction() {
		// 1.ȡ�������yk�ź�����
		String ykStr = jta_ykNum.getText().trim();
		int ykNum = Integer.parseInt(ykStr);
		String pwd = jta_pwd.getText();
		// 2.�����Ϸ�����
		if (conn.conn2Server()) {// ����������Ϸ�����
			// 3.��½
			if (conn.loginServer(ykNum, pwd)) {
				// 4.��ʾ���������� //��½�ɹ��ˣ�Ҫ�ص���½����
				MainUI mainUI = new MainUI(ykNum);
				mainUI.showMainUI();
				conn.start();// 5.���������߳�
				// 6.���û����Ӹ��������,��Ϊ��Ϣ������
				conn.addMsgListener(mainUI);
				jf_login.dispose();// �رյ�½����
			} else {
				conn.closeMe();
				JOptionPane.showMessageDialog(jf_login, "��½ʧ��,��ȷ���ʺ���ȷ!");
			}
		} else {
			conn.closeMe();
			JOptionPane.showMessageDialog(jf_login, "����ʧ��,��ȷ�Ϸ���������,IP�Ͷ˿���ȷ!");
		}
	}

	// ��������
	public static void main(String[] args) throws Exception {
		Main qu = new Main();
		qu.showLoginUI();
	}
}
