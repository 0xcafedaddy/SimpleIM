package com.langsin.im.model;

import java.util.ArrayList;
import java.util.List;

/**
 *即时通信系统 用户类定义
 *一个用户对象，包含有自己的好友分组对象队列
 */
public class UserInfo implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2375896568861250754L;
	private int ykNum;//标识每一个用户的唯一识别号
	private String pwd; //用户密码
	private String nikeName;//用户别名
	//属于这个用户的分组对象列表
	private List<TeamInfo> teams=new ArrayList<TeamInfo>();
	
	public UserInfo(int ykNum){
		this.ykNum=ykNum;
	 }
	
	public UserInfo(int ykNum,String nikeName){
		this.ykNum=ykNum;
		this.nikeName=nikeName;
	}
	
    public String toString(){
    	return nikeName.trim()+"("+this.ykNum+")";
    }
    
/**
 * 给此用户加入一个分组对象
 * @param team:一个好友组对象
 */
 public void addTeams(TeamInfo  team) {
		this.teams.add(team);
	}
//以下为getter/setter方法
 public List<TeamInfo> getTeams() {
		return teams;
	}
 
	public int getykNum() {
		return ykNum;
	}
public void setykNum(int ykNum) {
	this.ykNum = ykNum;
}
public String getNikeName() {
	return nikeName.trim();
}

public void setNikeName(String nikeName) {
	this.nikeName = nikeName;
}
public String getPwd() {
	return pwd;
}
public void setPwd(String pwd) {
	this.pwd = pwd;
}
}
