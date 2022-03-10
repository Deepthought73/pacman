import com.soywiz.korge.Korge
import com.soywiz.korim.color.Colors
import model.GameBoard
import model.offset

suspend fun main() = Korge(width = 224, height = 248 + offset, bgcolor = Colors["#000000"]) {
    val gameBoard = GameBoard.create(this)
    gameBoard.createPowerPellets()
    gameBoard.createDotObjects()


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