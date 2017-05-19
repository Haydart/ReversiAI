package logic.ai.evaluation

import logic.ai.evaluation.field_weights.FieldWeightProvider
import logic.board.FieldState
import logic.board.GameBoard

/**
 * Created by r.makowiecki on 17/05/2017.
 */
class FieldOwnershipEvaluator(fieldValueWeights: FieldWeightProvider) : Evaluator {
    val fieldWeights = fieldValueWeights.getFieldWeights()

    private val POSESSION_FACTOR: Float = 0f

    override fun evaluate(board: GameBoard, ownedFieldsType: FieldState): Int {
        val possessedFieldsIndicesArray = board.boardStateArray.filter { it.fieldState === ownedFieldsType }
        return (possessedFieldsIndicesArray.size * POSESSION_FACTOR + possessedFieldsIndicesArray.sumBy { fieldWeights[it.index] }).toInt()
    }
}