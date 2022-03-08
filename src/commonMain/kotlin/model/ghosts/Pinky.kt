package model.ghosts

import Animation
import com.soywiz.korge.view.Stage
import model.Directory

class Pinky private constructor(animations: Map<Directory, Animation>): Ghost(animations) {

    companion object {
        suspend fun create(game: Stage): Pinky {
            return Pinky(Animation.createDirectoryAnimationMap("ghosts/pinky"))
        }
    }

    override fun render() {
        TODO("Not yet implemented")
    }

}