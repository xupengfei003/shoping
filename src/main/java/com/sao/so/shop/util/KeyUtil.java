package com.sao.so.shop.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成RSA工具
 *
 * @author guangpu.yan
 * @create 2017-07-14 14:27
 **/
public class KeyUtil {

	public  static PrivateKey pkey ;
	
	public	static PublicKey pubkey ;

	/**
	 * rsa算法
	 */
	public final static String RSA_ALGORITHM = "RSA";
	 
	public static Map<String,Object> genKey() throws Exception{
		
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
		kpg.initialize(2048);
		KeyPair kep = kpg.generateKeyPair();
		Provider p  = kpg.getProvider();
		System.out.println(p.getName());
		 pkey = kep.getPrivate();
		 pubkey = kep.getPublic();
		System.out.println("生成的公钥"+new String(Base64.encode(pubkey.getEncoded())));
		System.out.println("生成的私钥"+new String(Base64.encode(pkey.getEncoded())));

		Map<String,Object> param=new HashMap<String,Object>();
		param.put("PublicKey", new String(Base64.encode(pubkey.getEncoded())));
		param.put("PrivateKey", new String(Base64.encode(pkey.getEncoded())));
		
		return param;
	}

	
	public static void main(String[] args) throws Exception{
		Map<String,Object> param=genKey();
		String pb=(String) param.get("PublicKey");
		String pr=(String) param.get("PrivateKey");
		System.out.println(pb);
		System.out.println(pr);	
	}
	
}

