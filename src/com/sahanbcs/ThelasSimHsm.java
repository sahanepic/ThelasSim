//package com.sahanbcs;
//
////package com.epic.ech.channel;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//
//import org.jpos.iso.ISOUtil;
//
//import com.epic.ech.cfg.InitConfigValues;
//import com.epic.ech.cfg.SysConfigValues;
//import com.epic.ech.logs.LogFileHandler;
//import com.epic.ech.message.queue.QueueESHSession;
//import com.epic.ech.util.SessionHandler;
//import com.epic.ech.util.UtilMethods;
//
//import epic.com.core.listener.init.APIPoolInit;
//import epic.com.core.listener.util.APISession;
//
//public class ThelasSimHsm implements Runnable {
//	private static DataInputStream IN = null;
//	private static DataOutputStream OUT = null;
//	private static Socket s = null;
//	private static boolean isConnect = false;
//	private static int readTimeoutValue = 0;
//	private static int conTimeoutValue = 0;
//	private static int conPort = 0;
//	private static String conIP = null;
//	private static String channelId = null;
//	private static boolean startup = true;
//	private static boolean keepAliveStatus = true;
//
//	public static boolean isConnect() {
//		return isConnect;
//	}
//
//	private static void connect(){
//
//                try{
//                        isConnect                                               = false;
//                        if(startup)channelId                    = UtilMethods.getChannelId();
//                        InetAddress anetAdd                     = InetAddress.getByName(conIP);
//                        s                                                               = new Socket();
//                        String debug                                    = null;
//                        InetSocketAddress sockAddress   = new InetSocketAddress(anetAdd,
//conPort);
//
//                        s.setKeepAlive(keepAliveStatus);
//                        s.connect(sockAddress, conTimeoutValue);
//
//                        IN      = new DataInputStream(s.getInputStream());
//                        OUT = new DataOutputStream(s.getOutputStream());
//
//                        if(startup){
//                                debug = "Channel (ESH) on Id "+channelId+" is successfully
//established : "+conIP+"  at "+UtilMethods.getTimestamp();
//                        }else{
//                                debug = "Channel (ESH) on Id "+channelId+" is successfully
//re-established : "+conIP+"  at "+UtilMethods.getTimestamp();
//                        }
//
//                        UtilMethods.debug(null,SysConfigValues.LOG_TYPE_INIT, null,debug, 0);
//                        LogFileHandler.writeIntoLogFile(debug, null,
//SysConfigValues.LOG_LEVEL_1, SysConfigValues.LOG_TYPE_INIT);
//
//                        debug           = null;
//                        startup         = false;
//                        isConnect       = true;
//
//
//                }catch (Exception e) {
//
//                        String debug = null;
//
//                        try {
//                                debug = "Channel (ESH) on Id "+channelId+" establishing is failed:
//"+conIP+"  at "+UtilMethods.getTimestamp()+"(Please refer error logs for
//more details ... )";
//                        } catch (Exception e1) {
//                                LogFileHandler.writeErrorIntoLogFile(e1, null);
//                        }
//
//                        UtilMethods.debug(null,SysConfigValues.LOG_TYPE_INIT, null,debug,0);
//                        LogFileHandler.writeIntoLogFile(debug, null,
//SysConfigValues.LOG_LEVEL_1, SysConfigValues.LOG_TYPE_INIT);
//                        LogFileHandler.writeErrorIntoLogFile(e, null);
//
//                }
//        }
//
//	public static void disconnect() throws Exception {
//
//		isConnect = false;
//		if (s != null) {
//			s.close();
//			s = null;
//		}
//		if (IN != null) {
//			IN.close();
//			IN = null;
//		}
//		if (OUT != null) {
//			OUT.close();
//			OUT = null;
//		}
//	}
//
//	public static void establish(boolean open) throws Exception {
//
//		readTimeoutValue = InitConfigValues.ESH_CH_INFOR.getTIMEOUT();
//		conPort = InitConfigValues.ESH_CH_INFOR.getPORT();
//		conTimeoutValue = InitConfigValues.ESH_CH_INFOR.getCONNECTION_TIMEOUT();
//		conIP = InitConfigValues.ESH_CH_INFOR.getIP();
//
//		if (InitConfigValues.ESH_CH_INFOR.getKEEPALIVE_STATUS() == SysConfigValues.STATUS_INACTIVE) {
//			keepAliveStatus = false;
//		}
//
//		if (open) {
//			new Thread(new ChannelHSM()).start();
//		}
//		Thread.sleep(1000);
//	}
//
//	public static boolean send(byte[] message, SessionHandler session)
//throws Exception {
//
//                if (isConnect) {
//                        synchronized (OUT) {
//
//                                session.setQueue_esh_time(readTimeoutValue);
//                                QueueESHSession.addSession(session);
//                                OUT.write(message);
//                                OUT.flush();
//
//                                UtilMethods.debug(session.getSessionId(),
//SysConfigValues.LOG_TYPE_INFOR, session.getOpen_thread_no(),"Sent
//message (ESH): " + conIP ,0);
//                                LogFileHandler.writeIntoLogFile("Sent message to (ESH) : "+
//ISOUtil.hexString(message),session.getSessionId(),
//SysConfigValues.LOG_LEVEL_3,SysConfigValues.LOG_TYPE_RAWDATA);
//
//                                return true;
//                        }
//                }else{
//                        UtilMethods.debug(session.getSessionId(),
//SysConfigValues.LOG_TYPE_ALERT, session.getOpen_thread_no(),"ESH channel
//may dropped ..... and failed", 0);
//                }
//                return false;
//        }
//
//	@Override
//        public void run() {
//
//                while(true){
//
//                        try {
//
//                                if(isConnect){
//
//                                        synchronized (IN) {
//
//                                                int HD_LEN  = 0;
//                                                byte[] HD       = new byte[2];
//
//                                                s.setSoTimeout(0);
//                                                IN.readFully(HD, 0, HD.length);
//
//                                                HD_LEN          = Integer.parseInt(ISOUtil.hexString(HD),16);
//
//                                                APISession apiSession = new APISession();
//                                                apiSession.setLISTENER_ID(channelId);
//                                                apiSession.setLISTENER_LOGFILE_PREFIX("ESH");
//                                                apiSession.setLISTENER_ACCEPTED_DATE_TIME(System.currentTimeMillis()+"");
//                                                //apiSession.setLISTENER_SESSION_ID(UtilMethods.getTransactionId());
//                                                apiSession.setLISTENER_LOGFILE_PATH(InitConfigValues.LOG_PATH);
//                                                apiSession.setLISTENER_PRIVATE_FIELD(SysConfigValues.INTERFACE_ESH);
//
//                                                byte BUFF[] = new byte[HD_LEN];
//
//                                                try{
//
//                                                        UtilMethods.debug(null,SysConfigValues.LOG_TYPE_INFOR,null,
//"Channel (ESH) read length is "+ HD_LEN+" from channel Id :"+channelId
//,0);
//
//                                                        if(HD_LEN > 0){
//                                                                s.setSoTimeout(readTimeoutValue);
//                                                                IN.readFully(BUFF,0,HD_LEN);
//                                                        }
//
//                                                }catch (Exception e){
//                                                        UtilMethods.debug(null,SysConfigValues.LOG_TYPE_INFOR,null,
//"Channel (ESH) read timeout is occurred in channel Id :"+channelId+"
//upon buffer size : "+HD_LEN, 0);
//                                                        HD_LEN = 0;
//                                                }
//
//                                                if(HD_LEN > 0){
//
//                                                        UtilMethods.debug(null,SysConfigValues.LOG_TYPE_INFOR,null,
//"Received message from (ESH): "+conIP+" channel Id :"+channelId , 0);
//                                                        LogFileHandler.writeIntoLogFile("Received message from (ESH) : "+
//ISOUtil.hexString(BUFF),apiSession.getLISTENER_SESSION_ID(),
//SysConfigValues.LOG_LEVEL_3,SysConfigValues.LOG_TYPE_RAWDATA);
//
//                                                        apiSession.setLISTENER_MESSAGE_BUFFER(BUFF);
//                                                        apiSession.setLISTENER_MESSAGE_LENGTH(HD);
//                                                        apiSession.setLISTENER_HEADER_SIZE(2);
//
//                                                        APIPoolInit.REQUEST_Q.add(apiSession);
//
//                                                }else{
//
//                                                        UtilMethods.debug(null,SysConfigValues.LOG_TYPE_INFOR,null,
//"Channel (ESH) on Id "+channelId+" connection is dropped due to reading
//timeout (plese refer alerts of system)",0);
//                                                        disconnect();
//                                                }
//                                        }
//
//                                }else{
//                                        Thread.sleep(3000);
//                                        connect();
//                                }
//
//                        } catch (Exception e) {
//
//                                LogFileHandler.writeErrorIntoLogFile(e, null);
//
//                                try {
//                                        disconnect();
//                                } catch (Exception e1) {
//                                        LogFileHandler.writeErrorIntoLogFile(e1, null);
//                                }
//
//                                String debug = "Channel (engine) on Id " + channelId+ " connection
//is dropped due to the error detection (Please refer error logs for more
//details ... )";
//                                UtilMethods.debug(null, SysConfigValues.LOG_TYPE_ALERT, null, debug,
//0);
//                                LogFileHandler.writeIntoLogFile(debug, null,
//SysConfigValues.LOG_LEVEL_1,SysConfigValues.LOG_TYPE_INIT);
//                                LogFileHandler.writeIntoLogFile(debug, null,
//SysConfigValues.LOG_LEVEL_1,SysConfigValues.LOG_TYPE_ALERT);
//                        }
//                }
//        }
//}