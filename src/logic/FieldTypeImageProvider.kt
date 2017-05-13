package logic

import java.awt.Dimension
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.ImageIcon

/**
 * Created by r.makowiecki on 13/05/2017.
 */
class FieldTypeImageProvider {
    fun getImageForFieldType(fieldType: FieldState, preferredImageSize: Dimension): ImageIcon? {
        var resourcePath = ""
        var bufferedImage: BufferedImage? = null
        when (fieldType) {
            FieldState.BLACK -> resourcePath = "~/src/res/black.png"
            FieldState.WHITE -> resourcePath = "~/src/res/white.png"
            FieldState.POSSIBLE -> resourcePath = "~/src/res/possible.png"
            else -> { }
        }
        try {
            bufferedImage = ImageIO.read(javaClass.getResource(resourcePath))
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            val image = ImageIcon(bufferedImage).image
            val scaledImage = image.getScaledInstance(preferredImageSize.width, preferredImageSize.height, java.awt.Image.SCALE_SMOOTH)
            return ImageIcon(scaledImage)
        }
    }
}