package model

import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.bitmap.slice
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Rectangle
import model.ghosts.*

class GameBoard private constructor(
    pacman: Pacman,
    ghosts: List<Ghost>,
    private val game: Stage,
    emptyGameBoard: Bitmap,
    dotDistributionBitmap: Bitmap
) {

    var dotObjects: MutableMap<String, SolidRect> = hashMapOf()

    companion object {
        suspend fun create(game: Stage): GameBoard {
            val res = GameBoard(
                Pacman.create(game),
                listOf(
                    Blinky.create(game),
                    //Pinky.create(game),
                    //Inky.create(game),
                    //Clyde.create(game)
                ),
                game,
                resourcesVfs["gameboard.png"].readBitmap(),
                resourcesVfs["dotDistribution.png"].readBitmap()
            )
            return res
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

    private val dotMap = Array(29) { row ->
        Array(26) { col ->  dotDistributionBitmap.getRgba(11+8*col, 11+8*row) == RGBA(255, 183, 174, 255) }
    }

    fun createDotObjects() {
        dotMap.forEachIndexed {
            y, row -> row.forEachIndexed {
                x, cell -> if (cell) {
                    val rect = SolidRect(2, 2, color=RGBA(255, 183, 174, 255))
                    rect.xy(11+8*x,11+8*y)
                    dotObjects["$x,$y"] = rect
                    game.addChild(rect)
                }
            }
        }
    }

    fun hasCollision(x: Int, y: Int, width: Int, height: Int): Boolean {
        for (i in x until x + width) {
            for (j in y until y + height) {
                if (gameMap[j][i % 56]) {
                    return true
                }
            }
        }
        return false
    }

    init {
        game.image(emptyGameBoard)

        pacman.addListener(this)
        for (g in ghosts)
            g.addListener(this)
    }

}