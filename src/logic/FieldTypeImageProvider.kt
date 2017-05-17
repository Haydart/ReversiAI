package logic

import logic.board.FieldState
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import java.io.File


/**
 * Created by r.makowiecki on 13/05/2017.
 */
class FieldTypeImageProvider(initialCellSize: Int) {
    val preferredImageSize = initialCellSize
    val whitePawnImage = retrieveImage("res/white.png")
    val blackPawnImage = retrieveImage("res/black.png")
    val possibleFieldImage = retrieveImage("res/possible.png")

    fun getImageForFieldType(fieldType: FieldState): ImageIcon? =
            when (fieldType) {
                FieldState.BLACK -> blackPawnImage
                FieldState.WHITE -> whitePawnImage
                FieldState.POSSIBLE -> possibleFieldImage
                else -> null
            }

    private fun retrieveImage(path: String): ImageIcon? =
            ImageIcon(ImageIcon(ImageIO.read(File(path))).image.getScaledInstance(preferredImageSize, preferredImageSize, java.awt.Image.SCALE_SMOOTH))
}