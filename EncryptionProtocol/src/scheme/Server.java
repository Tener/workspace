/* authors : Tanmay Mathur , Akshaya Ravichandran , Srivatsan Varadharajan
*/
package scheme;

import java.lang.*;
import java.io.*;
import java.net.*;

class Server {
	static String path = "C:\\crypto\\";
	public static void main(String args[]) throws IOException {
		try {
			Socket client;
			ServerSocket serv;
		    serv=new ServerSocket(4887);
			//wait for Connection
			System.out.println("Waiting for connection on port "+ "4887");
			client=serv.accept();
			System.out.println("Connection accepted");
			//ObjectInputStream ois=new ObjectInputStream(client.getInputStream());
			//PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			System.out.println("connection made..");
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			while(true)
			{
				//receive the file
				
				//read the file from the client
				String mode;
				if((mode = in.readLine()) != null) {
					System.out.println("Receieved the file in mode " + mode);
					
					if(mode.equalsIgnoreCase("upload")) {
						String fileName = in.readLine();
						System.out.println("filename:" + fileName);
						String cipherText = in.readLine();
						System.out.println("got cipherText: " + cipherText);
						writeToFile(path+fileName, cipherText.getBytes("UTF-8"));
						System.out.println("File saved in server!!");
					}
					else if(mode.equalsIgnoreCase("search")) {
						String word = in.readLine();
						System.out.println("got word: " + word);
						String key = in.readLine();
						System.out.println("got key:" + key);
					}
				}
				//in.close();
/*				byte[]b=new byte[(int)myfile.length()];
				FileDialog choo=new FileDialog(myFrame,"Choose File Destination",FileDialog.SAVE);
				choo.setDirectory(null);
				choo.setFile("enter file name here");
				choo.setVisible(true);
				FileOutputStream fos=new FileOutputStream(choo.getDirectory()+choo.getFile( )+".jpg");
				fos.write(b);*/
			}
			
		}
		catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	private static FileOutputStream writeToFile(String fileName, byte[] code) {
		/*String temp[] = fullFileName.split(".");
		System.out.println("leng:" + temp.length);
		for(int i=0; i<temp.length; i++)
			System.out.print(temp[i] + ".. ");*/
		/*int mid = fullFileName.lastIndexOf(".");
		String a = fullFileName.substring(0, mid);
		String fileName = a.concat("_cipher").concat(fullFileName.substring(mid, fullFileName.length()));*/
		//String fileName = a.concat(fullFileName.substring(mid, fullFileName.length()));
		 try {
			FileOutputStream fos = new FileOutputStream(fileName);
			fos.write(code);
			System.out.println("wrote the output file " + fileName);
			return fos;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
