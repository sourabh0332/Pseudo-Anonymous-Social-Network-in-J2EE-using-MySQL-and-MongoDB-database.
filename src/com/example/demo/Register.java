package com.example.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.example.mysql.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import com.mysql.jdbc.Connection;

/**
 * Servlet implementation class Register
 */
@WebServlet("/register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
    static final long MOD = 1000000000000007L;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		getServletContext().getRequestDispatcher("/register.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		/*
		When user Registers following happens : 
		 1.) Handle if emailID or userID clashes.
		 2.) Insert into tables -> `loginDetails`, `userDetails` (can user transaction here) and then redirect to login.jsp
		 3.) Create users Message table.
		*/
		String firstName = null, lastName = null, college = null, msgCount = "0", userID = null,
				password = null, email = null;
		byte[] salt = (firstName + lastName + email + password + 
				(String.valueOf((long)(Math.random()*Math.random()) % MOD))).toString().getBytes("UTF-8");
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(salt);
			userID = thedigest.toString();
			
			if(MySQLConnector.conn == null){
				MySQLConnector.conn = MySQLConnector.dbConnector();
			}
			Connection conn = (Connection) MySQLConnector.conn;
			firstName = request.getParameter("firstName");
			lastName = request.getParameter("lastName");
			email = request.getParameter("email");
			password = request.getParameter("password");
			college = request.getParameter("college");
			String query = "INSERT INTO `loginDetails` ( `email`, `password`, `userID` ) VALUES ( '" + 
					email +"', '"+ password +"', '"+ userID +"' );";
			Statement queryStmt = (Statement) conn.createStatement();
			queryStmt.execute(query);
			
			query = "INSERT INTO `userDetails` ( `lastname`, `firstName`, `college`, `email`, `msgCount`, `userID` )"
					+ " VALUES ( '" + lastName + "', '"+ firstName + "', '" + college + "', '" + email + "', '" + msgCount + "', '"
					+ userID + "'); " ;
			queryStmt = (Statement) conn.createStatement();
			queryStmt.execute(query);
			
			DB db = null;
			if(MongoDBConnector.db == null){
				db = MongoDBConnector.connect();
			}
			else db = MongoDBConnector.db;
			DBCollection msgCollection = db.getCollection("message_" + userID);
			DBCollection msgCacheCollection = db.getCollection("messageCache_" + userID);
			//PrintWriter out = response.getWriter();
			//out.println("<script language='JavaScript'>alert('Registratoin Complete! Will be redirected to Login page.');</script>");
			getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
		}
		catch (NoSuchAlgorithmException | SQLException e) {
			e.printStackTrace();
			System.out.println("[+] Error : " + e);
			System.out.println("Might be (email ID or userID) clash.");
		}
		
	}

}
