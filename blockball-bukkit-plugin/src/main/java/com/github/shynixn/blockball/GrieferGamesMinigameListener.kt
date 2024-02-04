package com.github.shynixn.blockball

import com.github.shynixn.blockball.api.business.service.GameActionService
import com.github.shynixn.blockball.api.business.service.GameService
import com.github.shynixn.blockball.griefergames.MinigameArena
import com.google.inject.Inject
import net.griefergames.minigame.event.MinigameLobbySetEvent
import net.griefergames.minigame.minigameService
import org.bukkit.event.Listener

class GrieferGamesMinigameListener @Inject constructor(
    private val gameService: GameService,
    private val gameActionService: GameActionService
): Listener {

    fun lobbySet(event: MinigameLobbySetEvent) {
        prepareArena()
    }

    private fun prepareArena() {
        val minigameLobby = minigameService.getMinigameLobby(true) ?: return
        val loadedMinigameMap = minigameLobby.map ?: return
        val arena = MinigameArena(minigameLobby)

    }


}