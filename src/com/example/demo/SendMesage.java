package com.example.demo;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.mysql.*;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

/**
 * Servlet implementation class SendMesage
 */
@WebServlet("/sendMesage")
public class SendMesage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendMesage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try{
			String recieverID = request.getParameter("recieverID");
			String message = request.getParameter("message");
		
			DB db = null;
			if(MongoDBConnector.db == null){
				db = MongoDBConnector.connect();
			}
			else db = MongoDBConnector.db;
			System.out.println("[G+] Details : Message-" + message + ", RecieverID-" + recieverID);
			
			HttpSession session = request.getSession(false);
			String userID = (String) session.getAttribute("userID");
			Boolean reciepientActive = true;
			Date curTime = new Date();
			String timeStamp = curTime.toString();
			String msgShown = "false";
			String msgRead = "false";
		
			DBCollection msgCollection1 = db.getCollection("message_" + recieverID);
			DBCollection msgCollection2 = db.getCollection("message_" + userID);
			DBCollection msgCacheCollection = db.getCollection("messageCache_" + recieverID);
			
			message.replace("'", "\'");
			sendMessage(false, userID, message, msgRead, timeStamp, msgCollection1);
			msgRead = "true";
			sendMessage(true, recieverID, message, msgRead, timeStamp, msgCollection2);
		
			System.out.println("G Message sent! => " + message + "\n My user ID = " + userID);
		}
		catch(Exception e){
			System.out.println("[+] Error : " + e);
		}
		finally{
			response.getWriter().append("Sent!").append(request.getContextPath());
			getServletContext().getRequestDispatcher("/home.jsp").forward(request, response);
		}
		//response.getWriter().append(json).append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("deprecation")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	private void sendMessage(boolean same, String from, String message, String read, String timeStamp, DBCollection msgCollection){
		BasicDBObject query = new BasicDBObject();
		query.put("userID", from);
		//query.put("read", read);
		BasicDBObject messages = new BasicDBObject();
		messages.put("message", message);
		if(same)messages.put("origin", "me");
		else messages.put("origin", "not_me");
		BasicDBObject update = new BasicDBObject();
		if(same)update.append("$set", new BasicDBObject("read", "true"));
		else update.append("$set", new BasicDBObject("read", "false"));
		update.put("$push", new BasicDBObject("messages", messages)); // ### it's pushing at last of array which is fine! but i want at begining
		msgCollection.update(query, update, true, true);
	}
}
