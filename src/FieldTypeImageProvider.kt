import java.awt.Dimension
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.ImageIcon

/**
 * Created by r.makowiecki on 13/05/2017.
 */
class FieldTypeImageProvider {
    fun getImageForFieldType(fieldType: FieldType, preferredImageSize: Dimension): ImageIcon? {
        var resourcePath = ""
        var bufferedImage: BufferedImage? = null
        when (fieldType) {
            FieldType.BLACK -> resourcePath = "res/black.png"
            FieldType.WHITE -> resourcePath = "res/white.png"
            FieldType.POSSIBLE -> resourcePath = "res/possible.png"
        }
        try {
            bufferedImage = ImageIO.read(javaClass.getResource(resourcePath))
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            val image = ImageIcon(bufferedImage).getImage()
            val scaledImage = image.getScaledInstance(preferredImageSize.width, preferredImageSize.height, java.awt.Image.SCALE_SMOOTH)
            return ImageIcon(scaledImage)
        }
    }
}