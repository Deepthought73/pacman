package model

import com.soywiz.klock.TimeSpan
import com.soywiz.klock.seconds
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.font.TtfFont
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import model.ghosts.*

const val offset = 50

class GameBoard private constructor(
    val pacman: Pacman,
    val ghosts: List<Ghost>,
    private val game: Stage,
    emptyGameBoard: Bitmap,
    dotDistributionBitmap: Bitmap,
    private val powerPellet: Bitmap,
    font: TtfFont
) {

    var dotObjects: MutableSet<SolidRect> = mutableSetOf()
    private var powerPellets: MutableSet<Image> = mutableSetOf()
    private var score = 0
    private var lives = 3

    var killStreet = 0
    var gameOver = false

    companion object {
        suspend fun create(game: Stage): GameBoard {
            Ghost.loadAnimations()
            Ghost.addListener(game)

            return GameBoard(
                Pacman.create(
                    game,
                    resourcesVfs["points/200.png"].readBitmap(),
                    resourcesVfs["points/400.png"].readBitmap(),
                    resourcesVfs["points/800.png"].readBitmap(),
                    resourcesVfs["points/1600.png"].readBitmap()
                ),
                listOf(
                    Blinky.create(game, offset),
                    Pinky.create(game, offset),
                    Inky.create(game, offset),
                    Clyde.create(game, offset)
                ),
                game,
                resourcesVfs["gameboard.png"].readBitmap(),
                resourcesVfs["dotDistribution.png"].readBitmap(),
                resourcesVfs["powerPellet.png"].readBitmap(),
                TtfFont(resourcesVfs["font.ttf"].readAll())
            )
        }
    }

    private val gameMap = Array(62) { row ->
        Array(56) { col ->
            var pixelCount = 0
            for (y in row * 4 until row * 4 + 4) {
                for (x in col * 4 until col * 4 + 4) {
                    if (emptyGameBoard.getRgba(x, y) != RGBA(0, 0, 0, 0)) {
                        pixelCount++
                    }
                }
            }
            pixelCount > 1
        }
    }

    private val dotMap = Array(29) { row ->
        Array(26) { col ->
            dotDistributionBitmap.getRgba(11 + 8 * col, 11 + 8 * row) == RGBA(255, 183, 174, 255) &&
                    dotDistributionBitmap.getRgba(10 + 8 * col, 10 + 8 * row) != RGBA(255, 183, 174, 255)
        }
    }

    private var scoreText = Text("00", textSize = 11.0, color = Colors.WHITE, font = font).xy(20, 20)

    private fun createPowerPellets() {
        powerPellets.add(game.image(powerPellet).xy(8, 24 + offset))
        powerPellets.add(game.image(powerPellet).xy(208, 24 + offset))
        powerPellets.add(game.image(powerPellet).xy(8, 184 + offset))
        powerPellets.add(game.image(powerPellet).xy(208, 184 + offset))
    }

    private fun updateScore() {
        scoreText.setText(score.toString())
    }

    private fun createDotObjects() {
        dotMap.forEachIndexed { y, row ->
            row.forEachIndexed { x, cell ->
                if (cell) {
                    val rect = SolidRect(2, 2, color = RGBA(255, 183, 174, 255))
                    rect.xy(11 + 8 * x, 11 + 8 * y + offset)
                    dotObjects.add(rect)
                    game.addChild(rect)
                }
            }
        }
    }

    fun hasCollision(x: Int, y: Int, width: Int, height: Int): Boolean {
        for (i in x until x + width) {
            for (j in y until y + height) {
                if (gameMap[j][i.mod(56)]) {
                    return true
                }
            }
        }
        return false
    }

    fun checkDotCollision() {
        for (dot in dotObjects) {
            if (dot.x > pacman.getX()
                && dot.x < pacman.getX() + 14
                && dot.y > pacman.getY()
                && dot.y < pacman.getY() + 14
            ) {
                game.removeChild(dot)
                dotObjects.remove(dot)
                score += 10
                pacman.isPause = true
                updateScore()
                break
            }
        }
    }

    fun checkPowerPalletCollision(): Boolean {
        for (pellet in powerPellets) {
            if (pellet.x > pacman.getX()
                && pellet.x < pacman.getX() + 14
                && pellet.y > pacman.getY()
                && pellet.y < pacman.getY() + 14
            ) {
                game.removeChild(pellet)
                powerPellets.remove(pellet)
                score += 50
                pacman.isPause = true
                updateScore()

                for (g in ghosts)
                    g.frighten(this)
                return true
            }
        }
        return false
    }

    fun checkGhostCollision() {
        for (ghost in ghosts) {
            if (ghost.getX() < pacman.getX() + 4
                && ghost.getX() + 4 > pacman.getX()
                && ghost.getY() < pacman.getY() + 4
                && ghost.getY() + 4 > pacman.getY()
            ) {
                if (Ghost.isOneFrightened && !ghost.isDead) {
                    ghost.kill()
                    val additionalPoints = 200*(1 shl killStreet++)
                    score += additionalPoints
                    pacman.showScore(additionalPoints)
                    Entity.killCooldownTimer = Entity.KILL_COOLDOWN_DURATION
                } else if (!ghost.isDead) {
                    lives--
                    if (lives > 0) {
                        pacman.initialPos()
                        ghosts.forEach { g -> g.initialPos() }
                    } else {
                        Entity.killCooldownTimer = 10.0.seconds
                        gameOver = true
                    }
                }
            }
        }
    }

    init {
        createDotObjects()
        createPowerPellets()

        game.image(emptyGameBoard).xy(0, offset)

        pacman.addListener(this)
        for (g in ghosts)
            g.addListener(this)

        game.addChild(scoreText)

        game.addUpdater(fun Stage.(_: TimeSpan) {
            if (gameOver) {
                Entity.killCooldownTimer = 10.0.seconds
            } else {
                checkDotCollision()
                checkPowerPalletCollision()
                checkGhostCollision()
            }
        })
    }

}