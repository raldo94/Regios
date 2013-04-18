package net.jzx7.regios.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtil {
	public boolean compareHashes(String hash1, String hash2) {
		if(hash1.equals(hash2)){
			return true;
		} else {
			return false;
		}
	}
	
	public String computeSHA2_384BitHash(String s) {
		try{
			String password = s;
			MessageDigest md = MessageDigest.getInstance("SHA-384");
	        md.update(password.getBytes());
	        
	        byte byteData[] = md.digest();
	 
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }
	 
	        StringBuffer hexString = new StringBuffer();
	    	for (int i=0;i<byteData.length;i++) {
	    		String hex=Integer.toHexString(0xff & byteData[i]);
	   	     	if(hex.length()==1) hexString.append('0');
	   	     	hexString.append(hex);
	    	}
			return hexString.toString();
		} catch (NoSuchAlgorithmException ex){
			ex.printStackTrace();
		}
		return null;
	} 
}