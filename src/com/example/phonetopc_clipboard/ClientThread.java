package com.example.phonetopc_clipboard;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import android.content.Context;
import android.widget.Toast;

public class ClientThread extends Thread {

	Socket targetSocket;
	Boolean success = false;

	int portNumber;
	String ipAddr;
	String path;
	String filename;
	Context context;

	FileInputStream fis;
	BufferedInputStream bis;
	BufferedOutputStream bos;

	public ClientThread(Context context, int portNumber, String ipAddr, String path,
			String filename) {
		this.context = context;
		this.portNumber = portNumber;
		this.ipAddr = ipAddr;
		this.path = path;
		this.filename = filename;
	}

	@Override
	public void run() {
		try {
			// Make connection
			targetSocket = new Socket(ipAddr, portNumber);

			// Send data
			File sourceFile = new File(path, filename);
			long filesize = sourceFile.length();
			byte[] dataByte = new byte[(int) filesize];

			try {
				fis = new FileInputStream(sourceFile);
				bis = new BufferedInputStream(fis);
				bos = new BufferedOutputStream(targetSocket.getOutputStream());

				bos.write(filename.getBytes());
				int count;
				while ((count = bis.read(dataByte)) > 0) {
					bos.write(dataByte, 0, count);
				}
				bos.flush();
				bos.close();
				bis.close();
				fis.close();

			} catch (IOException e) {
				//Toast.makeText(context, "There's a problem with the file", Toast.LENGTH_LONG).show();
				success = false;
				e.printStackTrace();
			}

			// Close connection
			if (success){
				//Toast.makeText(context, "File is successfully sent! ... I hope", Toast.LENGTH_LONG).show();
				targetSocket.close();
			}
			
		} catch (Exception e) {
			//Toast.makeText(context, "There's a problem connecting to the server", Toast.LENGTH_LONG).show();
			success = false;
			e.printStackTrace();
		}
	}

}
