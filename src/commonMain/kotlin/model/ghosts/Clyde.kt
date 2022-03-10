package model.ghosts

import Animation
import com.soywiz.korge.view.Stage
import model.Direction

class Clyde private constructor(animations: Map<Direction, Animation>, game: Stage): Ghost(animations, game) {

    companion object {
        suspend fun create(game: Stage): Clyde {
            return Clyde(Animation.createDirectoryAnimationMap("ghosts/clyde"), game)
        }
    }

    override fun getTarget(): Pair<Int, Int> {
        TODO("Not yet implemented")
    }

}