package server;

import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import constants.ServerConstants;
import database.DatabaseConnection;
import java.io.FileInputStream;
import java.sql.Connection;

/**
 *
 * @author Emilyx3
 */
public class ServerProperties {

    private static final Properties props = new Properties();

    private ServerProperties() {
    }

    static {
        props.setProperty("net.sf.odinms.world.host", System.getProperty("org.whitestar.gateway_ip", "127.0.0.1"));
        props.setProperty("net.sf.odinms.channel.net.interface",
                System.getProperty("org.whitestar.gateway_ip", "127.0.0.1"));
        props.setProperty("net.sf.odinms.world.exp", "1");
        props.setProperty("net.sf.odinms.world.meso", "1");
        ServerConstants.serverType = System.getProperty("whitestar.servertype", "old").equals("new"); //180.210.29.213
        props.setProperty("net.sf.odinms.channel.count", "4");
        props.setProperty("net.sf.odinms.world.serverMessage", "");
        props.setProperty("net.sf.odinms.login.serverName", "스카니아");
        props.setProperty("net.sf.odinms.world.flags", "0");
        props.setProperty("net.sf.odinms.world.admin", "false");
        props.setProperty("net.sf.odinms.channel.net.port", "8585");
        props.setProperty("net.sf.odinms.login.userlimit", "1500");
        props.setProperty("net.sf.odinms.login.eventMessage", "");
        props.setProperty("net.sf.odinms.login.flag", "0");
        props.setProperty("net.sf.odinms.login.maxCharacters", "6");
        System.setProperty("net.sf.odinms.wzpath", "wz");
        props.setProperty(
                "net.sf.odinms.channel.events",
                "HenesysPQ,"
                + "HenesysPQBonus,"
                + "OrbisPQ,"
                + "Boats,"
                + "Flight,"
                + "Geenie,"
                + "Trains,"
                + "elevator,"
                + "3rdjob,"
                + "s4aWorld,"
                + "s4nest,"
                + "s4resurrection,"
                + "s4resurrection2,"
                + "ProtectTylus,"
                + "ZakumPQ,"
                + "ZakumBattle,"
                + "FireDemon,"
                + "LudiPQ,"
                + "ElementThanatos,"
                + "Papulatus,"
                + "cpq,"
                + "TamePig,"
                + "s4common2,"
                + "HorntailPQ,"
                + "HorntailBattle,"
                + "Pirate,"
                + "Adin,"
                + "KerningPQ,"
                + "GuildQuest,"
                + "ShanghaiBoss,"
                + "NightMarketBoss,"
                + "4jberserk,"
                + "4jrush,"
                + "DollHouse,"
                + "Ellin,"
                + "Juliet,"
                + "Romeo,"
                + "KyrinTest,"
                + "KyrinTrainingGroundC,"
                + "KyrinTrainingGroundV,"
                + "KyrinTest,"
                + "ProtectDelli,"
                + "AirStrike,"
                + "AirStrike2,"
                + "JenumistHomu,"
                + "YureteLab1,"
                + "YureteLab2,"
                + "YureteLab3,"
                + "PinkBeanBattle,"
                + "NautilusCow,"
                + "SnowRose,"
                + "DarkMagicianAgit,"
                + "Gojarani,"
                + "MachineRoom");

        //String toLoad = "channel.properties";
        // loadProperties(toLoad);
        // if (getProperty("GMS") != null) {
        // GameConstants.GMS = Boolean.parseBoolean(getProperty("GMS"));
        // }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM auth_server_channel_ip");
            rs = ps.executeQuery();
            while (rs.next()) {
                // if (rs.getString("name").equalsIgnoreCase("gms")) {
                // GameConstants.GMS =
                // Boolean.parseBoolean(rs.getString("value"));
                // } else {
                props.put(rs.getString("name") + rs.getInt("channelid"),
                        rs.getString("value"));
                // }
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(0); // Big ass error.
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                }
            }
        }
        // toLoad = GameConstants.GMS ? "worldGMS.properties" :
        // "world.properties";
        // loadProperties(toLoad);
        

    }
   /*     private ServerProperties() {
    }

    static {   
        props.setProperty("net.sf.odinms.world.host", System.getProperty("org.whitestar.gateway_ip", "127.0.0.1"));
        props.setProperty("net.sf.odinms.channel.net.interface", System.getProperty("org.whitestar.gateway_ip", "127.0.0.1"));
        ServerConstants.serverType = System.getProperty("whitestar.servertype", "old").equals("new"); //180.210.29.213
        System.setProperty("net.sf.odinms.wzpath", "wz");
        String toLoad = "VaconMaple.ini";
        loadProperties(toLoad);
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM auth_server_channel_ip");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                props.put(rs.getString("name") + rs.getInt("channelid"), rs.getString("value"));
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(0); //Big ass error.
        }
    }*/

    public static void loadProperties(String s) {
        FileReader fr;
        try {
            fr = new FileReader(s);
            props.load(fr);
            fr.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getProperty(String s) {
        return props.getProperty(s);
    }

    public static void setProperty(String prop, String newInf) {
        props.setProperty(prop, newInf);
    }

    public static String getProperty(String s, String def) {
        return props.getProperty(s, def);
    }
}
