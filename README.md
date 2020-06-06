# SCALABLE-SERVER

	
## Compiling and executing
- To compile the source files you have to execute the command "make all" inside the ScalingServer folder
- To delete class files from the bin folder you have to execute the command "make clean" inside the ScalingServer folder
- To execute the programs you have to go inside the bin folder then execute the the programs as described below:
  - To start the Server:
    - java  cs455.scaling.server.Server  portnum  [thread-pool-size]

  - To start a Client:
    - java cs455.scaling.client.Client  server-host  server-port  [message-rate]


