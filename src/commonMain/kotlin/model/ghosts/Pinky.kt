package model.ghosts

import Animation
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.xy
import model.Direction

class Pinky private constructor(animations: Map<Direction, Animation>, game: Stage): Ghost(animations, game) {

    companion object {
        suspend fun create(game: Stage): Pinky {
            return Pinky(Animation.createDirectoryAnimationMap("ghosts/pinky"), game)
        }
    }

    init {
        image.xy(26 * 4, 21 * 4)
    }

    override fun getTarget(): Pair<Int, Int> {
        return Pair(0, 0)
    }

}