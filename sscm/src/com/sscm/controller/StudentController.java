package com.sscm.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sscm.entity.BarInfo;
import com.sscm.entity.DatatablesViewPage;
import com.sscm.service.DataService;
import com.sscm.service.StudentService;
import com.sscm.entity.Student;
@Controller
public class StudentController {
	
	private static Logger logger = Logger.getLogger(StudentController.class);
	
	@Resource
	private StudentService studentService;
	
	@Resource
	private DataService dataService;
	
	@RequestMapping(value="/admin/queryStudents", method=RequestMethod.GET)
	@ResponseBody
	public DatatablesViewPage<Student> querStudents(HttpServletRequest request,HttpServletResponse response){
		response.reset();
		int start =Integer.parseInt(request.getParameter("start"));    
        int length = Integer.parseInt(request.getParameter("length"));  
        String state = request.getParameter("state");
        List<Student> list = null;
        int num = 0;
        if (state.equals("0")){
        	list = studentService.getStudents(start, length);
        	num = studentService.getStudentsNum();
        } else if (state.equals("1")) {
			list = studentService.getStudentsBySno(request.getParameter("sno"));
			num = 1;
		}else if (state.equals("2")) {
			String sdept = request.getParameter("sdept");
			String sname = request.getParameter("sname");
			String sdate = request.getParameter("sdate");
			String edate = request.getParameter("edate");
			num = studentService.getStudentsByNum(sdept, sname, sdate, edate);
			list = studentService.getStudentsByArg(start, length, sdept, sname, sdate, edate);
		}else {
			return null;
		}
		DatatablesViewPage<Student> view = new DatatablesViewPage<Student>();
		view.setiTotalDisplayRecords(num);
		view.setiTotalRecords(5);
		view.setAaData(list); 
		return view;
	}
	
	@RequestMapping(value="/admin/deleteStudents", method=RequestMethod.POST)
	public void deleteStudents(String sno,HttpServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();
		try {
			if(sno==null||sno.equals("")){
				out.print("false");
			}else {
				studentService.delete(sno);
			}	
		} catch (Exception e) {
			logger.info("deleteStudents �����ˣ�",e);
			out.print("false");
		}
	}
	
	@RequestMapping(value="/admin/updateStudents", method=RequestMethod.POST)
	public void updateStudents(Student student,HttpServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();
		try {
			if(student==null){
				out.print("false");
			}else {
				studentService.update(student);
			}	
		} catch (Exception e) {
			logger.info("updateStudents �����ˣ�",e);
			out.print("false");
		}
	}
	
	@RequestMapping(value="/admin/addStudents", method=RequestMethod.POST)
	public void addStudents(Student student,HttpServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();
		try {
			if(student==null){
				out.print("false");
			}else {
				studentService.add(student);
				out.print("true");
			}	
		}
		catch (DuplicateKeyException e){
			out.print("key");
		}
		catch (Exception e) {
			logger.info("addStudents �����ˣ�",e);
			out.print("false");
		}
	}
	
	@RequestMapping(value="/student/getBar", method=RequestMethod.GET)
	@ResponseBody
	public BarInfo getBar(HttpServletRequest request){
		HttpSession session = request.getSession();
		Student student = (Student) session.getAttribute("student");
		BarInfo bar = dataService.getBar(student.getSname());
		return bar;
	}
	
	@RequestMapping(value="/student/getStudent", method=RequestMethod.GET)
	@ResponseBody
	public Student getStudent(HttpServletRequest request){
		HttpSession session = request.getSession();
		Student student = (Student) session.getAttribute("student");
		student.setPassword("");
		return student;
	}
	
	@RequestMapping(value="/student/updateStudents", method=RequestMethod.POST)
	public void changeStudents(Student student,HttpServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();
		try {
			if(student==null){
				out.print("false");
			}else {
				studentService.update(student);
			}	
		} catch (Exception e) {
			logger.info("changeStudents �����ˣ�",e);
			out.print("false");
		}
	}
	
	@RequestMapping(value="/teacher/queryStudent", method=RequestMethod.GET)
	@ResponseBody
	public DatatablesViewPage<Student> queryStudent(int id,HttpServletResponse response,HttpServletRequest request){
		response.reset();
		try {
			int start =Integer.parseInt(request.getParameter("start"));    
	        int length = Integer.parseInt(request.getParameter("length"));
			DatatablesViewPage<Student> view = studentService.querySelectStudent(id, start, length);
			return view;
		} catch (Exception e) {
			logger.info("changeStudents �����ˣ�",e);
			return null;
		}
	}
	
	@RequestMapping(value="/teacher/getStudentFile", method=RequestMethod.GET)
	public void getStudentFile(int id,HttpServletResponse response,HttpServletRequest request) throws IOException, RowsExceededException, WriteException{
		OutputStream os = response.getOutputStream();// ȡ�������
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=ѧ������.xls");// �趨����ļ�ͷ
        response.setContentType("application/msexcel");// �����������
        WritableWorkbook wbook = Workbook.createWorkbook(os); // ����excel�ļ�
        WritableSheet wsheet = wbook.createSheet("ѧ������", 0); // sheet����
        // ����excel����
        /*
        WritableFont wfont = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD, false,
                                              UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        WritableCellFormat wcfFC = new WritableCellFormat(wfont);
        wcfFC.setBackground(Colour.AQUA);
        wsheet.addCell(new Label(1, 0, "ѧ������", wcfFC));
        wfont = new jxl.write.WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false,
                                           UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        wcfFC = new WritableCellFormat(wfont);*/
        //������
        wsheet.addCell(new Label(0, 0, "ѧ��"));
        wsheet.addCell(new Label(1, 0, "����"));
        wsheet.addCell(new Label(2, 0, "�Ա�"));
        wsheet.addCell(new Label(3, 0, "����"));
        wsheet.addCell(new Label(4, 0, "רҵ"));
        wsheet.addCell(new Label(5, 0, "Ժϵ"));
        List<Student> list = studentService.getStudentFile(id);
        int count = 1;
        for (Student student : list) {
        	wsheet.addCell(new Label(0, count, student.getSno()));
            wsheet.addCell(new Label(1, count, student.getSname()));
            wsheet.addCell(new Label(2, count, student.isSsex()?"��":"Ů"));
            wsheet.addCell(new Label(3, count, String.valueOf(student.getSage())));
            wsheet.addCell(new Label(4, count, student.getMajor()));
            wsheet.addCell(new Label(5, count, student.getDt()));
            count++;
		}
        wbook.write(); // д���ļ�
        wbook.close();
        os.close(); // �ر���
	}
	
	
}
