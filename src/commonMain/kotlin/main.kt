import com.soywiz.klock.seconds
import com.soywiz.korge.Korge
import com.soywiz.korge.tween.get
import com.soywiz.korge.tween.tween
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.degrees
import com.soywiz.korma.interpolation.Easing
import model.GameBoard

suspend fun main() = Korge(width = 512, height = 512, bgcolor = Colors["#2b2b2b"]) {
    val emptyGameBoardSprite = resourcesVfs["gameboard.png"].readBitmap()
    val gameBoard = GameBoard(emptyGameBoardSprite)


    /*
    var minDegrees = (-16).degrees
    val maxDegrees = (+16).degrees

    val image = image(resourcesVfs["korge.png"].readBitmap()) {
        rotation = maxDegrees
        anchor(.5, .5)
        scale(.8)
        position(256, 256)
    }

    while (true) {
        image.tween(image::rotation[minDegrees], time = 1.seconds, easing = Easing.LINEAR)
        minDegrees = (minDegrees.degrees - 20).degrees
    }*/
}