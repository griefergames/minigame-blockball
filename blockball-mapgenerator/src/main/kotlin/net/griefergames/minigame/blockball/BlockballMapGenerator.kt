package net.griefergames.minigame.blockball

import net.griefergames.minigame.shared.model.Location
import net.griefergames.minigame.shared.model.MinigameMap
import net.griefergames.minigame.shared.registry.MapGenerator
import org.bukkit.Material

class BlockballMapGenerator : MapGenerator {

    private val errors = mutableListOf<String>()

    override fun analyseBlock(map: MinigameMap, blockList: MutableMap<String, MutableList<Location>>): MutableMap<Location, String> {
        errors.clear()

        if(!blockList.containsKey(Material.DISPENSER.name)) {
            errors.add("minigame.game.blockball.error.no_prespawn_dispenser")
            return mutableMapOf()
        }
        if(!blockList.containsKey(Material.REDSTONE_BLOCK.name)) {
            errors.add("minigame.game.blockball.error.no_redteam_goal")
            return mutableMapOf()
        }
        if(!blockList.containsKey(Material.LAPIS_BLOCK.name)) {
            errors.add("minigame.game.blockball.error.no_blueteam_goal")
            return mutableMapOf()
        }
        val replacements = mutableMapOf<Location, String>()
        var redGoali = 0
        blockList[Material.REDSTONE_BLOCK.name]?.forEach {
            redGoali++
            replacements[it] = Material.IRON_BLOCK.name
            map.locations["red_goal_$redGoali"] = it
        }
        val redGoals = map.locations.filter { it.key.startsWith("red_goal_") }
        val highestRedGoal = redGoals.maxByOrNull { it.value.y }
        if(highestRedGoal != null) {
            blockList.entries.firstOrNull {
                it.value.any { subit ->
                    subit.x == highestRedGoal.value.x && subit.y == highestRedGoal.value.y - 1 && subit.z == highestRedGoal.value.z
                }
            }?.let { repl ->
                redGoals.forEach {
                    replacements[it.value] = repl.key
                }
            }
        }
        var blueGoali = 0
        blockList[Material.LAPIS_BLOCK.name]?.forEach {
            blueGoali++
            replacements[it] = Material.IRON_BLOCK.name
            map.locations["blue_goal_$blueGoali"] = it
        }
        val blueGoals = map.locations.filter { it.key.startsWith("blue_goal_") }
        val highestRedBlueGoal = redGoals.maxByOrNull { it.value.y }
        if(highestRedBlueGoal != null) {
            blockList.entries.firstOrNull {
                it.value.any { subit ->
                    subit.x == highestRedBlueGoal.value.x && subit.y == highestRedBlueGoal.value.y - 1 && subit.z == highestRedBlueGoal.value.z
                }
            }?.let { repl ->
                blueGoals.forEach {
                    replacements[it.value] = repl.key
                }
            }
        }

        blockList[Material.DISPENSER.name]?.forEach {
            replacements[it] = Material.AIR.name
            map.locations["pre_spawn"] = it.clone().apply {
                this.x += 0.5
                this.z += 0.5
            }
        }
        var spawnCounter = 0
        blockList[Material.DROPPER.name]?.forEach {
            spawnCounter++
            replacements[it] = Material.AIR.name
            map.locations["spawn_$spawnCounter"] = it.clone().apply {
                this.x += 0.5
                this.z += 0.5
            }
        }
        var edgeCounter = 0
        blockList[Material.FURNACE.name]?.forEach {
            edgeCounter++
            replacements[it] = Material.AIR.name
            map.locations["area_$edgeCounter"] = it.clone().apply {
                this.x += 0.5
                this.z += 0.5
            }
        }

        return replacements
    }

    override fun getErrors(): MutableList<String> {
        return errors
    }
}