package com.elane.wjgjcrygl.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class SocketSender implements Runnable {
	Socket s;
	// 定义向UI线程发送消息的Handler对象
	Handler handler;
	// 定义接收UI线程的Handler对象
	public Handler sendHandler;
	// 该线程处理Socket所对用的输入输出流
	BufferedReader br = null;
	OutputStream os = null;
	InputStream in = null;

	public SocketSender(Handler handler) {
		this.handler = handler;
	}

	@SuppressLint("HandlerLeak")
	@Override
	public void run() {
		s = new Socket();

		try {
			s.connect(new InetSocketAddress("192.168.4.79", 22555), 5000);
			in = s.getInputStream();
			br = new BufferedReader(new InputStreamReader(in));
			os = s.getOutputStream();

			/*
			 * // 启动一条子线程来读取服务器相应的数据 new Thread() { public void run() { String
			 * content = null; // 不断的读取Socket输入流的内容 try {
			 * 
			 * while (true) { int length = in.available(); byte[] buffer = new
			 * byte[length]; // 每当读取到来自服务器的数据之后，发送的消息通知程序 // 界面显示该数据 if
			 * (in.read(buffer) > 0) { content = EncodingUtils
			 * .getString(buffer, "GBK"); Message msg = new Message(); msg.what
			 * = 2; msg.obj = content; handler.sendMessage(msg); Log.d("接收成功",
			 * "==================" + content); break; }
			 * 
			 * } } catch (IOException e) { e.printStackTrace(); } }; }.start();
			 */
			// 为当前线程初始化Looper
			Looper.prepare();
			// 创建revHandler对象
			sendHandler = new Handler() {
				@SuppressLint("HandlerLeak")
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					// 接收到UI线程的中用户输入的数据
					if (msg.what == 1) {
						// 将用户在文本框输入的内容写入网络
						// String json =
						// CreatDaiChuLiJSON.creatJson().toString();
						String json = msg.obj.toString();
						try {
							os.write(json.getBytes("UTF-8"));

							Log.d("发送成功", "==================");
						} catch (Exception e) {
							e.printStackTrace();
						}finally  {
							try {
								in.close();
								os.close();
								s.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
					}
				}
			};
			// 启动Looper
			Looper.loop();

		} catch (SocketTimeoutException e) {
			Message msg = new Message();
			msg.what = 2;
			msg.obj = "网络连接超时！";
			handler.sendMessage(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 
}
