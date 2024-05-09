package com.github.shynixn.blockball.griefergames

import com.github.shynixn.blockball.api.business.annotation.YamlSerialize
import com.github.shynixn.blockball.api.business.enumeration.GameMode
import com.github.shynixn.blockball.api.persistence.entity.LobbyMeta
import com.github.shynixn.blockball.api.persistence.entity.Position
import com.github.shynixn.blockball.entity.PositionEntity
import com.github.shynixn.blockball.entity.SignCollection
import net.griefergames.minigame.shared.model.MinigameLobby

class MinigameLobbyMeta(val lobby: MinigameLobby) : LobbyMeta {

    override var maxScore: Int = lobby.gameSettingOverrides["max-score"]?.toInt() ?: 10

    /** Should players automatically join the other team to even out them?*/
    override var onlyAllowEventTeams: Boolean = false

    /** List of signs which can be clicked to join the game. */
    override val joinSigns: MutableList<Position> get() = sign.joinSigns as MutableList<Position>

    /** Lines displayed on the sign for leaving the match. */
    override var joinSignLines: List<String> = arrayListOf(
        "%blockball_lang_joinSignLine1%",
        "%blockball_lang_joinSignLine2%",
        "%blockball_lang_joinSignLine3%",
        "%blockball_lang_joinSignLine4%"
    )
    /** Lines displayed on the sign for leaving the match. */
    override var leaveSignLines: List<String> = arrayListOf(
        "%blockball_lang_leaveSignLine1%",
        "%blockball_lang_leaveSignLine2%",
        "%blockball_lang_leaveSignLine3%",
        "%blockball_lang_leaveSignLine4%"
    )

    /** List of signs which can be clicked to leave the game. */
    override val leaveSigns: MutableList<Position> get() = sign.leaveSigns as MutableList<Position>

    /** Spawnpoint when someone leaves the hub game. */
    override var leaveSpawnpoint: Position? = null

    /** Minecraft gamemode (Survival, Adventure, Creative) the players should be */
    override var gamemode: GameMode = GameMode.ADVENTURE

    private val sign: SignCollection = SignCollection()

}