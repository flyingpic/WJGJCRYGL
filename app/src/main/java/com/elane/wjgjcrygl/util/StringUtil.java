package com.elane.wjgjcrygl.util;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class StringUtil {

	/**
	 * InputStream转换为String类型，可用于xml转换为String�?
	 *
	 * @param inputStream
	 * @return
	 */
	public static String inputStreamToString(InputStream inputStream,
											 Map<String, Object> params) {
		String desStr;
		ByteArrayOutputStream outStream = null;
		byte[] buffer = new byte[1024];
		int len = -1;
		outStream = new ByteArrayOutputStream();
		try {
			while ((len = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			outStream.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] data = outStream.toByteArray();
		desStr = new String(data);
		return replaceParamsOfXml(desStr, params);
	}

	/**
	 * 替换文件中的占位�?
	 *
	 * @param paramsXML
	 * @param params
	 * @return
	 */
	public static String replaceParamsOfXml(String paramsXML, Map<String, Object> params) {
		String result = paramsXML;
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				String name = "\\#" + entry.getKey() + "\\#";
				Pattern pattern = Pattern.compile(name);
				Matcher matcher = pattern.matcher(result);
				if (matcher.find()) {
					result = matcher.replaceAll(entry.getValue().toString());
				} else {
					System.out.println(name);
				}
			}
		}
		return result;
	}

	/**
	 * 使用 DOM 解析 Xml
	 *
	 * @param respondXML
	 * @param handler  数据解析回调句柄�?1：解析成功，-1：解析异常，-2：数据访问失�?
	 */
	public static void parseXML(final String respondXML, final Handler handler) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				InputStream inStream = new ByteArrayInputStream(respondXML.getBytes());
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = null;
				Document dom = null;
				Message message = handler.obtainMessage();

				try {
					builder = factory.newDocumentBuilder();
					try {
						dom = builder.parse(inStream);
						Element root = dom.getDocumentElement();
						List<HashMap<String, String>> hms = new ArrayList<HashMap<String, String>>();
						HashMap<String, String> hm;
						NodeList results = root.getElementsByTagName("RESULT"); // 查找�?有RESULT节点

						for (int i = 0; i < results.getLength(); i++) {
							Element personNode = (Element) results.item(i);
							String s = personNode.getAttribute("STATUS");

							if (s.equals("0")) {
								message.what = 1;
								Log.e("XML解析", "无返回数�?");
							} else if (s.equals("1")) {
								NodeList datas = root.getElementsByTagName("DATA"); // 查找�?有person节点

								for (int i1 = 0; i1 < datas.getLength(); i1++) {
									// SERVICE NAME="#EAIOS_ZYRYXX"><RESULT
									// STATUS="0"
									// MSG="没有数据"
									Element personNode1 = (Element) datas.item(i1);
									NodeList childitems = personNode1.getChildNodes();
									hm = new HashMap<String, String>();
									for (int ci = 0; childitems != null && ci < childitems.getLength(); ci++) {
										Element n = (Element) childitems.item(ci);
										hm.put(n.getNodeName(), n.getTextContent());
									}
									hms.add(hm);
								}
								message.what = 1;
							} else if (s.equals("-1")) {
								message.what = -2;
								Log.e("XML解析", "数据访问失败");
							}
						}
						message.obj = hms;
						handler.sendMessage(message);
					} catch (SAXException e) {
						message.what = -1;
						handler.sendMessage(message);
						e.printStackTrace();
					} catch (IOException e) {
						message.what = -1;
						handler.sendMessage(message);
						e.printStackTrace();
					}
				} catch (ParserConfigurationException e) {
					message.what = -1;
					handler.sendMessage(message);
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 替换keys中的单位编码
	 * @param keys
	 * @param dwbm
	 * @return
	 */
	public static String parseKeysDWBM(String keys, String dwbm) {
		return keys.toLowerCase().replace("#dwbm#", dwbm);
	}

	/**
	 * �?�? sp 里面参数是否为空,是否为指定size的集�?
	 * @param sp
	 * @return size
	 */
	public static boolean isComplement(SharedPreferences  sp,int size) {
		boolean ret = true;
		Map<String,String> map = (Map<String,String> )sp.getAll();

		if(map == null || map.size() != size){
			ret = false;
		} else {
			for(Map.Entry<String,String> item:map.entrySet()) {
				if(item.getValue() == null || item.getValue().replace(" ", "").equals("")) {
					ret = false;
					break;
				}
			}
		}
		return ret;
	}

	/**
	 *判断map中的每一个value是否为空
	 * @param map
	 * @return boolean
	 */
	public static boolean isMapNullForEveryVaule(Map<String,String>map){
		boolean ret = true;
		if (map == null) {
			ret = false;
			return ret;
		}
		for(Map.Entry<String,String> item : map.entrySet()) {
			if(item.getValue() == null || item.getValue().replace(" ", "").equals("")) {
				ret = false;
				break;
			}
		}
		return ret;
	}

	public static void sortByFjh (List<HashMap<String,String>> list) {
		Collections.sort(list, new Comparator<HashMap<String, String>>() {
			@Override
			public int compare(HashMap<String, String> t0, HashMap<String, String> t1) {

				return parseString(t0.get("FJH")).compareTo(parseString(t1.get("FJH")));
			}
		});
	}
	private static Long parseString (String str) {
		long result = 0;
		if (str == null || str.equals("")){

		}
		try{
			result = Long.parseLong(str);
		}	catch ( Exception e){
			result =  Long.MAX_VALUE;
		}
		return result;
	}

	public static boolean isNullOrEmpty(String str){
		return str == null ? true : (str.equals("")?true:false);
	}
}
