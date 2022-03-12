import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import model.Direction

class Animation(
    private val images: List<Bitmap>,
    private val cyclesPerChange: Int = 6
) {

    private var state = 0

    companion object {
        suspend fun createDirectoryAnimationMap(
            entityName: String,
            animationCount: Int = 2
        ): Map<Direction, Animation> {
            return buildMap {
                Direction.values().forEach { directory ->
                    this[directory] = Animation((0 until animationCount).map { index ->
                        resourcesVfs["$entityName/${directory.name.lowercase()}/$index.png"].readBitmap()
                    })
                }
            }
        }
    }

    fun next(): Bitmap {
        return images[state++ / cyclesPerChange].apply {
            if (state == images.size * cyclesPerChange) state = 0
        }
    }

    fun reset() {
        state = 0
    }

}