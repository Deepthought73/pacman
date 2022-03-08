package model.ghosts

import Animation
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.image
import model.Directory

class Clyde private constructor(animations: Map<Directory, Animation>, game: Stage): Ghost(animations, game) {

    companion object {
        suspend fun create(game: Stage): Clyde {
            return Clyde(Animation.createDirectoryAnimationMap("ghosts/clyde"), game)
        }
    }

}