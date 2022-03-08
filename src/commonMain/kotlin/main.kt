import com.soywiz.klock.TimeSpan
import com.soywiz.klock.milliseconds
import com.soywiz.korev.Key
import com.soywiz.korge.Korge
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.addUpdater
import com.soywiz.korim.color.Colors
import model.GameBoard

suspend fun main() = Korge(width = 224, height = 248, bgcolor = Colors["#000000"]) {
    GameBoard.create(this)


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