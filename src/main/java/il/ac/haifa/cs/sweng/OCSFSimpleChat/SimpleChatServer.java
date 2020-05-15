package il.ac.haifa.cs.sweng.OCSFSimpleChat;
import dbconnection.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import il.ac.haifa.cs.sweng.OCSFSimpleChat.ocsf.server.AbstractServer;
import il.ac.haifa.cs.sweng.OCSFSimpleChat.ocsf.server.ConnectionToClient;
import il.ac.haifa.cs.sweng.OCSFSimpleChat.ocsf.server.globally;

public class SimpleChatServer extends AbstractServer {

	public SimpleChatServer(int port) {
		super(port);
	}

   public static void printAllQuestions(HashMap<Integer,ArrayList<String>> questions,ConnectionToClient client)
		{
		       questions.forEach((id,question) -> {	    	   
		    	   try {
					client.sendToClient(id + "." + question + " ");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	   
		            });
		}
   
	ArrayList<String> newQuestion = new ArrayList<String>();
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		   ArrayList<String> UpdatedQuestion = new ArrayList<String>();
		   dbconnections db = new dbconnections();
		   HashMap<Integer,ArrayList<String>> questions;
		   String msgg;
		     if(msg.toString().equals("show"))
			{
			     	questions=db.getAllquestions();
			     	printAllQuestions(questions,client);
			     	try {
						client.sendToClient("Type # and the number of the question you want to edit or Exit for exit");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		     if(msg.toString().startsWith("#")) {
						newQuestion.clear();
		    	 		msgg = msg.toString().replace("#","");
		    	 		globally.setQues(msgg);
		    	 		newQuestion.add(msgg);
		    	 		try {
							client.sendToClient("Type 'the new question is:(TYPE HERE THE QUESTION)'");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		     }
			 if(msg.toString().startsWith("the new question is:")) {
				 		msgg = msg.toString().replace("the new question is:","");
						newQuestion.add(msgg);
						try {
							client.sendToClient("Type 'the right answer is:(TYPE HERE THE ANSWER)'");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			 }
			 if(msg.toString().startsWith("the right answer is:")) {
				 		msgg = msg.toString().replace("the right answer is:","");
						newQuestion.add(msgg);
						try {
							client.sendToClient("Type 'the first wrong answer is:(TYPE HERE YOUR ANSWER)'");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			 }
			 if (msg.toString().startsWith("the first wrong answer is:"))
			 {
				 		msgg = msg.toString().replace("the first wrong answer:","");
						newQuestion.add(msgg);	
						try {
							client.sendToClient("Type 'the second wrong answer is:(TYPE HERE YOUR ANSWER)'");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			 }
			 if (msg.toString().startsWith("the second wrong answer is:")){
			 		msgg = msg.toString().replace("the second wrong answer is:","");
					newQuestion.add(msgg);	
					try {
						client.sendToClient("Type 'the third wrong answer is:(TYPE HERE YOUR ANSWER)'");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		     }
			 if (msg.toString().startsWith("the third wrong answer is:")){
			 		msgg = msg.toString().replace("the third wrong answer:","");
					newQuestion.add(msgg);	
					db.editQuestion(newQuestion);
		     }
			 
			 try {
				client.sendToClient("Type 'show the updated question' if you want to see the updated question or Exit for exit");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 if (msg.toString().startsWith("show the updated question")){
				 UpdatedQuestion=db.showUpdatedquestion(globally.getQues());
				 try {
					client.sendToClient("The updated question is: "+UpdatedQuestion.get(0) +" \nthe right answer is:"+UpdatedQuestion.get(1)+" \nthe other answers are: "+UpdatedQuestion.get(2)+","+(UpdatedQuestion.get(3))+","+UpdatedQuestion.get(4)+".\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		     }
		
	  }
	@Override
	protected synchronized void clientDisconnected(ConnectionToClient client) {
		// TODO Auto-generated method stub
		
		System.out.println("Client Disconnected.");
		super.clientDisconnected(client);
	}
	@Override
	protected void clientConnected(ConnectionToClient client) {
		super.clientConnected(client);
		System.out.println("Client connected: " + client.getInetAddress());
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Required argument: <port>");
		} else {
			SimpleChatServer server = new SimpleChatServer(Integer.parseInt(args[0]));
			server.listen();
		}
	}
}
