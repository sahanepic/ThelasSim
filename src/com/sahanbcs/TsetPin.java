package com.sahanbcs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
 

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;

 

public class TsetPin {
	public static void main(String[] args) throws IOException, ISOException  {
		
		Socket ss = null;
		DataInputStream din = null;
		DataOutputStream dout = null;
		
		try {
			ss = new Socket("192.168.20.216",8888);
		//	ss = new Socket("127.0.0.1",9997);
			 din= new DataInputStream(ss.getInputStream());
			 dout= new DataOutputStream(ss.getOutputStream());
//			 byte[] request =ISOUtil.hex2byte("01");
			 
			//pin generation related code 
			 // 100% correct Code
//			byte[] request =ISOUtil.hex2byte("EE0E04" + "00" + "04" + "01" + "123412341234" + "1111" + "9CAAEF5CD4E554029CAAEF5CD4E55402");
			
			 
			 //pin Set related Command
			// byte[] request =ISOUtil.hex2byte("EE3020" + "00" + "F26B14CF1DFAF3E7"+ "1111" + "9CAAEF5CD4E554029CAAEF5CD4E55402"+ "01" +"123412341234" );
			 
			 
			 // clear pi generation
			// byte[] request =ISOUtil.hex2byte("EE0600" + "00" + "04"+ "1234" + "123412341234" + "1111" + "9CAAEF5CD4E554029CAAEF5CD4E55402" );
			 
			 
			 //mac commands  EE0700
// 			 byte[] request =ISOUtil.hex2byte("EE0700" + "00" + "11"+ "0000000000000000" + "11"+"FB69AFE8C1B47E30E7A15EC404B35200" + "2054" + "9CAAEF5CD4E554029CAAEF5CD4E55402" );
			 
			//mac commands  EE0701
 			 byte[] request =ISOUtil.hex2byte("EE0701" + "00" + "00"+ "08"+ "0000000000000000"  +"11"+"11FB69AFE8C1B47E30E7A15EC404B35193" + "18" + "E9A02CEBFA202F6DC1D46250A6AEAB4CA07C265862F359C2"  );
			 
			 
			 //generate sha hash EE9008
//			 byte[] request =ISOUtil.hex2byte("EE9008" + "00" + "01" + "00" + "0000000000000000" + "0000000000000000"   + "12341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234123412341234"   );
			 
			 String hlen = Integer.toHexString(request.length);			  
			  final String hd = "01010000" + ISOUtil.zeropad(hlen, 4);
			  request = ISOUtil.concat(ISOUtil.hex2byte(hd), request);
			  
					//ISOUtil.hex2byte("010100000003FFF000");
			   
			   
			//byte[] request = ISOUtil.hex2byte("00173030303142413132333430313739393939393030303133") ;
			   
			byte[] response = new byte[1024];
			
			System.out.println("\nREQUEST :\n" + ISOUtil.hexdump(request));
			
			dout.write(request);
			dout.flush();
			
			
			final int len = din.read(response,0,1024);   
			System.out.println("\nRESPONSE :\n" + ISOUtil.hexdump(response));
			if (len >= 5) {
                final String rc = ISOUtil.hexString(response).substring(18, 20);
                if ("00".equals(rc)) {
                     System.out.println("Success Full Responce!!!!!!!!");
                      String epb = ISOUtil.hexString(response).substring(20, 36);
                      System.out.println("Enc Pin Block " + epb);
                }else {
                	System.out.println("Error Responce!!!!!!!!" + len + " " + rc );
                	
//                	String aa = "0";
//                	System.out.println("The Hex  " +  ISOUtil.hexString(aa.getBytes())  );		
                }
            }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(din != null) {
				din.close();		
			}
			if(dout != null) {
			dout.close();				
						}
			if(ss != null) {
				ss.close();
			}
		}
		
		
	}
}
