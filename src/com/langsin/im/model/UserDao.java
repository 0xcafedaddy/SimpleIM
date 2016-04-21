package com.langsin.im.model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.langsin.im.utils.LogTools;


 
/**
 *即时通信系统 数据存储模块实现
 */
@SuppressWarnings("unchecked")
public class UserDao {
     
	/** 检查用户是否登陆成功
	 * @param ykNum:用户ykNum号
	 * @param pwd:用户密码
	 * @return:成功返回此用户对象，失败返回null; */
	public static UserInfo checkLogin(int ykNum,String pwd){
		UserInfo user=userDB.get(ykNum);
		if(null!=user&&user.getPwd().equals(pwd)){
			LogTools.INFO(UserDao.class, "登陆成功 ykNum:"+ykNum);
			return user;
		}
		 LogTools.ERROR(UserDao.class, "登陆失败 ykNum:"+ykNum);
		return null;
	}
	/** 注册一个用户对象
	 * @param pwd:用户密码
	 * @param nikeName:用户呢称
	 * @return:注册成功的用户ykNum;
	 */
	public static int regUser(String pwd,String nikeName){
		//号码增加一个,用以生成用户注册的yk号
		if(userDB.size()>0){
		 //取得注册yk号的最大值
		 maxykNum=java.util.Collections.max(userDB.keySet());
		}
		 maxykNum++;
		UserInfo user=new UserInfo(maxykNum);
		user.setPwd(pwd);
		user.setNikeName(nikeName);
		//创建这个用户的默认分组对象
		TeamInfo team=new TeamInfo(0,"我的好友",user);
		user.addTeams(team);//加入分组;
		userDB.put(maxykNum, user);
		saveDB();//存储到文件
		return maxykNum;
	}
	 
	
	/*** 查找所有用户
	 * @return:系统中注册的用户表*/
	public static List<UserInfo> findUser(){
		List<UserInfo> lis=new ArrayList<UserInfo>();
		lis.addAll(userDB.values());
		return lis;
	}
	
	/** 将某一个用户加为好友,互相加为好友
	 * @param srcykNum:请求者yk号
	 * @param destykNum:被请求者yk号，
	 * @return:被加的好友对象
	 */
	public static UserInfo  addFriend(int srcykNum,int destykNum){
		//默认用户只有一个分组
		UserInfo user1=userDB.get(srcykNum);
		UserInfo user2=userDB.get(destykNum);
		//必须是互为好友
		user1.getTeams().get(0).addBudy(user2);
		user2.getTeams().get(0).addBudy(user1);
		saveDB();//存储到文件
		return user2;
	}
	 
	
	/** 保存内存中的用户数据表到文件中*/
	private static void saveDB(){
		try{
			java.io.OutputStream ous=new java.io.FileOutputStream(dbFileName);
			java.io.ObjectOutputStream oos=new ObjectOutputStream(ous);
			oos.writeObject(userDB);
			oos.flush();
			ous.close(); 
			 LogTools.INFO(UserDao.class, "刷新数据文件成功！");
		}catch(Exception ef){
			 LogTools.ERROR(UserDao.class, "刷新数据文件失败！"+ef);
		}
	}
    
	private static int  maxykNum=1000;//申请yk号的基数
	//保存数据的文件名字
	private static final String dbFileName="langsinJava.dat";
	/**在内存中保存用户信息的表:key为yk号，value为用户对象*/
	private static  Map<Integer,UserInfo> userDB=new HashMap<Integer,UserInfo>();
	
	//静态块，用以初始化内存中的用户表
	static{
		try{
	 java.io.File df=new java.io.File(dbFileName);
	 if(df.exists()&&!df.isDirectory()){//数据文件存在，且不是目录,则读取
		java.io.InputStream ins=new java.io.FileInputStream(dbFileName);
		@SuppressWarnings("resource")
		java.io.ObjectInputStream ons=new  ObjectInputStream(ins);
		userDB=(Map<Integer, UserInfo>)ons.readObject();
	 LogTools.INFO(UserDao.class,"从数据文件读取数据成功!");
	 }else{
	 LogTools.INFO(UserDao.class,"不存在数据文件，创建空表");
	 }
    }catch(Exception ef){
 LogTools.ERROR(UserDao.class,"初始数据文件出错!"+ef);
	 }
	}
}
