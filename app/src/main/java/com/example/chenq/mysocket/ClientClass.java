import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientClass{
	//private  String IP = "192.168.1.101" ;
	private  String IP ;
	private JFrame mainWin;
	private JPanel centerPanel = new JPanel();
	private JPanel rightPanel = new JPanel();
	private JPanel bottomPanel = new JPanel();
	private JTextArea showArea = new JTextArea(20 , 54);
	private JList userList = new JList();
	private JTextField inputField = new JTextField(60);
	private JButton sendBn = new JButton("发送按钮");

	public static void main(String[] args)throws IOException{
		new ClientClass().init();
	}
	public void init()
	{
		mainWin = new JFrame("对话框");
		// centerPanel添加到中间
		mainWin.add(centerPanel);
		// 向centerPanel中添加showArea
		showArea.setFont(new Font("宋体" , Font.PLAIN , 18));
		showArea.setEditable(false);
		centerPanel.add(new JScrollPane(showArea));

		mainWin.add(rightPanel , BorderLayout.EAST);
		rightPanel.setLayout(new BorderLayout());
		userList.setFixedCellWidth(180);
		rightPanel.add(new JScrollPane(userList));
		//rightPanel.add(isPrivateBox , BorderLayout.SOUTH);

		mainWin.add(bottomPanel , BorderLayout.SOUTH);
		inputField.setFont(new Font("宋体" , Font.PLAIN , 18));
		// 向下面区域中添加了输入框、按钮
		bottomPanel.add(inputField);
		bottomPanel.add(sendBn);

		ActionListener senderListener = null;
		
		
		try{
			InetAddress ia =InetAddress.getLocalHost();
			IP = ia.getHostAddress();
			// 尝试向对方主机建立连接
			System.out.print("ip : "+IP);
			Socket s = new Socket(IP, 30000);
			// 获取对应网络的输出流
			final PrintStream ps = new PrintStream(s.getOutputStream());
			// 启动子线程
			new ClientThread(s).start();
			//监听
			senderListener = new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent ae){
					// 将用户输入的内容发送到网络
					ps.println(inputField.getText());
					// 清空原有内容
					inputField.setText("");
				}
			};
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		// 用户按下按钮，在输入框按回车时，都会发送消息
		sendBn.addActionListener(senderListener);
		inputField.addActionListener(senderListener);
		mainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWin.pack();
		mainWin.setVisible(true);
	}

	//线程类
	class ClientThread extends Thread{
		private Socket s;
		public ClientThread(Socket s){
			this.s = s;
		}
		// 子线程：负责不断读取网络，将读到的数据输出到屏幕
		@Override
		public void run(){
			try(
				BufferedReader socketBr = new BufferedReader(new InputStreamReader(s.getInputStream()));){
				String line = null;
				// 采用循环，不断读取将输入流中每行
				while( (line = socketBr.readLine()) != null){
					showArea.append(line + "\n");
				}
			}
			catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}
}

