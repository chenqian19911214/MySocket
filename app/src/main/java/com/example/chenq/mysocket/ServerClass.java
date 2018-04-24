
import java.util.*;
import java.net.*;
import java.io.*;

public class ServerClass{
	
	public static LinkedList<Socket> clients= new LinkedList<>();// 用一个集合，来记录所有客户端在服务端连接的Socket

	public static void main(String[] args)throws Exception{
		// 设置端口
		ServerSocket ss = new ServerSocket(30000);

		while(true){
			Socket socket = ss.accept();//创建套接字

			// 将新连接的Socket添加到clients集合中
			clients.add(socket);
			System.out.println("已经有" + clients.size()+"个设备连接到服务器");

		/**
		*每新建 一个客户端 就new 一个线程
		*/
			new ServerThread(socket).start();
		}
	}
}
//线程内部类
class ServerThread extends Thread{
	private Socket socket;
	public ServerThread(Socket socket){
		this.socket = socket;
	}
	@Override
	public void run()
	{
		// 服务器负责读取客户端发送过来的数据，并在控制台输出。
		try(
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));)
		{
			String data = null;
			// 采用循环，将输入流中所有数据都读入进来。
			while( (data = br.readLine()) != null)
			{
				// 只是简单地将读取的数据输出到控制台。
         		System.out.println("接收到的数据 ：" +data);

				// 遍历每个客户端关联的Socket
				for (Socket client : ServerClass.clients )
				{
					try
					{
						// 获取对应于该客户端的输出流
						PrintStream ps = new PrintStream(client.getOutputStream());

						// 输出数据
						ps.println(data);
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}
			}
		}
		// 只要捕获到该Socket出现了异常，即可判断与该Socket连接到客户端已经出现了问题。
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
