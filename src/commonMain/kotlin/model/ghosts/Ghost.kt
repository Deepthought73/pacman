package model.ghosts

import Animation
import com.soywiz.klock.TimeSpan
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.addUpdater
import model.Directory
import model.Entity

abstract class Ghost(animations: Map<Directory, Animation>) : Entity(animations, 0, 0) {

}