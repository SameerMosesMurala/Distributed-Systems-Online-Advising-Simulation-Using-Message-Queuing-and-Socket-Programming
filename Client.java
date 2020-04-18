/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * Name: Sameer Moses Murala
 * Student ID: sxm6494
 * @author Sameer
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
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.*;
import java.text.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
//Client Class
public class Client  {
// to read from the socket
	private ObjectInputStream sInput;		
        // to write on the socket
	private ObjectOutputStream sOutput;
        //Socket for the connection
	private Socket socket;
       //Client GUI Object
	//private ClientGUI cg;
        //Advisor Process class object
        private AdvisorProcess AP;
        //Notification Process Class Object
        private NotificationProcess NP;
        //Server Name and Client User Name
	private String server, username;
        //Port Number
	private int port;
        //boolean to start and stop the timer
        public boolean timerStart;
        //rand method
        Random rand = new Random();
        //select a value between 0 and 50
        int timer_start_value = rand.nextInt(51);
        int low = 2;
        int high = 10;
        Random r = new Random();
        //select a value between 2 and 10
        int result_time = r.nextInt(high-low) + low;
        //string array 
        public String[] clienttimerdecode=null;
        //Client Constructor
	//Client(String server, int port, String username) {
	//	this(server, port, username);
	//}
	//Client Constructor
	Client(String server, int port, String username) {
		this.server = server;
		this.port = port;
		this.username = username;
		//this.cg = cg;
	}
        //Client constructor for advisor process
        Client(String server, int port, String username, AdvisorProcess AP) {
		this.server = server;
		this.port = port;
		this.username = username;
		this.AP = AP;
	}
        //Client constructor for notification process
        Client(String server, int port, String username, NotificationProcess NP) {
		this.server = server;
		this.port = port;
		this.username = username;
		this.NP =NP ;
	}
	//Client started 
	public boolean start() {
		//request for connection
		try {
			socket = new Socket(server, port);
		} 
		catch(Exception ec) {
			display("Error connectiong to server:" + ec);
			return false;
		}
		//if connection is accepted
		String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
                //Display event on the GUI
		display(msg);
		try
		{
                    //I/O streams 
			sInput  = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
		}
		catch (IOException eIO) {
			display("Exception creating new Input/output Streams: " + eIO);
			return false;
		}
		//  Thread to listen from the server 
		new ListenFromServer().start();
               //Send username to the server taken from user
		try
		{
			sOutput.writeObject(username);
		}
		catch (IOException eIO) {
			display("Exception doing login : " + eIO);
			disconnect();
			return false;
		}
               // timerMethod();
               new TimerThread().start();
		return true;
	}
//Display message on the GUI and any event on the GUI 
        //Inputs String Message Output: The message in the GUI
	private void display(String msg) {
//			cg.append(msg + "\n");		
	}
        //A new timer thread for every client in the network
        class TimerThread extends Thread {
        //public void timerMethod()
        //Method that runs forever
            public void run() {
        {
            //run until timerstart is set to true
            timerStart=true;
            //infinite loop
            while(timerStart)
            {
                //Random r = new Random();
                //int low = 2;
                //int high = 10;
               // int result_time = r.nextInt(high-low) + low;
                //int result_time1=result_time*1000;
                //Timer timer = new Timer();
                //timer.schedule(new sendTimerMessage(), 0, result_time1);
                //a random timer value is selected between 0 and 50 
                //Converted to string to send as a mesage
                String  timer_start_value_String= Integer.toString(timer_start_value);
                //messag is sent for every 2 to 10 seconds
                for(int i=0;i<result_time;i++)
                {
                    //convert to string
                    timer_start_value_String= Integer.toString(timer_start_value);
                    //show the timer value on the GUI
                 //*   //cg.timerAppend(timer_start_value_String);
                    //sleep the tread for a second
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                //increament the timer value by 1
                timer_start_value++;
                }
                //send the timer message 
                sendMessage(new ChatMessage(ChatMessage.TIMERCLIENT,"Timer"+timer_start_value_String));
               //1 //cg.timerAppend(timer_start_value_String);
                //sendMessage(new ChatMessage(ChatMessage.TIMERCLIENT,timer_start_value_String));
                //
                //cg.timerAppend(timer_start_value_String);
                //try {
                   // //for(int i=0;i<=result_time;i++)
                    ////{
                    ////}
                    //TimeUnit.SECONDS.sleep(1);
                //} catch (InterruptedException ex) {
                  //  Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                //}
             // 1 // timer_start_value++;
            }
            
        }
        }
        }
        //method to stop the timer 
        public void stopTimerMethod()
        {
            //setting the boolean timerstart to false stops the timer
            timerStart=false;
        }
	 // To send a chatmessage to the server
        //respond according to the message type
        //Inputs: ChatMessag Object Output:Message to the Server
	void sendMessage(ChatMessage msg) {
		try {
                    String toEncodeMessage=msg.getMessage();
                    //BroadCast Message
                    if(msg.getType()==1)
                    {
                        //Encode the message according to the HTTP Format
                    String finalMessage=encode(toEncodeMessage);
                    //Create a object of tye Chat Message 
                    ChatMessage msg1= new ChatMessage(ChatMessage.MESSAGE,finalMessage);
                    //Send message to the server
		    sOutput.writeObject(msg1);
                    }
                    //One to one Single CLient
                    else if( msg.getType()==4)
                    {
                        //Encode the message according to the HTTP Format
                    String finalMessage=encode(toEncodeMessage);
                    //Create a object of tye Chat Message
                    ChatMessage msg1= new ChatMessage(ChatMessage.SINGLECLIENT,finalMessage);
                    //Send message to the server
		    sOutput.writeObject(msg1);
                    }
                    else {
                        //Send message to the server
			sOutput.writeObject(msg);
                    }
		}
		catch(IOException e) {
			display("Exception writing to server: " + e);
		}
	}
        //Encode the message in HTTP Format
        //Inputs: String Message Outputs:Encoded Message in HTTP Format
        public String encode (String message)
        {
            //Date Present
            Date dNow = new Date( );
            //Date Format
            SimpleDateFormat ft = new SimpleDateFormat ("E, yyyy.MM.dd  hh:mm:ss a zzz");
            String encodedMessage;
            String encodedMessage1;
            String encodedMessage2;
            encodedMessage1 = "POST /path/script.cgi HTTP/1.0"+"\n"+"Host:"+"127.0.0.1"+"\n"+"User Agent:"+"Socket Client Application"+"\n"+"ContentType:text/plain"+"\n";
            encodedMessage2="Content-Length:"+message.length()+"\n"+"Date:"+ft.format(dNow)+"."+"\n"+message;
            //Final Encode Message
            encodedMessage=encodedMessage1+encodedMessage2;
            return encodedMessage;
        }

       //If any error disconnect the client
        //Close all the input and output streams and the socket
	private void disconnect() {
		try { 
			if(sInput != null) sInput.close();
		}
		catch(Exception e) {} 
		try {
			if(sOutput != null) sOutput.close();
		}
		catch(Exception e) {} 
        try{
			if(socket != null) socket.close();
		}
		catch(Exception e) {} 
		
//*		if(cg != null)
//*			cg.connectionFailed();
			
	}
        //Main Method
        //Starts the Client
		public static void main(String[] args) {
		int portNumber = 1500;
		String serverAddress = "localhost";
		String userName = "Anonymous";
		// create the Client object
		Client client = new Client(serverAddress, portNumber, userName);
	        //Start the Client
		if(!client.start())
			return;
                //Disconnect the client
		client.disconnect();	
	}
                
        class sendTimerMessage extends TimerTask {
    public void run() {
       System.out.println("Hello World!");
       String  timer_start_value_String= Integer.toString(timer_start_value); 
       sendMessage(new ChatMessage(ChatMessage.TIMERCLIENT,timer_start_value_String));
//*       cg.timerAppend(timer_start_value_String);
      // String timerClientListString=clienttimerdecode[0];
       //String[] timerClientList=timerClientListString.split(";",2);
       //int timerClientListLength;
        //timerClientListLength = timerClientList.length();
       
      // clienttimerdecode=
       
        }
       }        
        //Class that listens for message from the server
	class ListenFromServer extends Thread {
                //Method runs forever and listens from the Server for incoming messages
            //Inputs: None Outputs:None
		public void run() {
			while(true) {
				try {
					String msg = (String) sInput.readObject();
					//Check the content in the meesage
				        if(msg.contains("clientname"))
                                        {
                                            //Show the active clients
//*                                            cg.comboboxlistpopulate(msg);
                                        }
                                        //else if(msg.contains("clienttimer"))
                                        //{
                                            
                                          //  clienttimerdecode=msg.split(",",2);
                                            
                                        //}   
                                        //Decode the recieved message from the server and display 
                                        //on the client GUI
                                        //if the recieved message is a timer message
                                        else if (msg.contains("Timer"))
                                        {
                                            //string array to store the message
                                            String[] msg_without_timer=null;
                                            //spilt the message to 2 parts 
                                            msg_without_timer=msg.split("Timer",2);
                                            //the final message does not contain the timer string 
                                            String msg_final=msg_without_timer[0]+msg_without_timer[1];
                                            //display the message on the GUI
// *                                           cg.append(msg_final+"\n");
                                            //convert the timer string to integer
                                            int timer_to_compare=Integer.parseInt(msg_without_timer[1]);
                                            //compare the recipient timer with the sender timer 
                                            //if equal or recipient timer greater than the sender timer
                                            if(timer_start_value>timer_to_compare || timer_start_value==timer_to_compare )
                                            {
                                                //display no adjustment necassary
// *                                               cg.append("No adjustment Necessary"+"\n");
                                            }
                                            else
                                            {
                                                //display the necessary adjustment
//*                                                cg.append("Necessary Adjustment:"+(timer_to_compare+1)+"\n");
                                                //update the recipient timer value 
                                                timer_start_value=timer_to_compare+1;
                                            }
                                            //cg.append(msg_final+"\n");
                                        }
                                        //if the message contains RecipientClient
                                        else if (msg.contains("RecipientClient"))
                                        {
                                            //string array to store the message
                                            String[] msg_without_timer=null;
                                            //split the string 
                                            msg_without_timer=msg.split("RecipientClient",2);
                                            //take the string with the timer value
                                            String msg_final=msg_without_timer[1];
                                            //display on the GUI
//  *                                          cg.timer_client_name_append(msg_final);
                                        }
                                        //if the message contains advisor message
                                        else if (msg.contains("AdvisorMessage"))
                                        {
                                            //string array to store the message
                                            String[] msg_without_advisor=null;
                                            //split the string 
                                            msg_without_advisor=msg.split(":",2);
                                            //take the string with the advisor message value
                                            String msg_final=msg_without_advisor[0];
                                            //display on the GUI
                                           System.out.println("Listener"+msg_final);
                                           AP.approvedSetText(msg_final+"\n");
                                        }
                                        //if the message contains notificaton message
                                        else if (msg.contains("NotificationMessage"))
                                        {
                                            //string array to store the message
                                            String[] msg_without_notification=null;
                                            //split the string
                                            msg_without_notification=msg.split("split",2);
                                            //string array to store the message
                                            String[] msg_final_string=null;
                                            //split the string
                                            msg_final_string=msg_without_notification[0].split(":");
                                            //run a loop for every string in the string array
                                            for(int i=0;i<msg_final_string.length-1;i++)
                                            {
                                                //replace the ";" with a space
                                                String msg_final1=msg_final_string[i].replace(";","     ");
                                                //replace the "," with a space
                                                String msg_final=msg_final1.replace(",","    ");
                                                System.out.println("Listener"+msg_final);
                                                //Display thr message on the GUI
                                                NP.notificationSetText(msg_final+"\n"); 
                                                
                                            }
                                           
                                        }
					else {
                                              String decodemsg;
                                              //Decode the Message that is Unparse the HTTP Format Message
                                              decodemsg=decode(msg);
                                              //Display on the Client
//*					      cg.append(decodemsg);
					}
				}
				catch(IOException e) {
					display("Server has close the connection");
//*					if(cg != null) 
//*						cg.connectionFailed();
					break;
				}
				catch(ClassNotFoundException e2) {
				}
			}
		}
             //Decode method to unparse the HTTP Format messsage
             //Inputs: HTTP Parsed Mesage Outpus:Unparsed HTTP Message   
             public String decode(String message)
             {
                 //String methods are used to get the fields required
                 String paragraph=message;
                 String[] decodedMessage1string=null;
                 String decodedMessage2;
                 String decodedMessage1;
                 String decodedMessage;
                 decodedMessage1string=message.split("POST",2);
                 decodedMessage1=decodedMessage1string[0];
                 decodedMessage2=message.substring(message.lastIndexOf(".")+2);
                 decodedMessage=decodedMessage1+decodedMessage2;
                 System.out.println(message);
                 System.out.println(decodedMessage);
                 return decodedMessage;
             }
	}
}
