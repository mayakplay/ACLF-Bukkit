package com.mayakplay.aclf;

import org.bukkit.plugin.Plugin;

public class ACLF {

    public static Plugin getACLF() {
        return ACLFPluginAdapter.getPlugin(ACLFPluginAdapter.class);
    }

}