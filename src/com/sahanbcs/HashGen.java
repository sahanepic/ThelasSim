package com.sahanbcs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.jpos.iso.ISOUtil;

public class HashGen {
	
	public static void main(String[] args) throws IOException {
		
		
		Socket socket = null;
		DataInputStream din = null;
		DataOutputStream dout = null;
		
		try {
			
			socket = new Socket("192.168.20.216",8888);
			 din= new DataInputStream(socket.getInputStream());
			 dout= new DataOutputStream(socket.getOutputStream());
			
			//generate md5 hash EE9007 
			 //gave answer
//			 byte[] request =ISOUtil.hex2byte("EE9007" + "00" + "00" + "0000000000000000" + "00000000000000000000000000000000" +"18" + "E9A02CEBFA202F6DC1D46250A6AEAB4CA07C265862F359C2" );
			 
			 
			 
			 
			 //generate sha hash EE9008
			 // saha 1
			 // worked
//			 byte[] request =ISOUtil.hex2byte("EE9008" + "00" + "00" + "00" + "0000000000000000"  + "14"+ "0000000000000000000000000000000000000000"    +"18"  + "E9A02CEBFA202F6DC1D46250A6AEAB4CA07C265862F359C2"   );
			 
			//sha 224
			 // not worked
			// byte[] request =ISOUtil.hex2byte("EE9008" + "00" + "01" + "00" + "0000000000000000"  + "1c"+ "00000000000000000000000000000000000000000000000000000000"    +"30"  + "E9A02CEBFA202F6DC1D46250A6AEAB4CA07C265862F359C2E9A02CEBFA202F6DC1D46250A6AEAB4CA07C265862F359C2"   );
//			
			 // but Worked
			 byte[] request =ISOUtil.hex2byte("EE9008" + "00" + "01" + "00" + "0000000000000000"  + "20"+ "0000000000000000000000000000000000000000000000000000000000000000"    +"30"  + "E9A02CEBFA202F6DC1D46250A6AEAB4CA07C265862F359C2E9A02CEBFA202F6DC1D46250A6AEAB4CA07C265862F359C2"   );
			 
			 
			 //sha 256
			 //worked
//			 byte[] request =ISOUtil.hex2byte("EE9008" + "00" + "02" + "00" + "0000000000000000"  + "20"+ "0000000000000000000000000000000000000000000000000000000000000000"    +"30"  + "E9A02CEBFA202F6DC1D46250A6AEAB4CA07C265862F359C2E9A02CEBFA202F6DC1D46250A6AEAB4CA07C265862F359C2"   );
			 
			 
			//sha 384
			
			 // not worked
//			 byte[] request =ISOUtil.hex2byte("EE9008" + "00" + "03" + "00" + "0000000000000000"  + "30"+ "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"    +"30"  + "E9A02CEBFA202F6DC1D46250A6AEAB4CA07C265862F359C2E9A02CEBFA202F6DC1D46250A6AEAB4CA07C265862F359C2"   );
			 
			 //worked
//			 byte[] request =ISOUtil.hex2byte("EE9008" + "00" + "03" + "00" + "0000000000000000"  + "40"+ "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"    +"30"  + "E9A02CEBFA202F6DC1D46250A6AEAB4CA07C265862F359C2E9A02CEBFA202F6DC1D46250A6AEAB4CA07C265862F359C2"   );
			 
			
			//sha 512
			 //worked
//			 byte[] request =ISOUtil.hex2byte("EE9008" + "00" + "04" + "00" + "0000000000000000"  + "40"+ "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"    +"30"  + "E9A02CEBFA202F6DC1D46250A6AEAB4CA07C265862F359C2E9A02CEBFA202F6DC1D46250A6AEAB4CA07C265862F359C2"   );
			 
			 
			 
			 
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
			 
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			if(din != null) {
				din.close();		
			}
			if(dout != null) {
			dout.close();				
						}
			if(socket != null) {
				socket.close();
			}
		}
		
		
		
		
		
	}
	

}
