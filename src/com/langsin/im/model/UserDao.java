package com.langsin.im.model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.langsin.im.utils.LogTools;


 
/**
 *��ʱͨ��ϵͳ ���ݴ洢ģ��ʵ��
 */
@SuppressWarnings("unchecked")
public class UserDao {
     
	/** ����û��Ƿ��½�ɹ�
	 * @param ykNum:�û�ykNum��
	 * @param pwd:�û�����
	 * @return:�ɹ����ش��û�����ʧ�ܷ���null; */
	public static UserInfo checkLogin(int ykNum,String pwd){
		UserInfo user=userDB.get(ykNum);
		if(null!=user&&user.getPwd().equals(pwd)){
			LogTools.INFO(UserDao.class, "��½�ɹ� ykNum:"+ykNum);
			return user;
		}
		 LogTools.ERROR(UserDao.class, "��½ʧ�� ykNum:"+ykNum);
		return null;
	}
	/** ע��һ���û�����
	 * @param pwd:�û�����
	 * @param nikeName:�û��س�
	 * @return:ע��ɹ����û�ykNum;
	 */
	public static int regUser(String pwd,String nikeName){
		//��������һ��,���������û�ע���yk��
		if(userDB.size()>0){
		 //ȡ��ע��yk�ŵ����ֵ
		 maxykNum=java.util.Collections.max(userDB.keySet());
		}
		 maxykNum++;
		UserInfo user=new UserInfo(maxykNum);
		user.setPwd(pwd);
		user.setNikeName(nikeName);
		//��������û���Ĭ�Ϸ������
		TeamInfo team=new TeamInfo(0,"�ҵĺ���",user);
		user.addTeams(team);//�������;
		userDB.put(maxykNum, user);
		saveDB();//�洢���ļ�
		return maxykNum;
	}
	 
	
	/*** ���������û�
	 * @return:ϵͳ��ע����û���*/
	public static List<UserInfo> findUser(){
		List<UserInfo> lis=new ArrayList<UserInfo>();
		lis.addAll(userDB.values());
		return lis;
	}
	
	/** ��ĳһ���û���Ϊ����,�����Ϊ����
	 * @param srcykNum:������yk��
	 * @param destykNum:��������yk�ţ�
	 * @return:���ӵĺ��Ѷ���
	 */
	public static UserInfo  addFriend(int srcykNum,int destykNum){
		//Ĭ���û�ֻ��һ������
		UserInfo user1=userDB.get(srcykNum);
		UserInfo user2=userDB.get(destykNum);
		//�����ǻ�Ϊ����
		user1.getTeams().get(0).addBudy(user2);
		user2.getTeams().get(0).addBudy(user1);
		saveDB();//�洢���ļ�
		return user2;
	}
	 
	
	/** �����ڴ��е��û����ݱ��ļ���*/
	private static void saveDB(){
		try{
			java.io.OutputStream ous=new java.io.FileOutputStream(dbFileName);
			java.io.ObjectOutputStream oos=new ObjectOutputStream(ous);
			oos.writeObject(userDB);
			oos.flush();
			ous.close(); 
			 LogTools.INFO(UserDao.class, "ˢ�������ļ��ɹ���");
		}catch(Exception ef){
			 LogTools.ERROR(UserDao.class, "ˢ�������ļ�ʧ�ܣ�"+ef);
		}
	}
    
	private static int  maxykNum=1000;//����yk�ŵĻ���
	//�������ݵ��ļ�����
	private static final String dbFileName="langsinJava.dat";
	/**���ڴ��б����û���Ϣ�ı�:keyΪyk�ţ�valueΪ�û�����*/
	private static  Map<Integer,UserInfo> userDB=new HashMap<Integer,UserInfo>();
	
	//��̬�飬���Գ�ʼ���ڴ��е��û���
	static{
		try{
	 java.io.File df=new java.io.File(dbFileName);
	 if(df.exists()&&!df.isDirectory()){//�����ļ����ڣ��Ҳ���Ŀ¼,���ȡ
		java.io.InputStream ins=new java.io.FileInputStream(dbFileName);
		@SuppressWarnings("resource")
		java.io.ObjectInputStream ons=new  ObjectInputStream(ins);
		userDB=(Map<Integer, UserInfo>)ons.readObject();
	 LogTools.INFO(UserDao.class,"�������ļ���ȡ���ݳɹ�!");
	 }else{
	 LogTools.INFO(UserDao.class,"�����������ļ��������ձ�");
	 }
    }catch(Exception ef){
 LogTools.ERROR(UserDao.class,"��ʼ�����ļ�����!"+ef);
	 }
	}
}
