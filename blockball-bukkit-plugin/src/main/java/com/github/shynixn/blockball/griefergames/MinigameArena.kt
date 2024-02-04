package com.github.shynixn.blockball.griefergames

import com.github.shynixn.blockball.api.business.enumeration.BlockDirection
import com.github.shynixn.blockball.api.business.enumeration.GameType
import com.github.shynixn.blockball.api.persistence.entity.*
import net.griefergames.minigame.minigameService
import net.griefergames.minigame.service.MinigameService
import net.griefergames.minigame.shared.model.MinigameLobby
import net.griefergames.minigame.shared.model.MinigameMap

class MinigameArena(
    private val lobby: MinigameLobby
) : Arena {

    val map = lobby.map!!
    val mapMeta = MinigameArenaMeta(lobby)

    override val name: String get() = map.name
    override var enabled: Boolean get() = true
        set(value) {}
    override var displayName: String get() = map.name
        set(value) {}
    override var gameType: GameType get() = GameType.MINIGAME
        set(value) {}
    override val meta: ArenaMeta get() = mapMeta
    override val upperCorner: Position get() = MinigamePosition(map.minLocation)
    override val lowerCorner: Position get() = MinigamePosition(map.maxLocation)
    override val center: Position get() = TODO("Not yet implemented")
    override val offsetX: Int get() = TODO("Not yet implemented")
    override val offsetY: Int get() = TODO("Not yet implemented")
    override val offsetZ: Int get() = TODO("Not yet implemented")

    override fun isLocationInSelection(location: Position): Boolean {
        TODO("Not yet implemented")
    }

    override fun getRelativeBlockDirectionToLocation(location: Position): BlockDirection {
        TODO("Not yet implemented")
    }

    override fun setCorners(corner1: Position, corner2: Position) {
        TODO("Not yet implemented")
    }


    class MinigameArenaMeta(private val lobby: MinigameLobby) : ArenaMeta {

        override val hubLobbyMeta: HubLobbyMeta
            get() = TODO("Not yet implemented")
        override val spectatorMeta: SpectatorMeta
            get() = TODO("Not yet implemented")
        override val lobbyMeta: LobbyMeta
            get() = TODO("Not yet implemented")
        override val minigameMeta: MinigameLobbyMeta
            get() = TODO("Not yet implemented")
        override val bungeeCordMeta: BungeeCordMeta
            get() = TODO("Not yet implemented")
        override val redTeamMeta: TeamMeta
            get() = TODO("Not yet implemented")
        override val blueTeamMeta: TeamMeta
            get() = TODO("Not yet implemented")
        override val hologramMetas: MutableList<HologramMeta>
            get() = TODO("Not yet implemented")
        override val ballMeta: BallMeta
            get() = TODO("Not yet implemented")
        override val protectionMeta: ArenaProtectionMeta
            get() = TODO("Not yet implemented")
        override val rewardMeta: RewardMeta
            get() = TODO("Not yet implemented")
        override val scoreboardMeta: ScoreboardMeta
            get() = TODO("Not yet implemented")
        override val bossBarMeta: BossBarMeta
            get() = TODO("Not yet implemented")
        override val doubleJumpMeta: DoubleJumpMeta
            get() = TODO("Not yet implemented")
        override val customizingMeta: CustomizationMeta
            get() = TODO("Not yet implemented")


    }

}