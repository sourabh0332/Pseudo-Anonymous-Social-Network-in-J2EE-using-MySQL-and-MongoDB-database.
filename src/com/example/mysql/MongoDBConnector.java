package com.example.mysql;

import java.util.Arrays;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnector {

	public static MongoClient mongo = null;
	public static DB db = null;
	public void MongoDBConnector(String username, String database, String password, String host, int port) {
		// TODO Auto-generated constructor stub
		//MongoCredential credential = new MongoCredential.createCredential(username, database, password.toCharArray());
		//mongo = new MongoClient(new ServerAddress(host, port), Arrays.asList(credential));
	}
	public static DB connect(){
		try{
		MongoCredential credential = null;
		//credential.createCredential("mongo", "chatTest", "".toCharArray());
		//mongo = new MongoClient(new ServerAddress("127.0.0.1", 27017), Arrays.asList(credential));
		mongo = new MongoClient(new ServerAddress("127.0.0.1", 27017));
		//MongoDatabase database = connMongo.getDatabase("test");
		db = mongo.getDB("chatTest");
		System.out.println("Success!");
		}
		catch (Exception e){
			System.out.println(e);
		}
		return db;
	}

}
