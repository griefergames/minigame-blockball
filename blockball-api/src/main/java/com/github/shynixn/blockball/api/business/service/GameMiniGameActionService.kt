package com.github.shynixn.blockball.api.business.service

import com.github.shynixn.blockball.api.business.enumeration.Team
import com.github.shynixn.blockball.api.persistence.entity.MiniGame

/**
 * Created by Shynixn 2018.
 * <p>
 * Version 1.2
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2018 by Shynixn
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
interface GameMiniGameActionService {
    /**
     * Lets the given [player] leave join the given [game]. Optional can the prefered
     * [team] be specified but the team can still change because of arena settings.
     * Does nothing if the player is already in a Game.
     */
    fun <P> joinGame(game: MiniGame, player: P, team: Team?): Boolean

    /**
     * Lets the given [player] leave the given [game].
     * Does nothing if the player is not in the game.
     */
    fun <P> leaveGame(game: MiniGame, player: P)

    /**
     * Closes the given game and all underlying resources.
     */
    fun closeGame(game: MiniGame)

    /**
     * Lets the given [player] leave spectate the given [game].
     * Does nothing if the player is already spectating a Game.
     */
    fun <P> spectateGame(game: MiniGame, player: P)

    /**
     * Gets called when the given [game] ends with a draw.
     */
    fun onDraw(game: MiniGame)

    /**
     * Actives the next match time. Closes the match if no match time is available.
     */
    fun switchToNextMatchTime(game: MiniGame)

    /**
     * Handles the game actions per tick. [ticks] parameter shows the amount of ticks
     * 0 - 20 for each second.
     */
    fun handle(game: MiniGame, ticks: Int)
}