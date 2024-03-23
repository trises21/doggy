/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License version 3
as published by the Free Software Foundation. You may not use, modify
or distribute this program under any other version of the
GNU Affero General Public License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package constants;

import client.MapleClient;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import tools.SystemUtils;

public class ServerConstants implements ServerConstantsMBean {

    public static MapleClient cli = null;
    public static boolean TESPIA = false; // true = uses GMS test server, for MSEA it does nothing though
    public static boolean serverType = false; // true : new, false : old
  
  public static String Gateway_IP = "";    
  public static int serverCount;
  public static String dbHost = "";
  public static String dbUser = "";
  public static String dbPassword = "";
  public static String eventMessage = "";
  public static String serverMessage = "";
  public static String recommendMessage = "";
  public static String serverName = "";
  public static String serverWelcome = "";
  public static String linuxDumpPath = "";
  public static String windowsDumpPath = "";
  public static byte defaultFlag;
  public static byte backGroundImg;
  public static int defaultMaxCharacters;
  public static int defaultMaxChannelLoad;
  public static int defaultExpRate;
  public static int defaultDropRate;
  public static int defaultMesoRate;
  public static int defaultCashRate;
  public static int defaultBossCashRate;
  public static int basePorts;
  public static int LoginPort;
  public static int cashShopPort;
  public static int userlimit;
  public static String adminIP;
  public static int MySQLMINCONNECTION = 10;
  public static int MySQLMAXCONNECTION = 5000;
    public static boolean showPacket = false;
    public static boolean Use_Localhost = false;//Boolean.parseBoolean(ServerProperties.getProperty("net.sf.odinms.world.admin")); // true = packets are logged, false = others can connect to server
    public static boolean Use_SiteDB = false;
    
    public static boolean realese = false;
    public static boolean logChat = true;
    public static boolean logTrade = true;
    public static boolean logItem = true;
    
    public static final short MAPLE_VERSION = (short) 65;
    public static final byte MAPLE_CHECK = 1;
    public static final byte MAPLE_PATCH = 1;
    public static boolean Use_Fixed_IV = false; // true = disable sniffing, false = server can connect to itself
    //public static final byte[] Gateway_IP = {(byte)183,(byte)91,(byte)237,(byte)75};
//    public static final byte[] Gateway_IP = {(byte)192,(byte)168,(byte)31,(byte)21};
    //Inject a DLL that hooks SetupDiGetClassDevsExA and returns 0.
   
    /*
     * Specifics which job gives an additional EXP to party
     * returns the percentage of EXP to increase
     */
    
 protected static String toUni(String kor)
    throws UnsupportedEncodingException
  {
    return new String(kor.getBytes("KSC5601"), "8859_1");
  }    
    public static final byte Class_Bonus_EXP(final int job) {
        switch (job) {
            case 501:
            case 530:
            case 531:
            case 532:
            case 2300:
            case 2310:
            case 2311:
            case 2312:
            case 3100:
            case 3110:
            case 3111:
            case 3112:
            case 800:
            case 900:
            case 910:
                return 10;
        }
        return 0;
    }
    
    public static final int DLL_VERSION = 105;
    
    
    public static List<String> eligibleIP = new LinkedList<String>(), localhostIP = new LinkedList<String>();

    public static enum PlayerGMRank {

        NORMAL('@', 0),
        DONATOR('#', 1),
        SUPERDONATOR('$', 2),
        INTERN('%', 3),
        GM('!', 4),
        SUPERGM('!', 5),
        ADMIN('!', 6);
        private char commandPrefix;
        private int level;

        PlayerGMRank(char ch, int level) {
            commandPrefix = ch;
            this.level = level;
        }

        public char getCommandPrefix() {
            return commandPrefix;
        }

        public int getLevel() {
            return level;
        }
    }

    public static enum CommandType {

        NORMAL(0),
        TRADE(1),
        POKEMON(2);
        private int level;

        CommandType(int level) {
            this.level = level;
        }

        public int getType() {
            return level;
        }
    }

    public static boolean isEligible(final String sessionIP) {
        return eligibleIP.contains(sessionIP.replace("/", ""));
    }


    public static boolean isIPLocalhost(final String sessionIP) {
        return localhostIP.contains(sessionIP.replace("/", "")) && ServerConstants.Use_Localhost;
    }

    static {
        localhostIP.add("127.0.0.1");
    }
    
    //Packeges.constants.localhostIP.remove("183.91.251.28");
    public static ServerConstants instance;

    public void run() {
        updateIP();
    }

  public void updateIP() {
    eligibleIP.clear();
    String[] eligibleIPs = { "14.35.237.177",adminIP };
    for (int i = 0; i < eligibleIPs.length; i++)
      try {
        eligibleIP.add(InetAddress.getByName(eligibleIPs[i]).getHostAddress().replace("/", ""));
      }
      catch (Exception e) {
      }
  }

    public static void registerMBean() {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            instance = new ServerConstants();
            instance.updateIP();
            mBeanServer.registerMBean(instance, new ObjectName("constants:type=ServerConstants"));
        } catch (Exception e) {
            System.out.println("Error registering Shutdown MBean");
            e.printStackTrace();
        }
    }

    
    
    
public static String getServerHost(MapleClient ha)
  {
    try
    {
      return InetAddress.getByName(Gateway_IP).getHostAddress().replace("/", "");
    } catch (Exception e) {
      if (!realese)
        e.printStackTrace();
    }
    return Gateway_IP;
  }

  static
  {
    try
    {
      FileInputStream msg = new FileInputStream("VaconMaple.ini");
      Properties msgprobs = new Properties();
      msgprobs.load(msg);
      msg.close();
      eventMessage = new String(msgprobs.getProperty(toUni("채널메세지")).getBytes("ISO-8859-1"), "euc-kr");
      serverMessage = new String(msgprobs.getProperty(toUni("서버메세지")).getBytes("ISO-8859-1"), "euc-kr");
      recommendMessage = new String(msgprobs.getProperty(toUni("추천메세지")).getBytes("ISO-8859-1"), "euc-kr");

      serverName = new String(msgprobs.getProperty(toUni("서버이름")).getBytes("ISO-8859-1"), "euc-kr");

      FileInputStream conf1 = new FileInputStream("VaconMaple.ini");
      Properties probs = new Properties();
      probs.load(conf1);
      conf1.close();
      
      Gateway_IP = probs.getProperty(toUni("자신의아이피"));
      adminIP = probs.getProperty(toUni("자신의아이피"));

      FileInputStream db = new FileInputStream("VaconMaple.ini");
      Properties dbprobs = new Properties();
      dbprobs.load(db);
      db.close();
      dbHost = "jdbc:mysql://" + dbprobs.getProperty(toUni("자신의아이피")) + ":3306/vaconmaple?autoReconnect=true&characterEncoding=euckr";

      dbUser = "root";
      dbPassword = dbprobs.getProperty(toUni("마이쿼리비번"));

      linuxDumpPath = "/opt/lampp/bin/";
      windowsDumpPath = "C:\\Program Files (x86)\\MySQL\\MySQL Server 6.0\\bin";

      serverCount = Integer.parseInt(probs.getProperty(toUni("서버갯수")));
      defaultMaxCharacters = Integer.parseInt(probs.getProperty(toUni("최대캐릭터갯수")));

      userlimit = Integer.parseInt(probs.getProperty(toUni("최대인원")));
      defaultMaxChannelLoad = 80;
      defaultExpRate = Integer.parseInt(probs.getProperty(toUni("경험치배율")));
      defaultDropRate = Integer.parseInt(probs.getProperty(toUni("드롭배율")));
      defaultMesoRate = Integer.parseInt(probs.getProperty(toUni("메소배율")));
      defaultCashRate = Integer.parseInt(probs.getProperty(toUni("캐쉬배율")));
      defaultBossCashRate = Integer.parseInt(probs.getProperty(toUni("보스캐쉬배율")));

      defaultFlag = Byte.parseByte(probs.getProperty(toUni("이벤트깃발")));

      basePorts = Integer.parseInt(probs.getProperty(toUni("채널포트")));
      LoginPort = Integer.parseInt(probs.getProperty(toUni("로그인포트")));
      cashShopPort = Integer.parseInt(probs.getProperty(toUni("캐시샵포트"))); 
      
    }
    catch (Exception e)
    {
      System.err.println("[Error] Failed of Database ini File load");
      if (!realese) {
        e.printStackTrace();
      }

    }
eligibleIP = new LinkedList();
localhostIP = new LinkedList();
    localhostIP.add("127.0.0.1");
  }    
}

