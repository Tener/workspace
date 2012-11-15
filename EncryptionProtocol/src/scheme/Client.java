/* authors : Tanmay Mathur , Akshaya Ravichandran , Srivatsan Varadharajan
*/
package scheme;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import util.SchemeConstants;

class Client {
	//static Socket sock;
	
	static List<File> result = new ArrayList<File>();
    
	
	public static void main(String args[]) throws UnknownHostException, IOException {
		Socket sock = null;
		
		try {
			System.out.println("starting client");
			sock = new Socket("localhost",4887);
			//BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			System.out.println("Connection made (clientSide)");
			/*if(args.length == 0) {
				System.out.println("No command line arguments found!!");
				System.exit(0);
			}*/
			String mode = "upload";
			String fileName="textfile.txt";
			if(mode.equalsIgnoreCase("upload")) {
				if(fileName.isEmpty())  {
					System.out.println("No filename found!!");
					System.exit(0);
				}
				try {
					System.out.println("uploading the file.. " + fileName);
					byte[] cipherText = Encrypt.encryptDoc(fileName);
					System.out.println(new String(cipherText));
					PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
					out.println(mode);
					out.flush();
					System.out.println("sent the mode.. now sending the fileName");
					out.println(fileName);
					out.flush();
					System.out.println("sent the filename.. now sending the cipher");
					out.println(new String(cipherText));
					out.flush();
					
					System.out.println("decrypting..");
					out.println("decrypt");
					out.flush();
					System.out.println("Enter the word to be searched (in plaintext format):");
					Scanner keyboard = new Scanner(System.in);
					String encrypted_word = keyboard.nextLine();
					out.println(encrypted_word);
					out.flush();
					search_word(encrypted_word);
					//out.close();
					
					System.out.println("the result of the search is follows, : " + result.size());
					for(int i=0;i<result.size(); i++) {
						System.out.println("file: " + result.get(i).getName());
						//result.get(i).
					}
				} catch (InvalidKeyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			sock.close();
		} catch (Exception e) {
			System.out.println(e);
			sock.close();
		}
	}
	
	public static void message_send(byte[] cipherText, String fileName, SecretKey key_dash,SecretKey key) throws UnknownHostException, IOException {
		String file = "C:\\crypto" + File.separator + fileName;
		//sock=new Socket("localhost",4887);
		System.out.println("Connection made (clientSide)..");
		
		decrypt(fileName,cipherText,key_dash);
	}
	
	public void sendFile() throws Exception
	{
		/*File myFile = new File("img.jpg");
		ObjectOutputStream oos=new ObjectOutputStream(sock.getOutputStream());
		oos.writeObject(myFile);*/
	}
	
	private static String decrypt(String fileName, byte[] cipherText, SecretKey key_dash) {
		//byte[]  = 
		Cipher c = null;

		try {
			//SecretKey key = KeyGenerator.getInstance("DES").generateKey();
			c = Cipher.getInstance("AES");
			c.init(Cipher.ENCRYPT_MODE, key_dash);
			//c = crypto("DES", Cipher.ENCRYPT_MODE, key);
		
			byte[] seed = c.doFinal(fileName.getBytes("UTF-8"));
			
			byte[] S_i = Encrypt.genKey(cipherText.length/2, seed);
			System.out.println("\nLength"+cipherText.length/2);
			System.out.println("Stream cipher generated is :" + Arrays.toString(S_i)+"\nlength"+S_i.length);
			byte[] si_fi = new byte[cipherText.length];
			for(int i=0;i<cipherText.length/SchemeConstants.wordCipherLength;i++)
			{
				//byte[] div_Si= {S_i[i+0], S_i[i+1], S_i[i+2]};
				byte[] f_ki = c.doFinal(S_i, i*SchemeConstants.wordCipherLength/2, SchemeConstants.wordCipherLength/2);
				for(int j=0; j<SchemeConstants.wordCipherLength/2; j++) {
					si_fi[(i*SchemeConstants.wordCipherLength)+j] = S_i[(i*SchemeConstants.wordCipherLength/2) + j];
				}
				//System.out.println(Arrays.toString(si_fi[i]));
				for(int j=0; j<SchemeConstants.wordCipherLength/2; j++) {
					si_fi[(i*SchemeConstants.wordCipherLength) +j + SchemeConstants.wordCipherLength/2] = f_ki[j];
				}
			}
			
			byte[] plainText = new byte[cipherText.length];
			for(int i=0; i < cipherText.length; i++ ) {
				plainText[i] = (byte) (cipherText[i] ^ si_fi[i]);
			}
			
			System.out.println("plaintext is: " + new String(plainText));
			return (new String(plainText));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null; 
		
		//c = crypto("DES", Cipher.ENCRYPT_MODE, key);
		
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
	
	static void search_word(String encrypted_word) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException
	{
		Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter source directory: ");
        String src = keyboard.nextLine();
        try {
			filereader(src,encrypted_word);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static List<File> filereader(String directpath,String encrypted_word) throws Exception
	{
		try {
	             byte[] l_i = null;
	             byte[] encrypted_words=encrypted_word.getBytes();
	             File f=new File(SchemeConstants.plainTextPath);
	             File[] files=f.listFiles();
	             
	             for(int i=0; i<files.length; i++) {
	            	 
	            	 String filename = files[i].getName();
	            	 File file = files[i];
	            	 FileInputStream fstream = new FileInputStream(file);
	            	 // Get the object of DataInputStream
	            	 DataInputStream in = new DataInputStream(fstream);
	            	 BufferedReader br = new BufferedReader(new InputStreamReader(in));
	            	 String strLine;
	            	 int occurence = 0;
	            	 while((strLine=br.readLine()) != null) {
	            		 if(strLine.contains(encrypted_word)) {
	            			 if(occurence==0)
	            				 result.add(file);
	            			 occurence++;
	            			 //break;
	            		 }
	            	 }
	            	 System.out.println("found the word " + encrypted_word + " in file " + filename + ", " + occurence + " times");
        			 
	             }
	             
	             for(int i=0;i<files.length;i++)
	             {
	                    byte[] file_name_bytes=files[i].getName().getBytes("UTF-8");
	                    Cipher c;
	                    SecretKey key_dash = KeyGenerator.getInstance("AES").generateKey();
	                    c=Cipher.getInstance("AES");
	                    c.init(Cipher.ENCRYPT_MODE, key_dash);
	                    byte[]seed = c.doFinal(file_name_bytes);
	                    byte[] stream_cipher = new byte[(int) files[i].length()/2];
	                    //long seed = (long) Math.floor(Math.random() * 10);
	                    SecureRandom rand = new SecureRandom();
	                    rand.setSeed(seed);
	                    rand.nextBytes(stream_cipher);
	                    for(int k=0;i<encrypted_words.length;i++)
	                    {
	                           l_i[k]=(byte)(encrypted_words[k] ^ stream_cipher[k]);
	                    }
	                    FileInputStream fstream = new FileInputStream(files[i]);
	                    // Get the object of DataInputStream
	                    DataInputStream in = new DataInputStream(fstream);
	                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	                    String strLine;
	                    //List<String> document = new ArrayList<String>();
	                    List<String> docToBeEncrypted = new ArrayList<String>();
	                    // Read File Line By Line
	                    StringBuffer strBuf = new StringBuffer();
	                    while ((strLine = br.readLine()) != null) {
	                           // Print the content on the console
	                           // System.out.println(strLine);
	                           strBuf.append(strLine);
	                           String[] tmp = strLine.split(" ");
	                           //Collections.addAll(inputDoc, tmp);
	                           strBuf.append("\n");
	                    }
	                    
	             }
		} catch (Exception e) {
			// TODO: handle exception
		}
	             return result;
	       }
	}
