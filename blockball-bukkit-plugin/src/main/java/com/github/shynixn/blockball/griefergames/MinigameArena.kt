package com.github.shynixn.blockball.griefergames

import com.github.shynixn.blockball.api.business.enumeration.BlockDirection
import com.github.shynixn.blockball.api.business.enumeration.GameType
import com.github.shynixn.blockball.api.persistence.entity.*
import com.github.shynixn.blockball.entity.*
import net.griefergames.minigame.shared.model.MinigameLobby
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

class MinigameArena(
    private val lobby: MinigameLobby
) : Arena {

    val map = lobby.map!!

    override val name: String = map.name
    override var enabled: Boolean = true
    override var displayName: String = map.name
    override var gameType: GameType = GameType.MINIGAME
    override val meta: ArenaMeta = MinigameArenaMeta(lobby)
    override var upperCorner: Position = MinigamePosition(map.minLocation)
    override var lowerCorner: Position = MinigamePosition(map.maxLocation)
    override val center: Position get() {
        return PositionEntity(
            this.lowerCorner.worldName!!,
            (this.lowerCorner.blockX + this.offsetX / 2).toDouble(),
            (this.lowerCorner.blockY + offsetX / 2).toDouble(), (this.lowerCorner.blockZ + offsetZ / 2).toDouble()
        )
    }
    override val offsetX: Int get() {
        return this.upperCorner.blockX - this.lowerCorner.blockX + 1
    }
    override val offsetY: Int get() {
        return this.upperCorner.blockY - this.lowerCorner.blockY + 1
    }
    override val offsetZ: Int get() {
        return this.upperCorner.blockZ - this.lowerCorner.blockZ
    }

    init {
        val spawns = map.locations.entries.filter { it.key.startsWith("area_") }
        if(spawns.size >= 2) {
            setCorners(MinigamePosition(spawns.first().value), MinigamePosition(spawns.last().value))
        }else {
            setCorners(MinigamePosition(map.minLocation), MinigamePosition(map.maxLocation))
        }
    }

    override fun isLocationInSelection(location: Position): Boolean {
        if (location.worldName != null && location.worldName == this.upperCorner.worldName) {
            if (this.upperCorner.x >= location.x && this.lowerCorner.x <= location.x) {
                if (this.upperCorner.y >= location.y + 1 && this.lowerCorner.y <= location.y + 1) {
                    if (this.upperCorner.z >= location.z && this.lowerCorner.z <= location.z) {
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun getRelativeBlockDirectionToLocation(location: Position): BlockDirection {
        if (location.blockX >= upperCorner.blockX && this.upperCorner.z >= location.z && this.lowerCorner.z <= location.z) {
            return BlockDirection.WEST
        }

        if (location.blockX <= lowerCorner.blockX && this.upperCorner.z >= location.z && this.lowerCorner.z <= location.z) {
            return BlockDirection.EAST
        }

        if (location.blockZ >= upperCorner.blockZ && this.upperCorner.x >= location.x && this.lowerCorner.x <= location.x) {
            return BlockDirection.NORTH
        }

        if (location.blockZ <= lowerCorner.blockZ && this.upperCorner.x >= location.x && this.lowerCorner.x <= location.x) {
            return BlockDirection.SOUTH
        }

        return BlockDirection.DOWN
    }

    override fun setCorners(corner1: Position, corner2: Position) {
        this.calculateDownLocation(corner1, corner2)
        this.calculateUpLocation(corner1, corner2)
    }

    private fun calculateUpLocation(corner1: Position, corner2: Position) {
        val x: Int = if (corner1.blockX > corner2.blockX) {
            corner1.blockX
        } else {
            corner2.blockX
        }
        val y: Int = if (corner1.blockY > corner2.blockY) {
            corner1.blockY
        } else {
            corner2.blockY
        }
        val z: Int = if (corner1.blockZ > corner2.blockZ) {
            corner1.blockZ
        } else {
            corner2.blockZ
        }
        this.upperCorner = PositionEntity(corner1.worldName!!, x.toDouble(), y.toDouble(), z.toDouble())
    }

    private fun calculateDownLocation(corner1: Position, corner2: Position) {
        val x: Int = if (corner1.blockX < corner2.blockX) {
            corner1.blockX
        } else {
            corner2.blockX
        }
        val y: Int = if (corner1.blockY < corner2.blockY) {
            corner1.blockY
        } else {
            corner2.blockY
        }
        val z: Int = if (corner1.blockZ < corner2.blockZ) {
            corner1.blockZ
        } else {
            corner2.blockZ
        }
        this.lowerCorner = PositionEntity(corner1.worldName!!, x.toDouble(), y.toDouble(), z.toDouble())
    }

    class MinigameArenaMeta(private val lobby: MinigameLobby) : ArenaMeta {

        override val hubLobbyMeta: HubLobbyMeta = HubLobbyMetaEntity()
        override val spectatorMeta: SpectatorMeta = SpectatorMetaEntity().apply {
            this.spectatorModeEnabled = false
        }
        override val lobbyMeta: LobbyMeta = MinigameLobbyMeta(lobby)

        override val minigameMeta: MinigameMinigameLobbyMeta = MinigameMinigameLobbyMeta(lobby)
        override val bungeeCordMeta: BungeeCordMeta = BungeeCordMetaEntity()

        override val redTeamMeta: TeamMeta = TeamMetaEntity(
            displayName = "Red Team",
            prefix = "RED"
        ).apply {
            val location1 = lobby.map.locations.get("red_goal_1")?.let { MinigamePosition(it) }!!
            val location2 = lobby.map.locations.get("red_goal_2")?.let { MinigamePosition(it) }!!
            this.goal.setCorners(location1, location2)
            this.minAmount = lobby.maxPlayers
            this.maxAmount = (lobby.maxPlayers / 2)
            this.armorContents[1] = ItemStack(Material.IRON_CHESTPLATE).apply {
                this.itemMeta = this.itemMeta.apply {
                    (this as ArmorMeta).trim = ArmorTrim(TrimMaterial.REDSTONE, TrimPattern.WARD)
                }
            }
            this.armorContents[2] = ItemStack(Material.IRON_LEGGINGS).apply {
                this.itemMeta = this.itemMeta.apply {
                    (this as ArmorMeta).trim = ArmorTrim(TrimMaterial.REDSTONE, TrimPattern.WARD)
                }
            }
            this.armorContents[3] = ItemStack(Material.LEATHER_BOOTS).apply {
                this.itemMeta = this.itemMeta.apply {
                    (this as LeatherArmorMeta).setColor(Color.BLACK)
                    (this as ArmorMeta).trim = ArmorTrim(TrimMaterial.QUARTZ, TrimPattern.SHAPER)
                }
            }
        }
        override val blueTeamMeta: TeamMeta = TeamMetaEntity(
            displayName = "Blue Team",
            prefix = "BLUE"
        ).apply {
            val location1 = lobby.map.locations.get("blue_goal_1")?.let { MinigamePosition(it) }!!
            val location2 = lobby.map.locations.get("blue_goal_2")?.let { MinigamePosition(it) }!!
            this.goal.setCorners(location1, location2)
            this.minAmount = lobby.maxPlayers
            this.maxAmount = (lobby.maxPlayers / 2)
            this.armorContents[1] = ItemStack(Material.IRON_CHESTPLATE).apply {
                this.itemMeta = this.itemMeta.apply {
                    (this as ArmorMeta).trim = ArmorTrim(TrimMaterial.LAPIS, TrimPattern.WARD)
                }
            }
            this.armorContents[2] = ItemStack(Material.IRON_LEGGINGS).apply {
                this.itemMeta = this.itemMeta.apply {
                    (this as ArmorMeta).trim = ArmorTrim(TrimMaterial.LAPIS, TrimPattern.WARD)
                }
            }
            this.armorContents[3] = ItemStack(Material.LEATHER_BOOTS).apply {
                this.itemMeta = this.itemMeta.apply {
                    (this as LeatherArmorMeta).setColor(Color.BLACK)
                    (this as ArmorMeta).trim = ArmorTrim(TrimMaterial.QUARTZ, TrimPattern.SHAPER)
                }
            }
        }
        override val hologramMetas: MutableList<HologramMeta> = mutableListOf()
        override val ballMeta: BallMeta = MinigameBallMeta(lobby)
        override val protectionMeta: ArenaProtectionMeta = ArenaProtectionMetaEntity()
        override val rewardMeta: RewardMeta = RewardEntity().apply {
            this.moneyReward.clear()
            this.commandReward.clear()
        }
        override val scoreboardMeta: ScoreboardMeta = ScoreboardEntity().apply {
            this.enabled = false
        }
        override val bossBarMeta: BossBarMeta = BossBarMetaEntity().apply {
            this.enabled = false
        }
        override val doubleJumpMeta: DoubleJumpMeta = DoubleJumpMetaEntity().apply {

        }
        override val customizingMeta: CustomizationMeta = CustomizationMetaEntity().apply {

        }

    }

}