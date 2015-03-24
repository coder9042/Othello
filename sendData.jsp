<%@ page import="java.io.*, java.net.*" %>
<%
	String ipAddress = request.getParameter("addr");
	String port = request.getParameter("port");
	String row = request.getParameter("row");
	String col = request.getParameter("col");
	String turn = request.getParameter("turn");

	DatagramSocket sendSocket = new DatagramSocket();
	String data = turn+","+row+","+col;
	byte[] sendData = data.getBytes();
	DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(ipAddress), Integer.parseInt(port));
	sendSocket.send(sendPacket);
	out.println("Success");
%>