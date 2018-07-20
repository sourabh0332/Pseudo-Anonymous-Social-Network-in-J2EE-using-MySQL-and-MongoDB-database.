package com.example.demo;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.example.mysql.*;

/**
 * Servlet implementation class CheckNewMessage
 */
@WebServlet("/checkNewMessage")
public class CheckNewMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckNewMessage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//System.out.println("hi ");
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		DB db = null;
		if(MongoDBConnector.db == null){
			db = MongoDBConnector.connect();
		}
		else db = MongoDBConnector.db;
		HttpSession session = request.getSession(false);
		String userID = (String) session.getAttribute("userID");
		DBCollection msgCollection = db.getCollection("message_" + userID);
		
		BasicDBObject query = new BasicDBObject("read", "false");
		DBCursor result = msgCollection.find(query);
		System.out.println("[+G] Here are your new messages : ");
		String messages = null, finalMsg = null;
		while(result.hasNext()){
			messages = result.next().toString();
			System.out.println(messages);
			if(messages == "null")continue;
			if(messages != null){
				String msg = null;
				for(int i = 0; i < messages.length() - 3; i++){
					if(messages.charAt(i) == 'a' && messages.charAt(i+1) == 'g' && messages.charAt(i+2) == 'e' && messages.charAt(i+3) == '"'){
						i = i + 4;
						while(messages.charAt(i) != '"')i++;
						i += 1;
						for(int j = i; ;j++){
							if(messages.charAt(j) == '"')break;
							msg += messages.charAt(j);
						}
					}
				}
				msg += "\n\n";
				finalMsg += (msg);
				System.out.println(msg);
			}
		}
		request.setAttribute("messages", finalMsg);
		response.getWriter().append(finalMsg).append(request.getContextPath());
		//getServletContext().getRequestDispatcher("/home.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		DB db = null;
		if(MongoDBConnector.db == null){
			db = MongoDBConnector.connect();
		}
		else db = MongoDBConnector.db;
		HttpSession session = request.getSession(false);
		String userID = (String) session.getAttribute("userID");
		BasicDBObject query = new BasicDBObject();
		query.put("msgShown", "false");
		DBCollection collection = db.getCollection("message_" + userID); // collection==table
		DBCursor result = collection.find(query);
		System.out.println("[+] Here are your new messages : ");
		String messages = null;
		while(result.hasNext()){
			messages += result.next().toString();
			System.out.println(messages);
		}
		request.setAttribute("messages", messages);
		getServletContext().getRequestDispatcher("home.jsp").forward(request, response);
	}

}
