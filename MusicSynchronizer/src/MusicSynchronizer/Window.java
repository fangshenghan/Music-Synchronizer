package MusicSynchronizer;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Toolkit;

public class Window {

	public JFrame frame;
	public static JTextArea log;
	public static JTextField offset;
	public static JTextField hostField;
	public static JTextField portField;
	public static JComboBox targetsBox;
	public static JComboBox mode;
	public static JLabel yourid;
	public static JTextField sendDelay;
	public static JScrollPane scrollpane;
	private JLabel musiclabel;

	/**
	 * Create the application.
	 */
	public Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Window.class.getResource("/image/kt.png")));
		frame.setResizable(false);
		frame.setTitle("MusicSynchronizer v1.0");
		frame.setBounds(100, 100, 520, 511);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		scrollpane = new JScrollPane();
		scrollpane.setBounds(20, 41, 467, 251);
		frame.getContentPane().add(scrollpane);
		scrollpane.setAutoscrolls(true);
		
		log = new JTextArea();
		log.setWrapStyleWord(true);
		log.setLineWrap(true);
		scrollpane.setViewportView(log);
		
		offset = new JTextField();
		offset.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				try {
					Main.offset = Integer.valueOf(offset.getText());
				}catch(Exception ex) {
					offset.setText("0");
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					Main.offset = Integer.valueOf(offset.getText());
				}catch(Exception ex) {
					offset.setText("0");
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {
				try {
					Main.offset = Integer.valueOf(offset.getText());
				}catch(Exception ex) {
					offset.setText("0");
				}
			}
		});
		offset.setText("50");
		offset.setColumns(10);
		offset.setBounds(125, 407, 136, 21);
		frame.getContentPane().add(offset);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("发送偏移(毫秒):");
		lblNewLabel_1_1_1_1.setFont(new Font("宋体", Font.PLAIN, 14));
		lblNewLabel_1_1_1_1.setBounds(14, 403, 163, 28);
		frame.getContentPane().add(lblNewLabel_1_1_1_1);
		
		mode = new JComboBox();
		mode.setModel(new DefaultComboBoxModel(new String[] {"发送", "接收"}));
		mode.setBounds(66, 368, 95, 23);
		frame.getContentPane().add(mode);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("模式:");
		lblNewLabel_1_1_2.setFont(new Font("宋体", Font.PLAIN, 14));
		lblNewLabel_1_1_2.setBounds(16, 365, 40, 28);
		frame.getContentPane().add(lblNewLabel_1_1_2);
		
		JLabel hostLable = new JLabel("地址:");
		hostLable.setFont(new Font("宋体", Font.PLAIN, 14));
		hostLable.setBounds(17, 301, 67, 21);
		frame.getContentPane().add(hostLable);
		
		hostField = new JTextField();
		hostField.setText("java.sharkmc.cn");
		hostField.setColumns(10);
		hostField.setBounds(65, 301, 292, 21);
		frame.getContentPane().add(hostField);
		
		JLabel portLable = new JLabel("端口:");
		portLable.setFont(new Font("宋体", Font.PLAIN, 14));
		portLable.setBounds(17, 332, 67, 21);
		frame.getContentPane().add(portLable);
		
		portField = new JTextField();
		portField.setText("47832");
		portField.setColumns(10);
		portField.setBounds(65, 332, 95, 21);
		frame.getContentPane().add(portField);
		
		targetsBox = new JComboBox();
		targetsBox.setBounds(170, 332, 187, 22);
		frame.getContentPane().add(targetsBox);
		
		JButton connectBtn = new JButton("连接");
		connectBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!Main.connected) {
					Main.connect();
				}else {
					JOptionPane.showMessageDialog(null, "已经连接了", "连接", 2);
				}
			}
		});
		connectBtn.setBounds(169, 368, 145, 23);
		frame.getContentPane().add(connectBtn);
		
		JButton btnDisconnect = new JButton("断开连接");
		btnDisconnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Main.disconnect();
			}
		});
		btnDisconnect.setBounds(324, 368, 163, 23);
		frame.getContentPane().add(btnDisconnect);
		
		JButton refresh = new JButton("刷新目标");
		refresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Window.targetsBox.removeAllItems();
				Utils.sendMessage("", "getTargets");
			}
		});
		refresh.setBounds(367, 331, 121, 23);
		frame.getContentPane().add(refresh);
		
		JLabel lblNewLabel_1_1_1_1_1 = new JLabel("你的ID:");
		lblNewLabel_1_1_1_1_1.setFont(new Font("宋体", Font.PLAIN, 14));
		lblNewLabel_1_1_1_1_1.setBounds(20, 10, 75, 28);
		frame.getContentPane().add(lblNewLabel_1_1_1_1_1);
		
		yourid = new JLabel("未知");
		yourid.setFont(new Font("宋体", Font.PLAIN, 14));
		yourid.setBounds(74, 10, 266, 28);
		frame.getContentPane().add(yourid);
		
		JLabel lblNewLabel_1_1_1_1_2 = new JLabel("发送延迟(毫秒):");
		lblNewLabel_1_1_1_1_2.setFont(new Font("宋体", Font.PLAIN, 14));
		lblNewLabel_1_1_1_1_2.setBounds(270, 400, 111, 28);
		frame.getContentPane().add(lblNewLabel_1_1_1_1_2);
		
		sendDelay = new JTextField();
		sendDelay.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				try {
					if(Long.valueOf(sendDelay.getText()) < 50) {
						sendDelay.setText("50");
					}
				}catch(Exception ex) {
					sendDelay.setText("50");
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					if(Long.valueOf(sendDelay.getText()) < 50) {
						sendDelay.setText("50");
					}
				}catch(Exception ex) {
					sendDelay.setText("50");
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {
				try {
					if(Long.valueOf(sendDelay.getText()) < 50) {
						sendDelay.setText("50");
					}
				}catch(Exception ex) {
					sendDelay.setText("50");
				}
			}
		});
		sendDelay.setText("200");
		sendDelay.setColumns(10);
		sendDelay.setBounds(379, 403, 111, 21);
		frame.getContentPane().add(sendDelay);
		
		JLabel lblBysharky = new JLabel("By 鲨鱼君Sharky ");
		lblBysharky.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBysharky.setFont(new Font("微软雅黑", Font.BOLD | Font.ITALIC, 14));
		lblBysharky.setBounds(324, 10, 163, 28);
		frame.getContentPane().add(lblBysharky);
		
		JButton clearLog = new JButton("清空日志");
		clearLog.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				log.setText("");
			}
			@Override
			public void mousePressed(MouseEvent e) {
				log.setText("");
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				log.setText("");
			}
		});
		clearLog.setBounds(367, 300, 121, 23);
		frame.getContentPane().add(clearLog);
		
		JButton btnNewButton = new JButton("选择音频");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if(fc.showOpenDialog(frame) == 0) {
					File f = fc.getSelectedFile();
					if(f.getName().endsWith(".wav") || f.getName().endsWith(".ogg") || f.getName().endsWith(".mp3")) {
						try {
							if(Main.clip.isOpen()) {
								Main.clip.close();
							}
							Main.clip.open(AudioSystem.getAudioInputStream(f));
							musiclabel.setText(f.getName());
							JOptionPane.showMessageDialog(frame, "加载成功!");
						}catch(Exception ex) {
							JOptionPane.showMessageDialog(frame, "音频加载失败\n" + ex.toString(), "错误", 0, null);
						}
					}
				}else {
					JOptionPane.showMessageDialog(frame, "音频加载失败", "错误", 0, null);
				}
			}
		});
		btnNewButton.setBounds(389, 434, 98, 28);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblNewLabel_1_1_1_1_3 = new JLabel("音频文件(wav)：");
		lblNewLabel_1_1_1_1_3.setFont(new Font("宋体", Font.PLAIN, 14));
		lblNewLabel_1_1_1_1_3.setBounds(14, 438, 147, 28);
		frame.getContentPane().add(lblNewLabel_1_1_1_1_3);
		
		musiclabel = new JLabel("未选择文件");
		musiclabel.setFont(new Font("宋体", Font.PLAIN, 14));
		musiclabel.setBounds(115, 438, 266, 28);
		frame.getContentPane().add(musiclabel);
	}
	public JLabel getMusiclabel() {
		return musiclabel;
	}
}
