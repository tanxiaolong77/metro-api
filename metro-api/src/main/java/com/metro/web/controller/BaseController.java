package com.metro.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.servlet.ModelAndView;

public class BaseController {

	public String error() {
		return "redirect:/error";
	}

	public ModelAndView toIndexMAV() {
		ModelAndView result = new ModelAndView("redirect:/index");
		return result;
	}

	public static String getServerIp() {
		// 获取操作系统类型
		String sysType = System.getProperties().getProperty("os.name");
		String ip;
		if (sysType.toLowerCase().startsWith("win")) { // 如果是Windows系统，获取本地IP地址
			String localIP = null;
			try {
				localIP = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			if (localIP != null) {
				return localIP;
			}
		} else {
			ip = getIpByEthNum("eth0"); // 兼容Linux
			if (ip != null) {
				return ip;
			}
		}
		return "获取服务器IP错误";
	}

	/**
	 * 根据网络接口获取IP地址
	 * 
	 * @param ethNum
	 *            网络接口名，Linux下是eth0
	 * @return
	 */
	private static String getIpByEthNum(String ethNum) {
		try {
			Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip;
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				if (ethNum.equals(netInterface.getName())) {
					Enumeration addresses = netInterface.getInetAddresses();
					while (addresses.hasMoreElements()) {
						ip = (InetAddress) addresses.nextElement();
						if (ip != null && ip instanceof Inet4Address) {
							return ip.getHostAddress();
						}
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return "获取服务器IP错误";
	}

	protected String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	protected int getPort(HttpServletRequest request) {
		return request.getLocalPort();
	}

	/**
	 * xml转换为map
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();

		InputStream ins = null;
		try {
			ins = request.getInputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Document doc = null;
		try {
			doc = reader.read(ins);
			Element root = doc.getRootElement();

			List<Element> list = root.elements();

			for (Element e : list) {
				map.put(e.getName(), e.getText());
			}

			return map;
		} catch (DocumentException e1) {
			e1.printStackTrace();
		} finally {
			ins.close();
		}

		return null;
	}

}
