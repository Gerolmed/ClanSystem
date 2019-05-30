/*
 * Copyright (c) 2018.
 * This content has been created by Gerolmed! Sharing this content without permission is not allowed, as well
 * as modifying any code parts and claiming ownership!
 */

package de.gerolmed.bungee.clan.utils;

import de.gerolmed.bungee.clan.BungeeClan;
import de.gerolmed.bungee.clan.ClanManager;
import de.gerolmed.bungee.clan.DataSender;
import de.gerolmed.lib.clan.Clan;
import de.gerolmed.lib.clan.ClanUser;
import de.gerolmed.lib.clan.ClanUserManager;
import de.gerolmed.lib.clan.utils.ClanRank;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.UUID;

public class ClanCommand extends Command {

    private BungeeClan bungeeClan;

    public ClanCommand(BungeeClan bungeeClan) {
        super("clan", null);
        this.bungeeClan = bungeeClan;
        ProxyServer.getInstance().getPluginManager().registerCommand(bungeeClan,this);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if(!(commandSender instanceof ProxiedPlayer))
            return;

        ProxiedPlayer proxiedPlayer = (ProxiedPlayer) commandSender;

        ClanManager clanManager = bungeeClan.getClanManager();
        ClanUser user = ClanUserManager.getInstance().getUser(proxiedPlayer.getUniqueId());

        if(user == null)
        {
            return;
        }

        Clan clan = user.getClan();

        //No clan commands
        if(clan == null)
        {
            if(args.length == 0) {
                sendHelp(proxiedPlayer, false);
                return;
            }
            String label = args[0];

            if(label.equalsIgnoreCase("create"))
            {
                if(args.length == 3) {
                    String clanName = args[1];
                    String clanShort = args[2];

                    if(clanShort.length() < 2 || clanShort.length() > 4)
                    {
                        sendMessage(proxiedPlayer, "§7[§6Clan§7] §cDas Kürzel muss 2-4 Zeichen haben");
                        return;
                    }
                    if(clanName.length() < 2 || clanName.length() > 6)
                    {
                        sendMessage(proxiedPlayer, "§7[§6Clan§7] §cDer Name muss 2-6 Zeichen haben");
                        return;
                    }

                    if(clanManager.getClan(clanShort) != null)
                    {
                        sendMessage(proxiedPlayer, "§7[§6Clan§7] §cDer Clan Kürzel ist bereits vergeben!");
                        return;
                    }

                    clanManager.addClan(new Clan(clanShort, clanName, user));
                    sendMessage(proxiedPlayer, "§7[§6Clan§7] §bDer Clan §6"+clanName+"§7[§b"+clanShort + "§7]§b wurde erstellt!");
                    sendUpdateToServer(user);

                    return;
                }
                sendMessage(proxiedPlayer, "§7[§6AtsukaMC§7] §b§lBenutze: §c/clan create [ClanName] [Kürzl.]");
                return;
            }

            if(label.equalsIgnoreCase("accept")) {
                if(args.length != 2) {
                    sendMessage(proxiedPlayer, "§7[§6AtsukaMC§7] §b§lBenutze: §c/clan accept [ClanShort]");
                    return;
                }
                String clanShort = args[1];
                clan = clanManager.getClan(clanShort);

                if(clan == null) {
                    sendMessage(proxiedPlayer, "§7[§6AtsukaMC§7] §cDieser Clan existiert nicht!");
                    return;
                }

                if(clan.hasInvited(user.getUUID()))
                {
                    sendMessageIfOnline("§7[§6Clan§7] §6"+proxiedPlayer.getDisplayName() + "§a ist dem Clan beigetreten!", clan.getUsers());
                    clan.addUser(user);
                    sendUpdateToServer(user);
                    sendMessage(proxiedPlayer, "§7[§6Clan§7] §aDu bist dem Clan §6"+clan.getName()+"§7[§b"+clan.getShort() + "§7]§a beigetreten!");
                } else {
                    sendMessage(proxiedPlayer, "§7[§6AtsukaMC§7] §cDieser Clan hat dich nicht eingeladen!");
                    return;
                }
                return;
            }
            if(label.equalsIgnoreCase("decline") || label.equalsIgnoreCase("deny")) {
                if(args.length != 2) {
                    sendMessage(proxiedPlayer, "§7[§6AtsukaMC§7] §b§lBenutze: §c/clan decline [ClanShort]");
                    return;
                }
                String clanShort = args[1];
                clan = clanManager.getClan(clanShort);

                if(clan == null) {
                    sendMessage(proxiedPlayer, "§7[§6AtsukaMC§7] §cDieser Clan existiert nicht!");
                    return;
                }

                if(clan.hasInvited(user.getUUID()))
                {
                    clan.removeInvitation(user.getUUID());
                    sendMessage(proxiedPlayer, "§7[§6Clan§7] §aDu hast die Einladung des Clans §6"+clan.getName()+"§7[§b"+clan.getShort() + "§7]§a abgelehnt!");
                } else {
                    sendMessage(proxiedPlayer, "§7[§6AtsukaMC§7] §cDieser Clan hat dich nicht eingeladen!");
                    return;
                }
                return;
            }
            sendHelp(proxiedPlayer, false);
        }
        //In clan command
        else {
            if(args.length == 0) {
                sendHelp(proxiedPlayer, true);
                return;
            }

            String label = args[0];

            //Info command
            if(label.equalsIgnoreCase("info"))
            {
                sendMessage(proxiedPlayer, "§a=========[§bClan§a]=========");

                sendMessage(proxiedPlayer, " §6§lClan Name: §a" +clan.getName());
                sendMessage(proxiedPlayer, " §6§lClan Abkürzung: §a" +clan.getShort());

                sendMessage(proxiedPlayer, " §6§lMitglieder:");

                for(ClanUser clanUser : clan.getUsersSorted()) {
                    sendMessage(proxiedPlayer, "  §7- §6"+ getDisplayname(clanUser) + " §7("+clanUser.getRank().getName()+"§7)");
                }

                sendMessage(proxiedPlayer, "§a========================");
                return;
            }

            //Invite Command
            if(label.equalsIgnoreCase("invite"))
            {
                if(user.getRank().canInvite())
                {
                    if(args.length != 2)
                    {
                        sendMessage(proxiedPlayer, "§7[§6AtsukaMC§7] §b§lBenutze: §c/clan invite [UserName]");
                        return;
                    }

                    UUID id = null;
                    String name = args[1];

                    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);

                    if(player != null)
                        id = player.getUniqueId();

                    if(id == null) {
                        sendMessage(proxiedPlayer, "§7[§9System§7] §cDieser Spieler ist nicht online");
                        return;
                    }

                    ClanUser target = ClanUserManager.getInstance().getUser(id);

                    if(target.getClan() != null) {
                        sendMessage(proxiedPlayer, "§7[§6Clan§7] §cDieser Spieler ist bereits in einem Clan");
                        return;
                    }

                    sendInvitation(player, clan);
                    clan.addInvitation(target.getUUID());
                    sendMessage(proxiedPlayer, "§7[§6Clan§7] §6"+player.getDisplayName() + "§b wurde eingeladen!");

                } else {
                    sendMessage(proxiedPlayer, "§7[§9System§7] §cDafür hast du keine Berechtigung... ");
                }
                return;
            }

            //Kick Command
            if(label.equalsIgnoreCase("kick"))
            {
                if(user.getRank().canKick())
                {
                    if(args.length != 2)
                    {
                        sendMessage(proxiedPlayer, "§7[§6AtsukaMC§7] §b§lBenutze: §c/clan kick [UserName]");
                        return;
                    }

                    UUID id = null;
                    String name = args[1];

                    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);

                    if(player != null)
                        id = player.getUniqueId();

                    if(id == null)
                        id = ClanUserManager.getInstance().getIdFromName(name);

                    if(id == null) {
                        sendMessage(proxiedPlayer, "§7[§9System§7] §cDieser Spieler war noch nie online");
                        return;
                    }

                    ClanUser target = ClanUserManager.getInstance().getUser(id);

                    if(target.getClan() != clan) {
                        sendMessage(proxiedPlayer, "§7[§6Clan§7] §cDieser Spieler ist nicht in diesem Clan");
                        return;
                    }
                    if(user.getRank().canKick(target.getRank())) {
                        try {
                            boolean result = true;

                            if(target.isUser(user.getUUID()))
                                result = false;

                            if(result) {

                                clan.removeUser(target);

                                sendMessage(proxiedPlayer, "§7[§6Clan§7] §6"+player.getDisplayName() + "§b wurde gekickt!");

                                sendMessageIfOnline(target, "§7[§6Clan§7] §aDu wurdest vom Clan gekickt");
                                sendUpdateToServer(target);

                            } else {
                                sendMessage(proxiedPlayer, "§7[§6Clan§7] §6"+player.getDisplayName() + "§c konnte nicht gekickt werden!");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        sendMessage(proxiedPlayer, "§7[§9System§7] §cDafür hast du keine Berechtigung... ");
                    }

                } else {
                    sendMessage(proxiedPlayer, "§7[§9System§7] §cDafür hast du keine Berechtigung... ");
                }
                return;
            }

            //Promote Command
            if(label.equalsIgnoreCase("promote"))
            {
                if(user.getRank().canPromote())
                {
                    if(args.length != 2)
                    {
                        sendMessage(proxiedPlayer, "§7[§6AtsukaMC§7] §b§lBenutze: §c/clan promote [UserName]");
                        return;
                    }

                    UUID id = null;
                    String name = args[1];

                    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);

                    if(player != null)
                        id = player.getUniqueId();

                    if(id == null)
                        id = ClanUserManager.getInstance().getIdFromName(name);

                    if(id == null) {
                        sendMessage(proxiedPlayer, "§7[§9System§7] §cDieser Spieler war noch nie online");
                        return;
                    }

                    ClanUser target = ClanUserManager.getInstance().getUser(id);

                    if(target.getClan() != clan) {
                        sendMessage(proxiedPlayer, "§7[§6Clan§7] §cDieser Spieler ist nicht in diesem Clan");
                        return;
                    }
                    if(user.getRank().canPromote(target.getRank())) {
                        try {
                            boolean result = clan.promote(target);

                            if(result) {
                                sendMessage(proxiedPlayer, "§7[§6Clan§7] §6"+player.getDisplayName() + "§b wurde befördert! (nun: §6"+target.getRank().getName()+"§b)");

                                sendMessageIfOnline(target, "§7[§6Clan§7] §aDu wurdest zum §6" +target.getRank().getName() + " §abefördert!");
                                sendUpdateToServer(target);

                            } else {
                                sendMessage(proxiedPlayer, "§7[§6Clan§7] §6"+player.getDisplayName() + "§c konnte nicht befördert werden!");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        sendMessage(proxiedPlayer, "§7[§9System§7] §cDafür hast du keine Berechtigung... ");
                    }

                } else {
                    sendMessage(proxiedPlayer, "§7[§9System§7] §cDafür hast du keine Berechtigung... ");
                }
                return;
            }

            //Demote Command
            if(label.equalsIgnoreCase("demote"))
            {
                if(user.getRank().canDemote())
                {
                    if(args.length != 2)
                    {
                        sendMessage(proxiedPlayer, "§7[§6AtsukaMC§7] §b§lBenutze: §c/clan demote [UserName]");
                        return;
                    }

                    UUID id = null;
                    String name = args[1];

                    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(name);

                    if(player != null)
                        id = player.getUniqueId();

                    if(id == null)
                        id = ClanUserManager.getInstance().getIdFromName(name);

                    if(id == null) {
                        sendMessage(proxiedPlayer, "§7[§9System§7] §cDieser Spieler war noch nie online");
                        return;
                    }

                    ClanUser target = ClanUserManager.getInstance().getUser(id);

                    if(target.getClan() != clan) {
                        sendMessage(proxiedPlayer, "§7[§6Clan§7] §cDieser Spieler ist nicht in diesem Clan");
                        return;
                    }
                    if(user.getRank().canDemote(target.getRank())) {
                        try {
                            boolean result = clan.demote(target);

                            if(result) {
                                sendMessage(proxiedPlayer, "§7[§6Clan§7] §6"+player.getDisplayName() + "§b wurde degradiert! (nun: §6"+target.getRank().getName()+"§b)");

                                sendMessageIfOnline(target, "§7[§6Clan§7] §aDu wurdest zum §6" +target.getRank().getName() + " §adegradiert!");
                                sendUpdateToServer(target);

                            } else {
                                sendMessage(proxiedPlayer, "§7[§6Clan§7] §6"+player.getDisplayName() + "§c konnte nicht degradiert werden!");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        sendMessage(proxiedPlayer, "§7[§9System§7] §cDafür hast du keine Berechtigung... ");
                    }

                } else {
                    sendMessage(proxiedPlayer, "§7[§9System§7] §cDafür hast du keine Berechtigung... ");
                }
                return;
            }

            //Leave Command
            if(label.equalsIgnoreCase("leave"))
            {

                if(args.length != 1)
                {
                    sendMessage(proxiedPlayer, "§7[§6AtsukaMC§7] §b§lBenutze: §c/clan leave");
                    return;
                }

                if(clan.getUsers().length == 1) {
                    sendMessage(proxiedPlayer, "§7[§9System§7] §cNutze /clan abandon");
                    return;
                }

                if(user.getRank() == ClanRank.ADMIN) {
                    sendMessage(proxiedPlayer, "§7[§6Clan§7] §cBenutze zuerst /clan demote [Username], dann führe /clan leave erneut aus!");
                    return;
                }

                clan.removeUser(user);
                sendMessage(proxiedPlayer, "§7[§6Clan§7] §aDu hast den Clan §6"+clan.getName()+"§7[§b"+clan.getShort() + "§7]§a verlassen!");
                sendMessageIfOnline("§7[§6Clan§7] §6"+proxiedPlayer.getDisplayName() + "§c hat den Clan verlassen!", clan.getUsers());
                sendUpdateToServer(user);

                return;
            }

            //Abandon Command
            if(label.equalsIgnoreCase("abandon"))
            {
                if(args.length != 1)
                {
                    sendMessage(proxiedPlayer, "§7[§6AtsukaMC§7] §b§lBenutze: §c/clan abandon");
                    return;
                }

                if(user.getRank() != ClanRank.ADMIN) {
                    sendMessage(proxiedPlayer, "§7[§9System§7] §c§Dafür hast du keine Berechtigung... ");
                    return;
                }

                ClanUser[] allUsers = clan.getUsers();
                for(ClanUser user1 : allUsers)
                    clan.removeUser(user1);

                sendMessageIfOnline("§7[§6Clan§7] §aDein Clan §6"+clan.getName()+"§7[§b"+clan.getShort() + "§7]§a wurde aufgelöst!", allUsers);

                sendMessage(proxiedPlayer, "§7[§6Clan§7] §aDu hast den Clan §6"+clan.getName()+"§7[§b"+clan.getShort() + "§7]§a aufgelöst!");
                sendUpdateToServer(allUsers);

                clanManager.removeClan(clan);
                clanManager.saveClans();

                return;
            }
            sendHelp(proxiedPlayer, true);
        }


    }

    private void sendMessageIfOnline(String message, ClanUser[] users) {
        for(ClanUser user : users)
            sendMessageIfOnline(user, message);
    }

    private void sendMessageIfOnline(ClanUser target, String message) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(target.getUUID());

        if(player != null)
            player.sendMessage(message);
    }

    private void sendInvitation(ProxiedPlayer player, Clan clan) {
        sendMessage(player, "§7[§6Clan§7] §aDer Clan  §6"+clan.getName()+"§7[§b"+clan.getShort() +"§7]§a hat dich eingeladen!");

        TextComponent accept = new TextComponent("[Annehmen]  ");
        accept.setColor(ChatColor.GREEN);
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan accept "+clan.getShort()));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new BaseComponent[] {new TextComponent("§aTritt der Gang §6" + clan.getName() + "§a bei!")}));

        TextComponent decline = new TextComponent("[Ablehnen]");
        decline.setColor(ChatColor.RED);
        decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan decline "+clan.getShort()));
        decline.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new BaseComponent[] {new TextComponent("§cLehne das Angebot des clans §6" + clan.getName() + "§c ab!")}));

        player.sendMessage(accept,decline);
    }

    private String getDisplayname(ClanUser user) {

        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(user.getUUID());
        if(player == null) {
            return user.getNameString();
        }
        return player.getDisplayName();

    }

    public static void sendUpdateToServer(ClanUser... usersToUpdate) {
        ArrayList<ProxiedPlayer> players = new ArrayList<>();
            for(ClanUser user : usersToUpdate) {
                if(user == null)
                    return;
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(user.getUUID());
            if(player != null)
                players.add(player);
        }

        for(ProxiedPlayer player : players) {
            ClanUser user = ClanUserManager.getInstance().getUser(player.getUniqueId());
            if(user != null)
                DataSender.sendToBukkit("clanupdate", user.serializeToBukkit(), player);
        }
    }

    private void sendMessage(ProxiedPlayer proxiedPlayer, String message) {
        proxiedPlayer.sendMessage(message);
    }

    private void sendHelp(ProxiedPlayer proxiedPlayer, boolean inClan) {
        if(!inClan)
            proxiedPlayer.sendMessage("§7[§6AtsukaMC§7] §b§lBenutze: §c/clan create, accept, decline");
        else
            proxiedPlayer.sendMessage("§7[§6AtsukaMC§7] §b§lBenutze: §c/clan info, invite, join, promote, demote, leave, abandon");
    }
}
