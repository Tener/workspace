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
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import util.SchemeConstants;

public class Encrypt {
	static int words=0;
	static byte[] encrypted_plain;
	static SecretKey[] sec_key; 
	static SecretKey key_dash;
	static SecretKey key;
	static String search_word;
	/**
	 * @param args
	 * @throws InvalidKeyException 
	 * @throws NoSuchPaddingException 
	 */
	//public static void main(String[] args) 
	public static byte[] encryptDoc(String file) throws UnsupportedEncodingException, InvalidKeyException, NoSuchPaddingException {
		// TODO Auto-generated method stub
		try {
			FileInputStream fstream = new FileInputStream(file);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			key_dash=KeyGenerator.getInstance("AES").generateKey();
			key= KeyGenerator.getInstance("AES").generateKey();
			String strLine;
			StringBuffer strBuf = new StringBuffer();
			List<String> inputDoc = new ArrayList<String>();
			//List<String> document = new ArrayList<String>();
			List<String> docToBeEncrypted = new ArrayList<String>();
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				// System.out.println(strLine);
				strBuf.append(strLine);
				String[] tmp = strLine.split(" ");
				Collections.addAll(inputDoc, tmp);
				strBuf.append("\n");
			}
			//byte[] infoBin = new byte[SchemeConstants.wordCipherLength];
			String word = "", tempWord = "";
			//int size = inputDoc.size();
			System.out.println(strBuf);
			System.out.println("hiiiiiiiiiii");
			for (int i = 0; i < inputDoc.size(); i++) {
				word = inputDoc.get(i);
				System.out.println(i + " line: " + word);
				//System.out.println(word);

				int wordLen = word.length();
				
				if(wordLen < SchemeConstants.wordCipherLength-1) {
					System.out.println("wordLen lesser..");
					tempWord = word;
					for(int j=wordLen; j<SchemeConstants.wordCipherLength; j++) {
						tempWord += " ";
					}
					docToBeEncrypted.add(tempWord);
					System.out.println("word " +i + " is: " + tempWord);
					//Server.message_send(tempWord);
				}
				else if(wordLen > SchemeConstants.wordCipherLength-1) {
					System.out.println("wordLen greater.. so splitting..");
					int t=0;
					while(t<wordLen) {
						if(wordLen > t+SchemeConstants.wordCipherLength-1)
							tempWord = word.substring(t, t+SchemeConstants.wordCipherLength);
						else
							tempWord = word.substring(t);
						
						for(int j=tempWord.length(); j<SchemeConstants.wordCipherLength; j++) {
							tempWord += " ";
						}
						docToBeEncrypted.add(tempWord);
						System.out.println("word " +i + " is: " + tempWord);
						
						t=t+SchemeConstants.wordCipherLength;
					}
				}
				else {
					tempWord = word;
					tempWord += " ";
					docToBeEncrypted.add(tempWord);
					System.out.println("word " +i + " is: " + tempWord);
					//Server.message_send(tempWord);
				}
				
				System.out.println(word);
				System.out.println();
			}
			System.out.println("\n\n\n docToBeEncrypted list's contents: \n\n");
			for(int i=0 ; i<docToBeEncrypted.size(); i++) {
				System.out.print(docToBeEncrypted.get(i) + "\t");
			
				/**************************************/
				/********		TO BE DONE		*******/
				// XOR each element with PRF and then use the method:
				// Server.message_send(document.get(i));
				/**************************************/
			}
			words=docToBeEncrypted.size();
			
			System.out.println("\n\n\n Generating seed and the key:");
			Cipher c = null;
			SecretKey key_filename = KeyGenerator.getInstance("AES").generateKey();
			
			c = crypto("AES", Cipher.ENCRYPT_MODE, key_dash);
			byte[] fileName = SchemeConstants.doc.getBytes("UTF-8");
			byte[] seed = c.doFinal(fileName);
			byte[] full_padded_plain=new byte[docToBeEncrypted.size()*SchemeConstants.wordCipherLength];
			for(int i=0 ; i<docToBeEncrypted.size(); i++) {
				byte[] plain = docToBeEncrypted.get(i).getBytes("UTF-8");
				System.out.print(new String(plain) + "\t");
				for(int j=0; j<plain.length; j++) {
					full_padded_plain[(i*SchemeConstants.wordCipherLength) + j] = plain[j];
				}
			}
			
			System.out.println("\n\nFull padded string\n" + new String(full_padded_plain));
			byte[] S_i = genKey(full_padded_plain.length/2, seed);
			System.out.println("\nLength"+full_padded_plain.length/2);
			System.out.println("Stream cipher generated is :" + Arrays.toString(S_i)+"\nlength"+S_i.length);
			byte[] si_fi = new byte[(docToBeEncrypted.size() * 16)+1];
			for(int i=0;i<docToBeEncrypted.size();i++)
			{
				//byte[] div_Si= {S_i[i+0], S_i[i+1], S_i[i+2]};
				byte[] key_i=block_cipher(full_padded_plain, i);
				SecretKeySpec key_final=new SecretKeySpec(key_i,0,key_i.length,"AES");
				//sec_key[i]=key_final;
				Cipher pseudorandom_func=Cipher.getInstance("AES");
				pseudorandom_func.init(Cipher.ENCRYPT_MODE, key_final);
				byte[] f_ki = pseudorandom_func.doFinal(S_i, i*SchemeConstants.wordCipherLength/2, SchemeConstants.wordCipherLength/2);
				for(int j=0; j<SchemeConstants.wordCipherLength/2; j++) {
					si_fi[(i*16)+j] = S_i[(i*SchemeConstants.wordCipherLength/2) + j];
				}
				//System.out.println(Arrays.toString(si_fi[i]));
				for(int j=0; j<(16-SchemeConstants.wordCipherLength/2); j++) {
					si_fi[(i*16) +j + SchemeConstants.wordCipherLength/2] = f_ki[j];
				}
				System.out.println("si_fi" +Arrays.toString(si_fi));
			}
			System.out.println("\n\nChecking the fk(si):");
			byte[] cipherText = new byte[docToBeEncrypted.size()*SchemeConstants.wordCipherLength];
			int x=0;
			for(int i=0; i<si_fi.length; i++) {
				/*System.out.println(S_i[(i*SchemeConstants.wordCipherLength/2)+0] +" " 
									+S_i[(i*SchemeConstants.wordCipherLength/2)+1] +" "
									+S_i[(i*SchemeConstants.wordCipherLength/2)+2] + "\t"
									+ si_fi[i]);*/
				//if((i%6)==0|| (i%6)==1 || (i%6)==2) {
					System.out.println("\nLength of si-fi ::"+si_fi.length);
					//System.out.println(S_i[x++] + " " + si_fi[i]);
				//}
			}

			System.out.println("\n\n Cipher byte is: ");
			for(int i=0; i<full_padded_plain.length; i++) {
				/*for(int j=0; j<SchemeConstants.wordCipherLength; j++) {
					cipherText[(i*SchemeConstants.wordCipherLength)+j] 
					           = (byte) (full_padded_plain[(i*SchemeConstants.wordCipherLength)+j] ^ si_fi[i][j]);
				}*/
				cipherText[i] = (byte) (encrypted_plain[i] ^ si_fi[i]);
				System.out.print(cipherText[i] + "\t");
			}
			System.out.println("\n\ncipher String is:");
			System.out.println(new String(cipherText));
			//FileOutputStream file = writeToFile(SchemeConstants.doc, cipherText);
			System.out.println("sending file to server..");
			//Client.message_send(cipherText, SchemeConstants.doc,key_dash,key);
			int mid = file.lastIndexOf(".");
			String a = file.substring(0, mid);
			writeToFile(a.concat("_keyDash").concat(file.substring(mid, file.length())), key_dash.getEncoded());
			writeToFile(a.concat("_key").concat(file.substring(mid, file.length())), key.getEncoded());
			br.close();
			return cipherText;
			/*
			//System.out.println("F(ki) generated is :" + Arrays.toString(f_ki));
			System.out.println("Do you want to search a work(Y/N)");
			System.out.println("Enter the word to be searched");
			//Client.searching_word(search_word,key_dash,key);
			// System.out.println(strBuf);
			br.close();*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Cipher crypto(String scheme, int mode, SecretKey key) {
		Cipher c = null;
		
		try {
			c = Cipher.getInstance(scheme);
			c.init(mode, key);
			return c;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return c;
	}
	
	public static byte[] genKey(long msgLength, byte[] seed) {
		byte[] gk = new byte[(int) msgLength];
		//long seed = (long) Math.floor(Math.random() * 10);
		SecureRandom rand = new SecureRandom();
		rand.setSeed(seed);
		rand.nextBytes(gk);
		return gk;
	}
	
	public static byte[] keygen_pseudo(byte[] l_i,int i) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException
	{
		try {
			Cipher c= Cipher.getInstance("AES");
			c.init(Cipher.ENCRYPT_MODE, key_dash);
			byte[] key_i= c.doFinal(l_i, 0 , SchemeConstants.wordCipherLength-1);
			System.out.println(Arrays.toString(key_i));
			return key_i;
		}catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	static byte[] block_cipher(byte[] full_padded_plain,int i) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
	{
		
		Cipher c=Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE,key);
		encrypted_plain=c.doFinal(full_padded_plain);
			byte[] enc_plain=c.doFinal(full_padded_plain,i*SchemeConstants.wordCipherLength,SchemeConstants.wordCipherLength);
			System.out.println("\nLength of each w::"+enc_plain.length);
			System.out.println(Arrays.toString(enc_plain));
			byte[] l_i=Arrays.copyOf(enc_plain, 8);
			byte[] key_i=keygen_pseudo(l_i,i);
			return key_i;
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
