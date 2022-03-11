import com.soywiz.korge.Korge
import com.soywiz.korim.color.Colors
import model.GameBoard
import model.offset

suspend fun main() = Korge(width = 224, height = 248 + offset, bgcolor = Colors["#000000"]) {
    val gameBoard = GameBoard.create(this)
}