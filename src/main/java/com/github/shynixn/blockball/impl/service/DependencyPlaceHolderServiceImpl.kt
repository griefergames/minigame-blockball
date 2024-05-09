package com.github.shynixn.blockball.impl.service

import com.github.shynixn.blockball.contract.GameService
import com.github.shynixn.blockball.contract.PlaceHolderService
import com.github.shynixn.blockball.entity.Game
import com.github.shynixn.blockball.entity.PlayerInformation
import com.github.shynixn.blockball.entity.TeamMeta
import com.github.shynixn.mcutils.database.api.CachePlayerRepository
import com.google.inject.Inject
import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/**
 * Handles the connection to the placeholder API plugin.
 */
class DependencyPlaceHolderServiceImpl @Inject constructor(
    private val plugin: Plugin,
    private val gameService: GameService,
    private val cachePlayerRepository: CachePlayerRepository<PlayerInformation>,
) : PlaceholderExpansion(), PlaceHolderService {
    private val placeHolderService = PlaceHolderServiceImpl(gameService, cachePlayerRepository)
    private var registered: Boolean = false

    init {
        this.registerListener()
    }

    /**
     * Registers the placeholder hook if it is not already registered.
     */
    fun registerListener() {
        if (!registered) {
            this.register()
            registered = true
        }
    }

    /**
     * Gets the expansion version which is the same of the plugin version.
     */
    override fun getVersion(): String {
        return plugin.description.version
    }

    /**
     * Gets the expansion author for placeholderapi.
     */
    override fun getAuthor(): String {
        return plugin.description.authors[0]
    }

    /**
     * Gets the identifier which is required by placeholderapi to match the placeholder against this plugin.
     */
    override fun getIdentifier(): String {
        return "blockball"
    }

    override fun onPlaceholderRequest(player: Player?, params: String?): String? {
        if (params == null) {
            return null
        }

        val parts = params.split("_")
        val finalPart = parts[parts.size - 1]
        val newParams = parts.dropLast(1).joinToString("_")

        var optSelectedGame = gameService.getGameFromName(finalPart)
        if (optSelectedGame.isPresent) {
            val selectedGame = optSelectedGame.get()
            val teamPair = if (player != null && selectedGame.redTeam.contains(player)) {
                Pair(selectedGame.arena.meta.redTeamMeta, selectedGame.redTeam.size)
            } else if (player != null && selectedGame.blueTeam.contains(player)) {
                Pair(selectedGame.arena.meta.blueTeamMeta, selectedGame.blueTeam.size)
            } else {
                Pair(null, null)
            }
            return replacePlaceHolders(
                "%blockball_${newParams}%", player, selectedGame, teamPair.first, teamPair.second
            )
        }

        if (player != null) {
            optSelectedGame = gameService.getGameFromPlayer(player)

            if (optSelectedGame.isPresent) {
                val selectedGame = optSelectedGame.get()
                val teamPair = if (selectedGame.redTeam.contains(player)) {
                    Pair(selectedGame.arena.meta.redTeamMeta, selectedGame.redTeam.size)
                } else if (selectedGame.blueTeam.contains(player)) {
                    Pair(selectedGame.arena.meta.blueTeamMeta, selectedGame.blueTeam.size)
                } else {
                    Pair(null, null)
                }
                return replacePlaceHolders(
                    "%blockball_${params}%", player, selectedGame, teamPair.first, teamPair.second
                )
            } else {
                return replacePlaceHolders("%blockball_${params}%", player, null, null, null)
            }
        }

        return null
    }

    /**
     * Replaces the given text with properties from the given [game], optional [teamMeta] and optional size.
     */
    override fun replacePlaceHolders(
        text: String, player: Player?, game: Game?, teamMeta: TeamMeta?, currentTeamSize: Int?
    ): String {
        val replacedInput = placeHolderService.replacePlaceHolders(text, player, game, teamMeta, currentTeamSize)
        return PlaceholderAPI.setPlaceholders(player, replacedInput)
    }
}
