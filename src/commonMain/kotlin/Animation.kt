import com.soywiz.korim.bitmap.Bitmap
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import model.Directory

class Animation(
    private val images: List<Bitmap>,
    private val cyclesPerChange: Int = 7
) {

    private var state = 0;

    companion object {
        suspend fun createDirectoryAnimationMap(entityName: String): Map<Directory, Animation> {
            return buildMap {
                Directory.values().forEach { directory ->
                    this[directory] = Animation(
                        listOf(
                            resourcesVfs["$entityName/${directory.name.lowercase()}/0.png"].readBitmap(),
                            resourcesVfs["$entityName/${directory.name.lowercase()}/1.png"].readBitmap()
                        )
                    )
                }
            }
        }
    }

    fun next(): Bitmap {
        return images[state++ / cyclesPerChange].apply {
            if (state == images.size * cyclesPerChange) state = 0
        }
    }

}