package MusicSynchronizer;

import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

import MusicSynchronizer.Server.ServerMain;

public class Main {
	
	public static Socket socket;
	public static ExecutorService es = Executors.newCachedThreadPool();
	
	public static String host = "192.168.0.100";
	public static int port = 7722;
	
	public static DataInputStream in;
	public static DataOutputStream out;
	
	public static boolean connected = false;
	
	public static int offset = 50;
	public static AudioInputStream audioStream;
	public static AudioFormat audioFormat;
	
	public static Clip clip;
	public static CopyOnWriteArrayList<MusicAction> actions = new CopyOnWriteArrayList<MusicAction>();
	
	
	public static void main(String[] args) {
		try {
			clip = AudioSystem.getClip();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "启动失败", "错误", 0, null);
			System.exit(0);
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try{
					Window window = new Window();
					window.frame.setVisible(true);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		es.submit(new Runnable(){
			@Override
			public void run() {
				try {
					Thread.sleep(1500);
					ServerMain.startServer();
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		es.submit(new Runnable(){
			@Override
			public void run() {
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						for(MusicAction ma : actions) {
							if(ma.executeTime > System.currentTimeMillis()) {
								continue;
							}
							if(ma.type.equals("musicstart")) {
								if(Main.clip.isOpen()) {
									if(Math.abs(Main.clip.getMicrosecondPosition() - ma.musictime) > 1000L * 1000L) {
										Utils.addLog("音乐已对齐至" + (ma.musictime / 1000.0D / 1000.0D) + "s");
									}
									if(Math.abs(Main.clip.getMicrosecondPosition() - ma.musictime) > 10L * 1000L) {
										clip.setMicrosecondPosition(ma.musictime);
										if(!clip.isRunning()) {
											clip.start();
										}
									}
								}
							}else if(ma.type.equals("musicstop")) {
								if(Main.clip.isOpen()) {
									Main.clip.stop();
									Utils.addLog("音乐已停止");
								}
							}
							actions.remove(ma);
						}
					}
				}, 1L, 1L);
			}
		});
	}
	
	public static void connect() {
		es.submit(new Runnable() {
			@Override
			public void run() {
				try {
					Main.disconnect();
					
					host = Window.hostField.getText();
					port = Integer.valueOf(Window.portField.getText());
					socket = new Socket(host, port);
					
					connected = true;
					in = new DataInputStream(socket.getInputStream());
					out = new DataOutputStream(socket.getOutputStream());
					String name = "MusicSynchronizer-" + new Random().nextInt(1000000);
					Utils.sendMessage("", "ThreadName-S-" + name);
					Utils.sendMessage("", "getTargets");
					JOptionPane.showMessageDialog(null, "连接成功!", "连接", 1);
					Window.yourid.setText(name);
					
					try{
						while(true) {
							String sendTo = in.readUTF();
							int length = in.readInt();
							byte[] bytes = new byte[length];
							in.readFully(bytes);
							Main.onMessageReceive(sendTo, bytes);
						}
					}catch(Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "连接已断开!", "连接", 2);
						connected = false;
					}
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, "连接失败!", "连接", 0);
					connected = false;
					ex.printStackTrace();
				}
			}
		});
	}
	
	public static void disconnect() {
		if(socket != null) {
			Window.yourid.setText("未知");
			try{
				socket.close();
			}catch(IOException e) {
				JOptionPane.showMessageDialog(null, "断开连接失败!", "连接", 0);
				e.printStackTrace();
			}
		}
	}
	
	public static void onMessageReceive(String sendTo, byte[] b) {
		String msg = new String(b);
		if(msg.startsWith("targets")){
			String s = msg.replaceFirst("targets", "");
			String[] split = s.split("-T-");
			for(String t : split) {
				if(t.contains("MusicSynchronizer")) {
					Window.targetsBox.addItem(t);
				}
			}
		}
		if(Window.mode.getSelectedIndex() == 1) {
			if(msg.startsWith("musicstart")){
				String[] split = msg.split(",");
				//Utils.addLog("start: " + split[1]);
				//Utils.addLog("time till execute: " + (Long.valueOf(split[2]) - System.currentTimeMillis()));
				actions.add(new MusicAction("musicstart",
						(long) (Double.valueOf(split[1]) * 1000.0D * 1000.0D),
						Long.valueOf(split[2])));
			}else if(msg.startsWith("musicstop")){
				//Utils.addLog("stop");
				String[] split = msg.split(",");
				actions.add(new MusicAction("musicstop", Long.valueOf(split[1])));
			}
		}
	}
	
}
