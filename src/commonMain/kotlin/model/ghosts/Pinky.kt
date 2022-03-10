package model.ghosts

import Animation
import com.soywiz.korge.view.Stage
import model.Direction

class Pinky private constructor(animations: Map<Direction, Animation>, game: Stage): Ghost(animations, game) {

    companion object {
        suspend fun create(game: Stage): Pinky {
            return Pinky(Animation.createDirectoryAnimationMap("ghosts/pinky"), game)
        }
    }

    override fun getAim(): Pair<Int, Int> {
        TODO("Not yet implemented")
    }

}