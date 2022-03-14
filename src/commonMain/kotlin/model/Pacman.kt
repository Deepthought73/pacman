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
    private val score200: Bitmap,
    private val score400: Bitmap,
    private val score800: Bitmap,
    private val score1600: Bitmap,
    game: Stage,
    offset: Int
) :
    Entity(animations, game) {

    private var showScoreTimer = 0.0
    private var showScore = 200

    companion object {
        suspend fun create(game: Stage,
                           score200: Bitmap,
                           score400: Bitmap,
                           score800: Bitmap,
                           score1600: Bitmap,): Pacman {
            return Pacman(
                Animation.createDirectoryAnimationMap("pacman"),
                score200,
                score400,
                score800,
                score1600,
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
        return if (showScoreTimer <= 0) {
            super.getNextBitmap(gameBoard)
        } else {
            showScoreTimer -= 0.1
            when(showScore) {
                200 -> score200
                400 -> score400
                800 -> score800
                1600 -> score1600
                else -> score200
            }.slice()
        }
    }

    fun showScore(score: Int) {
        showScoreTimer = 3.0
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