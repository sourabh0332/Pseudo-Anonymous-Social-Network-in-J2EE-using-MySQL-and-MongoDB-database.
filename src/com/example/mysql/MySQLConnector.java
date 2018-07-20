package com.example.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import com.mysql.jdbc.Statement;

public class MySQLConnector {
	public static Connection conn = null;
	public MySQLConnector() {
		// TODO Auto-generated constructor stub
	}
	public static Connection dbConnector(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testFB1", "root", "");
			/*
			Statement myst = (Statement) conn.createStatement();
			ResultSet myres = myst.executeQuery("select * from funny;");
			while(myres.next()){
				System.out.println(myres.getString("col1"));
			}
			*/
			//JOptionPane.showMessageDialog(null, "Succesfull Connection");
			return conn;
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, e);
			return null;
		}
		
	}
}
