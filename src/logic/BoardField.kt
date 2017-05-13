package logic

import java.awt.Point

/**
 * Created by r.makowiecki on 13/05/2017.
 */
class BoardField() {
    var fieldState: FieldState = FieldState.BLACK
    val coordinates: Point

    init {
        objectsCount++
        coordinates = Point(objectsCount / 8, objectsCount % 8)
    }

    companion object {
        var objectsCount: Int = 0
    }
}