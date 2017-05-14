package logic

import java.awt.Point

/**
 * Created by r.makowiecki on 13/05/2017.
 */
class BoardField {
    var fieldState: FieldState = FieldState.EMPTY
    val index: Int
    val coordinates: Point

    init {
        coordinates = Point(objectsCount % 8, objectsCount / 8)
        index = objectsCount
        objectsCount++
        println("$coordinates, $objectsCount")
    }

    companion object {
        var objectsCount: Int = 0
    }
}