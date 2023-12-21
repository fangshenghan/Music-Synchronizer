package MusicSynchronizer.Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import MusicSynchronizer.Utils;

public class ServerMain {
	
	public static ServerSocket server;
	public static HashMap<String, ConnectionThread> connections = new HashMap<String, ConnectionThread>();
	
	public static void startServer() {
		try {
			server = new ServerSocket(47836);
			Utils.addLog("服务器已启动");
			
			while(true) {
				Socket socket = ServerMain.server.accept();
				new ConnectionThread(socket);
			}
		}catch(Exception ex){
			Utils.addLog("服务器启动失败!");
			ex.printStackTrace();
		}
	}
	
}
