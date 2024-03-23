/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.messages.commands;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.messages.commands.InternCommand.Ban;
import client.messages.commands.InternCommand.TimeBan;
import constants.ServerConstants.PlayerGMRank;

/**
 *
 * @author Emilyx3
 */
public class GMCommand {

    public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.GM;
    }

    public static class Invincible extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            if (player.isInvincible()) {
                player.setInvincible(false);
                player.dropMessage(6, "Invincibility deactivated.");
            } else {
                player.setInvincible(true);
                player.dropMessage(6, "Invincibility activated.");
            }
            return 1;
        }
    }
    
    public static class Cmds extends 명령어 {}

    public static class 명령어 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            player.dropMessage(6, "!검색 <맵/몹/스킬/아이템> <검색어> : 맵/몹/스킬/아이템 고유코드 검색");
            player.dropMessage(6, "!맵 <맵코드> :  맵 고유코드로 순간이동");
            player.dropMessage(6, "!말하기 <할말> : 개인적으로 스피커합니다.");
            player.dropMessage(6, "!전체적으로말하기 <할말> :  전체적으로 스피커합니다.");
            player.dropMessage(6, "!직업 <직업 코드> : 자신의 직업을 변경");
            player.dropMessage(6, "!버그사용자 : 현재 핵으로 의심가는 사람들 목록 (숫자는 의심 감지 횟수)");
            player.dropMessage(6, "!정지 <대상이름/이메일주소/IP> <밴 사유> : 대상을 해당 밴 사유로 밴.  (예 : !정지 영원 핵 사용 )");
            player.dropMessage(6, "!정지해제 <대상이름/이메일주소/IP> : 대상을 밴 해제");
            player.dropMessage(6, "!영구정지 <대상이름/이메일주소/IP> <밴 사유> : 대상을 해당 밴 사유로 아이피 포함 영구밴.  (예 : !영구정지 영원 핵 사용 )");
            player.dropMessage(6, "!영구정지해제 <대상이름/이메일주소/IP> : 대상을 아이피 포함 영구밴 해제");
            player.dropMessage(6, "!온라인 : 현재 채널에 접속중인 사람");
            player.dropMessage(6, "!연결 : 총 접속중인 사람");
            player.dropMessage(6, "!캐릭터정보 <대상> : 계정, IP, 스탯, 메소 정보");
            player.dropMessage(6, "!드롭삭제 : 현재 맵에 떨어진 아이템 모두 삭제");
            player.dropMessage(6, "!킬올 : 현재 맵에 있는 몬스터 모두 죽이기");
            player.dropMessage(6, "!킬올드롭 : 현재 맵에 있는 몬스터 모두 죽이고 아이템 드랍");
            //player.dropMessage(6, "!경험치배율 <배율 숫자> : 경험치 배율 변경");
            //player.dropMessage(6, "!메소배율 <배율 숫자> : 메소 배율 변경");
            //player.dropMessage(6, "!드롭배율 <배율 숫자> : 드롭 배율 변경");
            player.dropMessage(6, "!레벨 <숫자> : 자신의 레벨을 변경");
            player.dropMessage(6, "!스킬마스터  : 모든 직업의 스킬을 마스터");
            player.dropMessage(6, "!내아이피 : 현재 자기의 아이피를 화면에 띄움");
            player.dropMessage(6, "!스킬 <스킬 코드/스킬 레벨/스킬 총 레벨> : 스킬 레벨을 인위적으로 올림");
            player.dropMessage(6, "!공지 : 채팅창에 공지를 띄움");
            player.dropMessage(6, "!스폰 <몬스터 코드> : 코드에 맞은 몬스터를 소환");
            player.dropMessage(6, "!소환 <유저아이디> : 해당 유저를 현재 맵에 소환");
            player.dropMessage(6, "!풀메소 : 메이플 스토리의 더 이상 메소를 얻을 수 없을 만큼의 메소를 획득");
            player.dropMessage(6, "!체력회복 : 체력을 회복함");    
            player.dropMessage(6, "!서버종료 <초 단위의 시간> : 현재 가동중인 서버가 해당 시간이 지나면 운영을 종료합니다.");
            player.dropMessage(6, "!서버종료시간 : 현재 가동중인 서버가 언제 종료되는 지를 알려줍니다.");
            player.dropMessage(6, "!엔피시 <엔피시코드> : 해당 엔피시를 자신이 위치한 위치에 세웁니다.");
            player.dropMessage(6, "!엔피시삭제 : 임의적으로 세운 엔피시를 맵에서 제거합니다.");
            player.dropMessage(6, "!드롭 <아이템코드> <갯수> : 해당 아이템을 해당 갯수만큼 드롭합니다.");
            player.dropMessage(6, "!결혼 <상대아이디> <반지코드> : 대상과 선택한 반지로 결혼함");
            player.dropMessage(6, "!저장 : 모든 변경 내역을 저장합니다.");
            player.dropMessage(6, "!메소 : 해당 양 만큼 메소가 들어옴.");
             return 1;
        }
    }


    public static class 현재맵유저확인 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            StringBuilder builder = new StringBuilder("현재 맵에 있는 유저 : ").append(c.getPlayer().getMap().getCharactersThreadsafe().size()).append(", ");
            for (MapleCharacter chr : c.getPlayer().getMap().getCharactersThreadsafe()) {
                if (builder.length() > 150) { // wild guess :o
                    builder.setLength(builder.length() - 2);
                    c.getPlayer().dropMessage(6, builder.toString());
                    builder = new StringBuilder();
                }
                builder.append(MapleCharacterUtil.makeMapleReadable(chr.getName()));
                builder.append(" 명, ");
            }
            builder.setLength(builder.length() - 2);
            c.getPlayer().dropMessage(6, builder.toString());
            return 1;
        }
    }

    public static class TempBanIP extends TimeBan {

        public TempBanIP() {
            ipBan = true;
        }
    }

    public static class BanIP extends Ban {

        public BanIP() {
            ipBan = true;
        }
    }

}
