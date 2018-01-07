package com.sscm.service;

import java.util.List;

import com.sscm.entity.Student;

public interface StudentService {
	public Student login(String username,String password);
	public int getStudentsNum();
	public int getStudentsByNum(String sdept,String sname,String sdate,String edate);
	public List<Student> getStudents(int start,int end);
	public List<Student> getStudentsBySno(String sno);
	public List<Student> getAll();
	public List<Student> getStudentsByArg(int start,int end,String sdept,String sname,String sdate,String edate);
	public void delete(String sno);
	public void update(Student student);
	public void add(Student student);
	public void changepassword(String sno,String password);
}
