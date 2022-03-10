package model

enum class Direction {
    UP, DOWN, RIGHT, LEFT;

    fun invert(): Direction {
        return when (this) {
            UP -> DOWN
            DOWN -> UP
            RIGHT -> LEFT
            LEFT -> RIGHT
        }
    }
}