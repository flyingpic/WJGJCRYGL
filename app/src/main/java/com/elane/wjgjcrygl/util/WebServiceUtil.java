package com.elane.wjgjcrygl.util;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.elane.wjgjcrygl.Global;

import android.os.Handler;
import android.os.Message;




public class WebServiceUtil {
	private final static int TIME_OUT = 10 * 1000;

	/**
	 * 通过 SoapObject 访问 Webservice
	 *
	 * @param requestXML
	 * @param handler  访问WebService回调句柄�?9：有响应�?-1：异常，
	 */
	public static void getSoapObject(final String requestXML, final Handler handler) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				SoapObject soapObject = new SoapObject(Global.NAMESPACE, Global.METHODNAME);
				soapObject.addProperty("xml", requestXML);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
				envelope.bodyOut = soapObject;
				envelope.dotNet = false;
				envelope.setOutputSoapObject(soapObject);

				HttpTransportSE transportSE = new HttpTransportSE(Global.eudap_wsdl, TIME_OUT);
				transportSE.setXmlVersionTag(Global.XMLVersionTag);
				transportSE.debug = true;

				String result = null;
				try {
					transportSE.call(null, envelope);
					if (envelope != null) {
						result = envelope.getResponse().toString();
						Message message = handler.obtainMessage();
						message.obj = result;
						message.what = 9;
						handler.sendMessage(message);
					}
				} catch (Exception e) {
					Message message = handler.obtainMessage();
					message.what = -1;
					handler.sendMessage(message);
					e.printStackTrace();
				}
			}
		}).start();
	}
}
