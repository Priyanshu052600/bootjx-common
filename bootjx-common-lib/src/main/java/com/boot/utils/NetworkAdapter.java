package com.boot.utils;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class NetworkAdapter {

	public static class NetAddress implements Serializable {
		private static final long serialVersionUID = -5853182786747016797L;
		String mac;
		String localIp;
		String hostName;
		String userName;
		String port;

		public String getPort() {
			return port;
		}

		public void setPort(String port) {
			this.port = port;
		}

		public String getHostName() {
			return hostName;
		}

		public void setHostName(String hostName) {
			this.hostName = hostName;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getMac() {
			return mac;
		}

		public void setMac(String mac) {
			this.mac = mac;
		}

		public String getLocalIp() {
			return localIp;
		}

		public void setLocalIp(String localIp) {
			this.localIp = localIp;
		}
	}

	public static NetAddress getAddress() {

		NetAddress netAddress = new NetAddress();

		netAddress.setUserName(System.getProperty("user.name"));
		InetAddress lanIp = null;
		try {

			InetAddress localMachine = InetAddress.getLocalHost();

			netAddress.setHostName(localMachine.getHostName());

			String ipAddress = null;
			Enumeration<NetworkInterface> net = null;
			net = NetworkInterface.getNetworkInterfaces();

			while (net.hasMoreElements()) {
				NetworkInterface element = net.nextElement();
				Enumeration<InetAddress> addresses = element.getInetAddresses();

				while (addresses.hasMoreElements() && element.getHardwareAddress() != null
						&& element.getHardwareAddress().length > 0 && !isVMMac(element.getHardwareAddress())) {
					InetAddress ip = addresses.nextElement();
					if (ip instanceof Inet4Address) {

						if (ip.isSiteLocalAddress()) {
							ipAddress = ip.getHostAddress();
							lanIp = InetAddress.getByName(ipAddress);
						}

					}

				}
			}

			if (lanIp == null) {
				return null;
			}

			netAddress.setLocalIp(lanIp.toString().replaceAll("^/+", ""));

			netAddress.setMac(resolveMacAddress(lanIp));

		} catch (UnknownHostException ex) {

			ex.printStackTrace();

		} catch (SocketException ex) {

			ex.printStackTrace();

		} catch (Exception ex) {

			ex.printStackTrace();

		}

		return netAddress;

	}

	private static String resolveMacAddress(InetAddress ip) {
		String address = null;
		try {

			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			address = sb.toString();

		} catch (SocketException ex) {

			ex.printStackTrace();

		}

		return address;
	}

	private static boolean isVMMac(byte[] mac) {
		if (null == mac)
			return false;
		byte invalidMacs[][] = { { 0x00, 0x05, 0x69 }, // VMWare
				{ 0x00, 0x1C, 0x14 }, // VMWare
				{ 0x00, 0x0C, 0x29 }, // VMWare
				{ 0x00, 0x50, 0x56 }, // VMWare
				{ 0x08, 0x00, 0x27 }, // Virtualbox
				{ 0x0A, 0x00, 0x27 }, // Virtualbox
				{ 0x00, 0x03, (byte) 0xFF }, // Virtual-PC
				{ 0x00, 0x15, 0x5D } // Hyper-V
		};

		for (byte[] invalid : invalidMacs) {
			if (invalid[0] == mac[0] && invalid[1] == mac[1] && invalid[2] == mac[2])
				return true;
		}

		return false;
	}
}
