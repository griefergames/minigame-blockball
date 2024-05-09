package com.github.shynixn.blockball.griefergames

import com.github.shynixn.blockball.api.business.enumeration.MatchTimeCloseType
import com.github.shynixn.blockball.api.persistence.entity.MatchTimeMeta
import com.github.shynixn.blockball.api.persistence.entity.MinigameLobbyMeta
import com.github.shynixn.blockball.api.persistence.entity.Position
import com.github.shynixn.blockball.entity.MatchTimeMetaEntity
import com.github.shynixn.mcutils.common.ChatColor
import net.griefergames.minigame.getSettingOrDefault
import net.griefergames.minigame.shared.model.MinigameLobby

class MinigameMinigameLobbyMeta(val lobby: MinigameLobby) : MinigameLobbyMeta {

    private val internalMatchTimes = mutableListOf<MatchTimeMeta>()

    override val matchTimes: MutableList<MatchTimeMeta> get() {
        return internalMatchTimes
    }
    override var lobbyDuration: Int = (lobby.getSettingOrDefault("duration") ?: "20").toInt()
    override var lobbySpawnpoint: Position? = lobby.map.locations.entries
        .firstOrNull { it.key == "pre_spawn" }?.let { MinigamePosition(it.value) }
    override var playersRequiredToStartMessage: String = "%blockball_lang_miniGameRemainingPlayers%"

    init {
        val firstPeriod = MatchTimeMetaEntity()
        firstPeriod.duration = 150
        firstPeriod.respawnEnabled = true

        val firstPeriodOverTime = MatchTimeMetaEntity()
        firstPeriodOverTime.closeType = MatchTimeCloseType.NEXT_GOAL
        firstPeriodOverTime.duration = 15
        firstPeriodOverTime.respawnEnabled = false
        firstPeriodOverTime.startMessageTitle = ChatColor.GOLD.toString() + "Overtime"
        firstPeriodOverTime.startMessageSubTitle = "Only a few seconds left"

        val breakPeriod = MatchTimeMetaEntity()
        breakPeriod.duration = 10
        breakPeriod.playAbleBall = false
        breakPeriod.respawnEnabled = false
        breakPeriod.startMessageTitle = ChatColor.GOLD.toString() + "Break"
        breakPeriod.startMessageSubTitle = "Take a short break"

        val secondPeriod = MatchTimeMetaEntity()
        secondPeriod.duration = 150
        secondPeriod.respawnEnabled = true
        secondPeriod.isSwitchGoalsEnabled = true

        val secondPeriodOverTime = MatchTimeMetaEntity()
        secondPeriodOverTime.closeType = MatchTimeCloseType.NEXT_GOAL
        secondPeriodOverTime.duration = 15
        secondPeriodOverTime.respawnEnabled = false
        secondPeriodOverTime.startMessageTitle = ChatColor.GOLD.toString() + "Overtime"
        secondPeriodOverTime.startMessageSubTitle = "Only a few seconds left"

        val coolDownPeriod = MatchTimeMetaEntity()
        coolDownPeriod.duration = 10
        coolDownPeriod.playAbleBall = false
        coolDownPeriod.respawnEnabled = false

        internalMatchTimes.clear()
        internalMatchTimes.addAll(arrayOf(
            firstPeriod,
            firstPeriodOverTime,
            breakPeriod,
            secondPeriod,
            secondPeriodOverTime,
            coolDownPeriod
        ))
    }

}