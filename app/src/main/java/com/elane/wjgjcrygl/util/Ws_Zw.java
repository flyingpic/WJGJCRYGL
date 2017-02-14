package com.elane.wjgjcrygl.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.elane.wjgjcrygl.Global;

import android.util.Log;
import android.util.Xml;

/**
 * @ClassName: C.java
 * @author XUX
 * @version V1.0
 * @Date 2014楠烇拷4閺堬拷15閺冿拷 娑撳锟�?2:52:21
 * @links com.elane.database C.java
 * @Description: 閹稿洨姹楁惔鎾舵畱WEBSERVICE 鐠佸潡锟�?
 */
public class Ws_Zw {
	// webservice閻ㄥ嫬銇旀穱鈩冧紖
	public static String soap = "";
	private static int chongs = 2;
	private static boolean issend = true;

	/**
	 * 鎸囩汗姣斿锛岃姹傚強鍝嶅簲
	 *
	 * @throws Exception
	 */
	public static String networkconn() throws Exception {
		byte[] data = soap.getBytes();
		// 閹绘劒姘ost鐠囬攱锟�?
		URL url = new URL(Global.FingerprintUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		try {
			conn.setConnectTimeout(12 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
			return "-10";
		}
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		conn.setRequestProperty("SOAPAction",
				"http://tempuri.org/IFingerprintMatch/GetZYRYBHFromFingerprint");
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));

		OutputStream outStream = conn.getOutputStream();
		outStream.write(data);

		outStream.flush();
		outStream.close();

		int rscode = conn.getResponseCode();
		if (rscode == 200) {
			InputStreamReader isr = new InputStreamReader(
					conn.getInputStream(), "UTF-8");
			BufferedReader bf = new BufferedReader(isr);
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = bf.readLine()) != null) {
				buffer.append(line);
			}
			Log.i("test", "返回信息-----------------------" + buffer.toString());
			String resul = parseResponseXML(buffer.toString());
			return resul;
		} else {
			System.out.println(conn.getResponseCode());
		}
		conn.disconnect();
		return "Error";
	}

	private String readSoapFile(InputStream inStream, String baseStr)
			throws Exception {
		// byte[] data = readInputStream(inStream);
		// String soapxml = new String(data);
		String xml = replace(null, baseStr, Global.FingerprintUserName,
				Global.FingerprintPassWord);
		return xml;
	}

	/**
	 * 鐠囪褰囧ù浣蜂繆閹拷
	 *
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	private static byte[] readInputStream(InputStream inputStream)
			throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inputStream.close();
		return outSteam.toByteArray();
	}

	/**
	 * 閺囨寧宕查弬鍥︽娑擃厼宕版担宥囶儊
	 *
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	private static String replace(String xml, String fp, String username,
								  String password) throws Exception {
		String result = xml;
		// if (params != null && !params.isEmpty()) {
		// for (Map.Entry<String, String> entry : params.entrySet()) {
		// String name = "\\#" + entry.getKey() + "\\#";
		// Pattern pattern = Pattern.compile(name);
		// Matcher matcher = pattern.matcher(result);
		// if (matcher.find()) {
		// result = matcher.replaceAll(entry.getValue().toString());
		// } else {
		// System.out.println(name);
		// }
		// }
		// }
		result = "<?xml version=\"1.0\"?><s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" ><s:Body><GetZYRYBHFromFingerprint xmlns=\"http://tempuri.org/\"><base64imagefinger>"
				+ fp
				+ "</base64imagefinger><username>"
				+ username
				+ "</username><password>"
				+ password
				+ "</password></GetZYRYBHFromFingerprint></s:Body></s:Envelope>";
		return result;
	}

	/**
	 * 鐟欙絾鐎絏ML閺傚洣锟�?
	 *
	 * @return
	 * @throws Exception
	 */
	private static String parseResponseXML(String str) throws Exception {
		String restr = "";
		InputStream in = new ByteArrayInputStream(str.getBytes());
		XmlPullParser p = XmlPullParserFactory.newInstance().newPullParser();
		p.setInput(in, "UTF-8");
		int type = p.getEventType();
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if ("GetZYRYBHFromFingerprintResult".equals(p.getName())) {
						restr = p.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					break;
			}
			type = p.next();
		}
		in.close();
		return restr;
	}

	private HashMap parseXML(String str) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputStream inStream = new ByteArrayInputStream(str.getBytes());
		Document dom = builder.parse(inStream);
		Element root = dom.getDocumentElement();
		HashMap hm = new HashMap();
		NodeList items = root.getElementsByTagName("RESULT");// 閺屻儲澹橀幍锟介張濉旹SULT閼哄倻锟�?
		Element n = null;
		String s = null;
		String m = null;
		Element personNode = null;
		NodeList childitems = null;
		for (int i = 0; i < items.getLength(); i++) {
			personNode = (Element) items.item(i);
			s = personNode.getAttribute("STATUS");
			m = personNode.getAttribute("MSG");
			if (s.equals("0")) return hm;
		}

		items = root.getElementsByTagName("DATA");// 閺屻儲澹橀幍锟介張濉竐rson閼哄倻锟�?
		for (int i = 0; i < items.getLength(); i++) {
			personNode = (Element) items.item(i);
			childitems = personNode.getChildNodes();
			for (int ci = 0; childitems != null && ci < childitems.getLength(); ci++) {
				n = (Element) childitems.item(ci);
				hm.put(n.getNodeName(), n.getTextContent());
			}
		}
		inStream.close();
		return hm;
	}

	/**
	 * @Title: getsoap
	 * @param: @param open
	 * @param: @param hmpar
	 * @param: @return
	 * @return: HashMap
	 * @throws
	 * @author XUX
	 * @Date 2014楠烇拷4閺堬拷11閺冿拷 娑撳﹤锟�?10:04:55
	 * @Description:
	 * @param open
	 * @param hmpar
	 * @return
	 * @throws Exception
	 */
	public String sendinfo(InputStream open, String baseStr) {
		String soapr = null;
		try {
			soap = readSoapFile(open, baseStr);
			soapr = networkconn();
			return soapr;
		} catch (Exception e) {
			try {
				soap = readSoapFile(open, baseStr);
				soapr = networkconn();
				return soapr;
			} catch (Exception e1) {
				try{
					soap = readSoapFile(open, baseStr);
					soapr = networkconn();}
				catch(Exception e2){
					return "连接指纹服务器超时";
				}
			}
		}

		if (soapr == null) {
			String str = "连接指纹服务器超时";
			return str;
		} else {
			return soapr;
		}

		// System.out.println(soapr);

	}

	/**
	 * @Title: parseXML_pu
	 * @param: @param soapr
	 * @param: @return
	 * @return: HashMap
	 * @throws
	 * @author XUX
	 * @Date 2014楠烇拷4閺堬拷14閺冿拷 娑撳﹤锟�?9:39:00
	 * @Description:
	 * @param soapr
	 * @return
	 */
	private HashMap parseXML_pu(String soapr) {
		XmlPullParser xmlpp = Xml.newPullParser();
		InputStream inStream = new ByteArrayInputStream(soapr.getBytes());
		try {
			xmlpp.setInput(inStream, "UTF-8");
			int eventType = xmlpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					String name = xmlpp.getName();
					// if(name.equals("CSQX"))
					// Log.v("??", xmlpp.getName() + "=" + xmlpp.nextText());
				}
				if (eventType == XmlPullParser.TEXT) {
					System.out.println(xmlpp.getText());
				}
				eventType = xmlpp.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
