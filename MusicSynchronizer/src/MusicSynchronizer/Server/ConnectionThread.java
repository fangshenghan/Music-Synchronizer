package MusicSynchronizer.Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import MusicSynchronizer.Main;
import MusicSynchronizer.Utils;
import MusicSynchronizer.Window;

public class ConnectionThread{
	
	public String name;
	public Socket socket;
	
	public BufferedInputStream in;
	public BufferedOutputStream out;
	public boolean exit = false;
	public boolean connected = false;
	
	public long registerTime;
	public ExecutorService es = Executors.newCachedThreadPool();
	
	public ConnectionThread subbed = null;
	public List<String> log = new ArrayList<String>();
	
	public ConnectionThread(Socket socket) {
		this.socket = socket;
		this.registerTime = System.currentTimeMillis();
		
		try{
			this.in = new BufferedInputStream(socket.getInputStream());
			this.out = new BufferedOutputStream(socket.getOutputStream());
			Utils.addLog("新的连接注册成功...");
			connected = true;
			processConnection();
		}catch(IOException e) {
			e.printStackTrace();
			Utils.addLog("新的连接注册失败!");
		}
	}
	
	public void processConnection() {
		es.submit(new Runnable() {
			@Override
			public void run() {
				try {
					while(true) {
						byte[] bytes = new byte[1024];
						in.read(bytes);
						if(!onMessageReceive(bytes)) {
							exit = true;
							connected = false;
							ServerMain.connections.remove(name);
							Utils.addLog("连接 " + name + " 已断开...");
							break;
						}
					}
				}catch(Exception ex) {
					exit = true;
					connected = false;
					ServerMain.connections.remove(name);
					Utils.addLog("连接 " + name + " 已断开...");
				}
			}
		});
		
		es.submit(new Runnable() {
			@Override
			public void run() {
				try{
					while(true) {
						if(name == null && System.currentTimeMillis() - registerTime >= 5000L){
							exit = true;
							connected = false;
							ServerMain.connections.remove(name);
							Utils.addLog("连接超时");
						}
						if(exit) {
							in.close();
							out.close();
							socket.close();
							connected = false;
							ServerMain.connections.remove(name);
							return;
						}
						Thread.sleep(10);
					}
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		});
	}
	
	public boolean sendMessage(String data) {
		return ServerUtils.sendMessage(this, data);
	}
	
	public boolean sendMessage(byte[] data) {
		return ServerUtils.sendMessage(this, data);
	}
	
	public boolean onMessageReceive(byte[] b) {
		String msg = new String(b);
		for(int i = msg.length() - 1;i >= 0;i--) {
			if(msg.charAt(i) != 0) {
				msg = msg.substring(0, i + 1);
				break;
			}
		}
		if(msg.startsWith("ThreadName") && name == null) {
			name = msg.split("-S-")[1];
			ServerMain.connections.put(name, this);
			connected = true;
			Utils.addLog("连接 " + name + " 成功启动!");
			return true;
		}else if(name == null) {
			return true;
		}
		
		if(Window.mode.getSelectedIndex() == 0) {
			if(msg.startsWith("mstart")) {
				if(Main.connected) {
					try {
						String[] split = msg.split("-");
						float time = Float.valueOf(split[1]) + (Float.valueOf(Window.sendDelay.getText()) / 1000.0F);
						long sendTime = Long.valueOf(split[2]);
						time = time - ((float) Main.offset / 1000.0F);
						Utils.sendMessage(Window.targetsBox.getSelectedItem().toString(), 
								"musicstart," + time + "," + (sendTime + Long.valueOf(Window.sendDelay.getText())));
					}catch(Exception ex) {
						Utils.addLog(ex.toString());
					}
				}
				//Utils.addLog("MusicStart + Offset: " + time);
			}else if(msg.startsWith("mstop")) {
				if(Main.connected) {
					String[] split = msg.split("-");
					long sendTime = Long.valueOf(split[1]);
					Utils.sendMessage(Window.targetsBox.getSelectedItem().toString(), 
							"musicstop," + (sendTime + Long.valueOf(Window.sendDelay.getText()) + 200L));
				}
			}
		}

		return true;
	}

}
