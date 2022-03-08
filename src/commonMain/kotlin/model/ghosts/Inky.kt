package model.ghosts

import Animation
import com.soywiz.korge.view.Stage
import model.Directory

class Inky private constructor(animations: Map<Directory, Animation>): Ghost(animations) {

    companion object {
        suspend fun create(game: Stage): Inky {
            return Inky(Animation.createDirectoryAnimationMap("ghosts/inky"))
        }
    }

    override fun render() {
        TODO("Not yet implemented")
    }

}