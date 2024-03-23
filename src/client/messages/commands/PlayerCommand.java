package client.messages.commands;

//import client.MapleInventory;
//import client.MapleInventoryType;
import client.MapleCharacter;
import client.MapleClient;
import constants.ServerConstants.PlayerGMRank;
import handling.channel.ChannelServer;
import handling.world.World;
import scripting.NPCScriptManager;
import server.MedalRanking;
import server.marriage.MarriageManager;
import server.shops.MinervaOwlSearchTop;
import tools.MaplePacketCreator;

/**
 *
 * @author Emilyx3
 */
public class PlayerCommand {

    public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.NORMAL;
    }
    public static class 렉 extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            c.removeClickedNPC();
            NPCScriptManager.getInstance().dispose(c);
            c.getSession().write(MaplePacketCreator.enableActions());
            return 1;
        }    
    }
    public static class 저장 extends CommandExecute {
        
        public int execute(MapleClient c, String[] splitted) {
            // User Data Save Start
         for (ChannelServer ch : ChannelServer.getAllInstances())
             for (MapleCharacter chr : ch.getPlayerStorage().getAllCharacters())
            chr.saveToDB(true, true);
            // User Data Save End
            // Server Data Save Start
            World.Guild.save();
            World.Alliance.save();
            World.Family.save();
            MarriageManager.getInstance().saveAll();
            MinervaOwlSearchTop.getInstance().saveToFile();
            MedalRanking.saveAll();
            // Server Data Save End
            c.getPlayer().dropMessage(6, "저장이 완료되었습니다.");
            return 1;
        }
    }    
    
    public static class 명령어 extends CommandExecute {
        
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            player.dropMessage(6, "@렉 : 오류로 인한 이미 활성중인 상태를 비활성으로 전환");
            player.dropMessage(6, "@저장: 모든 내용을 저장함. (남용 금지!) ");
             return 1;
        }
    }
}
