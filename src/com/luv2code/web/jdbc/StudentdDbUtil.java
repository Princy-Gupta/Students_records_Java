package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;



import java.sql.PreparedStatement;




public class StudentdDbUtil 
{
	private   DataSource dataSource;
	
	public StudentdDbUtil(DataSource theDataSource)
	{
		dataSource = theDataSource;
	}
	
	public List<Student> getStudents() throws Exception
	{
		List<Student> students = new ArrayList<>();
		Connection myConnection = null;
		Statement myStatement = null;
		ResultSet myResultSet = null;
		
		try {
		
		// get a connection
			
			myConnection = dataSource.getConnection();		
		// create a sql statement
		String sql = "select * from student order by last_name";
		myStatement = myConnection.createStatement();
		// execute query
		
		myResultSet = myStatement.executeQuery(sql);
		
		//process result set
		
		while(myResultSet.next())
		{
			// retrieve data from result set rpw
			int id = myResultSet.getInt("id");
			String firstName = myResultSet.getString("first_name");
			String lastName = myResultSet.getString("last_name");
			String email = myResultSet.getString("email");
			
			
			// create new student object
			
			Student tempStudent = new Student(id, firstName, lastName, email);
			
			// add it to the list of students
			
			students.add(tempStudent);
		}
		
		// close jdbc objects
			
			
			return students;
		}
		finally 
		{
			//close JDVC objects
			
			close(myConnection,myStatement,myResultSet);
				
			
		}
		
	}

	private void close(Connection myConnection, Statement myStatement, ResultSet myResultSet) 
	{
		// TODO Auto-generated method stub
		
			try 
			{
				if(myResultSet != null)
					myResultSet.close();
				if(myStatement != null)
					myStatement.close();
				if(myConnection != null)
					myConnection.close();
				
			} 
			catch (Exception e)
			{
				// TODO: handle exception
				e.printStackTrace();
			}
		
	}

	public void addStudent(Student theStudent) throws SQLException {
		// TODO Auto-generated method stub
		
		
		Connection myConnection = null;
		PreparedStatement myStatement = null;
		
		try {
			
			myConnection = dataSource.getConnection();
			// create sql for insert
			
			String sql = "insert into student "
							+ "(first_name,last_name,email) "
							+ "values(?,?,?)";
			
			myStatement = myConnection.prepareStatement(sql);
			
			
			// set the param values
			
			 myStatement.setString(1, theStudent.getFirstName());
			 myStatement.setString(2, theStudent.getLastName());
			 myStatement.setString(3, theStudent.getEmail());
			
			// execute sql insert
			
			 myStatement.execute();
		} finally {
			close(myConnection, myStatement, null);
			
		}
		
		
		
		
	}

	

	public  Student getStudent(String theStudentId) throws Exception {
		// TODO Auto-generated method stub
		
		Student theStudent = null;
		
		Connection myConnection = null;
		PreparedStatement myStatement = null;
		ResultSet myResultSet = null;
		
		int studentId;
		
		try 
		{
			// convert student id to int
			
			studentId = Integer.parseInt(theStudentId);
			
			// get connection to database
			
			myConnection = dataSource.getConnection();
			
			//create sql to get selected student
			
			String sql = "select * from student where id=?";
			
			
			//create prepared statement
			myStatement = myConnection.prepareStatement(sql);
			
			//set params
			myStatement.setInt(1, studentId);
			
			// execute statement
			myResultSet = myStatement.executeQuery();
			
			// retrieve data frrom result set row
			if(myResultSet.next())
			{
				String firstName = myResultSet.getString("first_name");
				String lastName = myResultSet.getString("last_name");
				String email = myResultSet.getString("email");
			
					// use the studnet id during constructiion
				
				theStudent = new Student(firstName, lastName, email);
			
			}
			else {
				throw new Exception("Could not find student id: "+studentId);
			}
			
			return theStudent;
		} finally {
			// TODO: handle finally clause
		}
	}

	public void updateStudent(Student theStudent) 
	throws Exception{
		
		Connection myConnection = null;
		PreparedStatement myStatement = null;
		
		try {
		// get db connection
		
		myConnection = dataSource.getConnection();
		
		// create sql update stament
		
		String sql = "update student "
				+ "set first_name=?, last_name=?, email=? "
				+ "where id=?";
		
		//prepare statment
		
		myStatement = myConnection.prepareStatement(sql);
		// set params
		
		myStatement.setString(1, theStudent.getFirstName());
		myStatement.setString(2, theStudent.getLastName());
		myStatement.setString(3, theStudent.getEmail());
		myStatement.setInt(4, theStudent.getId());
		//execute SQL statment
		
		myStatement.execute();
	}
	
	finally {
		// clean up JDBC
		close(myConnection, myStatement, null);
	
	}
	
	}

}
