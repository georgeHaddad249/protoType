package dbconnection;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.net.ssl.SSLException;
public class dbconnections {
	
	static private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	// update USER, PASS and DB URL according to credentials provided by the website:
	static private final String DB = "sql12338488";
	static private final String DB_URL = "jdbc:mysql://sql12.freemysqlhosting.net/"+ DB + "?useSSL=false";
	static private final String USER = "sql12338488";
	static private final String PASS = "sohail05";
	static private Connection conn = null;
	static private Statement stmt = null;
	public dbconnections()
	{
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
		}
		catch (SQLException se) {
			se.printStackTrace();
			System.out.println("SQLException: " + se.getMessage());
            System.out.println("SQLState: " + se.getSQLState());
            System.out.println("VendorError: " + se.getErrorCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void printAllQuestions(HashMap<Integer,ArrayList<String>> questions)
	{
	       questions.forEach((id,question) -> {
	            System.out.println(id + "." + question + " ");
	            });
	}
	public static HashMap<Integer,ArrayList<String>> getAllquestions ()
	{
		HashMap<Integer,ArrayList<String>> questions = new HashMap<Integer,ArrayList<String>>();
		try {
			String sql = "SELECT * FROM questions ";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int id=rs.getInt("id");
				ArrayList<String> question = new ArrayList<String>();
				question.add(0, rs.getString("question"));
				question.add(1, rs.getString("rightanswer"));
				question.add(2,rs.getString("answers1"));
				question.add(3,rs.getString("answers2"));
				question.add(4,rs.getString("answers3"));				
				questions.put(id,question);
				}
			}
			catch (SQLException se) {
				se.printStackTrace();
				System.out.println("SQLException: " + se.getMessage());
	            System.out.println("SQLState: " + se.getSQLState());
	            System.out.println("VendorError: " + se.getErrorCode());
			} catch (Exception e) {
				e.printStackTrace();
		}
		printAllQuestions(questions);
		return questions;
	}
	public static ArrayList<String> showUpdatedquestion(String message)
	{
		ArrayList<String> AllInfo = new ArrayList<String>();
		try {
			int id=Integer.parseInt(message);
			String sql = "SELECT * FROM questions WHERE id = "+id;
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			String ques=rs.getString("question");
		    AllInfo.add(ques);
		    AllInfo.add(rs.getString("rightanswer"));
		    AllInfo.add(rs.getString("answers1"));
		    AllInfo.add(rs.getString("answers2"));
		    AllInfo.add(rs.getString("answers3"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return AllInfo;
	}
	public static void editQuestion(ArrayList<String> newQuestion)
	{
		try { 
		String ques=newQuestion.get(1);
		int msg = Integer.parseInt(newQuestion.get(0));
		String sql = "UPDATE questions SET question = '" + (ques) + "' WHERE id = "+(msg);
		boolean rs = stmt.execute(sql);
		ques=newQuestion.get(2);
		sql = "UPDATE questions SET rightanswer = '" + (ques) + "' WHERE id = "+(msg);
		rs = stmt.execute(sql);
		ques=newQuestion.get(3);
		sql= "UPDATE questions SET answers1 = '" + (ques) + "' WHERE id = "+(msg);
		rs = stmt.execute(sql);
		ques=newQuestion.get(4);
		sql= "UPDATE questions SET answers2 = '" + (ques) + "' WHERE id = "+(msg);
		rs = stmt.execute(sql);
		ques=newQuestion.get(5);
		sql = "UPDATE questions SET answers3 = '" + (ques) + "' WHERE id = "+(msg);
		rs = stmt.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}