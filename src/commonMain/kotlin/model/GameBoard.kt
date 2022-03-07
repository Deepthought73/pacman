package model

import com.soywiz.klock.seconds
import com.soywiz.korge.Korge
import com.soywiz.korge.tween.get
import com.soywiz.korge.tween.tween
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.degrees
import com.soywiz.korma.interpolation.Easing

class GameBoard(emptySpriteMap: Bitmap) {

    private val gameMap = Array(62) { row ->
        Array(56) { col ->
            for (y in row * 4 until row * 4 + 4) {
                for (x in col * 4 until col + 4) {

                }
            }
            emptySpriteMap.getRgba()
            false
        }
    }

}