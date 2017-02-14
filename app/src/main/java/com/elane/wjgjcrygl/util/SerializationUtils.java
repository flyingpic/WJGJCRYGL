package com.elane.wjgjcrygl.util;
/**
 * 序列化和反序列化工具�?
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.HashMap;

/**
 * Simple serialization/deserialization demonstrator.
 * 
 * @author Dustin
 */
public class SerializationUtils {
	
	/**
	 * 序列�?
	 * 
	 * @param optionMap
	 * @return
	 */
	public static byte[] serialize(HashMap<String, String> optionMap) {
		try {
			ByteArrayOutputStream mem_out = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(mem_out);

			out.writeObject(optionMap);

			out.close();
			mem_out.close();

			byte[] bytes = mem_out.toByteArray();
			return bytes;
		} catch (IOException e) {
			return null;
		}
	}
	/**
	 * 反序列化
	 * 
	 * @param bytes
	 * @return
	 */
	public static HashMap<String, String> deserialize(byte[] bytes) {
		try {
			ByteArrayInputStream mem_in = new ByteArrayInputStream(bytes);
			ObjectInputStream in = new ObjectInputStream(mem_in);

			HashMap<String, String> hashMap = (HashMap<String, String>) in
					.readObject();

			in.close();
			mem_in.close();

			return hashMap;
		} catch (StreamCorruptedException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}
//	/**
//	 * 反序列化user集合
//	 * 
//	 * @param bytes
//	 * @return
//	 */
//	public static HashMap<String, User> userDeserialize(byte[] bytes) {
//		try {
//			ByteArrayInputStream mem_in = new ByteArrayInputStream(bytes);
//			ObjectInputStream in = new ObjectInputStream(mem_in);
//
//			HashMap<String, User> hashMap = (HashMap<String, User>) in
//					.readObject();
//
//			in.ic_close();
//			mem_in.ic_close();
//
//			return hashMap;
//		} catch (StreamCorruptedException e) {
//			return null;
//		} catch (ClassNotFoundException e) {
//			return null;
//		} catch (IOException e) {
//			return null;
//		}
//	}
}