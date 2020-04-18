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

import java.io.*;


public class ChatMessage implements Serializable {

 

    protected static final long serialVersionUID = 1112122200L;

 
//Different type of messages 
    static final int WHOISIN = 0, MESSAGE = 1, LOGOUT = 2, CLIENTNAMELIST=3,SINGLECLIENT=4 , TIMERCLIENT=5 , REGISTERMESSAGE=6 , ADVISORMESSAGE=7 , NOTIFICATIONMESSAGE=8;
     //Message type
    private int type;
    //Message Content or Data 
    private String message;

     
//Constructor
    ChatMessage(int type, String message) {

        this.type = type;

        this.message = message;

    }

     

    // getters
//return the type of message
    int getType() {

        return type;

    }
//return the message
    String getMessage() {

        return message;

    }

}

