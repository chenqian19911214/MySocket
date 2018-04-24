
import java.util.*;
import java.net.*;
import java.io.*;

public class ServerClass{
	
	public static LinkedList<Socket> clients= new LinkedList<>();// ��һ�����ϣ�����¼���пͻ����ڷ�������ӵ�Socket

	public static void main(String[] args)throws Exception{
		// ���ö˿�
		ServerSocket ss = new ServerSocket(30000);

		while(true){
			Socket socket = ss.accept();//�����׽���

			// �������ӵ�Socket��ӵ�clients������
			clients.add(socket);
			System.out.println("�Ѿ���" + clients.size()+"���豸���ӵ�������");

		/**
		*ÿ�½� һ���ͻ��� ��new һ���߳�
		*/
			new ServerThread(socket).start();
		}
	}
}
//�߳��ڲ���
class ServerThread extends Thread{
	private Socket socket;
	public ServerThread(Socket socket){
		this.socket = socket;
	}
	@Override
	public void run()
	{
		// �����������ȡ�ͻ��˷��͹��������ݣ����ڿ���̨�����
		try(
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));)
		{
			String data = null;
			// ����ѭ���������������������ݶ����������
			while( (data = br.readLine()) != null)
			{
				// ֻ�Ǽ򵥵ؽ���ȡ���������������̨��
         		System.out.println("���յ������� ��" +data);

				// ����ÿ���ͻ��˹�����Socket
				for (Socket client : ServerClass.clients )
				{
					try
					{
						// ��ȡ��Ӧ�ڸÿͻ��˵������
						PrintStream ps = new PrintStream(client.getOutputStream());

						// �������
						ps.println(data);
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
			}
		}
		// ֻҪ���񵽸�Socket�������쳣�������ж����Socket���ӵ��ͻ����Ѿ����������⡣
		catch(SocketException se)
		{
			ServerClass.clients.remove(socket);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
