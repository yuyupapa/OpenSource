package com.endofhope.scbook.ch04;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server_1 {
	public static final byte CR = '\r';
	public static final byte LF = '\n';

	private ServerSocket serverSocket;

	public static void main(String[] args) throws IOException {
		Server_1 server = new Server_1();
		server.boot();
	}

	private void boot() throws IOException {
		serverSocket = new ServerSocket(8000);
		Socket socket = serverSocket.accept();
		InputStream in = socket.getInputStream();
		
		int oneInt = -1;
		byte oldByte = (byte) -1;
		
		StringBuilder sb = new StringBuilder();
		int lineNumber = 0;
		while (-1 != (oneInt = in.read())) {
			byte thisByte = (byte) oneInt;
			if (thisByte == Server_1.LF && oldByte == Server_1.CR) {
				// CRLF 가 완성되었다. 따라서 직전 CRLF 부터 여기까지가 한 행이다.
				// -2가 아니라 -1을 하는 이유는 아직 LF 가 버퍼에 들어가기 전이기 때문이다.
				String oneLine = sb.substring(0, sb.length() - 1);
				lineNumber++;
				System.out.printf("%d: %s\n", lineNumber, oneLine);
				if (oneLine.length() <= 0) {
					// 내용이 없는 행
					// 따라서 메시지 헤더의 마지막일 경우이다.
					System.out.println("[SYS] 내용이 없는 헤더, 즉 메시지 헤더의 끝");
					// 현 상황에서는 메시지 바디는 처리하지 말기로 한다.
					break;
				}
				sb.setLength(0);
			} else {
				sb.append((char) thisByte);
			}
			oldByte = (byte) oneInt;
		}
		in.close();
		socket.close();
	}
}