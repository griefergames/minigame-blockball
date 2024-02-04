package net.griefergames.minigame.blockball

import net.griefergames.minigame.minigameService
import net.griefergames.minigame.shared.MinigameMainPlugin
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class BlockBallMapGeneratorPlugin : JavaPlugin() {

    override fun onEnable() {
        val minigameSystem = Bukkit.getPluginManager().getPlugin("MiniGameSystem") as MinigameMainPlugin
        minigameSystem.mapGeneratorRegistry.register("blockball", BlockballMapGenerator())
    }

}