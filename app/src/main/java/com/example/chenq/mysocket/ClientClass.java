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
	private JButton sendBn = new JButton("���Ͱ�ť");

	public static void main(String[] args)throws IOException{
		new ClientClass().init();
	}
	public void init()
	{
		mainWin = new JFrame("�Ի���");
		// centerPanel��ӵ��м�
		mainWin.add(centerPanel);
		// ��centerPanel�����showArea
		showArea.setFont(new Font("����" , Font.PLAIN , 18));
		showArea.setEditable(false);
		centerPanel.add(new JScrollPane(showArea));

		mainWin.add(rightPanel , BorderLayout.EAST);
		rightPanel.setLayout(new BorderLayout());
		userList.setFixedCellWidth(180);
		rightPanel.add(new JScrollPane(userList));
		//rightPanel.add(isPrivateBox , BorderLayout.SOUTH);

		mainWin.add(bottomPanel , BorderLayout.SOUTH);
		inputField.setFont(new Font("����" , Font.PLAIN , 18));
		// ���������������������򡢰�ť
		bottomPanel.add(inputField);
		bottomPanel.add(sendBn);

		ActionListener senderListener = null;
		
		
		try{
			InetAddress ia =InetAddress.getLocalHost();
			IP = ia.getHostAddress();
			// ������Է�������������
			System.out.print("ip : "+IP);
			Socket s = new Socket(IP, 30000);
			// ��ȡ��Ӧ����������
			final PrintStream ps = new PrintStream(s.getOutputStream());
			// �������߳�
			new ClientThread(s).start();
			//����
			senderListener = new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent ae){
					// ���û���������ݷ��͵�����
					ps.println(inputField.getText());
					// ���ԭ������
					inputField.setText("");
				}
			};
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		// �û����°�ť��������򰴻س�ʱ�����ᷢ����Ϣ
		sendBn.addActionListener(senderListener);
		inputField.addActionListener(senderListener);
		mainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWin.pack();
		mainWin.setVisible(true);
	}

	//�߳���
	class ClientThread extends Thread{
		private Socket s;
		public ClientThread(Socket s){
			this.s = s;
		}
		// ���̣߳����𲻶϶�ȡ���磬�������������������Ļ
		@Override
		public void run(){
			try(
				BufferedReader socketBr = new BufferedReader(new InputStreamReader(s.getInputStream()));){
				String line = null;
				// ����ѭ�������϶�ȡ����������ÿ��
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

