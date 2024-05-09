package com.github.shynixn.blockball

import com.github.shynixn.blockball.api.business.service.GameActionService
import com.github.shynixn.blockball.api.business.service.GameService
import com.github.shynixn.blockball.api.persistence.entity.Arena
import com.github.shynixn.blockball.api.persistence.entity.Game
import com.github.shynixn.blockball.event.GameEndEvent
import com.github.shynixn.blockball.event.GameGoalEvent
import com.github.shynixn.blockball.griefergames.MinigameArena
import com.github.shynixn.blockball.griefergames.MinigamePosition
import com.github.shynixn.blockball.impl.service.GameServiceImpl
import com.google.inject.Inject
import net.griefergames.minigame.event.MinigameFinishEvent
import net.griefergames.minigame.event.MinigameLobbySetEvent
import net.griefergames.minigame.event.MinigamePlayerJoinEvent
import net.griefergames.minigame.event.MinigameReadyToStartEvent
import net.griefergames.minigame.minigameService
import net.griefergames.minigame.toBukkitGameLocation
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.i18n
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.scoreboard.Team
import java.util.UUID

class GrieferGamesMinigameListener @Inject constructor(
    private val gameService: GameService,
    private val gameActionService: GameActionService
): Listener {

    private var theGame: Game? = null
    private var arena: MinigameArena? = null

    private val preSpawn by lazy {
        minigameService.getMinigameLobby()?.map?.locations?.get("pre_spawn")
    }

    fun lobbySet(event: MinigameLobbySetEvent) {
        prepareGame()
    }

    @EventHandler
    fun onMinigamePlayerJoin(event: MinigamePlayerJoinEvent) {
        preSpawn?.let {
            event.player.teleport(it.toBukkitGameLocation())
        }
    }

    @EventHandler
    fun onMinigameReadyToStart(event: MinigameReadyToStartEvent) {
        val updatedLobby = minigameService.getMinigameLobby(true) ?: return
        arena!!.setCorrectPlayerAmount(updatedLobby)
        (gameService as GameServiceImpl).initGame(arena!!)
        theGame = gameService.getAllGames().lastOrNull { it.arena == arena } ?: return
        Bukkit.getOnlinePlayers().forEach {
            if(updatedLobby.players.contains(it.uniqueId.toString())) {
                gameActionService.joinGame(theGame!!, it, null)
            }
        }
        updateScores()
    }

    @EventHandler
    fun onMinigameFinish(event: MinigameFinishEvent) {
        if(!event.regularEnding) {
            theGame?.let {
                gameActionService.closeGame(it)
            }
        }
        runEndGame();
    }

    @EventHandler
    fun onGoal(event: GameGoalEvent) {
        val teamName = if(event.team == com.github.shynixn.blockball.api.business.enumeration.Team.RED) {
            "${event.game.arena.meta.redTeamMeta.prefix}${event.game.arena.meta.redTeamMeta.displayName}"
        } else {
            "${event.game.arena.meta.blueTeamMeta.prefix}${event.game.arena.meta.redTeamMeta.displayName}"
        }
        Bukkit.broadcast(i18n("minigame.game.blockball.goal", event.player?.name ?: "?", teamName))
        Bukkit.broadcast(i18n("minigame.game.blockball.current_stats",
            "${event.game.arena.meta.redTeamMeta.prefix}${event.game.redScore}",
            "${event.game.arena.meta.blueTeamMeta.prefix}${event.game.blueScore}"))
        updateScores()
    }

    private fun updateTime() {
        //@TODO: Implement
        minigameService.scoreboardService.setSlot4Title(i18n("minigame.game.blockball.current_time"))
        minigameService.scoreboardService.setSlot4Value(i18n("minigame.game.blockball.current_time_value",

        ))
    }

    private fun updateScores() {
        minigameService.scoreboardService.setSlot3Title(i18n("minigame.game.blockball.current_score"))
        minigameService.scoreboardService.setSlot3Value(i18n("minigame.game.blockball.current_score_value",
            Component.text("${theGame?.arena?.meta?.redTeamMeta?.prefix ?: "§c"}${theGame?.redScore ?: 0}"),
            Component.text("${theGame?.arena?.meta?.blueTeamMeta?.prefix ?: "§9"}${theGame?.blueScore ?: 0 }")
        ))
    }

    @EventHandler
    fun onGameEnd(event: GameEndEvent) {
        if(event.game.arena !is MinigameArena) {
            Bukkit.broadcast(Component.text("OTHER GAME ENDED"))
            return
        }
        updateScores();
        val winningTeam = event.winningTeam
        minigameService.setGameFinished(true)
    }

    /**
     * Prepares the arena and the game
     */
    private fun prepareGame() {
        val minigameLobby = minigameService.getMinigameLobby(true) ?: return
        val loadedMinigameMap = minigameLobby.map ?: return
        arena = MinigameArena(minigameLobby)
        gameService.getAllGames().forEach {
            it.closed = true
        }
    }

    private fun runEndGame() {
        val scores = mutableMapOf<UUID, Int>()
        theGame!!.redTeam.forEach {
            if(it is Player) {
                scores[it.uniqueId] = theGame!!.redScore
            }
        }
        theGame!!.blueTeam.forEach {
            if(it is Player) {
                scores[it.uniqueId] = theGame!!.blueScore
            }
        }
        minigameService.setPlayerScores(scores)
    }

}