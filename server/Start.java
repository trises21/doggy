package server;

import client.MapleCharacter;
import client.MapleClient;
import client.SkillFactory;
import client.inventory.MapleInventoryIdentifier;
import constants.ServerConstants;
import database.DatabaseConnection;
import database.MYSQL;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.channel.MapleGuildRanking;
import handling.etc.EtcServer;
import handling.etc.handler.EtcHandler;
import handling.login.LoginInformationProvider;
import handling.login.LoginServer;
import handling.world.World;
import handling.world.family.MapleFamily;
import handling.world.guild.MapleGuild;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.JOptionPane;
import server.AutobanManager;
import server.Debugger;
import server.ItemInformation;
import server.MedalRanking;
import server.PacketSender;
import server.RankingWorker;
import server.ServerProperties;
import server.ShutdownServer;
import server.Timer.BuffTimer;
import server.Timer.CheatTimer;
import server.Timer.CloneTimer;
import server.Timer.EtcTimer;
import server.Timer.EventTimer;
import server.Timer.MapTimer;
import server.Timer.PingTimer;
import server.Timer.WorldTimer;
import server.events.MapleOxQuizFactory;
import server.life.MapleLifeFactory;
import server.life.MapleMonsterInformationProvider;
import server.life.MobSkillFactory;
import server.life.PlayerNPC;
import server.log.DBLogger;
import server.maps.MapleMap;
import server.marriage.MarriageManager;
import server.quest.MapleQuest;
import server.shops.MinervaOwlSearchTop;
import tools.DatabaseBackup;
import tools.DeadLockDetector;
import tools.MaplePacketCreator;
import tools.MemoryUsageWatcher;
import tools.StringUtil;
import tools.SystemUtils;
import tools.packet.EtcPacket;
import tools.packet.Packet;


public class Start implements ActionListener {
    
    public static AtomicInteger CompletedLoadingThreads = new AtomicInteger(0);
    public static long startTime = System.currentTimeMillis();     
    
    
    //<editor-fold defaultstate="collapsed" desc="private">
    private MapleCharacter getPlayer(String name) {
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            if (cserv.getPlayerStorage().getCharacterByName(name) != null)
                return cserv.getPlayerStorage().getCharacterByName(name);
        }
        return null;
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="선언">
    static String OpenState, IsOpen, txtMsg, txtOpt;
    static int intOpt;
    static boolean boolHotTime;
    JFrame frame_1;
    JLabel exprate, droprate, mesorate, Eexprate, Edroprate, Emesorate, Eeventp, Eeventnote;
    JLabel burningCheck;
    JLabel label_1, label_2, label_3, label_4;
    JLabel label;
    JTextField txtfield;
    JTextField txtfield1;
    JTextField txtfield2;
    JTextField txtfield3;
    JTextField txtfield4;
    JTextField txtfielde, txtfieldd, txtfieldm;
    JRadioButton cb1;
    JRadioButton cb2;
    JRadioButton cb3;
    JRadioButton cb4;
    JButton btn_1, btn_2;
    JCheckBox check1;
    JRadioButton mn1;
    JRadioButton mn2;
    JRadioButton mn3;
    JRadioButton map1;
    JRadioButton map2;
    JRadioButton map3;
    JButton rate;
    
    static String yesno_title, yesno_msg;
    
    static TrayIcon trayicon = null;
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="버튼클릭시">
    @Override
    public void actionPerformed(ActionEvent e) {
        String mode = e.getActionCommand();
        if (mode.equals("launch")) { 
            if (OpenState.equals("OFF")) {
                OpenState = "ON";
                IsOpen = "ing";
                Start.LaunchServer ls = new Start.LaunchServer();
                ls.start();
            } else {
                txtMsg = "서버가 이미 구동중입니다.";
                txtOpt = "오류";
                intOpt = JOptionPane.ERROR_MESSAGE;
                Start.Alert al = new Start.Alert();
                al.start();
            }
        } else if (mode.equals("gmregist")) {
            if(OpenState.equals("ON") && !IsOpen.equals("ing")) {
                Start.RegistGM rg = new Start.RegistGM();
                rg.start();
            } else {
                txtMsg = "서버가 구동중일때만 가능합니다.";
                txtOpt = "오류";
                intOpt = JOptionPane.ERROR_MESSAGE;
                Start.Alert al = new Start.Alert();
                al.start();
            }
        } else if (mode.equals("gmremove")) {
            if(OpenState.equals("ON") && !IsOpen.equals("ing")) {
                Start.RemoveGM rg = new Start.RemoveGM();
                rg.start();
            } else {
                txtMsg = "서버가 구동중일때만 가능합니다.";
                txtOpt = "오류";
                intOpt = JOptionPane.ERROR_MESSAGE;
                Start.Alert al = new Start.Alert();
                al.start();
            }
        } else if (mode.equals("maker")) {
            Start.JMaker jtr = new Start.JMaker();
            jtr.start();            
        } else if (mode.equals("tray")) {
            yesno_title = "트레이로";
            yesno_msg = "트레이로 보내시겠습니까?";
            Start.JTray jtr = new Start.JTray();
            jtr.start();
        } else if (mode.equals("tray_backup")) {
            if(trayicon!=null) {
                frame_1.setVisible(true);
                SystemTray.getSystemTray().remove(trayicon);
            }
        } else if (mode.equals("end")) {
            yesno_title = "종료";
            yesno_msg = "정말로 종료하시겠습니까?";
            Start.JEnd je = new Start.JEnd();
            je.start();
        } else if (mode.equals("Burning")) {
            if (OpenState.equals("ON") && !IsOpen.equals("ing")) {
                Start.Burning br = new Start.Burning();
                br.start();
            } else {
                txtMsg = "서버가 구동중일때만 가능합니다.";
                txtOpt = "오류";
                intOpt = JOptionPane.ERROR_MESSAGE;
                Start.Alert al = new Start.Alert();
                al.start();
            }
        } else if (mode.equals("Hottime")) {
            if (OpenState.equals("ON") && !IsOpen.equals("ing")) {
                Start.Hottime ht = new Start.Hottime();
                ht.start();
            } else {
                txtMsg = "서버가 구동중일때만 가능합니다.";
                txtOpt = "오류";
                intOpt = JOptionPane.ERROR_MESSAGE;
                Start.Alert al = new Start.Alert();
                al.start();
            }
          } else if (mode.equals("Change")) {
            if (OpenState.equals("ON") && !IsOpen.equals("ing")) {
                Start.Change rg = new Start.Change();
                rg.start();
            } else {
                txtMsg = "서버가 구동중일때만 가능합니다.";
                txtOpt = "오류";
                intOpt = JOptionPane.ERROR_MESSAGE;
                Start.Alert al = new Start.Alert();
                al.start();
            }
           } else if (mode.equals("notice")) {
            if (OpenState.equals("ON") && !IsOpen.equals("ing")) {
                Start.notice nt = new Start.notice();
                nt.start();
            } else {
                txtMsg = "서버가 구동중일때만 가능합니다.";
                txtOpt = "오류";
                intOpt = JOptionPane.ERROR_MESSAGE;
                Start.Alert al = new Start.Alert();
                al.start();
            }
           } else if (mode.equals("givemn")) {
            if (OpenState.equals("ON") && !IsOpen.equals("ing")) {
                Start.givemn gn = new Start.givemn();
                gn.start();
            } else {
                txtMsg = "서버가 구동중일때만 가능합니다.";
                txtOpt = "오류";
                intOpt = JOptionPane.ERROR_MESSAGE;
                Start.Alert al = new Start.Alert();
                al.start();
            }
           } else if (mode.equals("give1")) {
            if (OpenState.equals("ON") && !IsOpen.equals("ing")) {
                Start.give1 gv1 = new Start.give1();
                gv1.start();
            } else {
                txtMsg = "서버가 구동중일때만 가능합니다.";
                txtOpt = "오류";
                intOpt = JOptionPane.ERROR_MESSAGE;
                Start.Alert al = new Start.Alert();
                al.start();
            }
           } else if (mode.equals("give2")) {
            if (OpenState.equals("ON") && !IsOpen.equals("ing")) {
                Start.give2 gv2 = new Start.give2();
                gv2.start();
            } else {
                txtMsg = "서버가 구동중일때만 가능합니다.";
                txtOpt = "오류";
                intOpt = JOptionPane.ERROR_MESSAGE;
                Start.Alert al = new Start.Alert();
                al.start();
            }
           } else if (mode.equals("give3")) {
            if (OpenState.equals("ON") && !IsOpen.equals("ing")) {
                Start.give3 gv3 = new Start.give3();
                gv3.start();
            } else {
                txtMsg = "서버가 구동중일때만 가능합니다.";
                txtOpt = "오류";
                intOpt = JOptionPane.ERROR_MESSAGE;
                Start.Alert al = new Start.Alert();
                al.start();
            }
           } else if (mode.equals("login")) {
            if (OpenState.equals("ON") && !IsOpen.equals("ing")) {
                Start.login lg = new Start.login();
                lg.start();
            } else {
                txtMsg = "서버가 구동중일때만 가능합니다.";
                txtOpt = "오류";
                intOpt = JOptionPane.ERROR_MESSAGE;
                Start.Alert al = new Start.Alert();
                al.start();
            }
           } else if (mode.equals("warp")) {
            if (OpenState.equals("ON") && !IsOpen.equals("ing")) {
                Start.warp wp = new Start.warp();
                wp.start();
            } else {
                txtMsg = "서버가 구동중일때만 가능합니다.";
                txtOpt = "오류";
                intOpt = JOptionPane.ERROR_MESSAGE;
                Start.Alert al = new Start.Alert();
                al.start();
            }
           } else if (mode.equals("cashgive")) {
            if (OpenState.equals("ON") && !IsOpen.equals("ing")) {
                Start.cashgive cg = new Start.cashgive();
                cg.start();
            } else {
                txtMsg = "서버가 구동중일때만 가능합니다.";
                txtOpt = "오류";
                intOpt = JOptionPane.ERROR_MESSAGE;
                Start.Alert al = new Start.Alert();
                al.start();
            }
           } else if (mode.equals("mesogive")) {
            if (OpenState.equals("ON") && !IsOpen.equals("ing")) {
                Start.mesogive mg = new Start.mesogive();
                mg.start();
            } else {
                txtMsg = "서버가 구동중일때만 가능합니다.";
                txtOpt = "오류";
                intOpt = JOptionPane.ERROR_MESSAGE;
                Start.Alert al = new Start.Alert();
                al.start();
            }
           } else if (mode.equals("itemgive")) {
            if (OpenState.equals("ON") && !IsOpen.equals("ing")) {
                Start.itemgive ig = new Start.itemgive();
                ig.start();
            } else {
                txtMsg = "서버가 구동중일때만 가능합니다.";
                txtOpt = "오류";
                intOpt = JOptionPane.ERROR_MESSAGE;
                Start.Alert al = new Start.Alert();
                al.start();
            }
           } else if (mode.equals("rate")) {
            if (OpenState.equals("ON") && !IsOpen.equals("ing")) {
                Start.Rate r = new Start.Rate();
                r.start();
            } else {
                txtMsg = "서버가 구동중일때만 가능합니다.";
                txtOpt = "오류";
                intOpt = JOptionPane.ERROR_MESSAGE;
                Start.Alert al = new Start.Alert();
                al.start();
            }
           } else if (mode.equals("blog")) { 
                Start.Blog bg = new Start.Blog();
                bg.start();
            }            
        }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="생성자">
    
    //<editor-fold defaultstate="collapsed" desc="START">
    public Start() {
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="프레임">
    public Start(JFrame frame_1) {
        this.frame_1 = frame_1;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="라벨1">
    public Start(JLabel label_1, JLabel exprate, JLabel droprate, JLabel mesorate, JLabel Eexprate, JLabel Edroprate, JLabel Emesorate, JLabel Eeventp, JLabel Eeventnote){ //생성자
        this.label_1 = label_1; 
        this.exprate = exprate;
        this.droprate = droprate;
        this.mesorate = mesorate;
        this.Eexprate = Eexprate; 
        this.Edroprate = Edroprate;
        this.Emesorate = Emesorate;
        this.Eeventp = Eeventp;
        this.Eeventnote = Eeventnote;  
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="라벨2, 버튼1">
    public Start(JLabel label, JLabel label_1, JButton btn_1){ //생성자
        this.label = label; 
        this.label_1 = label_1; 
        this.btn_1 = btn_1;        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="텍스트박스1">
    public Start(JTextField txtfield){ //생성자
        this.txtfield = txtfield;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="라벨2, 텍스트1">
    public Start(JLabel label_1, JLabel label_2, JTextField txtfield){ 
        this.label_1 = label_1; 
        this.label_2 = label_2;
        this.txtfield = txtfield;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="텍스트박스4">
    public Start(JTextField txtfield1, JTextField txtfield2, JTextField txtfield3, JTextField txtfield4){ //생성자
        this.txtfield1 = txtfield1;
        this.txtfield2 = txtfield2;
        this.txtfield3 = txtfield3;
        this.txtfield4 = txtfield4;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="체크박스1, 버튼2, 라벨3, 텍스트박스1">
    public Start(JCheckBox check1, JButton btn_1, JButton btn_2, JLabel label_1, JLabel label_2, JLabel label_3, JTextField txtfield) {
        this.check1 = check1;
        this.btn_1 = btn_1;
        this.btn_2 = btn_2;
        this.label_1 = label_1;
        this.label_2 = label_2;
        this.label_3 = label_3;
        this.txtfield = txtfield;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="텍스트박스2">
    public Start(JTextField txtfield1, JTextField txtfield2){ //생성자
        this.txtfield1 = txtfield1;
        this.txtfield2 = txtfield2;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="텍스트박스3">
    public Start(JTextField txtfield1, JTextField txtfield2, JTextField txtfield3){ //생성자
        this.txtfield1 = txtfield1;
        this.txtfield2 = txtfield2;
        this.txtfield3 = txtfield3;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="공지사항생성자">
    public Start(JTextField txtfield, JRadioButton cb, JRadioButton cb1, JRadioButton cb2, JRadioButton cb3){ //생성자
        this.txtfield = txtfield;
        this.cb1 = cb;
        this.cb2 = cb1;
        this.cb3 = cb2;
        this.cb4 = cb3;        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="텍스트박스1, 라디오버튼3">
    public Start(JTextField txtfield, JRadioButton chack, JRadioButton chack1, JRadioButton chack2){ //생성자
        this.txtfield = txtfield;
        this.mn1 = chack;
        this.mn2 = chack1;
        this.mn3 = chack2;
        this.map1 = chack;
        this.map2 = chack1;
        this.map3 = chack2;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="라벨1, 텍스트박스1">
    public Start(JLabel label_1, JTextField txtfield){ //생성자
        this.label_1 = label_1;
        this.txtfield = txtfield;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="버튼1">
    public Start(JButton btn_1) {
        this.btn_1 = btn_1;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="버튼1 라벨 3">
    public Start(JButton rate, JLabel exprate, JLabel droprate, JLabel mesorate, JTextField txtfielde, JTextField txtfieldd, JTextField txtfieldm) {
        this.rate = rate;
        this.exprate = exprate;
        this.droprate = droprate;
        this.mesorate = mesorate;
        this.txtfielde = txtfielde;
        this.txtfieldd = txtfieldd;
        this.txtfieldm = txtfieldm;
    }
    //</editor-fold>
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="메인">
    public static void main(String args[]) {
        OpenState = "OFF";
        
        //<editor-fold defaultstate="collapsed" desc="프레임 선언">
        JFrame frame = new JFrame("VaconMaple 1.2.65");
        Container cp = frame.getContentPane();
        JTabbedPane jtp = new JTabbedPane();
        JPanel panel_server = new JPanel();
        JPanel panel_account = new JPanel();
        JPanel panel_event = new JPanel();
        JPanel panel_give = new JPanel();
        JPanel panel_notice = new JPanel();
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="프레임설정">
        frame.setLocation(300, 200);
        frame.setPreferredSize(new Dimension(350, 420));
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.setResizable(false);
        File f = new File("");
        Image icoimage = null;
        try {
            icoimage = Toolkit.getDefaultToolkit().getImage(f.getCanonicalPath() + "\\icon.gif");
        } catch (Exception e) {
            
        }
        if (icoimage != null) {
            frame.setIconImage(icoimage);
        }
        //</editor-fold>
  
        //<editor-fold defaultstate="collapsed" desc="추가할 항목 선언">
        JSeparator jsp_1 = new JSeparator();
        JSeparator jsp_2 = new JSeparator();
        JSeparator jsp_3 = new JSeparator();
        JSeparator jsp_4 = new JSeparator();
        JSeparator jsp_5 = new JSeparator();
        JSeparator jsp_6 = new JSeparator();
        JSeparator jsp_7 = new JSeparator();
        JSeparator jsp_8 = new JSeparator();
        
        JButton BtnServerOpen = new JButton("서버시작");
        JButton BtnServerRestart = new JButton("VaconMaple");
        JButton BtnGMRegist = new JButton("운영자등록");
        JButton BtnGMRemove = new JButton("운영자박탈");
        
        JLabel lblChState_dumy = new JLabel("서버상태 : ");
        JLabel lblChState = new JLabel("OFF");
        JLabel lblNick = new JLabel("닉네임 : ");
        
        JTextField txtNick = new JTextField("");
                
        JMenuBar menu = new JMenuBar();
        JMenu mnu_file=new JMenu("파일");
        JMenuItem mnu_item_tray = new JMenuItem("트레이로 보내기(T)", 'T');
        JSeparator mnu_item_spl = new JSeparator();
        JMenuItem mnu_maker = new JMenuItem("　             개발자(M)", 'M');
        JSeparator mnu_makerspl = new JSeparator();
        JMenuItem mnu_item_end = new JMenuItem("　　　　　 종료(E)", 'E');
        
        JButton BtnBurning = new JButton("버닝타임");
        JLabel lblBurninging = new JLabel("버닝타임 :");
        JLabel lblBurning = new JLabel("미진행");
        
        JButton BtnHottime = new JButton("핫타임");
        JLabel lblitem = new JLabel("아이템 : ");
        JTextField txtitem = new JTextField("2000000");
        JLabel lblamount = new JLabel("갯수 : ");
        JTextField txtamount = new JTextField("0");
                JLabel lblitem1 = new JLabel("아이템 : ");
        JTextField txtitem1 = new JTextField("2000000");
        JLabel lblamount1 = new JLabel("갯수 : ");
        JTextField txtamount1 = new JTextField("0");
        
        JButton BtnChange = new JButton("닉네임변경");
        JLabel lblNick2 = new JLabel("변경 : ");
        JTextField txtNick2 = new JTextField("");
        
        JTextField txtnotice = new JTextField("공지사항 내용을 입력하세요!!");
        JRadioButton cbnotice = new JRadioButton("팝업공지사항");
        JRadioButton cbnotice1 = new JRadioButton("노란색공지사항");
        JRadioButton cbnotice2 = new JRadioButton("분홍색공지사항");
        JRadioButton cbnotice3 = new JRadioButton("파랑색공지사항");
        JButton Btnnotice = new JButton("공지보내기");
        
        
        JLabel lblname = new JLabel("닉네임 : ");
        JTextField txtname = new JTextField("");
        JRadioButton mncheck = new JRadioButton("후원 5000원");
        JRadioButton mncheck1 = new JRadioButton("후원 10000원");
        JRadioButton mncheck2 = new JRadioButton("후원 15000원");
        JButton Btngivemn = new JButton("후원아이템");
        JButton Btngive1 = new JButton("시간의 돌");
        JButton Btngive2 = new JButton("혼돈의 주문서");
        JButton Btngive3 = new JButton("백의 주문서");
        
        JButton Btnlogin = new JButton("접속수확인");
        
        JLabel Maker1 = new JLabel("Pack Name : VaconMaple");
        JLabel Maker1E = new JLabel("Developer : 꽃선우 (tjsdn9666@naver.com)");
        JLabel Maker2 = new JLabel("Version : KMS 1.2.65");
        JLabel Maker3 = new JLabel("지금 사용하고 계신 바콘팩의 버전은 0.5입니다.");
        JLabel Maker4 = new JLabel("무단기반, 무단수정, 무단배포 금지");
        
        JLabel ratenote = new JLabel("VaconMaple 1.2.65 !");
        JLabel exprate = new JLabel("경험치배율 :");
        JLabel droprate = new JLabel("드롭배율 :");
        JLabel mesorate = new JLabel("메소배율 :");
        JLabel exprate2 = new JLabel("서버미구동");
        JLabel droprate2 = new JLabel("서버미구동");
        JLabel mesorate2 = new JLabel("서버미구동"); 
        JButton rate = new JButton("배율바꾸기");
        
        JLabel Event = new JLabel("[ 현재 진행중인 이벤트 ]");
        JLabel EventNote = new JLabel("이벤트 내용");
        JLabel EventRate = new JLabel("이벤트 배율");
        JLabel EventP = new JLabel("이벤트 기간");
        
        JLabel EventNote1 = new JLabel("　       서버미구동 (서버를 먼저 구동시켜주세요)");
        JLabel EventRateExp = new JLabel("경험치배율");
        JLabel EventRateDrop = new JLabel("드롭배율");
        JLabel EventRateMeso = new JLabel("메소배율");
        JLabel EventRateExp1 = new JLabel("서버미구동");
        JLabel EventRateDrop1 = new JLabel("서버미구동");
        JLabel EventRateMeso1 = new JLabel("서버미구동");
        JLabel EventP1 = new JLabel("서버미구동 (서버를 먼저 구동시켜주세요)");
        
        JButton Btnwarp = new JButton("마을이동");
        JRadioButton mapcheck1 = new JRadioButton("헤네시스");
        JRadioButton mapcheck2 = new JRadioButton("자유시장");
        JRadioButton mapcheck3 = new JRadioButton("검은맵");
        
        JLabel lblname2 = new JLabel ("닉네임 : ");
        JTextField txtname2 = new JTextField("");
        
        JLabel lblcashgive = new JLabel ("캐시량 : ");
        JTextField txtcashgive = new JTextField("0");
        JButton Btncashgive = new JButton("캐시지급");
        
        JLabel lblmesogive = new JLabel ("메소량 : ");
        JTextField txtmesogive = new JTextField("0");
        JButton Btnmesogive = new JButton("메소지급");
        
        JLabel lblitemgive = new JLabel ("아이템 : ");
        JLabel lblitemamount = new JLabel ("갯수 : ");
        JTextField txtitemgive = new JTextField("0000000");
        JTextField txtitemamount = new JTextField("0");
        JButton Btnitemgive = new JButton("아이템지급");
        
        JLabel ExpR = new JLabel("경험치 : ");
        JLabel DropR = new JLabel("드롭 : ");
        JLabel MesoR = new JLabel("메소 : ");
        
        JTextField ExpR1 = new JTextField("0");
        JTextField DropR1 = new JTextField("0");
        JTextField MesoR1 = new JTextField("0");    
        
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="탭추가">
        jtp.add("서버", panel_server);
        jtp.add("계정", panel_account);   
        jtp.add("이벤트", panel_event);   
        jtp.add("지급", panel_give);
        jtp.add("공지", panel_notice);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="프레임에 항목추가">
        cp.add(jtp);
        frame.setJMenuBar(menu);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="메뉴에 항목추가">
        menu.add(mnu_file);
        mnu_file.add(mnu_item_tray);
        mnu_file.add(mnu_item_spl);
        mnu_file.add(mnu_maker);
        mnu_file.add(mnu_makerspl);
        mnu_file.add(mnu_item_end);
        mnu_item_spl.setForeground(Color.LIGHT_GRAY);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="메뉴단축키 설정">
        mnu_item_tray.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,KeyEvent.CTRL_MASK));
        mnu_item_end.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,KeyEvent.CTRL_MASK));
        mnu_maker.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,KeyEvent.CTRL_MASK));
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="메뉴버튼 클릭시">
        mnu_item_tray.addActionListener(new Start(frame));
        mnu_item_tray.setActionCommand("tray");
        mnu_item_end.addActionListener(new Start());
        mnu_item_end.setActionCommand("end");
        mnu_maker.addActionListener(new Start(frame));
        mnu_maker.setActionCommand("maker");
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="서버패널에 항목추가">
        panel_server.add(BtnServerOpen);
        panel_server.add(BtnServerRestart);
        panel_server.add(jsp_1); //줄
        panel_server.add(lblChState_dumy);
        panel_server.add(lblChState);
        
        panel_server.add(jsp_2); //줄
        
        panel_server.add(Btnlogin);
        panel_server.add(jsp_3); //줄
        
        panel_server.add(ExpR);
        panel_server.add(DropR);
        panel_server.add(MesoR);
        panel_server.add(ExpR1);
        panel_server.add(DropR1);
        panel_server.add(MesoR1);

        panel_server.add(rate); //배율리뉴얼
        panel_server.add(ratenote); //배율안내
        panel_server.add(exprate); //경험치배율
        panel_server.add(droprate); //드롭배율
        panel_server.add(mesorate); //메소배율
        panel_server.add(exprate2); //경험치배율
        panel_server.add(droprate2); //드롭배율
        panel_server.add(mesorate2); //메소배율      
        
        panel_server.add(Maker1); //관리기기반
        panel_server.add(Maker1E); //관리기기반        
        panel_server.add(Maker2); //관리기기반
        panel_server.add(Maker3); //관리기기반
        panel_server.add(Maker4); //관리기기반 
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="계정패널에 항목추가">
        panel_account.add(lblNick);
        panel_account.add(txtNick);
        panel_account.add(jsp_4); //줄
        
        panel_account.add(BtnGMRegist);
        panel_account.add(BtnGMRemove);
        
        panel_account.add(BtnChange);
        panel_account.add(txtNick2);
        panel_account.add(lblNick2);
        
        panel_account.add(Btnwarp);
        ButtonGroup map = new ButtonGroup();
        map.add(mapcheck1);
        map.add(mapcheck2);
        map.add(mapcheck3);
        panel_account.add(mapcheck1);
        panel_account.add(mapcheck2);
        panel_account.add(mapcheck3);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="이벤트패널에 항목추가">
        panel_event.add(jsp_5); //줄
        
        panel_event.add(BtnBurning);
        panel_event.add(lblBurning);
        panel_event.add(lblBurninging);
        
        panel_event.add(BtnHottime);
        panel_event.add(txtitem);
        panel_event.add(lblitem);
        panel_event.add(txtamount);
        panel_event.add(lblamount);
        panel_event.add(txtitem1);
        panel_event.add(lblitem1);
        panel_event.add(txtamount1);
        panel_event.add(lblamount1);
        
        
        panel_event.add(Event);
        panel_event.add(EventNote);
        panel_event.add(EventNote1);
        panel_event.add(EventRate);
        panel_event.add(EventRateExp);
        panel_event.add(EventRateDrop);
        panel_event.add(EventRateMeso);
        panel_event.add(EventRateExp1);
        panel_event.add(EventRateDrop1);
        panel_event.add(EventRateMeso1);        
        panel_event.add(EventP);
        panel_event.add(EventP1);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="지급패널에 항목추가">
        panel_give.add(lblname);
        panel_give.add(txtname);
        
        panel_give.add(lblcashgive);
        panel_give.add(txtcashgive);
        panel_give.add(Btncashgive);
        
        panel_give.add(lblmesogive);
        panel_give.add(txtmesogive);
        panel_give.add(Btnmesogive);
        
        panel_give.add(lblitemgive);
        panel_give.add(lblitemamount);
        panel_give.add(txtitemgive);
        panel_give.add(txtitemamount);
        panel_give.add(Btnitemgive);
        
        ButtonGroup mn = new ButtonGroup();
        panel_give.add(jsp_6); //줄
        mn.add(mncheck);
        mn.add(mncheck1);
        mn.add(mncheck2);
        panel_give.add(mncheck);
        panel_give.add(mncheck1);
        panel_give.add(mncheck2);
        panel_give.add(Btngivemn);
        panel_give.add(jsp_7); //줄
        panel_give.add(Btngive1);
        panel_give.add(Btngive2);
        panel_give.add(Btngive3);
        panel_give.add(jsp_8); //줄
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="공지패널에 항목추가">
        ButtonGroup gb = new ButtonGroup();
        panel_notice.add(txtnotice);
        gb.add(cbnotice);
        gb.add(cbnotice1);
        gb.add(cbnotice2);
        gb.add(cbnotice3);
        panel_notice.add(cbnotice);
        panel_notice.add(cbnotice1);
        panel_notice.add(cbnotice2);
        panel_notice.add(cbnotice3);
        panel_notice.add(Btnnotice);
        
        //</editor-fold>
        
        Font f1 = new Font("굴림", Font.PLAIN, 12);
        Font f2 = new Font("굴림", Font.PLAIN, 11);
        Font Maker = new Font("굴림", 1, 15);
        Font f3 = new Font("굴림", 1, 11);
        
        //<editor-fold defaultstate="collapsed" desc="서버패널 항목들 좌표 변경">
        panel_server.setLayout(null);
        panel_account.setLayout(null);
        panel_event.setLayout(null);
        panel_give.setLayout(null);
        panel_notice.setLayout(null);
        
        BtnServerOpen.setLocation(10, 10);
        BtnServerOpen.setSize(155, 30);
        BtnServerOpen.setFont(f1);
        BtnServerRestart.setLocation(175, 10);
        BtnServerRestart.setSize(155, 30);
        BtnServerRestart.setFont(f1);
        lblChState_dumy.setLocation(120, 40);
        lblChState_dumy.setSize(80, 20);
        lblChState_dumy.setFont(f1);
        lblChState_dumy.setForeground(Color.black);
        lblChState.setLocation(180, 40);
        lblChState.setSize(100, 20);
        lblChState.setFont(f1);
        lblChState.setForeground(Color.RED);
        jsp_1.setLocation(0, 68);
        jsp_1.setSize(400, 1);
        
        
        jsp_2.setLocation(0, 160);
        jsp_2.setSize(400, 1);
        
        
        jsp_3.setLocation(0, 216);
        jsp_3.setSize(400, 1);
        
        ExpR.setLocation(10, 180);
        DropR.setLocation(120, 180);
        MesoR.setLocation(230, 180);
        ExpR.setSize(300,20);
        DropR.setSize(300,20);
        MesoR.setSize(300,20);
        ExpR.setFont(Maker);
        DropR.setFont(Maker);
        MesoR.setFont(Maker);
        
        ExpR1.setLocation(75, 180);
        DropR1.setLocation(170, 180);
        MesoR1.setLocation(280, 180);
        ExpR1.setSize(35,20);
        DropR1.setSize(35,20);
        MesoR1.setSize(35,20);
        ExpR1.setFont(f1);
        DropR1.setFont(f1);
        MesoR1.setFont(f1);        
        
        Btnlogin.setLocation(10, 229);
        Btnlogin.setSize(105, 60);
        Btnlogin.setFont(f1);
        
        rate.setLocation(10, 300);
        rate.setSize(105, 33);
        rate.setFont(f1);        
        
        ratenote.setLocation(150, 230);
        ratenote.setSize(300, 20);
        ratenote.setFont(f1); 
        exprate.setLocation(150, 260);
        exprate.setSize(300, 20);
        exprate.setFont(f1); 
        exprate.setForeground(Color.black);
        droprate.setLocation(150, 285);
        droprate.setSize(300, 20);
        droprate.setFont(f1); 
        droprate.setForeground(Color.black);        
        mesorate.setLocation(150, 310);
        mesorate.setSize(300, 20);
        mesorate.setFont(f1); 
        mesorate.setForeground(Color.black);
        exprate2.setLocation(245, 260);
        exprate2.setSize(100, 20);
        exprate2.setFont(f1); 
        exprate2.setForeground(Color.blue);
        droprate2.setLocation(230, 285);
        droprate2.setSize(100, 20);
        droprate2.setFont(f1); 
        droprate2.setForeground(Color.blue);        
        mesorate2.setLocation(230, 310);
        mesorate2.setSize(100, 20);
        mesorate2.setFont(f1); 
        mesorate2.setForeground(Color.blue); 
        
        Maker1.setLocation(60, 70);
        Maker1.setSize(300, 20);
        Maker1.setFont(f1);
        Maker1.setForeground(Color.red);
        Maker1E.setLocation(60, 85);
        Maker1E.setSize(300, 20);
        Maker1E.setFont(f1);
        Maker1E.setForeground(Color.red);
        Maker2.setLocation(60, 100);
        Maker2.setSize(300, 20);
        Maker2.setFont(f1);
        Maker2.setForeground(Color.red);
        Maker3.setLocation(60, 115);
        Maker3.setSize(300, 20);
        Maker3.setFont(f1);
        Maker3.setForeground(Color.red);     
        Maker4.setLocation(60, 130);
        Maker4.setSize(300, 20);
        Maker4.setFont(f1);
        Maker4.setForeground(Color.red); 
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="계정패널 항목들 좌표 변경">
        lblNick.setLocation(100, 10);
        lblNick.setSize(60, 18);
        lblNick.setFont(f1);
        lblNick.setForeground(Color.black);
        txtNick.setLocation(150, 10);
        txtNick.setSize(80, 20);
        txtNick.setFont(f1);
        jsp_4.setLocation(0, 35);
        jsp_4.setSize(400, 1);
        
        BtnGMRegist.setLocation(10, 45);
        BtnGMRegist.setSize(120, 35);
        BtnGMRegist.setFont(f1);
        BtnGMRemove.setLocation(10, 85);
        BtnGMRemove.setSize(120, 35);
        BtnGMRemove.setFont(f1);
        
        BtnChange.setLocation(10, 150);
        BtnChange.setSize(120, 40);
        BtnChange.setFont(f1);
        lblNick2.setLocation(10, 130);
        lblNick2.setSize(80, 20);
        lblNick2.setFont(f1);
        lblNick2.setForeground(Color.black);
        txtNick2.setLocation(50, 130);
        txtNick2.setSize(80, 20);
        txtNick2.setFont(f1);
        
        Btnwarp.setLocation(155, 45);
        Btnwarp.setSize(100, 70);
        Btnwarp.setFont(f1);
        mapcheck1.setLocation(260, 45);
        mapcheck1.setSize(100, 25);
        mapcheck1.setFont(f1);
        mapcheck2.setLocation(260, 70);
        mapcheck2.setSize(100, 25);
        mapcheck2.setFont(f1);
        mapcheck3.setLocation(260, 95);
        mapcheck3.setSize(100, 25);
        mapcheck3.setFont(f1);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="이벤트패널 항목들 좌표 변경">
        
        BtnBurning.setLocation(10, 30);
        BtnBurning.setSize(100, 55);
        BtnBurning.setFont(f1);
        lblBurning.setLocation(70, 10);
        lblBurning.setSize(50, 20);
        lblBurning.setFont(f1);
        lblBurning.setForeground(Color.RED);
        lblBurninging.setLocation(10, 10);
        lblBurninging.setSize(80, 20);
        lblBurninging.setFont(f1);
        lblBurninging.setForeground(Color.black);
        
        BtnHottime.setLocation(125, 50);
        BtnHottime.setSize(205, 35);
        BtnHottime.setFont(f1);
        lblitem.setLocation(125, 10);
        lblitem.setSize(80, 20);
        lblitem.setFont(f1);
        lblitem.setForeground(Color.black);
        txtitem.setLocation(175, 10);
        txtitem.setSize(50, 20);
        txtitem.setFont(f1);
        lblamount.setLocation(125, 30);
        lblamount.setSize(80, 20);
        lblamount.setFont(f1);
        lblamount.setForeground(Color.black);
        txtamount.setLocation(175, 30);
        txtamount.setSize(50, 20);
        txtamount.setFont(f1);
        lblitem1.setLocation(230, 10);
        lblitem1.setSize(80, 20);
        lblitem1.setFont(f1);
        lblitem1.setForeground(Color.black);
        txtitem1.setLocation(280, 10);
        txtitem1.setSize(50, 20);
        txtitem1.setFont(f1);
        lblamount1.setLocation(230, 30);
        lblamount1.setSize(80, 20);
        lblamount1.setFont(f1);
        lblamount1.setForeground(Color.black);
        txtamount1.setLocation(280, 30);
        txtamount1.setSize(50, 20);
        txtamount1.setFont(f1);
        jsp_5.setLocation(0, 95);
        jsp_5.setSize(400, 1);

        
        EventNote1.setForeground(Color.BLUE);
        EventRateExp1.setForeground(Color.BLUE);
        EventRateDrop1.setForeground(Color.BLUE);
        EventRateMeso1.setForeground(Color.BLUE);        
        EventP1.setForeground(Color.BLUE);
        
        Event.setLocation(70, 110);
        Event.setSize(300, 20);
        Event.setFont(Maker);        
        EventP.setLocation(125, 150);
        EventP.setSize(100, 20);
        EventP.setFont(Maker);
        EventP1.setLocation(50, 175);
        EventP1.setSize(300, 20);
        EventP1.setFont(f1);
        EventRate.setLocation(125, 200);
        EventRate.setSize(100, 20);
        EventRate.setFont(Maker);
        EventRateExp.setLocation(70, 220);
        EventRateExp.setSize(100, 20);
        EventRateExp.setFont(f1);
        EventRateExp1.setLocation(200, 220);
        EventRateExp1.setSize(100, 20);
        EventRateExp1.setFont(f1);
        EventRateDrop.setLocation(76, 240);
        EventRateDrop.setSize(100, 20);
        EventRateDrop.setFont(f1);
        EventRateDrop1.setLocation(200, 240);
        EventRateDrop1.setSize(100, 20);
        EventRateDrop1.setFont(f1);
        EventRateMeso.setLocation(76, 260);
        EventRateMeso.setSize(100, 20);
        EventRateMeso.setFont(f1);
        EventRateMeso1.setLocation(200, 260);
        EventRateMeso1.setSize(100, 20);
        EventRateMeso1.setFont(f1);                
        EventNote.setLocation(125, 290);
        EventNote.setSize(100, 20);
        EventNote.setFont(Maker);
        EventNote1.setLocation(10, 310);
        EventNote1.setSize(300, 20);
        EventNote1.setFont(f1);
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="지급패널 항목들 좌표 변경">
        lblname.setLocation(100, 10);
        lblname.setSize(80, 20);
        lblname.setFont(f1);
        lblname.setForeground(Color.black);
        txtname.setLocation(150, 10);
        txtname.setSize(80, 20);
        txtname.setFont(f1);
        jsp_6.setLocation(0, 35);
        jsp_6.setSize(400, 1);
        
        lblcashgive.setLocation(30, 40);
        lblcashgive.setSize(80, 20);
        lblcashgive.setFont(f1);
        lblcashgive.setForeground(Color.black);
        txtcashgive.setLocation(80, 40);
        txtcashgive.setSize(80, 20);
        txtcashgive.setFont(f1);
        Btncashgive.setLocation(30, 60);
        Btncashgive.setSize(130, 55);
        Btncashgive.setFont(f1);
        
        lblmesogive.setLocation(180, 40);
        lblmesogive.setSize(80, 20);
        lblmesogive.setFont(f1);
        lblmesogive.setForeground(Color.black);
        txtmesogive.setLocation(230, 40);
        txtmesogive.setSize(80, 20);
        txtmesogive.setFont(f1);
        Btnmesogive.setLocation(180, 60);
        Btnmesogive.setSize(130, 55);
        Btnmesogive.setFont(f1);
        lblitemgive.setLocation(30, 130);
        lblitemgive.setSize(80, 20);
        lblitemgive.setFont(f1);
        lblitemgive.setForeground(Color.black);
        lblitemamount.setLocation(30, 150);
        lblitemamount.setSize(80, 20);
        lblitemamount.setFont(f1);
        lblitemamount.setForeground(Color.black);
        txtitemgive.setLocation(80, 130);
        txtitemgive.setSize(80, 20);
        txtitemgive.setFont(f1);
        txtitemamount.setLocation(80, 150);
        txtitemamount.setSize(80, 20);
        txtitemamount.setFont(f1);
        Btnitemgive.setLocation(30, 170);
        Btnitemgive.setSize(130, 35);
        Btnitemgive.setFont(f1);
        
        Btngive1.setLocation(180, 130);
        Btngive1.setSize(130, 20);
        Btngive1.setFont(f2);
        Btngive2.setLocation(180, 157);
        Btngive2.setSize(130, 20);
        Btngive2.setFont(f2);
        Btngive3.setLocation(180, 185);
        Btngive3.setSize(130, 20);
        Btngive3.setFont(f2);
        
        Btngivemn.setLocation(60, 215);
        Btngivemn.setSize(100, 70);
        Btngivemn.setFont(f1);
        mncheck.setLocation(180, 215);
        mncheck.setSize(100, 25);
        mncheck.setFont(f1);
        mncheck1.setLocation(180, 240);
        mncheck1.setSize(100, 25);
        mncheck1.setFont(f1);
        mncheck2.setLocation(180, 265);
        mncheck2.setSize(100, 25);
        mncheck2.setFont(f1);
        
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="공지패널에 항목들 좌표 변경">
        txtnotice.setLocation(60, 120);
        txtnotice.setSize(215, 20);
        txtnotice.setFont(f1);
        cbnotice.setLocation(120, 10);
        cbnotice.setSize(215, 25);
        cbnotice.setFont(f1);
        cbnotice1.setLocation(120, 35);
        cbnotice1.setSize(215, 25);
        cbnotice1.setFont(f1);
        cbnotice2.setLocation(120, 60);
        cbnotice2.setSize(215, 25);
        cbnotice2.setFont(f1);
        cbnotice3.setLocation(120, 85);
        cbnotice3.setSize(215, 25);
        cbnotice3.setFont(f1);
        Btnnotice.setLocation(120, 160);
        Btnnotice.setSize(105, 50);
        Btnnotice.setFont(f1);     
        
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="클릭시 이펙트 설정">
        BtnServerOpen.addActionListener(new Start(lblChState, exprate2, droprate2, mesorate2, EventRateExp1, EventRateDrop1, EventRateMeso1, EventP1, EventNote1)); 
        BtnServerOpen.setActionCommand("launch");
        BtnServerRestart.addActionListener(new Start()); 
        BtnServerRestart.setActionCommand("blog");        
        BtnGMRegist.addActionListener(new Start(txtNick));
        BtnGMRegist.setActionCommand("gmregist");
        BtnGMRemove.addActionListener(new Start(txtNick));
        BtnGMRemove.setActionCommand("gmremove");
              
        
        BtnBurning.addActionListener(new Start(lblBurning, lblBurninging, BtnBurning));
        BtnBurning.setActionCommand("Burning");       
        
        BtnHottime.addActionListener(new Start(txtitem, txtamount, txtitem1, txtamount1));
        BtnHottime.setActionCommand("Hottime");
        
        BtnChange.addActionListener(new Start(txtNick, txtNick2));
        BtnChange.setActionCommand("Change");
        
        Btnnotice.addActionListener(new Start(txtnotice, cbnotice, cbnotice1, cbnotice2, cbnotice3));
        Btnnotice.setActionCommand("notice");
        
        Btngivemn.addActionListener(new Start(txtname, mncheck, mncheck1, mncheck2));
        Btngivemn.setActionCommand("givemn");
        Btngive1.addActionListener(new Start(txtname));
        Btngive1.setActionCommand("give1");
        Btngive2.addActionListener(new Start(txtname));
        Btngive2.setActionCommand("give2");
        Btngive3.addActionListener(new Start(txtname));
        Btngive3.setActionCommand("give3");
        
        
        Btnlogin.addActionListener(new Start(Btnlogin));
        Btnlogin.setActionCommand("login");
        
        Btnwarp.addActionListener(new Start(txtNick, mapcheck1, mapcheck2, mapcheck3));
        Btnwarp.setActionCommand("warp");
        
        Btncashgive.addActionListener(new Start(txtname, txtcashgive));
        Btncashgive.setActionCommand("cashgive");
        Btnmesogive.addActionListener(new Start(txtname, txtmesogive));
        Btnmesogive.setActionCommand("mesogive");
        Btnitemgive.addActionListener(new Start(txtname, txtitemgive, txtitemamount));
        Btnitemgive.setActionCommand("itemgive");
        
        rate.addActionListener(new Start(rate, exprate2, droprate2, mesorate2, ExpR1, DropR1, MesoR1));
        rate.setActionCommand("rate");        
        
        //check_autoreboot_cycle.addAncestorListener(new Start(check_autoreboot_cycle, ));
        //</editor-fold>
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="서버부팅">
    class LaunchServer extends Thread {  
        @Override
        public void run() {        
      DatabaseConnection.init();

      if ((Boolean.parseBoolean(ServerProperties.getProperty("net.sf.odinms.world.admin"))) || (ServerConstants.Use_Localhost)) {
        ServerConstants.Use_Fixed_IV = false;
        System.out.println("[!!! Admin Only Mode Active !!!]");
      }

        String ip = System.getProperty("org.whitestar.gateway_ip");
        if (ip != null) {
            try {
                InetAddress address = InetAddress.getByName(ip); 
                String raw_address = address.getHostAddress();
                
                
                System.out.println("[VaconMaple] Resolved Host Address of Server Machine : " + raw_address);
            } catch (Exception e) {
                System.err.println("Error : Cannot set Gateway IP ");
                System.err.println("Set default gateway ip - 127.0.0.1 (loopback)");
                e.printStackTrace();
                
            }
        } else {
            System.out.println("Gateway IP was not specified. default : " + ServerConstants.Gateway_IP);
        }
      Connection con = null;
      PreparedStatement ps = null;
      try {
        con = DatabaseConnection.getConnection();
        ps = con.prepareStatement("UPDATE accounts SET loggedin = 0");
        ps.executeUpdate();
        ps.close();
      } catch (SQLException ex) {
        throw new RuntimeException("[EXCEPTION] Please check if the SQL server is active.");
      } finally {
        if (ps != null)
          try {
            ps.close();
          }
          catch (Exception e) {
          }
        if (con != null)
          try {
            con.close();
          }
          catch (Exception e) {
          }
      }
        System.out.println("[VaconMaple] Korean Maple Story Ver 1.2." + ServerConstants.MAPLE_VERSION + "");
        System.out.println("[VaconMaple] Pack Name : VaconMaple");
        System.out.println("[VaconMaple] Base Pack : TetraSEA or WhiteStar or SkyBySkyMS or Shift");
        System.out.println("[VaconMaple] Developer : Vacon [tjsdn9666@naver.com]");

      Start ld = new Start();
      try {
        LoadingThread();
      } catch (Exception ex) {
        Logger.getLogger(Start.class.getName()).log(Level.SEVERE, null, ex);
      }
      try {
        LoginServer.run_startup_configurations();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      try {
        ChannelServer.startChannel_Main();
      } catch (Exception e) {
        throw new RuntimeException();
      }
      for (int i = 0; i < ServerConstants.serverCount; i++) {
      }
      try {
        CashShopServer.run_startup_configurations();
      } catch (Exception e) {
        throw new RuntimeException();
      }
      Timer.CheatTimer.getInstance().register(AutobanManager.getInstance(), 60000L);
      Runtime.getRuntime().addShutdownHook(new Thread(new Shutdown()));
      World.registerRespawn();
      ServerConstants.registerMBean();
      ShutdownServer.registerMBean();

      PlayerNPC.loadAll();
      try
      {
        EtcServer.start();
      } catch (Exception e) {
        throw new RuntimeException();
      }
      DatabaseBackup.getInstance().startTasking();
      LoginServer.setOn();
      System.out.println(new StringBuilder().append("[VaconMaple] Event Fully Initialized in ").append((System.currentTimeMillis() - startTime) / 1000L).append(" seconds").toString());

        if (SystemUtils.getTimeMillisByDay(2014, 6, 24, 18, 0, 0) < System.currentTimeMillis() && System.currentTimeMillis() < SystemUtils.getTimeMillisByDay(2014, 6, 25, 21, 0, 0)) {
            Runnable eventStart = new Runnable() {
                @Override
                public void run() {
                    LoginServer.setFlag((byte) 1);
                    LoginServer.setEventMessage("#bVaconMaple\r\r 이벤트\r\r#r2월 2일 ~ 2월 5일#k\r오후 6시 ~ 오후 10시\r\r#b경험치 다섯배#k\r#b드롭률 두배#k");
                    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                    cserv.setExpRate(ServerConstants.defaultExpRate * 3);
                    cserv.setDropRate(ServerConstants.defaultDropRate * 3);
                    cserv.setMesoRate(ServerConstants.defaultMesoRate * 3);
                    cserv.setServerMessage("");
                    Eexprate.setForeground(Color.MAGENTA);
                    Edroprate.setForeground(Color.MAGENTA);
                    Emesorate.setForeground(Color.MAGENTA);
                    Eeventp.setForeground(Color.BLUE);
                    Eeventnote.setForeground(Color.GREEN);
                    Eexprate.setText("3배");
                    Edroprate.setText("3배");
                    Emesorate.setText("3배");
                    Eeventp.setText("2014년 6월 24일 18시 ~ 2014년 6월 5일 21시");                    
                    Eeventnote.setText("            VaconMaple GM의 통큰 이벤트 전체 3배!");
                    }
                }
            };
            Runnable eventEnd = new Runnable() {
                @Override
                public void run() {
                    LoginServer.setFlag((byte) 0);
                    LoginServer.setEventMessage("");
                    for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                    cserv.setExpRate(ServerConstants.defaultExpRate);
                    cserv.setDropRate(ServerConstants.defaultDropRate);
                    cserv.setMesoRate(ServerConstants.defaultMesoRate);
                    Eexprate.setForeground(Color.PINK);
                    Edroprate.setForeground(Color.PINK);
                    Emesorate.setForeground(Color.PINK);
                    Eeventp.setForeground(Color.PINK);
                    Eeventnote.setForeground(Color.PINK);                   
                    Eexprate.setText("이벤트 종료");
                    Edroprate.setText("이벤트 종료");
                    Emesorate.setText("이벤트 종료");
                    Eeventp.setText("             이벤트가 종료되었습니다.");
                    Eeventnote.setText("                 진행중인 이벤트가 현재 종료되었습니다.");
                    }
                }
            };
            SystemUtils.setScheduleAtTime(2014, 6, 24, 18, 0, 0, eventStart);
            SystemUtils.setScheduleAtTime(2014, 6, 25, 21, 0, 0, eventEnd);
            LoginServer.setEventMessage("#bVaconMaple 1.2.65\r\r오픈기념 이벤트#k\r\r#r6월 24일 ~ 6월 25일\r오전10시~ 오후10시#k\r\r모든배율 3배 상승!");
        }

      if (!ServerConstants.Use_Localhost)
      {
        Calendar cal = Calendar.getInstance();
        cal.set(11, 5);
        cal.set(12, 0);
        cal.set(13, 0);
        long time = cal.getTimeInMillis();
        long schedulewait = 0L;
        if (time > System.currentTimeMillis())
          schedulewait = time - System.currentTimeMillis();
        else {
          schedulewait = time + 86400000L - System.currentTimeMillis();
        }
        if (schedulewait < 3600000L) {
          schedulewait += 86400000L;
        }

        System.out.println(new StringBuilder().append("[VaconMaple] Server will shutdown automatically in ").append(StringUtil.getReadableMillis(0L, schedulewait).replace("일", "days ").replace("시간", "hours ").replace("분", "mins ").replace("초", "secs ")).append(".").toString());
        BroadcastMsgSchedule("잠시 후 서버 안정을 위하여 오전 5시에 서버 재시작이 있을 예정입니다. 접속중이신 분들은 서버 재시작 시각 이전에 종료해 주시기 바랍니다.", schedulewait - 3600000L);
        BroadcastMsgSchedule("30분 후 오전 5시에 서버 재시작이 있을 예정입니다. 접속중이신 분들은 서버 재시작 시각 이전에 종료해 주시기 바랍니다.", schedulewait - 1800000L);
        BroadcastMsgSchedule("15분 후 오전 5시에 서버 재시작이 있을 예정입니다. 접속중이신 분들은 서버 재시작 시각 이전에 종료해 주시기 바랍니다.", schedulewait - 900000L);
        BroadcastMsgSchedule("10분 후 오전 5시에 서버 재시작이 있을 예정입니다. 접속중이신 분들은 서버 재시작 시각 이전에 종료해 주시기 바랍니다.", schedulewait - 600000L);
        BroadcastMsgSchedule("5분 후 오전 5시에 서버 재시작이 있을 예정입니다. 접속중이신 분들은 서버 재시작 시각 이전에 종료해 주시기 바랍니다.", schedulewait - 300000L);
        BroadcastMsgSchedule("2분 후 오전 5시에 서버 재시작이 있을 예정입니다. 접속중이신 분들은 서버 재시작 시각 이전에 종료해 주시기 바랍니다.", schedulewait - 120000L);
        BroadcastMsgSchedule("1분 후 오전 5시에 서버 재시작이 있을 예정입니다. 접속중이신 분들은 서버 재시작 시각 이전에 종료해 주시기 바랍니다.", schedulewait - 60000L);
        Timer.WorldTimer.getInstance().schedule(new Shutdown(), schedulewait);
        RankingWorker.run();
      } else {
        System.out.println("[Warning] Skipped part of startup cause localhost property");
        new PacketSender().setVisible(true);
      }
      new MemoryUsageWatcher(88).start();
      new Debugger().setVisible(true);
      new DeadLockDetector(60, DeadLockDetector.RESTART).start();
      DBLogger.getInstance().clearLog(14, 30, 21); //Log Clear interval 14/30/21 days
      EtcHandler.handle((short) 0, null, null); // initialize class
      Iterator i$ = ChannelServer.getAllInstances().iterator(); if (i$.hasNext()) { ChannelServer cserv = (ChannelServer)i$.next();
            label_1.setText("ON");
            label_1.setForeground(Color.GREEN);
            exprate.setForeground(Color.black);
            droprate.setForeground(Color.black);
            mesorate.setForeground(Color.black);
            exprate.setText(new StringBuilder().append(cserv.getExpRate()).append(" 배").toString());
            droprate.setText(new StringBuilder().append(cserv.getDropRate()).append(" 배").toString());
            mesorate.setText(new StringBuilder().append(cserv.getMesoRate()).append(" 배").toString());
            IsOpen = "ed";
      if (SystemUtils.getTimeMillisByDay(2014, 6, 24, 18, 0, 0) < System.currentTimeMillis() && System.currentTimeMillis() < SystemUtils.getTimeMillisByDay(2014, 6, 25, 21, 0, 0)) {
         } else {
                    Eexprate.setForeground(Color.PINK);
                    Edroprate.setForeground(Color.PINK);
                    Emesorate.setForeground(Color.PINK);
                    Eeventp.setForeground(Color.PINK);
                    Eeventnote.setForeground(Color.PINK);                   
                    Eexprate.setText("이벤트 미진행");
                    Edroprate.setText("이벤트 미진행");
                    Emesorate.setText("이벤트 미진행");
                    Eeventp.setText("             이벤트 일정이 없습니다.");
                    Eeventnote.setText("                 현재 진행중인 이벤트가 없습니다.");         
                   }
            }
      }
}
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="제작자정보">
    class Blog extends Thread {
        @Override
        public void run() {
        JOptionPane.showMessageDialog(null, "[MapleStory 1.2.65]"
                + "\r\n\r\nVaconMaple / 바콘팩"
                + "\r\n네이버쪽지 문의 : tjsdn9666@naver.com", "알림", JOptionPane.INFORMATION_MESSAGE); 
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="운영자등록">
    class RegistGM extends Thread {
        @Override
        public void run() {
            String nick = txtfield.getText();
            MapleCharacter ply = null;
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                ply = cserv.getPlayerStorage().getCharacterByName(nick);
                if (ply != null) {
                    break;
                }
            }
            if (ply != null) {
                ply.setGMLevel(6);
                ply.dropMessage(1, "'" + nick + "' 님이 운영자의 권한을 획득하였습니다.");
                JOptionPane.showMessageDialog(null, nick + "님이 운영자가 되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int AccId = 0;
                try {
                    Connection con = MYSQL.getConnection();
                    PreparedStatement ps;
                    ps = con.prepareStatement("SELECT accountid FROM characters WHERE name = ?");
                    ps.setString(1, nick);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        AccId = rs.getInt("accountid");
                    }
                    rs.close();
                    ps.close();
                } catch (SQLException e) {
                        System.err.println("Error getting character default" + e);
                }
                if (AccId != 0) {
                    try {
                        Connection con = MYSQL.getConnection();
                        PreparedStatement ps = con.prepareStatement("UPDATE characters SET gm = ? WHERE name = ?");
                        ps.setInt(1, 6);
                        ps.setString(2, nick);
                        ps.execute();
                        ps.close();
                        JOptionPane.showMessageDialog(null, nick + "님이 운영자가 되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException se) {
                        System.err.println("SQL error: " + se.getLocalizedMessage() + se);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, nick + "은 존재하지 않는 닉네임으로 추정됩니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="운영자박탈">
    class RemoveGM extends Thread {
        @Override
        public void run() {
            String nick = txtfield.getText();
            MapleCharacter ply = null;
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                ply = cserv.getPlayerStorage().getCharacterByName(nick);
                if (ply != null) {
                    break;
                }
            }
            if (ply != null) {
                ply.setGMLevel(0);
                ply.dropMessage(1, "'" + nick + "' 님의 운영자 권한이 박탈되었습니다.");
                JOptionPane.showMessageDialog(null, nick + "님이 유저가 되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int AccId = 0;
                try {
                    Connection con = MYSQL.getConnection();
                    PreparedStatement ps;
                    ps = con.prepareStatement("SELECT accountid FROM characters WHERE name = ?");
                    ps.setString(1, nick);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        AccId = rs.getInt("accountid");
                    }
                    rs.close();
                    ps.close();
                } catch (SQLException e) {
                        System.err.println("Error getting character default" + e);
                }
                if (AccId != 0) {
                    try {
                        Connection con = MYSQL.getConnection();
                        PreparedStatement ps = con.prepareStatement("UPDATE characters SET gm = ? WHERE name = ?");
                        ps.setInt(1, 0);
                        ps.setString(2, nick);
                        ps.execute();
                        ps.close();
                         JOptionPane.showMessageDialog(null, nick + "님이 유저가 되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException se) {
                        System.err.println("SQL error: " + se.getLocalizedMessage() + se);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, nick + "은 존재하지 않는 닉네임으로 추정됩니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="알림창">
    class Alert extends Thread {
        @Override
        public void run() {
            try {
                Alert(txtMsg, txtOpt, intOpt);
            } catch (Exception e) {
                
            }
        }
    }
    
    private void Alert(String msg, String opt, int opt2) {
        JOptionPane.showMessageDialog(null, msg, opt, opt2);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="확인취소박스 활용부">    
    //<editor-fold defaultstate="collapsed" desc="트레이로 보내기">    
    class JTray extends Thread {
        @Override
        public void run() {
            int yesno_result = 99999;
            try {
                yesno_result = JYesNO(yesno_title, yesno_msg);
                while(yesno_result == 99999) {
                    Thread.sleep(1000);
                }
                if (yesno_result == JOptionPane.OK_OPTION) {
                    JTray();
                }
            } catch (Exception e) {
                
            }
        }
    }
    
    @SuppressWarnings("CallToThreadDumpStack")
    private void JTray() {
        try {
            if(SystemTray.isSupported()) {
                SystemTray tray=SystemTray.getSystemTray();
                File f = new File("");
                Image trayImage = Toolkit.getDefaultToolkit().getImage(f.getCanonicalPath() + "\\icon.gif");

                frame_1.setVisible(false);
                PopupMenu popup=new PopupMenu();
                MenuItem defaultItem=new MenuItem("구동기창 복구");
                defaultItem.addActionListener(new Start(frame_1));
                defaultItem.setActionCommand("tray_backup");
                popup.add(defaultItem);
                trayicon = new TrayIcon(trayImage,"Ndrive 구동기", popup);
                trayicon.addActionListener(new Start(frame_1));
                trayicon.setActionCommand("tray_backup");
                try {
                    tray.add(trayicon);
                } catch(AWTException e) {
                    e.printStackTrace();
                }
            } else {
                txtMsg = "트레이가 지원되지 않는 운영체제입니다.";
                txtOpt = "알림";
                intOpt = JOptionPane.INFORMATION_MESSAGE;
                Alert(txtMsg, txtOpt, intOpt);
            }
        } catch (Exception e) {
             
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="종료">
    class JEnd extends Thread {
        @Override
        public void run() {
            int yesno_result = 99999;
            try {
                yesno_result = JYesNO(yesno_title, yesno_msg);
                while(yesno_result == 99999) {
                    Thread.sleep(1000);
                }
                if (yesno_result == JOptionPane.OK_OPTION) {
                    System.exit(0);
                }
            } catch (Exception e) {
                
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="확인취소박스">    
    private int JYesNO(String title, String msg) {
        return JOptionPane.showConfirmDialog(null, msg, title, JOptionPane.OK_CANCEL_OPTION);
    }
    //</editor-fold>

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="버닝타임">
     class Burning extends Thread {
        @Override
        public void run() {
            String text = "버닝타임이 종료되었습니다.";
            for (ChannelServer ch : ChannelServer.getAllInstances()) {
                ch.toggleBurn();
                if (ch.burn) text = "버닝타임이 시작되었습니다.\r\n경험치를 두배로 더 획득할 수 있습니다.";
                label.setText(ch.burn ? "진행중" : "미진행");
                label.setForeground(ch.burn ? Color.green : Color.red);
                ChannelServer.getInstance(ch.getChannel()).broadcastPacket(MaplePacketCreator.serverNotice(1, text));
                }
            }
        }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="핫타임">
    class Hottime extends Thread {
        Packet packet;
        int channel;
        int itemId, itemId1;
        private Object gainItem;
        @Override
        public void run() {
            String txtitem = txtfield1.getText();
            String txtamount = txtfield2.getText();
            String txtitem1 = txtfield3.getText();
            String txtamount1 = txtfield4.getText();
            itemId = Integer.parseInt(txtitem);
            itemId1 = Integer.parseInt(txtitem1);
            short num = Short.parseShort(txtamount);
            short num1 = Short.parseShort(txtamount1);
            ItemInformation ii = ItemInformation.getInstance();
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                if (cserv.getPort() >= 0) {
                    for(MapleCharacter hp : cserv.getPlayerStorage().getAllCharHottime().values()) {
                        if (txtitem1.equals("") & txtamount1.equals("")) {
                            hp.gainItem(itemId, num, false, -1, "관리기로 아이템 지급");
                            hp.gainItem(itemId1, num, false, -1, "관리기로 아이템 지급");
                            hp.getClient().getPlayer().dropMessage(1, "서버관리기로 부터 핫타임 아이템이 지급되었습니다.");
                       } else {
                            hp.gainItem(itemId, num, false, -1, "관리기로 아이템 지급");
                            hp.gainItem(itemId1, num1, false, -1, "관리기로 아이템 지급");
                            hp.getClient().getPlayer().dropMessage(1, "서버관리기로 부터 핫타임 아이템이 지급되었습니다.");
                        }
                    }
                }
                System.out.println("[핫타임] " + (cserv.getChannel() + 1) + " 채널에 핫타임이 지급되었습니다.");
             }
        }
   }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="닉네임변경">
    class Change extends Thread {
        @Override
        public void run() {
            String txtNick = txtfield1.getText();
            String txtNick2 = txtfield2.getText();
            MapleCharacter ply = null;
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                ply = cserv.getPlayerStorage().getCharacterByName(txtNick);
                if (ply != null) {
                    break;
                }
            }
            if (ply != null) {
                ply.setName(txtNick2);
                ply.dropMessage(1, "'" + txtNick + "' 님의 닉네임이 '" + txtNick2 + "' 로 변경되었습니다.");
                JOptionPane.showMessageDialog(null, "(" + txtNick + ")님의 닉네임 (" + txtNick2 +")님으로 변경되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
            } else {
                int AccId = 0;
                try {
                    Connection con = MYSQL.getConnection();
                    PreparedStatement ps;
                    ps = con.prepareStatement("SELECT accountid FROM characters WHERE name = ?");
                    ps.setString(1, txtNick);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        AccId = rs.getInt("accountid");
                    }
                    rs.close();
                    ps.close();
                } catch (SQLException e) {
                        System.err.println("Error getting character default" + e);
                }
                if (AccId != 0) {
                    try {
                        Connection con = MYSQL.getConnection();
                        PreparedStatement ps = con.prepareStatement("UPDATE characters SET name = ? WHERE name = ?");
                        ps.setString(1, txtNick2);
                        ps.setString(2, txtNick);
                        ps.execute();
                        ps.close();                      
                        JOptionPane.showMessageDialog(null, "(" + txtNick + ")님의 닉네임 (" + txtNick2 +")님으로 변경되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException se) {
                        System.err.println("SQL error: " + se.getLocalizedMessage() + se);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, txtNick + "은 존재하지 않는 닉네임입니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="접속자확인">
    class login extends Thread {
        @Override
        public void run() {
            int login = 0;
            for (ChannelServer csev : ChannelServer.getAllInstances()) {
                login += csev.getPlayerStorage().getAllCharacters().size();
            }
            JOptionPane.showMessageDialog(null, "[접속확인] 현재 접속중인 유저수 : " + login + "명", "접속자확인", JOptionPane.INFORMATION_MESSAGE);
        }
   }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="공지보내기">
    class notice extends Thread {
       private final JRadioButton test[] = {cb1, cb2, cb3, cb4};
        @Override
        public void run() {
            String txtnotice = txtfield.getText();
                    if (test[0].isSelected()) { //팝업
                        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(1, "[서버공지]\r\n" + txtnotice)); // 전체적으로 알림 Broadcasting
                        JOptionPane.showMessageDialog(null, "서버에 메세지가 보내졌습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
                    } else if (test[1].isSelected()) { //노랑
                        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(0, " [서버공지] " + txtnotice)); // 전체적으로 알림 Broadcasting
                        JOptionPane.showMessageDialog(null, "서버에 메세지가 보내졌습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
                    } else if (test[2].isSelected()) { //분홍
                        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(5, " [서버공지] " + txtnotice)); // 전체적으로 알림 Broadcasting
                        JOptionPane.showMessageDialog(null, "서버에 메세지가 보내졌습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
                    } else if (test[3].isSelected()) { //파랑
                        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, " [서버공지] " + txtnotice)); // 전체적으로 알림 Broadcasting
                        JOptionPane.showMessageDialog(null, "서버에 메세지가 보내졌습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
                 }
            }
        }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="후원">
    class givemn extends Thread {
       private final JRadioButton give[] = {mn1, mn2, mn3};
        @Override
        public void run() {
            String txtname = txtfield.getText();
            MapleCharacter hp = getPlayer(txtname);
            if (hp == null) {
                JOptionPane.showMessageDialog(null, "서버에서 " + txtname + "님을 찾을수없습니다!", "오류", JOptionPane.ERROR_MESSAGE);
            } else if (give[0].isSelected()) { //5000원
                hp.gainItem(4031120, (short) 1, false, -1, "관리기로 아이템 지급"); // gainItem(아이템코드, (short) 갯수, flase, -1, "지급방식");
                hp.getClient().getPlayer().dropMessage(1, "서버관리기로 부터 후원아이템이 지급되었습니다.");
                JOptionPane.showMessageDialog(null, txtname + " 님께 후원아이템이 지급되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
            } else if (give[1].isSelected()) { //10000원
                hp.gainItem(4031120, (short) 2, false, -1, "관리기로 아이템 지급"); // gainItem(아이템코드, (short) 갯수, flase, -1, "지급방식");
                hp.getClient().getPlayer().dropMessage(1, "서버관리기로 부터 후원아이템이 지급되었습니다.");
                JOptionPane.showMessageDialog(null, txtname + " 님께 후원아이템이 지급되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
            } else if (give[2].isSelected()) { //15000원
                hp.gainItem(4031120, (short) 3, false, -1, "관리기로 아이템 지급"); // gainItem(아이템코드, (short) 갯수, flase, -1, "지급방식");
                hp.getClient().getPlayer().dropMessage(1, "서버관리기로 부터 후원아이템이 지급되었습니다.");
                JOptionPane.showMessageDialog(null, txtname + " 님께 후원아이템이 지급되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "오른쪽 후원금액을 선택해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    class give1 extends Thread { // 시간의 돌
        @Override
        public void run() {
            String txtname = txtfield.getText();
            MapleCharacter hp = getPlayer(txtname);
            if (hp == null) {
                JOptionPane.showMessageDialog(null, txtname + "님을 찾을수없습니다!");
            } else { //닉네임을 찾을경우
                    hp.gainItem(4021010, (short) 3, false, -1, "관리기로 아이템 지급");
                  //hp.gainItem(아이템코드, (short) 갯수, false, -1, "지급방식");
                    hp.getClient().getPlayer().dropMessage(1, "서버관리기로 부터 후원아이템이 지급되었습니다.");
                    JOptionPane.showMessageDialog(null, txtname + "님께 후원아이템이 지급되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
        
        class give2 extends Thread { // 혼줌
        @Override
        public void run() {
            String txtname = txtfield.getText();
            MapleCharacter hp = getPlayer(txtname);
            if (hp == null) {
                JOptionPane.showMessageDialog(null, txtname + "님을 찾을수없습니다!");
            } else { //닉네임을 찾을경우
                    hp.gainItem(2049100, (short) 1, false, -1, "관리기로 아이템 지급");
                  //hp.gainItem(아이템코드, (short) 갯수, false, -1, "지급방식");
                    hp.getClient().getPlayer().dropMessage(1, "서버관리기로 부터 후원아이템이 지급되었습니다.");
                    JOptionPane.showMessageDialog(null, txtname + "님께 후원아이템이 지급되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
        
        class give3 extends Thread { // 백줌 5%
        @Override
        public void run() {
            String txtname = txtfield.getText();
            MapleCharacter hp = getPlayer(txtname);
            if (hp == null) {
                JOptionPane.showMessageDialog(null, txtname + "님을 찾을수없습니다!");
            } else { //닉네임을 찾을경우
                    hp.gainItem(2049002, (short) 2, false, -1, "관리기로 아이템 지급");
                  //hp.gainItem(아이템코드, (short) 갯수, false, -1, "지급방식");
                    hp.getClient().getPlayer().dropMessage(1, "서버관리기로 부터 후원아이템이 지급되었습니다.");
                    JOptionPane.showMessageDialog(null, txtname + "님께 후원아이템이 지급되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
       
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="유저이동">
    class warp extends Thread {
        private final JRadioButton mapwarp[] = {map1, map2, map3};
        @Override
        public void run() {
            String txtNick = txtfield.getText();
            MapleCharacter hp = getPlayer(txtNick);
            if (hp == null) {
            JOptionPane.showMessageDialog(null, "서버에서 " + txtNick + "님을 찾을수없습니다!", "오류", JOptionPane.ERROR_MESSAGE);
            } else if (mapwarp[0].isSelected()) {
                MapleMap map = hp.getClient().getChannelServer().getMapFactory().getMap(100000000);
                hp.changeMap(map, map.getPortal(0));
                JOptionPane.showMessageDialog(null, txtNick + "님이 헤네시스로 이동되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
                } else if (mapwarp[1].isSelected()) {
                MapleMap map = hp.getClient().getChannelServer().getMapFactory().getMap(910000000);
                hp.changeMap(map, map.getPortal(0));
                JOptionPane.showMessageDialog(null, txtNick + "님이 자유시장으로 이동되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
                } else if (mapwarp[2].isSelected()) {
                MapleMap map = hp.getClient().getChannelServer().getMapFactory().getMap(180000001);
                hp.changeMap(map, map.getPortal(0));
                JOptionPane.showMessageDialog(null, txtNick + "님이 검은맵으로 이동되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
                } else {
                JOptionPane.showMessageDialog(null, "오른쪽 맵을 선택해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="지급">
    class cashgive extends Thread {
        @Override
        public void run() {
            String txtname = txtfield1.getText();
            String txtcashgive = txtfield2.getText();
            int cash = Integer.parseInt(txtcashgive);
            MapleCharacter hp = getPlayer(txtname);
            if (hp == null) {
            JOptionPane.showMessageDialog(null, "서버에서 " + txtname + "님을 찾을수없습니다!", "오류", JOptionPane.ERROR_MESSAGE);
            } else {
                    hp.modifyCSPoints(1, cash, false);
                    hp.getClient().getPlayer().dropMessage(1, "서버관리기로 부터 " + txtcashgive + " 캐시가 지급되었습니다.");
                System.out.println(txtname + "님께 캐시를 지급했습니다.");
            }
        }
    }
    class mesogive extends Thread {
        @Override
        public void run() {
            String txtname = txtfield1.getText();
            String txtmesogive = txtfield2.getText();
            int meso = Integer.parseInt(txtmesogive);
            MapleCharacter hp = getPlayer(txtname);
            if (hp == null) {
            JOptionPane.showMessageDialog(null, "서버에서 " + txtname + "님을 찾을수없습니다!", "오류", JOptionPane.ERROR_MESSAGE);
            } else {
                    hp.gainMeso(meso, false);
                    hp.getClient().getPlayer().dropMessage(1, "서버관리기로 부터 " + txtmesogive + " 메소가 지급되었습니다.");
                System.out.println(txtname + "님께 메소를 지급했습니다.");
            }
        }
    }
    class itemgive extends Thread {
        @Override
        public void run() {
            String txtname = txtfield1.getText();
            String txtitemgive = txtfield2.getText();
            String txtitemamount = txtfield3.getText();
            int itemId = Integer.parseInt(txtitemgive);
            short num = Short.parseShort(txtitemamount);
            MapleCharacter hp = getPlayer(txtname);
            if (hp == null) {
            JOptionPane.showMessageDialog(null, "서버에서 " + txtname + "님을 찾을수없습니다!", "오류", JOptionPane.ERROR_MESSAGE);
            } else {
                    hp.gainItem(itemId, num, false, -1, "관리기로 아이템 지급");
                    hp.getClient().getPlayer().dropMessage(1, "서버관리기로 부터 지정한 아이템 " + txtitemamount + " 개를 지급되었습니다.");
                 
                System.out.println(txtname + "님께 아이템을 지급했습니다.");
            }
        }
    }
    //</editor-fold>
              //  WorldBroadcasting.broadcastMessage(MainPacketCreator.serverNotice(1, "[서버공지]\r\n잠시후에 서버가 리붓됩니다.")); // 전체적으로 알림 Broadcasting
    //<editor-fold defaultstate="collapsed" desc="서버구동매니저">    

  public static final void LoadingThread()
    throws Exception
  {
    Start start = new Start();
    start.ThreadLoader();
  }

  public final void ThreadLoader()
    throws InterruptedException
  {
    World.init();
    WorldTimer.getInstance().start();
    EtcTimer.getInstance().start();
    MapTimer.getInstance().start();
    CloneTimer.getInstance().start();
    EventTimer.getInstance().start();
    BuffTimer.getInstance().start();
    PingTimer.getInstance().start();

    LoadingThread WorldLoader = new LoadingThread(new Runnable() {
      public void run() {
        MapleGuildRanking.getInstance().load();
        MapleGuild.loadAll();
      }
    }
    , "WorldLoader", this);

    LoadingThread MarriageLoader = new LoadingThread(new Runnable() {
      public void run() {
        MarriageManager.getInstance();
      }
    }
    , "MarriageLoader", this);

    LoadingThread MedalRankingLoader = new LoadingThread(new Runnable() {
      public void run() {
        MedalRanking.loadAll();
      }
    }
    , "MedalRankingLoader", this);

    LoadingThread FamilyLoader = new LoadingThread(new Runnable() {
      public void run() {
        MapleFamily.loadAll();
      }
    }
    , "FamilyLoader", this);

    LoadingThread QuestLoader = new LoadingThread(new Runnable() {
      public void run() {
        MapleLifeFactory.loadQuestCounts();
        MapleQuest.initQuests();
      }
    }
    , "QuestLoader", this);

    LoadingThread ProviderLoader = new LoadingThread(new Runnable() {
      public void run() {
        MapleItemInformationProvider.getInstance().runEtc();
      }
    }
    , "ProviderLoader", this);

    LoadingThread MonsterLoader = new LoadingThread(new Runnable() {
      public void run() {
        MapleMonsterInformationProvider.getInstance().load();
      }
    }
    , "MonsterLoader", this);

    LoadingThread ItemLoader = new LoadingThread(new Runnable() {
      public void run() {
        MapleItemInformationProvider.getInstance().runItems();
      }
    }
    , "ItemLoader", this);


    LoadingThread SkillFactoryLoader = new LoadingThread(new Runnable() {
      public void run() {
        SkillFactory.load();
      }
    }
    , "SkillFactoryLoader", this);

    LoadingThread BasicLoader = new LoadingThread(new Runnable() {
      public void run() {
        LoginInformationProvider.getInstance();
        RandomRewards.load();
        RandomRewards.loadGachaponRewardFromINI("ini/gachapon.ini");
        MapleOxQuizFactory.getInstance();
        MapleCarnivalFactory.getInstance();
        MobSkillFactory.getInstance();
        SpeedRunner.loadSpeedRuns();
        MinervaOwlSearchTop.getInstance().loadFromFile();
      }
    }
    , "BasicLoader", this);

    LoadingThread MIILoader = new LoadingThread(new Runnable() {
      public void run() {
        MapleInventoryIdentifier.getInstance();
      }
    }
    , "MIILoader", this);

    LoadingThread CashItemLoader = new LoadingThread(new Runnable() {
      public void run() {
        CashItemFactory.getInstance().initialize();
      }
    }
    , "CashItemLoader", this);



    LoadingThread[] LoadingThreads = { WorldLoader, FamilyLoader, QuestLoader, ProviderLoader, SkillFactoryLoader, BasicLoader, CashItemLoader, MIILoader, MonsterLoader, ItemLoader, MarriageLoader, MedalRankingLoader };

    for (Thread t : LoadingThreads) {
      t.start();
    }
    synchronized (this) {
      wait();
    }
    while (CompletedLoadingThreads.get() != LoadingThreads.length) {
      synchronized (this) {
        wait();
      }
    }
    System.out.println("[VaconMaple] Caching Quest Item Information...");
    MapleItemInformationProvider.getInstance().runQuest();
    System.out.println("[VaconMaple] Cached Quest Item Information...");
  }

    public static void BroadcastMsgSchedule(final String msg, long schedule) {
        Timer.CloneTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                World.Broadcast.broadcastMessage(MaplePacketCreator.yellowChat(msg));
            }
        }, schedule);
    }

  public static class Shutdown
    implements Runnable
  {
    public void run()
    {
      ShutdownServer.getInstance().run();
      ShutdownServer.getInstance().run();
    }
  }

    private static class LoadingThread extends Thread {

        protected String LoadingThreadName;

        private LoadingThread(Runnable r, String t, Object o) {
            super(new NotifyingRunnable(r, o, t));
            LoadingThreadName = t;
        }


        @Override
        public synchronized void start() {
            System.out.println("[VaconMaple] Started " + LoadingThreadName + " Thread");
            super.start();
        }
    }

    private static class NotifyingRunnable implements Runnable {

        private String LoadingThreadName;
        private long StartTime;
        private Runnable WrappedRunnable;
        private final Object ToNotify;

        private NotifyingRunnable(Runnable r, Object o, String name) {
            WrappedRunnable = r;
            ToNotify = o;
            LoadingThreadName = name;
        }

        public void run() {
            StartTime = System.currentTimeMillis();
            WrappedRunnable.run();
            System.out.println("[VaconMaple] " + LoadingThreadName + " | Completed in " + (System.currentTimeMillis() - StartTime) + " Milliseconds. (" + (CompletedLoadingThreads.get() + 1) + "/9)");
            synchronized (ToNotify) {
                CompletedLoadingThreads.incrementAndGet();
                ToNotify.notify();
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="관리기정보">
    class JMaker extends Thread {
      @Override
        public void run() {
        JOptionPane.showMessageDialog(null, "VaconMaple 1.2.65"
                + "\r\n\r\nPack Name : 바콘팩 / VaconPack"
                + "\r\n\r\nDeveloper : 꽃선우 (tjsdn9666@naver.com)"
                + "\r\n개발용이 아닌 서버용 목적으로 개발했습니다.", "알림", JOptionPane.INFORMATION_MESSAGE); 
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="배율확인리뉴얼">
    class Rate extends Thread {
      @Override
        public void run() {
        //Iterator i$ = ChannelServer.getAllInstances().iterator(); if (i$.hasNext()) { ChannelServer cserv = (ChannelServer)i$.next();
        for (ChannelServer cserv : ChannelServer.getAllInstances()){
        String txtratee = txtfielde.getText();
        String txtrated = txtfieldd.getText();
        String txtratem = txtfieldm.getText();
        int exprater = Integer.valueOf(txtratee).intValue();
        int droprater = Integer.valueOf(txtrated).intValue();
        int mesorater = Integer.valueOf(txtratem).intValue();      
        if (cserv.burn) {
        cserv.setExpRate(exprater * 2);
        cserv.setDropRate(droprater);    
        cserv.setMesoRate(mesorater);            
        } else {
        cserv.setExpRate(exprater);
        cserv.setDropRate(droprater);    
        cserv.setMesoRate(mesorater);
        }
        exprate.setText(new StringBuilder().append(cserv.getExpRate()).append(" 배").toString());
        droprate.setText(new StringBuilder().append(cserv.getDropRate()).append(" 배").toString());
        mesorate.setText(new StringBuilder().append(cserv.getMesoRate()).append(" 배").toString());
        JOptionPane.showMessageDialog(null, "원하시는 배율로 현재 배율 상태가 리뉴얼 되었습니다.\r\n운영자로 지정된 임의적인 배율설정은 적용되지 않습니다.\r\n\r\n현재 경험치 배율 : " + exprater + " 배\r\n현재 드롭 배율 : " + droprater + " 배\r\n현재 메소 배율 : " + mesorater + " 배\r\n\r\n", "성공", JOptionPane.INFORMATION_MESSAGE);
        }
      }
    }
    //</editor-fold>
    
}