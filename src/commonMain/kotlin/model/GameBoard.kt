package model

import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.RGBA

class GameBoard(emptySpriteMap: Bitmap) {

    private val gameMap = Array(62) { row ->
        Array(56) { col ->
            var pixelCount = 0
            for (y in row * 4 until row * 4 + 4) {
                for (x in col * 4 until col * 4 + 4) {
                    if (emptySpriteMap.getRgba(x, y) != RGBA(0, 0, 0)) {
                        pixelCount++
                    }
                }
            }
            pixelCount > 1
        }
    }

    init {
        for (i in gameMap.indices) {
            for (j in gameMap[0].indices) {
                print(if (gameMap[i][j]) "11" else "  ")
            }
            println()
        }
    }

}