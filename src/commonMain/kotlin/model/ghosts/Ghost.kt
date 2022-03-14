package model.ghosts

import Animation
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.seconds
import com.soywiz.korge.view.SolidRect
import com.soywiz.korge.view.Stage
import com.soywiz.korge.view.addUpdater
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.bitmap.BitmapSlice
import com.soywiz.korim.bitmap.slice
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import model.Direction
import model.Entity
import model.GameBoard
import model.offset
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

abstract class Ghost(animations: Map<Direction, Animation>, game: Stage) : Entity(animations, game) {

    private var decisionCooldown = 0

    var isFrightened = false
    private var frightenedTimer = 0.0.seconds
    protected var isDead = false
    private val frightenedBlinkingAnimation = frightenedAnimations2.clone()

    companion object {
        private val FRIGHTENED_DURATION = 10.0.seconds
        private val FRIGHTENED_BLINKING_DURATION = 2.0.seconds

        var isScattering = true
        private var scatterTimer = 0.0.seconds
        private val scatterChanges = mutableListOf(7, 27, 34, 54, 59, 79, 84)
        var isOneFrightened = false

        private lateinit var frightenedAnimations: Animation
        private lateinit var frightenedAnimations2: Animation
        private lateinit var deadAnimations: Map<Direction, Animation>

        fun addListener(game: Stage) {
            game.addUpdater(fun Stage.(dt: TimeSpan) {
                scatterTimer += dt

                if (scatterTimer.seconds.toInt() in scatterChanges) {
                    scatterChanges.remove(scatterTimer.seconds.toInt())
                    isScattering = !isScattering
                }
            })
        }

        suspend fun loadAnimations() {
            frightenedAnimations = Animation(
                listOf(
                    resourcesVfs["ghosts/afraid/0/0.png"].readBitmap(),
                    resourcesVfs["ghosts/afraid/0/1.png"].readBitmap()
                )
            )

            frightenedAnimations2 = Animation(
                listOf(
                    resourcesVfs["ghosts/afraid/0/0.png"].readBitmap(),
                    resourcesVfs["ghosts/afraid/0/1.png"].readBitmap(),
                    resourcesVfs["ghosts/afraid/0/0.png"].readBitmap(),
                    resourcesVfs["ghosts/afraid/0/1.png"].readBitmap(),
                    resourcesVfs["ghosts/afraid/1/0.png"].readBitmap(),
                    resourcesVfs["ghosts/afraid/1/1.png"].readBitmap(),
                    resourcesVfs["ghosts/afraid/1/0.png"].readBitmap(),
                    resourcesVfs["ghosts/afraid/1/1.png"].readBitmap(),
                )
            )

            deadAnimations = Animation.createDirectoryAnimationMap("ghosts/dead", 1)
        }
    }

    fun frighten() {
        isOneFrightened = true
        isFrightened = true
        frightenedTimer = FRIGHTENED_DURATION
    }

    fun kill() {
        isDead = true
    }

    override fun getSpeed(): Double {
        return if (isDead) 1.0
        else if (inTunnel()) 0.4
        else if (isFrightened) 0.5
        else 0.75
    }

    private fun inTunnel(): Boolean {
        return image.y - offset in 108.0..120.0 && (image.x <= 28.0 || 180.0 <= image.x)
    }

    private val targetRect = SolidRect(5.0, 5.0, RGBA(255, 0, 0))

    override fun addListener(gameBoard: GameBoard) {
        super.addListener(gameBoard)

        game.addChild(targetRect)

        game.addUpdater(fun Stage.(dt: TimeSpan) {
            val target = getTarget(gameBoard)
            targetRect.x = target.first.toDouble()
            targetRect.y = target.second.toDouble()

            nextDirection = calculateNextDirection(gameBoard)

            if (frightenedTimer > 0.0.seconds)
                frightenedTimer -= dt
            else {
                isFrightened = false
                if (!gameBoard.ghosts.any { it.isFrightened })
                    isOneFrightened = false
            }
        })
    }

    override fun getNextBitmap(gameBoard: GameBoard): BitmapSlice<Bitmap> {
        return if (isDead) {
            deadAnimations[direction]!!.next().slice()
        } else if (isFrightened) {
            if (frightenedTimer <= FRIGHTENED_BLINKING_DURATION)
                frightenedBlinkingAnimation.next().slice()
            else
                frightenedAnimations.next().slice()
        } else
            super.getNextBitmap(gameBoard)
    }

    open fun getTarget(gameBoard: GameBoard): Pair<Int, Int> {
        return Pair(4 * 26, 4 * 21 + offset)
    }

    private fun calculateNextDirection(gameBoard: GameBoard): Direction {
        if (isInBox(gameBoard)) {
            return if (image.y >= 28 * 4 + offset) Direction.UP
            else if (image.y <= 26 * 4 + offset) Direction.DOWN
            else direction
        } else if (decisionCooldown-- <= 0) {
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
                val distance = if (isFrightened && !isDead)
                    Random.nextDouble(0.0, 100.0)
                else
                    sqrt((target.first - image.x).pow(2) + (target.second - image.y).pow(2))
                reShift(dir)

                if (distance < minDistance) {
                    minDistance = distance
                    minDirection = dir
                }
            }

            decisionCooldown = 4
            return minDirection
        } else return nextDirection
    }

    abstract fun initialPos()

    abstract fun isInBox(gameBoard: GameBoard): Boolean

}