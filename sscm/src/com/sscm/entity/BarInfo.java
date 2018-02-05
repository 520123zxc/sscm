package com.sscm.entity;

public class BarInfo {
	private String name;
	private int year;
	private boolean term;
	private String info;
	public BarInfo(String name,int year,boolean term){
		setName(name);
		setTerm(term);
		setYear(year);
		setInfo(String.format("%d ѧ�� - %d ѧ�� %sѧ��", year, year+1, term?"��":"��"));
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public boolean isTerm() {
		return term;
	}
	public void setTerm(boolean term) {
		this.term = term;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	@Override
	public String toString() {
		return "BarInfo [name=" + name + ", year=" + year + ", term=" + term
				+ ", info=" + info + "]";
	}
	
	
}
