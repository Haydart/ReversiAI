package logic

import java.awt.Dimension
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import com.sun.tools.corba.se.idl.Util.getAbsolutePath
import java.io.File



/**
 * Created by r.makowiecki on 13/05/2017.
 */
class FieldTypeImageProvider {
    fun getImageForFieldType(fieldType: FieldState, preferredImageSize: Dimension): ImageIcon? {
        var resourcePath = ""
        var bufferedImage: BufferedImage? = null
        when (fieldType) {
            FieldState.BLACK -> resourcePath = "res/black.png"
            FieldState.WHITE -> resourcePath = "res/white.png"
            FieldState.POSSIBLE -> resourcePath = "res/possible.png"
            else -> { }
        }
        try {
            val file = File(resourcePath)
            if (!file.exists()) {
                System.err.println("my file is not there, I was looking at " + file.absolutePath)
            }
            bufferedImage = ImageIO.read(file)
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            val image = ImageIcon(bufferedImage).image
            val scaledImage = image.getScaledInstance(preferredImageSize.width, preferredImageSize.height, java.awt.Image.SCALE_SMOOTH)
            return ImageIcon(scaledImage)
        }
    }
}