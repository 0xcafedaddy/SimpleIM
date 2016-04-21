package com.langsin.im.model;

import java.util.ArrayList;
import java.util.List;

/**
 *��ʱͨ��ϵͳ �û��ඨ��
 *һ���û����󣬰������Լ��ĺ��ѷ���������
 */
public class UserInfo implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2375896568861250754L;
	private int ykNum;//��ʶÿһ���û���Ψһʶ���
	private String pwd; //�û�����
	private String nikeName;//�û�����
	//��������û��ķ�������б�
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
 * �����û�����һ���������
 * @param team:һ�����������
 */
 public void addTeams(TeamInfo  team) {
		this.teams.add(team);
	}
//����Ϊgetter/setter����
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
