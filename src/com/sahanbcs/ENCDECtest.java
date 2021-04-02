package com.sahanbcs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;

public class ENCDECtest {
	
	public static void main(String[] args) throws IOException, ISOException {
		Socket socket = null;
		DataInputStream din = null ;
		DataOutputStream dout = null;
		
		try {
			 socket= new Socket("192.168.20.216",8888);
			 din = new DataInputStream(socket.getInputStream());
			 dout = new DataOutputStream(socket.getOutputStream());
			 
			 byte[] request =ISOUtil.hex2byte("EE0808" + "00" + "03" + "00" + "0000000000000000"  + "40"+ "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"    +"30"  + "E9A02CEBFA202F6DC1D46250A6AEAB4CA07C265862F359C2E9A02CEBFA202F6DC1D46250A6AEAB4CA07C265862F359C2"   );
			 
			 String hlen = Integer.toHexString(request.length);
			 String hd = "01010000" + ISOUtil.zeropad(hlen, 4);
			 request = ISOUtil.concat( ISOUtil.hex2byte(hd) , request);
			 
			 System.out.println("The Input Request :");
			 
			  System.out.println(ISOUtil.hexdump(request));
			 dout.write(request);
			 dout.flush();
			 
			 byte[] responce = new byte[1024];
			 
			 int count =  din.read(responce,0,1024);
			 
			 System.out.println("\n\n\nThe Input Request :" + count);
			 System.out.println(ISOUtil.hexdump(responce)); 
			 
			 
			 
		} catch (IOException e) {
			
			e.printStackTrace();
		}finally {
			if(din!=null) {
				din.close();
			}
			if(dout!=null) {
				dout.close();
			}
			if(socket!=null) {
				socket.close();
			}
			
			
			
		}
		
		
		
		
		
	}

}
