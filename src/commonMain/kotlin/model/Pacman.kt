package model

import Animation
import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.image
import com.soywiz.korge.view.xy
import com.soywiz.korim.bitmap.slice

import model.Directory.*

class Pacman private constructor(animations: Map<Directory, Animation>, game: Stage) : Entity(animations, 26, 45) {

    companion object {
        suspend fun create(game: Stage): Pacman {
            game.addUpdater(fun Stage.(dt: TimeSpan) {
                /*val scale = dt / 16.milliseconds
                if (input.keys[Key.LEFT]) x -= 2.0 * scale
                if (input.keys.pressing(Key.RIGHT)) x += 2.0 * scale*/
            })

            return Pacman(Animation.createDirectoryAnimationMap("pacman"), game)
        }
    }

    private var direction = UP

    private val image = game.image(animations[direction]!!.next())

    init {
        game.addUpdater {
            image.bitmap = animations[direction]!!.next().slice()
            image.xy(this@Pacman.x * 4, this@Pacman.y * 4)
        }
    }

    override fun render() {
        TODO("Not yet implemented")
    }

}