package com.github.shynixn.blockball

import com.github.shynixn.blockball.api.business.service.GameActionService
import com.github.shynixn.blockball.api.business.service.GameService
import com.github.shynixn.blockball.api.persistence.entity.Game
import com.github.shynixn.blockball.griefergames.MinigameArena
import com.github.shynixn.blockball.impl.service.GameServiceImpl
import com.google.inject.Inject
import net.griefergames.minigame.event.MinigameLobbySetEvent
import net.griefergames.minigame.event.MinigamePlayerJoinEvent
import net.griefergames.minigame.event.MinigameReadyToStartEvent
import net.griefergames.minigame.minigameService
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GrieferGamesMinigameListener @Inject constructor(
    private val gameService: GameService,
    private val gameActionService: GameActionService
): Listener {

    private lateinit var theGame: Game

    fun lobbySet(event: MinigameLobbySetEvent) {
        prepareGame()
    }

    @EventHandler
    fun onMinigamePlayerJoin(event: MinigamePlayerJoinEvent) {
        gameActionService.joinGame(theGame, event.player, null)
    }

    @EventHandler
    fun onMinigameReadyToStart(event: MinigameReadyToStartEvent) {
        theGame.arena.meta.redTeamMeta.minAmount = (event.lobby.maxPlayers / 2) - 1
        theGame.arena.meta.blueTeamMeta.minAmount = (event.lobby.maxPlayers / 2) - 1
    }

    /**
     * Prepares the arena and the game
     */
    private fun prepareGame() {
        val minigameLobby = minigameService.getMinigameLobby(true) ?: return
        val loadedMinigameMap = minigameLobby.map ?: return
        val arena = MinigameArena(minigameLobby)
        gameService.getAllGames().forEach {
            it.closed = true
        }
        (gameService as GameServiceImpl).initGame(arena)
        theGame = gameService.getAllGames().lastOrNull { it.arena == arena } ?: return
    }


}