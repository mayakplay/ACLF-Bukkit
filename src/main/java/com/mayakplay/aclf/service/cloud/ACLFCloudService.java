package com.mayakplay.aclf.service.cloud;

import com.mayakplay.aclf.ACLF;
import com.mayakplay.aclf.cloud.infrastructure.NettyGatewayClient;
import com.mayakplay.aclf.cloud.stereotype.GatewayClient;
import com.mayakplay.aclf.cloud.stereotype.GatewayInfo;
import com.mayakplay.aclf.cloud.stereotype.Nugget;
import com.mayakplay.aclf.event.NuggetReceiveEvent;
import com.mayakplay.aclf.exception.UnconnectedGatewayException;
import com.mayakplay.aclf.util.Pair;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 25.07.2019.
 */
@Component
public class ACLFCloudService implements CloudService {

    private static final String CONFIG_FILE_NAME = "cloud";

    private String cloudIp;
    private int cloudPort;

    @Getter
    private String clientType;

    private final GatewayClient gatewayClient;
    private GatewayInfo gatewayInfo;

    public ACLFCloudService() {
        if (loadConfig()) {
            this.gatewayClient = startCloudClient();
        } else {
            this.gatewayClient = null;
        }
    }

    private boolean loadConfig() {
        try {
            YamlConfiguration yamlConfiguration = new YamlConfiguration();
            yamlConfiguration.load(new File(ACLF.getACLF().getDataFolder(), CONFIG_FILE_NAME + ".yml"));

            final String host = yamlConfiguration.getString("host");

            assert host != null;
            final String[] hostStringSplit = host.split(":");

            this.cloudIp = hostStringSplit[0];
            this.cloudPort = Integer.parseInt(hostStringSplit[1].trim());
            this.clientType = yamlConfiguration.getString("client-type");

            System.out.println(cloudIp + "::" + cloudPort);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void onNuggetReceive(Nugget nugget) {
        final NuggetReceiveEvent event = new NuggetReceiveEvent(
                nugget.getParameter("tag"), nugget.getMessage(), nugget.getParameters());

        Bukkit.getPluginManager().callEvent(event);
    }


    private void setGatewayInfo(GatewayInfo gatewayInfo, Map<String, String> params) {
        this.gatewayInfo = gatewayInfo;
    }

    private GatewayClient startCloudClient() {
        final HashMap<String, String> map = new HashMap<>();
        map.put("minecraftPort", Bukkit.getServer().getPort() + "");

        final NettyGatewayClient gatewayClient = new NettyGatewayClient(cloudIp, cloudPort, clientType, map);
        gatewayClient.addRegistrationCallback(this::setGatewayInfo);
        gatewayClient.addReceiveCallback(this::onNuggetReceive);

        return gatewayClient;
    }

    @Override
    @SafeVarargs
    public final void sendNugget(String message, String tag, Pair<String, String>... parameters) throws UnconnectedGatewayException {
        if (gatewayInfo != null && gatewayClient != null) {
            final Pair<String, String> tagParamPair = Pair.of("tag", tag);

            final HashMap<String, String> parametersMap = Arrays.stream(parameters)
                    .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond, (a, b) -> b, HashMap::new));

            parametersMap.put(tagParamPair.getFirst(), tagParamPair.getSecond());

            gatewayClient.sendNugget(message, parametersMap);
        } else {
            throw new UnconnectedGatewayException();
        }
    }

    @Override
    public @Nullable String getAssignedId() {
        if (gatewayInfo != null) return gatewayInfo.getAssignedId();
        return null;
    }

}
