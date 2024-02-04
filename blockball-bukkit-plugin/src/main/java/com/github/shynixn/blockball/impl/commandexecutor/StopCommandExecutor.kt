package com.github.shynixn.blockball.impl.commandexecutor

import com.github.shynixn.blockball.BlockBallLanguage
import com.github.shynixn.blockball.api.business.executor.CommandExecutor
import com.github.shynixn.blockball.api.business.service.GameService
import com.github.shynixn.blockball.api.business.service.ProxyService
import com.github.shynixn.blockball.impl.extension.mergeArgs
import com.github.shynixn.blockball.impl.extension.stripChatColors
import com.github.shynixn.mcutils.common.ConfigurationService
import com.github.shynixn.mcutils.common.translateChatColors
import com.google.inject.Inject

/**
 * Handles all command patterns to stop running games.
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
class StopCommandExecutor @Inject constructor(
    private val gameService: GameService,
    private val proxyService: ProxyService,
) : CommandExecutor {
    /**
     * Gets called when the given [source] executes the defined command with the given [args].
     */
    override fun <S> onExecuteCommand(source: S, args: Array<out String>): Boolean {
        val mergedArgs = mergeArgs(0, args.size, args)

        for (game in gameService.getAllGames()) {
            if (game.arena.name.equals(mergedArgs, true)) {
                game.closing = true
                proxyService.sendMessage(
                    source,
                    BlockBallLanguage.stopGameMessage.format(
                        game.arena.name + 1,
                        game.arena.displayName.translateChatColors()
                    )
                )
                return true
            } else if (game.arena.displayName.translateChatColors().stripChatColors().equals(mergedArgs, true)) {
                game.closing = true
                proxyService.sendMessage(
                    source,
                    BlockBallLanguage.stopGameMessage.format(
                        game.arena.name + 1,
                        game.arena.displayName.translateChatColors()
                    )
                )
                return true
            }
        }

        return true
    }
}