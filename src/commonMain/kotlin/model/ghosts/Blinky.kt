package model.ghosts

import Animation
import com.soywiz.korge.view.Stage
import model.Directory

class Blinky private constructor(animations: Map<Directory, Animation>): Ghost(animations) {

    companion object {
        suspend fun create(game: Stage): Blinky {
            return Blinky(Animation.createDirectoryAnimationMap("ghosts/blinky"))
        }
    }

    override fun render() {
        TODO("Not yet implemented")
    }

}