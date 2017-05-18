package logic.ai.evaluation

import logic.ai.evaluation.field_weights.FieldWeightProvider
import logic.board.FieldState
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 17/05/2017.
 */
class OwnershipEvaluator(fieldValueWeights: FieldWeightProvider) : Evaluator {
    val fieldWeights = fieldValueWeights.getFieldWeights()

    private val POSESSION_FACTOR: Float = 1.0f

    override fun evaluate(board: GameBoard, ownedFieldsType: FieldState): Int {
        val posessedFieldsIndicesArray = board.boardStateArray.filter { it.fieldState === ownedFieldsType }
        return (posessedFieldsIndicesArray.size * POSESSION_FACTOR + posessedFieldsIndicesArray.sumBy { fieldWeights[it.index] }).toInt()
    }
}