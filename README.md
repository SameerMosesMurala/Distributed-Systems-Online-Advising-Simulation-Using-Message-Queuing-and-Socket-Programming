# Distributed-Systems-Online-Advising-Simulation-Using-Message-Queuing-and-Socket-Programming

simulate an online advising system which is similar to what you might use to register for classes. Students request 
clearance for a course from the advisor. At some other time, the advisor approves or disapproves the request, and then 
the student is notified of the advisor’s decision.
Four processes are required to simulate this: a Student Process, an Advisor Process, a Notification Process, and a Message 
Queuing Server (MQS). The Student Process, Advisor Process, and Notification Process communicate through the Message Queuing Server 
(message-oriented middleware). Communication from these processes to the Message Queuing Server utilizes both push and pull protocols.
