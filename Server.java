/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sameer
 * Name: Sameer Moses Murala
 * Student ID: sxm6494
 * References:
•	https://www.dreamincode.net/forums/topic/259777-a-simple-chat-program-with-clientserver-gui-optional/
•	https://www.geeksforgeeks.org/multi-threaded-chat-application-set-1/
•	https://www.geeksforgeeks.org/multi-threaded-chat-application-set-2/
*   https://www.geeksforgeeks.org/remove-element-arraylist-java/
*   https://www.geeksforgeeks.org/randomly-select-items-from-a-list-in-java/
*   https://www.geeksforgeeks.org/generating-random-numbers-in-java/
*   https://www.javatpoint.com/java-string-to-int
*   https://stackoverflow.com/questions/1795808/and-and-or-in-if-statements
*   https://stackoverflow.com/questions/24104313/how-do-i-make-a-delay-in-java
*   https://stackoverflow.com/questions/7190618/most-efficient-way-to-check-if-a-file-is-empty-in-java-on-windows 
*   https://stackoverflow.com/questions/4614227/how-to-add-a-new-line-of-text-to-an-existing-file-in-java
*   https://www.geeksforgeeks.org/java-appending-string-file/
*   https://stackoverflow.com/questions/14721397/checking-if-a-string-is-empty-or-null-in-java/14721414
*   https://stackoverflow.com/questions/6994518/how-to-delete-the-content-of-text-file-without-deleting-itself
* 
 */
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
	// Id created for each connection
	private static int uniqueId;
	// Array List  to keep track of the active clients
	private ArrayList<ClientThread> al;
	// ServerGUI Object
	private ServerGUI sg;
        // ClientGUI Object
        //private ClientGUI cg1;
	// time in standard format
	private SimpleDateFormat sdf;
	// the port number to listen for connection
	private int port;
	// the boolean that will be turned of to stop the server
	private boolean keepGoing;
        //Constructor
        // the Username of the Client taken from the user
	//String username;
	public Server(int port) {
		this(port, null);
	}
	//Constructor for GUI
	public Server(int port, ServerGUI sg) {
            //Server GUI
		this.sg = sg;
		//port number
		this.port = port;
		// Display time in standard format
		sdf = new SimpleDateFormat("HH:mm:ss");
		// Array List  to keep track of the active clients
		al = new ArrayList<ClientThread>();
	}
	//to Start the Server
	public void start() {
            //Boolean that is a condition to keep the server running 
            //If it is false the infinte loop breaks
		keepGoing = true;
                //Create a new server
		try 
		{
			// the socket created for the server
			ServerSocket serverSocket = new ServerSocket(port);

			// Run loop forever and wait for connections
			while(keepGoing) 
			{
				display("Server waiting for Clients on port " + port + ".");
				// accept connection from the client
				Socket socket = serverSocket.accept();  	
				// if stop button is pressed break the loop
				if(!keepGoing)
					break;
                                //for every client connection make a new thread for it
				ClientThread t = new ClientThread(socket);
                                //Add and start each client connection thread
				al.add(t);									// save it in the ArrayList
				t.start();
			}
		            //After pressing the stop button
                            //Stopping the server 
			try {
                            //Close the server
				serverSocket.close();
                               //Get each thread from the client thread and close each thread
				for(int i = 0; i < al.size(); ++i) {
					ClientThread tc = al.get(i);
					try {
					tc.sInput.close();
					tc.sOutput.close();
					tc.socket.close();
					}
					catch(IOException ioE) {
					}
				}
			}
                        //Exception Handler
			catch(Exception e) {
				display("Exception closing the server and clients: " + e);
			}
		}
		// //Exception Handler
		catch (IOException e) {
            String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
            //Display error message on the server GUI
			display(msg);
		}
	}		
    //Stop method when the stop button pressed on the GUI
	protected void stop() {
            //Boolean that breaks the Server running loop
		keepGoing = false;
		try {
			new Socket("localhost", port);
		}
		catch(Exception e) {
		}
	}
	//Display a list of all the active clients on the GUI
        //Inputs: None Outputs:Returns the list of all active clients
        public void activeclients()
        {
            String concatenatestring="";
            //Counter to get all the clients in the client array list
            for(int i = 0; i < al.size(); ++i) {
						ClientThread ct = al.get(i);
                                                concatenatestring=concatenatestring+"\n"+ct.username;
						//sg.appendClient(ct.username+"\n");
					}
            //Returns the list of all active clients
            sg.appendClient(concatenatestring);
        }
        //Display any event on the GUI
        //Inputs: Message Outputs:Message on the GUI
	private void display(String msg) {
            //Gets the system date and Time
		String time = sdf.format(new Date()) + " " +"\n"+ msg;
		if(sg == null)
			System.out.println(time);
		else
                    //Displays on the Server GUI
			sg.appendEvent(time + "\n");
	}
        //Broadcast message to every active client connected to the server
        //Inputs: Message Outputs:Message to all the active Clients
	private synchronized void broadcast(String message) {
		//Add time stamp to the message
		String time = sdf.format(new Date());
		String messageLf = time + " " + message + "\n";
		// display POST message on Server GUI
			sg.appendRoom(messageLf);     
		//Loop through all the connected clients and send the message
                //If any client not able to send message(not connected) then remove the client 
                String paragraph=message;
                 String[] decodedMessage1string=null;
                 String decodedMessage2;
                 String decodedMessage1;
                 String decodedMessage;
                 decodedMessage1string=message.split("POST",2);
                 decodedMessage1=decodedMessage1string[0];
                 decodedMessage2=message.substring(message.lastIndexOf(".")+2);
                 decodedMessage=decodedMessage1+decodedMessage2;
                 String[] decodedMessageFinalString=decodedMessage.split(":",2);
                 String decodedMessageFinal=decodedMessageFinalString[1];
                 Date dNow = new Date( );
                 SimpleDateFormat ft = new SimpleDateFormat ("E, yyyy.MM.dd  hh:mm:ss a zzz");
                 //The GET HTTP Message
                 String Postdata="GET /path/script.cgi HTTP/1.0"+"\n"+"Host:"+"127.0.0.1"+"\n"+"User Agent:"+"Socket Client Application"+"\n"+"ContentType:text/plain"+"\n"+"Content-Length:"+decodedMessageFinal.length()+"\n"+"Date:"+ft.format(dNow)+"."+"\n"+decodedMessageFinal;
                 // Counter that goes through client array list
		for(int i = al.size(); --i >= 0;) {
			ClientThread ct = al.get(i);
                        //Display Get Message on the Server
                        sg.appendRoom(ct.username+":"+Postdata+"\n");
                        // remove client if cannot send message to it
			if(!ct.writeMsg(messageLf)) {
				al.remove(i);
				display("Disconnected Client " + ct.username + " removed from list.");
			}
		}
	}
        //Send a message to the single client selected by the user 
        //Inputs:Message Outputs:Sends the message to the Selected client
private synchronized void singlemessage(String message) {
		// add timestamp to the message
                String message1=message.substring(message.lastIndexOf("\n"));
                String[] clientnameseperate=message1.split(";");
                String clienttosend=clientnameseperate[1];
                String Message1=clientnameseperate[0];
		String time = sdf.format(new Date());
                String clientnameremovedmessage=message.replace(";"+clienttosend,"");
		String messageLf = time + " " + clientnameremovedmessage + "\n";
                //Append every event to the server GUI the POST Message
		 sg.appendRoom(messageLf);     
                 String paragraph=message;
                 String[] decodedMessage1string=null;
                 String decodedMessage2;
                 String decodedMessage1;
                 String decodedMessage;
                 decodedMessage1string=message.split("POST",2);
                 decodedMessage1=decodedMessage1string[0];
                 decodedMessage2=message.substring(message.lastIndexOf(".")+2);
                 decodedMessage=decodedMessage1+decodedMessage2;
                 String[] decodedMessageFinalString=decodedMessage.split(":",2);
                 String decodedMessageFinal=decodedMessageFinalString[1];
                 String[] decodedMessageFinalString2=decodedMessageFinal.split(";",2);
                 String decodedMessageFinal2=decodedMessageFinalString2[0];
                 Date dNow = new Date( );
                 SimpleDateFormat ft = new SimpleDateFormat ("E, yyyy.MM.dd  hh:mm:ss a zzz");
                 //The GET MEssage
                 String Postdata="GET /path/script.cgi HTTP/1.0"+"\n"+"Host:"+"127.0.0.1"+"\n"+"User Agent:"+"Socket Client Application"+"\n"+"ContentType:text/plain"+"\n"+"Content-Length:"+decodedMessageFinal2.length()+"\n"+"Date:"+ft.format(dNow)+"."+"\n"+decodedMessageFinal2;
                //Loop through every client in the list to get the selected client  
                //Counter to get throught every client in the client array list
		for(int i = al.size(); --i >= 0;) {
			ClientThread ct = al.get(i);
			//If it is equal to the selected client
                        if(ct.username.equals(clienttosend))
                        {
                            sg.appendRoom(ct.username+":"+Postdata+"\n");
                            // remove client if failed to recieve the meesage
			if(!ct.writeMsg(messageLf)) {
				al.remove(i);
				display("Disconnected Client " + ct.username + " removed from list.");
			}
                        }
		}
	}
//method to send the timer message
private synchronized void sendTimerMessage(String message) {
    //int clientNumber=al.size();
    //Random rand = new Random(); 
    //int random_client = rand.nextInt(clientNumber+1);
    //integer to store the client sendingthe timer message
    int client_sending_timer=0;
    //string array to store the client not to send the timer message 
    //that is the client sending the timer message
    String[] usernamesplitstring=null;
    //split the string 
    usernamesplitstring=message.split(":",2);
    //assign the string 
    String username_not_to_send=usernamesplitstring[0];
    //array list to store the clients that the timer message can be sent
    ArrayList<ClientThread> al1;
            al1 = new ArrayList<ClientThread>();
            //add all client to the arraylist
    for(int i = al.size(); --i >= 0;)
    {
        ClientThread ct2 = al.get(i);
         al1.add(ct2);
    }
    //scan through the array list
    //remove the client that is sending the timer message
    for(int i = al1.size(); --i >= 0;)
    {
     ClientThread ct1 = al1.get(i); 
     //if equal to the client
      if(ct1.username.equals(username_not_to_send))
                        {
                            //remove from the arraylist
                         al1.remove(i);
                        }
    }
    //get the id of the client sending the timer message
    for(int i = 0;i < al1.size();i++)
    {
     ClientThread ct1 = al.get(i); 
      if(ct1.username.equals(username_not_to_send))
                        {
                        client_sending_timer=ct1.id; 
                        }
    }
    //random method
    Random rand = new Random();
    //get a random number between 0 and arraylist size
    int clientnumber=rand.nextInt(al1.size());
    System.out.println(clientnumber);
    //get the client to send the timer message from the arraylist
    ClientThread ct = al1.get(clientnumber);
    //send the timer message
    ct.writeMsg(message);
    //get the thread of the client sending the timer message
    ClientThread ct_recipient_display=al.get(client_sending_timer);
    //display on the gui
    ct_recipient_display.writeMsg("RecipientClient"+ct.username);
    Date dNow = new Date( );
    SimpleDateFormat ft = new SimpleDateFormat ("E, yyyy.MM.dd  hh:mm:ss a zzz");
    String Postdata="GET /path/script.cgi HTTP/1.0"+"\n"+"Host:"+"127.0.0.1"+"\n"+"User Agent:"+"Socket Client Application"+"\n"+"ContentType:text/plain"+"\n"+"Content-Length:"+message.length()+"\n"+"Date:"+ft.format(dNow)+"."+"\n"+message;
    sg.appendRoom(ct.username+":"+Postdata+"\n");
}
//method to register for the course
private synchronized void registerMessageMethod(String message)
{
    //display on the server GUI
    sg.appendRoom(message);
    //open the existing file
    File file = new File("C:\\Users\\Sameer\\Desktop\\sp.txt");
            try { 
                //buffered writer
                BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
                //writet the message
                bw.write(message);
                //new line
                bw.newLine();
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            
           
    
}
//method to give the course registration approval by random
private synchronized void advisorMessageMethod(String message)
{
    //file reader 
    FileReader fileReader = null;
            try {
                //display on the server GUI
                sg.appendRoom(message);
                //string to store the line from file
                String line;
                //concatenation striong
                String concatenatedLine="";
                //open existing file
                File file = new File("C:\\Users\\Sameer\\Desktop\\sp.txt");
                //file reader
                fileReader = new FileReader(file);
                //buffered reader to read the file
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                //read until end of the file
                while ((line = bufferedReader.readLine()) != null) {  
                    //System.out.println("not empty");
                    System.out.println(line);
                    //random method
                    Random rand = new Random();
                    //select a value by random between 0 and 1
                    int n = rand.nextInt(2);
                    //if value is 1
                    if(n==1)
                    {
                        //approve the course
                        concatenatedLine=concatenatedLine+line+","+"Approved"+":"+"\n";
                        
                      // File file1 = new File("C:\\Users\\Sameer\\Desktop\\ap.txt");
                        //try (BufferedWriter bw = new BufferedWriter(new FileWriter(file,true))) {
                          //  bw.write(line+","+"Approved");
                            //bw.newLine();
                            //client to send the message
                            int client_sending_approved=0;
                            //search the client to send the message
                            for(int i = 0;i < al.size();i++)
                              {
                                ClientThread ct1 = al.get(i); 
                                //if equal to the advisor process
                                if(ct1.username.equals("AdvisorProcess"))
                                 {
                                     //get the client id
                                   client_sending_approved=ct1.id; 
                                 }
                              }
                            //get the client thread by the client id
                          ClientThread ct = al.get(client_sending_approved-1); 
                          //send the approcval message
                          ct.writeMsg("Approved:AdvisorMessage");
                        }
                    //}
                    //if integer is equal to 0
                    else{
                        //do not approve the course
                        concatenatedLine=concatenatedLine+line+","+"Not Approved"+":"+"\n";
                     //File file1 = new File("C:\\Users\\Sameer\\Desktop\\ap.txt");
                       // try (BufferedWriter bw = new BufferedWriter(new FileWriter(file,true))) {
                         //   bw.write(line+","+"Not Approved");
                          //  bw.newLine();
                          //client to send the message
                          int client_sending_approved=0;
                          //search the client to send the message
                            for(int i = 0;i < al.size();i++)
                              {
                                ClientThread ct1 = al.get(i);
                                //if equal to the advisor process
                                if(ct1.username.equals("AdvisorProcess"))
                                 {
                                     //get the client id
                                   client_sending_approved=ct1.id; 
                                 }
                              }
                            //get the client thread by the client id
                          ClientThread ct = al.get(client_sending_approved-1); 
                          //send the rejection message
                          ct.writeMsg("Not Approved:AdvisorMessage");
                        }
                    }
                    
                
                System.out.println("empty");
                System.out.println(concatenatedLine);
                //close the buffered reader
                bufferedReader.close();
                //close the file reader 
                fileReader.close();
                //if final meesage is not empty
        if(!concatenatedLine.isEmpty()){
        System.out.println("apfile");
        //split the string 
        String[] concatenatedLineString=concatenatedLine.split(":");
        //for every string in the string array
        for(int i=0;i<concatenatedLineString.length-1;i++)
        {
        System.out.println(concatenatedLineString[i]);
        //open an existing file to store the advisor process decision
        File file1 = new File("C:\\Users\\Sameer\\Desktop\\ap.txt");
        //buffred reader
        BufferedWriter bw = new BufferedWriter(new FileWriter(file1,true));
        //write the decision
        bw.write(concatenatedLineString[i]+":");
        //new line
        bw.newLine();
        //closet he buffered writer
        bw.close();
        }
        //File file1 = new File("C:\\Users\\Sameer\\Desktop\\ap.txt"); 
        //BufferedWriter bw = new BufferedWriter(new FileWriter(file1,true));
        //bw.write(concatenatedLine);
        //bw.newLine();
        //bw.close();
        }
        //delete the contents of the previous existing file
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.print("");
            writer.close();
            System.out.println("deleted");
        }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
    
}
//notification message method
private synchronized void notificationMessageMethod(String message)
{
    //file reader
    FileReader fileReader = null;
            try {
                //display message on the server GUI
                sg.appendRoom(message);
                //string to store the line read from the file
                String line;
                //concatenation string
                String concatenatedLine="";
                //open existing file
                File file = new File("C:\\Users\\Sameer\\Desktop\\ap.txt");
                //filereader
                fileReader = new FileReader(file);
                //buffrered reader 
        try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            //read till end of the file
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                //concatenate the entire file into a string
                concatenatedLine=concatenatedLine+line+"\n";
                
            }
            //client to send the message
            int client_sending_approved=0;
                //search the client to send the message
                for(int i = 0;i < al.size();i++)
                {
                    ClientThread ct1 = al.get(i);
                    //if equal to the notification process
                    if(ct1.username.equals("NotificationProcess"))
                    {
                         //get the client id
                        client_sending_approved=ct1.id;
                    }
                }
                //get the client thread by the client id
                ClientThread ct = al.get(client_sending_approved-1);
                //send the notification message
                ct.writeMsg(concatenatedLine+"split"+"NotificationMessage");
            
            System.out.println("empty");
            System.out.println(concatenatedLine);
            //close the buffered reader
            bufferedReader.close();
        }                  
                //close the file reader 
                fileReader.close();
                //delete the contents of the file 
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.print("");
            writer.close();
            System.out.println("deleted");
        }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
    
}
	// If a client presses the logoff btton 
        // Logoff the client, remove it from the clientlist
       //Inputs:ClientID Outputs : Removes the client with the given client id
	synchronized void remove(int id) {
		//find the client from the client array list
                //Counter to get throught every client in the client array list
		for(int i = 0; i < al.size(); ++i) {
			ClientThread ct = al.get(i);
			if(ct.id == id) {
				al.remove(i);
				return;
			}
		}
	}
        //Main Method 
        //Starts the Server
	public static void main(String[] args) {
		// Default Port number unless specified 
		int portNumber = 1500;
		//Start the Server
		Server server = new Server(portNumber);
		server.start();
	}

	//One instance of this class will run for each client
	class ClientThread extends Thread {
		Socket socket;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;
		//Id for identification of the client
		int id;
		// the Username of the Client taken from the user
		String username;
		// the  message recieved object 
		ChatMessage cm;
		// the date client connected to server
		String date;

		// Constructor for the clientthread
		ClientThread(Socket socket) {
			// unique id for each client 
			id = ++uniqueId;
			this.socket = socket;
			try
			{
				// the input and output stream
				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sInput  = new ObjectInputStream(socket.getInputStream());
				// read the username from the user
				username = (String) sInput.readObject();
                                //Display client is connected on the server GUI
				display(username + " just connected.");
			}
                        //Exception Handler
			catch (IOException e) {
				display("Exception creating new Input/output Streams: " + e);
				return;
			}
			catch (ClassNotFoundException e) {
			}
            date = new Date().toString() + "\n";
		}

		// Client runs forever 
		public void run() {
			//Run until disconnect message
			boolean keepGoing = true;
			while(keepGoing) {
				try {
                                    //Casts object into ChatMessag Type Object
					cm = (ChatMessage) sInput.readObject();
				}
				catch (IOException e) {
					display(username + " Exception reading Streams: " + e);
					break;				
				}
				catch(ClassNotFoundException e2) {
					break;
				}
				// the messaage part of the ChatMessage
				String message = cm.getMessage();
				// Switch on the type of message received
				switch(cm.getType()) {
                               //To send a normal message
				case ChatMessage.MESSAGE:
                                    //Call the BroadCast Message
					broadcast("1 to N "+username + ": " + message);
					break;
                               //To logout the client         
				case ChatMessage.LOGOUT:
                                    //Display Client is Disconnected
					display(username + " disconnected with a LOGOUT message.");
                                        //Change value of the Boolean
					keepGoing = false;
					break;
                                //To get all the active clients
				case ChatMessage.WHOISIN:
					writeMsg("List of the users connected at " + sdf.format(new Date()) + "\n");
					// Get all the connected clients
					for(int i = 0; i < al.size(); ++i) {
						ClientThread ct = al.get(i);
						writeMsg((i+1) + ") " + ct.username + " since " + ct.date);
					}
					break;
                                //To send all the active users to the client that requests it
                                case ChatMessage.CLIENTNAMELIST:
                                       String clieintnamelist="clientname";
                                       //Counter to get throught every client in the client array list
                                    for(int i = 0; i < al.size(); ++i) {
						ClientThread ct = al.get(i);
                                                clieintnamelist=clieintnamelist+";"+ct.username;
						//writeMsg("clientname"+ct.username);
                                                //Send Details to the requested Client
                                                writeMsg(clieintnamelist);
					}
                                        break;
                                //TO send message to single client
                                case ChatMessage.SINGLECLIENT:
                                    //Call the singlemessage method
                                    singlemessage("1 to 1 "+username + ": " + message);
					break;
                                        //timer message
                                case ChatMessage.TIMERCLIENT:
                                    //String clieinttimerconcatenate="clienttimer";
                                   // String clieinttimerlist="";
                                       //Counter to get throught every client in the client array list
                                    //for(int i = 0; i < al.size(); ++i) {
					//	ClientThread ct = al.get(i);
                                        //        clieinttimerlist=ct.username+";";
						//writeMsg("clientname"+ct.username);
                                                //Send Details to the requested Client
					//}
                                    //clieinttimerlist=clieinttimerlist+","+clieinttimerconcatenate;
                                   // writeMsg(clieinttimerlist);
                                    //call the sendtimermessage method
                                    sendTimerMessage("1 to 1 "+username + ": " +message);
					break;   
                                        //if chat message type is register message
                                 case ChatMessage.REGISTERMESSAGE:
                                     //call the registermessage method
                                     registerMessageMethod(message);
                                     break;
                                     //if chat message type is advisor message
                                case ChatMessage.ADVISORMESSAGE:
                                    System.out.println("switchcase");
                                    //call the advisormessage method
                                     advisorMessageMethod(message);
                                     break;
                                     //if chat message type is notification message
                                case ChatMessage.NOTIFICATIONMESSAGE:
                                    System.out.println("switchcase");
                                    //call the notificationmessage method
                                     notificationMessageMethod(message);
                                     break;
				}
			}
                        //Once the client closes remove the client from the client array list
			remove(id);
                        //Close all the connections
			close();
		}
		
		// Close the client thread
		private void close() {
			try {
				if(sOutput != null) sOutput.close();
			}
			catch(Exception e) {}
			try {
				if(sInput != null) sInput.close();
			}
			catch(Exception e) {};
			try {
				if(socket != null) socket.close();
			}
			catch (Exception e) {}
		}
                //Write Message to the Client
		private boolean writeMsg(String msg) {
			if(!socket.isConnected()) {
				close();
				return false;
			}
			try {
				sOutput.writeObject(msg);
			}
                        //If error occurs just inform the client
			catch(IOException e) {
				display("Error sending message to " + username);
				display(e.toString());
			}
			return true;
		}
	}
}
