package model.ghosts

import Animation
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.xy
import model.Direction

class Inky private constructor(animations: Map<Direction, Animation>, game: Stage): Ghost(animations, game) {

    companion object {
        suspend fun create(game: Stage): Inky {
            return Inky(Animation.createDirectoryAnimationMap("ghosts/inky"), game)
        }
    }

    init {
        image.xy(26 * 4, 21 * 4)
    }

    override fun getTarget(): Pair<Int, Int> {
        return Pair(224, 248)
    }

}