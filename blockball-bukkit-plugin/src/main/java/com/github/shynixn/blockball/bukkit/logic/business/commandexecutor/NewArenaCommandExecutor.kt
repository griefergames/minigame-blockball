package com.github.shynixn.blockball.bukkit.logic.business.commandexecutor

import com.github.shynixn.blockball.bukkit.BlockBallPlugin
import com.github.shynixn.blockball.bukkit.logic.business.commandexecutor.menu.*
import com.github.shynixn.blockball.bukkit.logic.business.helper.ChatBuilder
import com.github.shynixn.blockball.bukkit.logic.persistence.controller.ArenaRepository
import com.google.inject.Inject
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin


/**
 * Created by Shynixn 2018.
 * <p>
 * Version 1.2
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2018 by Shynixn
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
class NewArenaCommandExecutor @Inject constructor(plugin: Plugin) : SimpleCommandExecutor.Registered("blockball", plugin as JavaPlugin) {
    companion object {
        private val HEADER_STANDARD = ChatColor.WHITE.toString() + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "                          BlockBall                         "
        private val FOOTER_STANDARD = ChatColor.WHITE.toString() + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "                           ┌1/1┐                            "
    }

    private val cache = HashMap<Player, Array<Any?>>()
    private var pagecache: MutableList<Page>? = null

    @Inject
    private val openPage: OpenPage? = null

    @Inject
    private val mainConfigurationPage: MainConfigurationPage? = null

    @Inject
    private val mainSettingsPage: MainSettingsPage? = null

    @Inject
    private val ballSettingsPage : BallSettingsPage? = null

    @Inject
    private val ballModifierPage : BallModifierSettingsPage? = null

    @Inject
    private val listablePage: ListablePage? = null

    @Inject
    private val teamSettingsPage: TeamSettingsPage? = null

    @Inject
    private val effectsSettingsPage: EffectsSettingsPage? = null

    @Inject
    private val multipleLinesPage: MultipleLinesPage? = null

    @Inject
    private val hologramsPage: HologramPage? = null

    @Inject
    private val scoreboardPage: ScoreboardPage? = null

    @Inject
    private val bossbarPage: BossbarPage? = null

    @Inject
    private val signSettingsPage: SignSettingsPage? = null

    @Inject
    private val arenaController: ArenaRepository? = null

    @Inject
    private val rewardsPage : RewardsPage? = null

    @Inject
    private val particlesPage: ParticleEffectPage? = null

    @Inject
    private val soundsPage: SoundEffectPage? = null

    @Inject
    private val abilitiesPage: AbilitiesSettingsPage? = null

    @Inject
    private val doubleJumpPage: DoubleJumpPage? = null

    @Inject
    private val miscPage: MiscSettingsPage? = null

    @Inject
    private val gamePropertiesPage: GamePropertiesPage? = null

    @Inject
    private val areaProtectionPage: AreaProtectionPage? = null

    @Inject
    private val teamTextBookPage : TeamTextBookPage? = null

    @Inject
    private val gameSettingsPage : GameSettingsPage? = null

    /**
     * Can be overwritten to listener to all executed commands.
     *
     * @param sender sender
     * @param args   args
     */
    override fun onCommandSenderExecuteCommand(sender: CommandSender, args: Array<out String>) {
        super.onCommandSenderExecuteCommand(sender, args)
        if (sender !is Player) {
            sender.sendMessage(BlockBallPlugin.PREFIX_CONSOLE + ChatColor.RED + "This command does not support console or command blocks.")
        }
    }

    /**
     * Can be overwritten to listen to player executed commands.
     *
     * @param player player
     * @param args   args
     */
    override fun onPlayerExecuteCommand(player: Player, args: Array<String>) {
        for (i in 0..19) {
            player.sendMessage("")
        }
        player.sendMessage(HEADER_STANDARD)
        player.sendMessage("\n")
        val cache: Array<Any?>? = this.cache[player]
        if (!this.cache.containsKey(player)) {
            val anyArray = arrayOfNulls<Any>(8)
            this.cache[player] = anyArray
        }
        val command = BlockBallCommand.from(args) ?: throw IllegalArgumentException("Command is not registered!")
        var usedPage: Page? = null
        for (page in this.getPageCache()) {
            if (page.getCommandKey() === command.key) {
                usedPage = page
                if (command == BlockBallCommand.BACK) {
                    val newPage = this.getPageById(Integer.parseInt(args[2]))
                    this.sendMessage(player, newPage.buildPage(cache!!)!!)
                } else if (command == BlockBallCommand.CLOSE) {
                    this.cache.remove(player)
                    for (i in 0..19) {
                        player.sendMessage("")
                    }
                    return
                } else {
                    val result = page.execute(player, command, cache!!, args)
                    if (result == CommandResult.BACK) {
                        player.performCommand("blockball open back " + usedPage.getPreviousIdFrom(cache))
                        return
                    }
                    if (result != CommandResult.SUCCESS && result != CommandResult.CANCEL_MESSAGE) {
                        ChatBuilder()
                                .component(ChatColor.WHITE.toString() + "" + ChatColor.BOLD + "[" + ChatColor.RED + ChatColor.BOLD + "!" + ChatColor.WHITE + ChatColor.BOLD + "]")
                                .setHoverText(result.message).builder().sendMessage(player)
                    }
                    if (result != CommandResult.CANCEL_MESSAGE) {
                        this.sendMessage(player, page.buildPage(cache)!!)
                    }
                }
                break
            }
        }
        if (usedPage == null)
            throw IllegalArgumentException("Cannot find page with key " + command.key)
        val builder = ChatBuilder()
                .text(ChatColor.STRIKETHROUGH.toString() + "----------------------------------------------------").nextLine()
                .component(" >>Save<< ")
                .setColor(ChatColor.GREEN)
                .setClickAction(ChatBuilder.ClickAction.RUN_COMMAND, BlockBallCommand.ARENA_SAVE.command)
                .setHoverText("Saves the current arena if possible.")
                .builder()
        if (usedPage is ListablePage) {
            builder.component(">>Back<<")
                    .setColor(ChatColor.RED)
                    .setClickAction(ChatBuilder.ClickAction.RUN_COMMAND, (cache!![3] as BlockBallCommand).command)
                    .setHoverText("Back.")
                    .builder()
        } else {
            builder.component(">>Back<<")
                    .setColor(ChatColor.RED)
                    .setClickAction(ChatBuilder.ClickAction.RUN_COMMAND, BlockBallCommand.BACK.command + " " + usedPage.getPreviousIdFrom(cache!!))
                    .setHoverText("Opens the blockball arena configuration.")
                    .builder()
        }
        builder.component(" >>Save and reload<<")
                .setColor(ChatColor.BLUE)
                .setClickAction(ChatBuilder.ClickAction.RUN_COMMAND, BlockBallCommand.ARENA_RELOAD.command)
                .setHoverText("Opens the blockball arena configuration.")
                .builder().sendMessage(player)

        player.sendMessage(FOOTER_STANDARD)
    }

    private fun getPageCache(): List<Page> {
        if (this.pagecache == null) {
            this.pagecache = ArrayList()
            this.pagecache!!.add(this.openPage!!)
            this.pagecache!!.add(this.mainConfigurationPage!!)
            this.pagecache!!.add(this.mainSettingsPage!!)
            this.pagecache!!.add(listablePage!!)
            this.pagecache!!.add(teamSettingsPage!!)
            this.pagecache!!.add(effectsSettingsPage!!)
            this.pagecache!!.add(scoreboardPage!!)
            this.pagecache!!.add(multipleLinesPage!!)
            this.pagecache!!.add(bossbarPage!!)
            this.pagecache!!.add(signSettingsPage!!)
            this.pagecache!!.add(hologramsPage!!)
            this.pagecache!!.add(particlesPage!!)
            this.pagecache!!.add(soundsPage!!)
            this.pagecache!!.add(abilitiesPage!!)
            this.pagecache!!.add(doubleJumpPage!!)
            this.pagecache!!.add(rewardsPage!!)
            this.pagecache!!.add(areaProtectionPage!!)
            this.pagecache!!.add(miscPage!!)
            this.pagecache!!.add(gamePropertiesPage!!)
            this.pagecache!!.add(teamTextBookPage!!)
            this.pagecache!!.add(gameSettingsPage!!)
            this.pagecache!!.add(ballModifierPage!!)
            this.pagecache!!.add(ballSettingsPage!!)
        }
        return this.pagecache!!
    }

    private fun getPageById(id: Int): Page {
        for (page in this.getPageCache()) {
            if (page.id == id) {
                return page
            }
        }
        throw RuntimeException("Page does not exist!")
    }

    private fun sendMessage(player: Player, builder: ChatBuilder) {
        builder.sendMessage(player)
    }
}