import com.soywiz.korge.Korge
import com.soywiz.korge.view.image
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import model.GameBoard

suspend fun main() = Korge(width = 224, height = 248, bgcolor = Colors["#000000"]) {
    val emptyGameBoardBitmap = resourcesVfs["gameboard.png"].readBitmap()
    val gameBoard = GameBoard(emptyGameBoardBitmap)

    image(emptyGameBoardBitmap)

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