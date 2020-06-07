# Scalable Server
Developing a scalable server that relies on thread pool and Non-blocking I/O to handle high network traffic. The implementation includes two components, a server and multiple clients (minimum 100 clients). The server accepts incoming network connections from the clients, receives incoming traffic from these connections, and responds to the clients in the order of received messages by sending back the hash codes of the received messages. Each client connects and maintains an active connection to the server, sends a message including random content of size 8KB R times in a second, and tracks the sent messages by comparing their hash codes with the ones received from the server.
	
## Compiling and executing
- To compile the source files you have to execute the command "make all" inside the ScalingServer folder
- To delete class files from the bin folder you have to execute the command "make clean" inside the ScalingServer folder
- To execute the programs you have to go inside the bin folder then execute the the programs as described below:
  - To start the Server:
    - java  cs455.scaling.server.Server  portnum  [thread-pool-size]
  - To start a Client:
    - java cs455.scaling.client.Client  server-host  server-port  [message-rate]


