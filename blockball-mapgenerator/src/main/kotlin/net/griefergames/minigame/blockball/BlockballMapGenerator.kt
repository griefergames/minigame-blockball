package net.griefergames.minigame.blockball

import net.griefergames.minigame.shared.model.Location
import net.griefergames.minigame.shared.model.MinigameMap
import net.griefergames.minigame.shared.registry.MapGenerator

class BlockballMapGenerator : MapGenerator {

    private val errors = mutableListOf<String>()

    override fun analyseBlock(map: MinigameMap?, blocks: MutableMap<String, MutableList<Location>>?): MutableMap<Location, String> {
        errors.clear()

        val replacements = mutableMapOf<Location, String>()



        return replacements
    }

    override fun getErrors(): MutableList<String> {
        return errors
    }
}