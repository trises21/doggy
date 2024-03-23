package handling.login.handler;

import client.LoginCryptoLegacy;
import client.MapleClient;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import tools.MaplePacketCreator;
import tools.packet.LoginPacket;

public class AutoRegister {
    public static final int ACCOUNTS_IP_COUNT = 5;
    public static int fm;
    public static final boolean AutoRegister = true;
    public static boolean CheckAccount(String id) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT name FROM accounts WHERE name = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                return true;
            }
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
        return false;
    }
    public static void createAccount(String id, String pwd, String ip, final MapleClient c) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ipc = con.prepareStatement("SELECT SessionIP FROM accounts WHERE SessionIP = ?");
            ipc.setString(1, ip);
            ResultSet rs = ipc.executeQuery();
            if (rs.first() == false || rs.last() == true && rs.getRow() < ACCOUNTS_IP_COUNT) {
                try {
                    PreparedStatement ps = con.prepareStatement("INSERT INTO accounts (name, password, email, birthday, macs, SessionIP,gender) VALUES (?, ?, ?, ?, ?, ?, ?)");
                    fm = id.indexOf("G_");
                    if (fm == 0){
                    ps.setString(1, id);
                    ps.setString(2, LoginCryptoLegacy.hashPassword(pwd));
                    ps.setString(3, "no@email.com");
                    ps.setString(4, "2013-12-25");
                    ps.setString(5, "00-00-00-00-00-00");
                    ps.setString(6, ip);
                    ps.setString(7, "1");
                    ps.executeUpdate();
                    rs.close();
                    c.clearInformation();
                    c.getSession().write(LoginPacket.getLoginFailed(20));
                    c.getSession().write(MaplePacketCreator.serverNotice(1, "[VaconMaple]\r\n여자계정으로 회원가입이 되었습니다.\r\n\r\n[남자계정]\r\n아이디만들때 앞에 G_를 안넣고 가입"));
                    } else if (fm == -1){
                    ps.setString(1, id);
                    ps.setString(2, LoginCryptoLegacy.hashPassword(pwd));
                    ps.setString(3, "no@email.com");
                    ps.setString(4, "2013-12-25");
                    ps.setString(5, "00-00-00-00-00-00");
                    ps.setString(6, ip);
                    ps.setString(7, "0");
                    ps.executeUpdate();
                    rs.close();
                    c.clearInformation();
                    c.getSession().write(LoginPacket.getLoginFailed(20));
                    c.getSession().write(MaplePacketCreator.serverNotice(1, "[VaconMaple]\r\n남자계정으로 회원가입 완료되었습니다.\r\n\r\n[여자계정]\r\n아이디만들때 앞에 G_를 넣고 가입"));
                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            } else {
                c.clearInformation();
                c.getSession().write(LoginPacket.getLoginFailed(20));
                c.getSession().write(MaplePacketCreator.serverNotice(1, "회원가입 제한 횟수 5번을 초과 하였습니다."));
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}