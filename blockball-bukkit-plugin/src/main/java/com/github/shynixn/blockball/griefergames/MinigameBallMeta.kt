package com.github.shynixn.blockball.griefergames

import com.github.shynixn.blockball.api.business.enumeration.BallActionType
import com.github.shynixn.blockball.api.business.enumeration.BallSize
import com.github.shynixn.blockball.api.persistence.entity.*
import com.github.shynixn.blockball.entity.MovementConfigurationEntity
import net.griefergames.minigame.shared.model.MinigameLobby

class MinigameBallMeta(val lobby: MinigameLobby, val arena: MinigameArena) : BallMeta {
    override var enabledKick: Boolean = true
    override var itemNbt: String? = ""
    override var itemType: String = "PLAYER_HEAD,397"
    override var itemDamage: Int = 3
    override var enabledPass: Boolean = true
    override var isSlimeVisible: Boolean = false
    override var enabledInteract: Boolean = true
    override var delayInTicks: Int = 0
    override var spawnpoint: Position? get() = arena.center.apply {
        this.y = arena.lowerCorner.y
    }
        set(value) {}
    override var size: BallSize = BallSize.NORMAL
    override var skin: String = "http://textures.minecraft.net/texture/8e4a70b7bbcd7a8c322d522520491a27ea6b83d60ecf961d2b4efbbf9f605d"
    override var rotating: Boolean = true
    override var hitBoxRelocation: Double = 0.0
    override var interactionHitBoxSize: Double = 2.0
    override var kickPassHitBoxSize: Double = 5.0
    override var kickPassDelay: Int = 5
    override var interactionCoolDown: Int = 20
    override var alwaysBounce: Boolean = true
    override val movementModifier: MovementConfiguration = MovementConfigurationEntity()
    override val particleEffects: MutableMap<BallActionType, Particle> = mutableMapOf()
    override val soundEffects: MutableMap<BallActionType, Sound> = mutableMapOf()
}