package logic.board

/**
 * Created by r.makowiecki on 13/05/2017.
 */
class BoardField {
    var fieldState: FieldState = FieldState.EMPTY
    var index: Int
    var coordinates: java.awt.Point

    init {
        coordinates = java.awt.Point(objectsCount % 8, objectsCount / 8)
        index = logic.board.BoardField.Companion.objectsCount
        logic.board.BoardField.Companion.objectsCount++
    }

    fun getCopy(): BoardField {
        val copy = BoardField()
        copy.index = this.index
        copy.coordinates = this.coordinates
        copy.fieldState = this.fieldState
        return copy
    }

    companion object {
        var objectsCount: Int = 0
    }
}