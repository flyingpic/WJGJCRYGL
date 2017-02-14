package com.elane.wjgjcrygl.util;

import java.io.IOException;
/**
 * 重启设备
 * @author jessing
 *
 */
public class RestartDevice {
	public RestartDevice() {
		super();
		try {
			String cmd = "su -c reboot";
			try {
				Runtime.getRuntime().exec(cmd);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
