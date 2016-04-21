package com.langsin.im.client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.langsin.im.model.UserInfo;
import com.langsin.im.msg.IMsgConstance;
import com.langsin.im.msg.MsgAddFriend;
import com.langsin.im.msg.MsgFindResp;
import com.langsin.im.msg.MsgHead;

/**
*��ʱͨ��ϵͳ �ͻ���������
* 1.�����������ͨ����������һ����Ϣ��������ʵ����
* IClientMsgListener�ӿ�,������Ϣ����ʱ�����������������Ϣ
* 2.������ͨ�������������ʾ�û��ĺ��ѱ�
* 3.ͨ���˵����ṩ���Һ��ѵĹ���
*/
public class MainUI extends JFrame implements IClientMsgListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int ykNum;//�û���yk��
	private UserTree userTree;//�����б���
	//ȡ���������ĵ�ʵ������
	private ClientConnection conn=ClientConnection.getIns();
	
	//�������������ʱ�������Ӧ��yk�ţ�
	//�����������󣬼ӵ�������
	public MainUI(int ykNum){
		this.ykNum=ykNum;
		userTree=new UserTree(ykNum);
	}
	
	//��ʾ������
	public void showMainUI(){
		this.setTitle("Yk��QQ:"+ykNum);
		java.awt.FlowLayout fl=new java.awt.FlowLayout();
		this.setLayout(fl);
		this.setSize(200,700);
		JMenuBar mb=getMB();//��ӽ����ϵĲ˵���
		this.setJMenuBar(mb);
		this.add(userTree);
		this.setVisible(true);
		this.setDefaultCloseOperation(3);
	}
	
	//ʵ��ͨѶ��Ϣ�������ӿ��еķ�����
	//����ͨѶ��������Ϣ
	public void fireMsg(MsgHead m){
		//����ǲ��ҷ��ص���Ϣ:�������ҽ����
		if(m.getType()==IMsgConstance.command_find_resp){
			MsgFindResp resp=(MsgFindResp)m;
			 List<UserInfo> users=resp.getUsers();//�ҵ����û����
			 showFindResult(users);//�������ҽ����
		}
		else if(m.getType()==IMsgConstance.command_chatText
    		   ||m.getType()==IMsgConstance.command_chatFile
    		   ||m.getType()==IMsgConstance.command_addFriend_Resp
    		   ||m.getType()==IMsgConstance.command_teamList
    		   ||m.getType()==IMsgConstance.command_offLine
    		   ||m.getType()==IMsgConstance.command_onLine){
    	   //��������죬�ļ����Ӻ���Ӧ�𣬺����б���Ϣ�����û���ȥ����
			this.userTree.onMsgRecive(m);
		}else{
			JOptionPane.showMessageDialog(this,"ʲô��Ϣ?"+m.getType());
		}
       
	}
	
	/**ȡ�ý���ķ��в˵��Ĳ˵�������*/
	private javax.swing.JMenuBar getMB(){
	 JMenuBar mb=new  JMenuBar();//����һ���˵�������
	 JMenu me_file=new  JMenu("�ļ�");//�����˵�����
	 JMenuItem mi_find=new  JMenuItem("���Һ���");//���µĲ˵���:
	 mi_find.setActionCommand("find");
	 JMenuItem mi_exit=new  JMenuItem("�˳�");
	 mi_exit.setActionCommand("exit"); 
	 //���˵���ӵ��˵�����
	 me_file.add(mi_find);
	 me_file.add(mi_exit);
		 //�˵���Ŀ���¼�������
    ActionListener al=new  ActionListener(){
			 public void actionPerformed(ActionEvent e){
				 //�����˲˵�ʱ������ͻ����
				 String command=e.getActionCommand();
				 if(command.equals("exit")){
					 System.exit(0);//�˳�
				 }
				 if(command.equals("find")){
					 findAction();//���������û�
				 }
			 }
		 };
		 mi_find.addActionListener(al);
		 mi_exit.addActionListener(al);
		 mb.add(me_file);
		return mb;//���ز˵�������
	}
	
	/**���Ͳ��������û�����Ϣ*/
	private void findAction(){
		try{
		 MsgHead findMsg=new MsgHead();
		 findMsg.setTotalLen(4+1+4+4);
		 findMsg.setType(IMsgConstance.command_find);
		 findMsg.setSrc(ykNum);
		 findMsg.setDest(IMsgConstance.Server_yk_NUMBER);
		 conn.sendMsg(findMsg);
		}catch(Exception ef){
			ef.printStackTrace();
			javax.swing.JOptionPane.showMessageDialog(this,"����ʧ��!");
		}
	}
	
	/**
	 * �����������ز��ҽ��ʱ�������ص��û������б�
	 * ͨ��Jtable��ʾ�ڵ����Ի�����
	 * @param users:���ҷ��ص��û������
	 */
	private void showFindResult(final List<UserInfo> users){
		final javax.swing.JDialog jda=new javax.swing.JDialog();
		jda.setTitle("���Ѳ��ҽ��:");
		jda.setSize(300,400);
		jda.setLayout(new  FlowLayout());
		final JTable table=new JTable();
		UserInfoTableMode model=new UserInfoTableMode(users);
		table.setModel(model);
		//˫��ĳ���û�,���ʾ�������û�
		table.addMouseListener(new java.awt.event.MouseAdapter(){
			 public void mouseClicked(MouseEvent e) {
				 if(e.getClickCount()==2){//�����˫��
					int index= table.getSelectedRow();//�õ�ѡ�е���
					if(index!=-1){//���ѡ���˱��е�һ�У�����Ҫ�ӵĺ���
						UserInfo destU=users.get(index);
						//��Ӻ��ѵ���Ϣ����
						MsgAddFriend ma=new MsgAddFriend();
						ma.setTotalLen(4+1+4+4+4);
						ma.setDest(IMsgConstance.Server_yk_NUMBER);
						ma.setSrc(ykNum);
						ma.setType(IMsgConstance.command_addFriend);
						ma.setFriendykNum(destU.getykNum());
						try{//���ͼӺ��ѵ���Ϣ
						 conn.sendMsg(ma);
						}catch(Exception ef){
							ef.printStackTrace();
						}
					}
				 }
			 }
		});
		jda.add(table);
		jda.setVisible(true);
	}
	

}
