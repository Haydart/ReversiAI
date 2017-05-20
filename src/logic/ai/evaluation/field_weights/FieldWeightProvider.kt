package logic.ai.evaluation.field_weights

/**
 * Created by r.makowiecki on 17/05/2017.
 */
interface FieldWeightProvider {
    fun getFieldWeights() : Array<Float>
}