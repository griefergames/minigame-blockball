package com.github.shynixn.blockball.griefergames

import com.github.shynixn.blockball.api.persistence.entity.Position
import net.griefergames.minigame.service.MinigameService
import net.griefergames.minigame.shared.model.Location

class MinigamePosition(val location: Location) : Position {
    override var worldName: String?
        get() = MinigameService.GAME_WORLD_NAME
        set(value) {}
    override var x: Double
        get() = location.x
        set(value) {}
    override var y: Double
        get() = location.y
        set(value) {}
    override var z: Double
        get() = location.z
        set(value) {}
    override var yaw: Double
        get() = location.yaw.toDouble()
        set(value) {}
    override var pitch: Double
        get() = location.pitch.toDouble()
        set(value) {}
    override val blockX: Int
        get() = location.x.toInt()
    override val blockY: Int
        get() = location.y.toInt()
    override val blockZ: Int
        get() = location.z.toInt()

    override fun add(x: Double, y: Double, z: Double): Position {
        TODO("Not yet implemented")
    }

    override fun subtract(position: Position): Position {
        TODO("Not yet implemented")
    }

    override fun distance(o: Position): Double {
        TODO("Not yet implemented")
    }

    override fun distanceSquared(o: Position): Double {
        TODO("Not yet implemented")
    }

    override fun normalize(): Position {
        TODO("Not yet implemented")
    }

    override fun dot(other: Position): Double {
        TODO("Not yet implemented")
    }

    override fun length(): Double {
        TODO("Not yet implemented")
    }

    override fun multiply(multiplier: Double): Position {
        TODO("Not yet implemented")
    }

    override fun clone(): Position {
        TODO("Not yet implemented")
    }
}