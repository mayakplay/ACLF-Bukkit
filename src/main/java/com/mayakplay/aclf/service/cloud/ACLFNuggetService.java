package com.mayakplay.aclf.service.cloud;

import com.google.gson.Gson;
import com.mayakplay.aclf.infrastructure.Nugget;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 05.07.2019.
 */
@Component
@AllArgsConstructor
public class ACLFNuggetService implements NuggetService {

    private final Gson gson;

    @Override
    public void sendToBungee(@NotNull Nugget nugget) {
        final String jsonString = nuggetToJson(nugget);


    }

    private String nuggetToJson(Nugget nugget) {
        return gson.toJson(nugget);
    }

}