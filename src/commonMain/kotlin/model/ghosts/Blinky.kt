package model.ghosts

import Animation
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.xy
import model.Direction

class Blinky private constructor(animations: Map<Direction, Animation>, game: Stage): Ghost(animations, game) {

    companion object {
        suspend fun create(game: Stage): Blinky {
            return Blinky(Animation.createDirectoryAnimationMap("ghosts/blinky"), game)
        }
    }

    init {
        image.xy(26 * 4, 21 * 4)
        direction = Direction.LEFT
        nextDirection = Direction.LEFT
    }

    override fun getAim(): Pair<Int, Int> {
        return Pair(0, 0)
    }

}