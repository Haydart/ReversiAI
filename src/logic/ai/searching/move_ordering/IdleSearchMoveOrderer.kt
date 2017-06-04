package logic.ai.searching.move_ordering

import logic.board.FieldState
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 03/06/2017.
 */
class IdleSearchMoveOrderer : SearchMoveOrderer {
    override fun getOrderedPossibleMoves(board: GameBoard, possibleMoves: Set<Int>, ownedFieldsType: FieldState): List<Int> = possibleMoves.toList()
}