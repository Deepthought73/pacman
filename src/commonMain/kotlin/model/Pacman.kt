package model

import Animation
import com.soywiz.klock.TimeSpan
import com.soywiz.korev.Key
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.addUpdater
import com.soywiz.korge.view.xy
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.bitmap.BitmapSlice
import com.soywiz.korim.bitmap.slice
import model.Direction.*
import model.ghosts.Ghost

class Pacman private constructor(
    animations: Map<Direction, Animation>,
    private val scoreBitmaps: Map<Int, Bitmap>,
    game: Stage,
    offset: Int
) :
    Entity(animations, game) {

    private var showScoreTimer = 0
    private var showScore = 200

    companion object {
        suspend fun create(game: Stage, scoreBitmaps: Map<Int, Bitmap>): Pacman {
            return Pacman(
                Animation.createDirectoryAnimationMap("pacman"),
                scoreBitmaps,
                game,
                offset
            )
        }
    }

    fun initialPos() {
        image.xy(26 * 4, 45 * 4 + offset)
    }

    init {
        initialPos()
    }

    override fun getNextBitmap(gameBoard: GameBoard): BitmapSlice<Bitmap> {
        if (showScoreTimer <= 0) {
            return super.getNextBitmap(gameBoard)
        } else {
            println("get bitmap for score: "+showScore)
            println(scoreBitmaps.keys)
            return scoreBitmaps[showScore]!!.slice()
        }
    }

    fun showScore(score: Int) {
        showScoreTimer = 3
        showScore = score
    }

    override fun addListener(gameBoard: GameBoard) {
        super.addListener(gameBoard)

        game.addUpdater(fun Stage.(_: TimeSpan) {
            if (input.keys.pressing(Key.LEFT)) nextDirection = LEFT
            else if (input.keys.pressing(Key.RIGHT)) nextDirection = RIGHT
            else if (input.keys.pressing(Key.UP)) nextDirection = UP
            else if (input.keys.pressing(Key.DOWN)) nextDirection = DOWN
            gameBoard.checkDotCollision()
            gameBoard.checkPowerPalletCollision()
            gameBoard.checkGhostCollision()
        })
    }

    override fun getSpeed(): Double {
        return if (Ghost.isOneFrightened) 0.9
        else 0.8
    }

}