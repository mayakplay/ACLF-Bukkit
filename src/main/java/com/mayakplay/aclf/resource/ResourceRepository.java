package com.mayakplay.aclf.resource;

import com.google.gson.Gson;
import com.mayakplay.aclf.ACLF;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Objects;

/**
 * @author mayakplay
 * @since 01.07.2019.
 */
@Component
@AllArgsConstructor
public final class ResourceRepository<T extends Resource> {

    private final Gson gson;

    public void sendToPlayer(T resource, Player player) {
        String json = gson.toJson(resource);

        sendJsonTo(player, json);
    }

    public void clearCache() {

    }


    @SneakyThrows
    private static void sendJsonTo(@NotNull Player player, @NotNull String text) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(text);

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        out.writeUTF(text);

        player.sendPluginMessage(ACLF.getACLF(), "ACLF_COMMANDS", b.toByteArray());
    }

}