package logic.board

import logic.GameResult

/**
 * Created by r.makowiecki on 15/05/2017.
 */
class GameState {
    var blackFieldsCount: Int = 2
    var whiteFieldsCount: Int = 2
    var blackMobility: Int = 4
    var whiteMobility: Int = 0

    fun getCopy(): GameState {
        val copy = GameState()
        copy.blackFieldsCount = this.blackFieldsCount
        copy.whiteFieldsCount = this.whiteFieldsCount
        copy.blackMobility = this.blackMobility
        copy.whiteMobility = this.whiteMobility
        return copy
    }

    fun isEndOfGame() = blackFieldsCount + whiteFieldsCount == 64
            || blackFieldsCount == 0 || whiteFieldsCount == 0
            || (blackMobility == 0 && whiteMobility == 0)

    fun getGameResult(): GameResult? {
        if (isEndOfGame() && blackFieldsCount == whiteFieldsCount) return GameResult.DRAW
        else if (isEndOfGame() && blackFieldsCount > whiteFieldsCount) return GameResult.BLACK_WINS
        else if (isEndOfGame() && blackFieldsCount < whiteFieldsCount) return  GameResult.WHITE_WINS
        else return null
    }

    fun recalculate(board: GameBoard) {
        blackFieldsCount = board.boardStateArray.filter { it.fieldState === FieldState.BLACK }.count()
        whiteFieldsCount = board.boardStateArray.filter { it.fieldState === FieldState.WHITE }.count()
        blackMobility = board.legalMoveManager.findLegalMoves(board, FieldState.BLACK).size
        whiteMobility = board.legalMoveManager.findLegalMoves(board, FieldState.WHITE).size
    }
}