package logic.board

/**
 * Created by r.makowiecki on 13/05/2017.
 */
class BoardField {
    var fieldState: FieldState = FieldState.EMPTY
    val index: Int
    val coordinates: java.awt.Point

    init {
        coordinates = java.awt.Point(objectsCount % 8, objectsCount / 8)
        index = logic.board.BoardField.Companion.objectsCount
        logic.board.BoardField.Companion.objectsCount++
//        //println("$coordinates, $objectsCount")
    }

    companion object {
        var objectsCount: Int = 0
    }
}