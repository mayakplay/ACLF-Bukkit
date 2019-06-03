package com.mayakplay.aclf.pojo;

import com.google.common.base.Objects;
import org.bukkit.entity.Player;

public final class ACLFPlayer {

    private Player player;

    public ACLFPlayer(Player player) {
        this.player = player;
    }

    //region Player getters
    public boolean isOnline() {
        return player.isOnline();
    }

    public String getUsername() {
        return player.getName();
    }

    public String getDisplayedName() {
        return player.getDisplayName();
    }

    public Player getPlayer() {
        return player;
    }
    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ACLFPlayer that = (ACLFPlayer) o;
        return Objects.equal(player.getName(), that.player.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(player.getName());
    }
}