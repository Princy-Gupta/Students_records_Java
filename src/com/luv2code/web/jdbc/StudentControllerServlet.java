package com.luv2code.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
		
	private  StudentdDbUtil studentdDbUtil;
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	
	
	
	@Override
	public void init() throws ServletException
	{
		
		super.init();
		
		// create our student db util..... and pass in the conn pool / datasource
		
		try 
		{
			 studentdDbUtil = new StudentdDbUtil(dataSource);
		}
		catch (Exception e) 
		{
			// TODO: handle exception
			throw new ServletException(e);
		}
	}



	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// list the students .. in mvc fashion
		try {
			
			//read thye command
			String theCommand = request.getParameter("command");
			
			// if the command is missing, the default to listning students
			
			if(theCommand == null)
			{
				theCommand = "LIST";
			}
			
			//route to appropriate method
			
			switch(theCommand)
			{
			case "LIST":
				listStudents(request, response);
				break;
				
			case "ADD":
				addStudent(request, response);
				break;
			case "LOAD":
				loadStudent(request,response);
				break;
			case "UPDATE":
				updateStudent(request,response);
				break;
			default:
				listStudents(request, response);
				
				
				
			}
			
			
			
			
			//listStudents(request,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new ServletException(e);
		}
		
	}



	private void updateStudent(HttpServletRequest request, HttpServletResponse response) 
	throws Exception{
				
		// read student info
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		
		//create a new student object
		
		Student theStudent = new Student(id, firstName, lastName, email);
		
		
		//perform update on database
		
		studentdDbUtil.updateStudent(theStudent);
		
		// send them back to the list students page
		
		listStudents(request, response);
	}



	private void loadStudent(HttpServletRequest request, HttpServletResponse response)throws Exception{
		// TODO Auto-generated method stub
		
		//read student id from form data
		String theStudentId = request.getParameter("studentId");
		
		
		
		//get student from databse
		
		Student theStudent = studentdDbUtil.getStudent(theStudentId);
		
		//place student in the request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		
		//send to jsp page 
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
		
		
	}



	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		// read student info from form data
		
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email= request.getParameter("email");
		
		// create new student object
		
		Student theStudent = new Student(firstName, lastName, email);
		 
		
		// add the student to database
		
		studentdDbUtil.addStudent(theStudent);
		
		// send back to main page
		
		listStudents(request, response);
		
		
	}



	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// get students from db util
		List<Student> students = studentdDbUtil.getStudents();
		
		
		// add students to the request
		request.setAttribute("STUDENT_LIST", students);
		
		// send to jsp page(view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
		
	}

}
