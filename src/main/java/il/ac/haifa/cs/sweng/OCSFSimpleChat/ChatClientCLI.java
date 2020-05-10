package il.ac.haifa.cs.sweng.OCSFSimpleChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import dbconnection.dbconnections;

public class ChatClientCLI {

	private SimpleChatClient client;
	private boolean isRunning;
	private static final String SHELL_STRING = "Enter message (or exit to quit)> ";
	private Thread loopThread;

	public ChatClientCLI(SimpleChatClient client) {
		this.client = client;
		this.isRunning = false;
	}

	public void loop() throws IOException {
		loopThread = new Thread(new Runnable() {
			@Override
			public void run() {
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				String message;
				dbconnections db = new dbconnections();
				while (client.isConnected()) {
					System.out.print(SHELL_STRING);
					try {
						System.out.println("Type show if u want to see the questions,Exit for exit");
						message = reader.readLine();
						if (message.isBlank())
							continue;

						if (message.equalsIgnoreCase("Exit")) {
							System.out.println("Closing connection.");
								client.closeConnection();
						} else if(message.equalsIgnoreCase("show"))
						{
						     	db.getAllquestions();
								System.out.println("Type the number of the question you want to edit or Exit for exit");
								message = reader.readLine();
								if (message.equalsIgnoreCase("Exit")) {
									System.out.println("Closing connection.");
										client.closeConnection();
								}
								else {
									ArrayList<String> newQuestion = new ArrayList<String>();
									String input;
									System.out.print("Type the new question:");
									input=reader.readLine();
									newQuestion.add(input);
									System.out.print("Type the right answer:");
									input=reader.readLine();
									newQuestion.add(input);
									for(int i=0;i<3;i++)
									{
									System.out.print("Type a wrong answer: ");
									input=reader.readLine();
									newQuestion.add(input);	
									}
									db.editQuestion(message,newQuestion);
									System.out.print("Type show if you want to see the updated question or Exit for exit");
									input=reader.readLine();
									if (input.equalsIgnoreCase("Exit")) {
										System.out.println("Closing connection.");
											client.closeConnection();
									}
									else
									{
										db.showUpdatedquestion(message);
									}
								}
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}
		});

		loopThread.start();
		this.isRunning = true;

	}

	public void displayMessage(Object message) {
		if (isRunning) {
			System.out.print("(Interrupted)\n");
		}
		System.out.println("Received message from server: " + message.toString());
		if (isRunning)
			System.out.print(SHELL_STRING);
	}

	public void closeConnection() {
		System.out.println("Connection closed.");
		System.exit(0);
	}
}
