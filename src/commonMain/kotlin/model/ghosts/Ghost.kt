package model.ghosts

import Animation
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.seconds
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.addUpdater
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.bitmap.BitmapSlice
import com.soywiz.korim.bitmap.slice
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import model.Direction
import model.Entity
import model.GameBoard
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

abstract class Ghost(animations: Map<Direction, Animation>, game: Stage) : Entity(animations, game) {

    private var decisionCooldown = 0

    companion object {
        private val FRIGHTENED_DURATION = 10.0.seconds

        var isFrightened = false
        var isScattering = true

        private var scatterTimer = TimeSpan(0.0)
        private val scatterChanges = mutableListOf(7, 27, 34, 54, 59, 79, 84)
        private var frightenedTimer = 0.0.seconds

        private lateinit var frightenedAnimations: Animation

        fun frighten() {
            isFrightened = true
            frightenedTimer = FRIGHTENED_DURATION
        }

        fun addListener(game: Stage) {
            game.addUpdater(fun Stage.(dt: TimeSpan) {
                scatterTimer += dt

                if (scatterTimer.seconds.toInt() in scatterChanges) {
                    scatterChanges.remove(scatterTimer.seconds.toInt())
                    isScattering = !isScattering
                }

                if (frightenedTimer > 0.0.seconds)
                    frightenedTimer -= dt
                else
                    isFrightened = false
            })
        }

        suspend fun loadAnimations() {
            frightenedAnimations = Animation(
                listOf(
                    resourcesVfs["ghosts/afraid/0/0.png"].readBitmap(),
                    resourcesVfs["ghosts/afraid/0/1.png"].readBitmap()
                )
            )
        }
    }

    override fun getSpeed(): Double {
        return if (inTunnel()) 0.4
        else if (isFrightened) 0.5
        else 0.75
    }

    private fun inTunnel(): Boolean {
        return false
    }

    override fun addListener(gameBoard: GameBoard) {
        super.addListener(gameBoard)

        game.addUpdater(fun Stage.(dt: TimeSpan) {
            nextDirection = calculateNextDirection(gameBoard)
        })
    }

    override fun getNextBitmap(): BitmapSlice<Bitmap> {
        return if (isFrightened) {
            frightenedAnimations.next().slice()
        } else
            super.getNextBitmap()
    }

    abstract fun getTarget(gameBoard: GameBoard): Pair<Int, Int>

    private fun calculateNextDirection(gameBoard: GameBoard): Direction {
        if (decisionCooldown-- <= 0) {
            val target = getTarget(gameBoard)
            var minDirection = direction
            var minDistance = Double.POSITIVE_INFINITY

            for (dir in Direction.values()
                .filter { it != direction.invert() }
                .filter {
                    var shiftCount = 0
                    var hadCollision = false
                    while (shiftCount < 10 && !hadCollision) {
                        shift(it)
                        shiftCount++
                        hadCollision = hasCollision(gameBoard)
                    }
                    for (i in 0 until shiftCount)
                        reShift(it)
                    !hadCollision
                }
            ) {
                shift(dir)
                val distance = if (isFrightened)
                    Random.nextDouble(0.0, 100.0)
                else
                    sqrt((target.first - image.x).pow(2) + (target.second - image.y).pow(2))
                reShift(dir)

                if (distance < minDistance) {
                    minDistance = distance
                    minDirection = dir
                }
            }

            decisionCooldown = 3
            return minDirection
        } else return nextDirection
    }

}