package com.example.demo;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import java.sql.*;
import com.example.mysql.*;
import javax.swing.JOptionPane;

/**
 * Servlet implementation class Main
 */
@WebServlet("/")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public static Connection conn = null;
	/**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at [post]: ").append(request.getContextPath());
		String email = null, password = null, userID = null;
		email = request.getParameter("email");
		password = request.getParameter("password");
		//MongoDBConnector.MongoConnector();
		try{
			conn = MySQLConnector.dbConnector();
			Statement queryStmt = (Statement) conn.createStatement();
			String query = "SELECT * FROM `loginDetails` WHERE email='" + email + "' AND password='"
					+ password + "'; ";
			ResultSet queryRes = queryStmt.executeQuery(query);
			/*
			while(queryRes.next()){
				System.out.println(queryRes.getString("col1"));
			}
			*/
			boolean userExists = false;
			if(queryRes.next()){ // checks if query resulted in any result ? if yes, successfull login
				userID = queryRes.getString("userID");
				userExists = true;
			}
			if(userExists){ // login successfull
				HttpSession session;
				session=request.getSession(true);
				session.setAttribute("userID", userID);
				query = "SELECT * FROM `userDetails` WHERE userID='" + userID + "'";
				queryRes = queryStmt.executeQuery(query);
				String firstName = null, lastName = null, college = null, msgCount = null;
				while(queryRes.next()){
					firstName = queryRes.getString("firstName");
					lastName = queryRes.getString("lastName");
					college = queryRes.getString("college");
					msgCount = queryRes.getString("msgCount");
				}
				// wrapped in session bcoz if user explicitly goes to home.jsp these values are lost as they can only be passes from here, if passed from request.setAttribute(...);
				session.setAttribute("firstName", firstName);
				session.setAttribute("lastName", lastName);
				session.setAttribute("college", college);
				session.setAttribute("msgCount", msgCount);
				//response.sendRedirect("home.jsp");
				getServletContext().getRequestDispatcher("/home.jsp").forward(request, response);
			}
			else{ // login unsuccessfull
				JOptionPane.showMessageDialog(null, "Login Failure");
				getServletContext().getRequestDispatcher("/login.jsp").forward(request, response);
			}
		}
		catch(Exception ex){
			JOptionPane.showMessageDialog(null, ex);
			System.out.println("some exception");
		}
	}

}
