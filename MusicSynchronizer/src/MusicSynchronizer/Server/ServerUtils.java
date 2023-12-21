package MusicSynchronizer.Server;

import java.io.IOException;

public class ServerUtils {
	
	public static boolean sendMessage(ConnectionThread connection, String data) {
		try {
			if(!connection.connected) {
				return false;
			}
			connection.out.write(data.getBytes());
			connection.out.flush();
			return true;
		}catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean sendMessage(ConnectionThread connection, byte[] data) {
		try {
			if(!connection.connected) {
				return false;
			}
			connection.out.write(data);
			connection.out.flush();
			return true;
		}catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
