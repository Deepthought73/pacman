package model

import Animation

abstract class Entity(
    protected val animations: Map<Directory, Animation>,
    protected var x: Int,
    protected var y: Int,
) {

    abstract fun render()

}