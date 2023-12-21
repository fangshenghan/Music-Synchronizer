package MusicSynchronizer;

import java.io.IOException;

public class Utils {
	
	public static boolean sendMessage(String sendTo, String data) {
		try {
			if(!Main.connected) {
				return false;
			}
			Main.out.writeUTF(sendTo);
			Main.out.writeInt(data.getBytes().length);
			Main.out.write(data.getBytes());
			Main.out.flush();
			return true;
		}catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean sendMessage(String sendTo, byte[] data) {
		try {
			if(!Main.connected) {
				return false;
			}
			Main.out.writeUTF(sendTo);
			Main.out.writeInt(data.length);
			Main.out.write(data);
			Main.out.flush();
			return true;
		}catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void addLog(String s) {
		Window.log.setText(Window.log.getText() + s + "\n");
	}
	
}
