package model.ghosts

import Animation
import com.soywiz.korge.view.Stage
import model.Direction

class Inky private constructor(animations: Map<Direction, Animation>, game: Stage): Ghost(animations, game) {

    companion object {
        suspend fun create(game: Stage): Inky {
            return Inky(Animation.createDirectoryAnimationMap("ghosts/inky"), game)
        }
    }

    override fun getAim(): Pair<Int, Int> {
        TODO("Not yet implemented")
    }

}