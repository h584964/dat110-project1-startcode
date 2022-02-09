package no.hvl.dat110.rpc;

import java.util.HashMap;

import no.hvl.dat110.TODO;
import no.hvl.dat110.messaging.Connection;
import no.hvl.dat110.messaging.Message;
import no.hvl.dat110.messaging.MessagingServer;

public class RPCServer {

	private MessagingServer msgserver;
	private Connection connection;
	
	// hashmap to register RPC methods which are required to extend RPCRemoteImpl	
	private HashMap<Byte,RPCRemoteImpl> services;
	
	public RPCServer(int port) {
		
		this.msgserver = new MessagingServer(port);
		this.services = new HashMap<Byte,RPCRemoteImpl>();
		
	}
	
	public void run() {
		
		// the stop RPC method is built into the server
		RPCRemoteImpl rpcstop = new RPCServerStopImpl(RPCCommon.RPIDSTOP,this);
		
		System.out.println("RPC SERVER RUN - Services: " + services.size());
			
		connection = msgserver.accept(); 
		
		System.out.println("RPC SERVER ACCEPTED");
		
		boolean stop = false;
		
		while (!stop) {
	    
		   byte rpcid = 0;
		   Message requestmsg,replymsg;
		   
		   requestmsg = connection.receive();
		   byte[] request = requestmsg.getData();
		   rpcid = request[0];
		  
		   
		   RPCRemoteImpl rpcImpl = services.get(rpcid);
		   byte[] reply = rpcImpl.invoke(RPCUtils.decapsulate(request));
		   replymsg = new Message(RPCUtils.encapsulate(rpcid, reply));
		   connection.send(replymsg);
		   
		 
		   
		   
		   
		   // TODO - START
		   // - receive Message containing RPC request
		   // - find the identifier for the RPC method to invoke
		   // - lookup the method to be invoked
		   // - invoke the method
		   // - send back message containing RPC reply
		   
		   // TODO - END
		   
		   if (rpcid == RPCCommon.RPIDSTOP) {
			   stop = true;
		   }
		}
	
	}
	
	// used by server side implementation to register themselves in the RPC server
	public void register(byte rpcid, RPCRemoteImpl impl) {
		services.put(rpcid, impl);
	}
	
	public void stop() {
		connection.close();
		msgserver.stop();
		
	}
}
