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

    fun isEndOfGame() = blackFieldsCount + whiteFieldsCount == 64
            || blackFieldsCount == 0 || whiteFieldsCount == 0
            || (blackMobility == 0 && whiteMobility == 0)

    fun getGameResult(): GameResult? {
        if (isEndOfGame() && blackFieldsCount == whiteFieldsCount) return GameResult.DRAW
        else if (isEndOfGame() && blackFieldsCount > whiteFieldsCount) return GameResult.BLACK_WINS
        else if (isEndOfGame() && blackFieldsCount < whiteFieldsCount) return  GameResult.WHITE_WINS
        else return null
    }
}