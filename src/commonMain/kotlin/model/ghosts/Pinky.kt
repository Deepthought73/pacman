package model.ghosts

import Animation
import com.soywiz.korge.view.Stage
import model.Directory

class Pinky private constructor(animations: Map<Directory, Animation>, game: Stage): Ghost(animations, game) {

    companion object {
        suspend fun create(game: Stage): Pinky {
            return Pinky(Animation.createDirectoryAnimationMap("ghosts/pinky"), game)
        }
    }

}