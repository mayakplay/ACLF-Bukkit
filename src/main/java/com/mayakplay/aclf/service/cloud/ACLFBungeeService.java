package com.mayakplay.aclf.service.cloud;

import com.mayakplay.aclf.ACLF;
import com.mayakplay.aclf.dto.ACLFCloudSettings;
import com.mayakplay.aclf.dto.CloudSettings;
import com.mayakplay.aclf.net.SpigotLinkServer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author mayakplay
 * @version 0.0.1
 * @since 07.07.2019.
 */
@Service
public class ACLFBungeeService implements BungeeService {

    @Nullable
    private SpigotLinkServer linkServer;

    @Getter
    @Nullable
    private CloudSettings cloudSettings = null;

    public ACLFBungeeService() {

        try {
            this.cloudSettings = loadCloudSettings();
            if (this.cloudSettings != null) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Cloud settings loaded.");
                connectToCloudServer(this.cloudSettings);
                Bukkit.getConsoleSender().sendMessage(
                        ChatColor.AQUA + "Connected to bungee " + ChatColor.YELLOW + cloudSettings.getBungeeIp() +
                        ChatColor.AQUA + " with nugget port = " + ChatColor.YELLOW + cloudSettings.getBungeeNuggetPort()
                );

            }
        } catch (IOException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @SuppressWarnings("ConstantConditions")
    private CloudSettings loadCloudSettings() throws IOException, InvalidConfigurationException {
        final File parentFile = ACLF.getACLF().getDataFolder();
        final File[] files = parentFile.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().equals("bungee.yml")) {

                    YamlConfiguration yamlConfiguration = new YamlConfiguration();
                    yamlConfiguration.load(file);

                    //getting the main yml sections
                    final MemorySection settingsMemorySection = (MemorySection) yamlConfiguration.get("settings");
                    final MemorySection cloudMemorySection    = (MemorySection) yamlConfiguration.get("cloud");

                    //find maps in sections
                    final Map<String, Object> settingsValues = settingsMemorySection.getValues(false);
                    final Map<String, Object> cloudValues    = cloudMemorySection.getValues(false);

                    //fill values from yml configuration
                    final int    nuggetPort =       (int)    settingsValues.get("nugget-port");
                    final String serverType =       (String) settingsValues.get("server-type");
                    final int    bungeeNuggetPort = (int)    cloudValues.get("nugget-port");
                    final String bungeeIp =         (String) cloudValues.get("bungee-ip");
                    final String bungeePassword =   (String) cloudValues.get("password");

                    return new ACLFCloudSettings(nuggetPort, serverType, bungeeNuggetPort, bungeeIp, bungeePassword);
                }
            }
        }
        return null;
    }

    private void connectToCloudServer(CloudSettings cloudSettings) {
//        linkServer = new SpigotLinkServer(cloudSettings.getNuggetPort());
//        linkServer.registerOnBungee(cloudSettings.getBungeeIp(), cloudSettings.getBungeeNuggetPort(), cloudSettings.getBungeePassword());
//        linkServer.startListening();
    }

}