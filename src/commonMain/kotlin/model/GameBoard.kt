package model

import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.image
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import model.ghosts.Blinky
import model.ghosts.Clyde
import model.ghosts.Inky
import model.ghosts.Pinky

class GameBoard private constructor(
    private val pacman: Pacman,
    private val blinky: Blinky,
    private val pinky: Pinky,
    private val inky: Inky,
    private val clyde: Clyde,
    game: Stage,
    emptyGameBoard: Bitmap
) {

    companion object {
        suspend fun create(game: Stage): GameBoard {
            return GameBoard(
                Pacman.create(game),
                Blinky.create(game),
                Pinky.create(game),
                Inky.create(game),
                Clyde.create(game),
                game,
                resourcesVfs["gameboard.png"].readBitmap()
            )
        }
    }

    private val gameMap = Array(62) { row ->
        Array(56) { col ->
            var pixelCount = 0
            for (y in row * 4 until row * 4 + 4) {
                for (x in col * 4 until col * 4 + 4) {
                    if (emptyGameBoard.getRgba(x, y) != RGBA(0, 0, 0, 0)) {
                        pixelCount++
                    }
                }
            }
            pixelCount > 1
        }
    }

    fun hasCollision(x: Int, y: Int, width: Int, height: Int): Boolean {
        for (i in x until x + width) {
            for (j in y until y + height) {
                if (gameMap[j][i%56]) {
                    return true
                }
            }
        }
        return false
    }

    init {
        game.image(emptyGameBoard)

        pacman.addListener(this)
    }

}