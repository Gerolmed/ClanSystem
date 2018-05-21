/*
 * Copyright (c) 2018.
 * This content has been created by Gerolmed! Sharing this content without permission is not aloud, as well as modifying any code parts and claiming ownership!
 */

package de.gerolmed.lib.clan.utils;

public enum ClanRank {
    GUEST(-1, "§7Gast", false, false, false, false, false),
    USER(-1, "§aMitglied", false, false, false, false, false),
    MOD(-1, "§a§lModerator", false, false, false, true, false),
    VICE_ADMIN(-1, "§cVize-Anführer", true, true, true, true, true),
    ADMIN(4, "§c§lClan Lord", true, true, true, true, true);


    private int maxAmount;
    private String name;
    private boolean canInvite, canPromote, canDemote, canKick, canDelete;

    ClanRank(int maxAmount, String name, boolean canInvite, boolean canPromote, boolean canDemote, boolean canKick, boolean canDelete) {
        this.maxAmount = maxAmount;
        this.name = name;
        this.canInvite = canInvite;
        this.canPromote = canPromote;
        this.canDemote = canDemote;
        this.canKick = canKick;
        this.canDelete = canDelete;
    }

    public static ClanRank getNextRank(ClanRank rank) {

        ClanRank[] ranks = values();

        int nextRankId = rank.ordinal();
        nextRankId++;

        if(nextRankId >= ranks.length)
            return null;

        return ranks[nextRankId];

    }

    public static ClanRank getPreviousRank(ClanRank rank) {

        ClanRank[] ranks = values();

        int nextRankId = rank.ordinal();
        nextRankId--;

        if(nextRankId < 0)
            return null;

        return ranks[nextRankId];
    }

    public static ClanRank findVal(String val) {
        ClanRank rank = null;

        try {
            rank = valueOf(val);
        } catch (Exception ex) {}

        return rank;
    }

    public int getMaxUsers() {
        return maxAmount;
    }

    public String getName() {
        return name;
    }

    public boolean canInvite() {
        return canInvite;
    }

    public boolean canPromote() {
        return canPromote;
    }
    public boolean canDemote() {
        return canDemote;
    }

    public boolean canPromote(ClanRank rank) {
        return ordinal() > rank.ordinal() || this == ClanRank.ADMIN;
    }
    public boolean canDemote(ClanRank rank) {
        return ordinal() > rank.ordinal() || this == ClanRank.ADMIN;
    }

    public boolean canKick(ClanRank rank) {
        return ordinal() > rank.ordinal() || this == ClanRank.ADMIN;
    }

    public boolean canKick() {
        return canKick;
    }
}
