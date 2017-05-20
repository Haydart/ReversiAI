package logic.ai.evaluation.field_weights

/**
 * Created by r.makowiecki on 20/05/2017.
 */
class NewFieldWeightProvider : FieldWeightProvider{
    override fun getFieldWeights() = arrayOf(
            20f, -3f, 11f, 8f, 8f, 11f, -3f, 20f,
            -3f, -7f, -4f, 1f, 1f, -4f, -7f, -3f,
            11f, -4f, 2f, 2f, 2f, 2f, -4f, 11f,
            8f, 1f, 2f, -3f, -3f, 2f, 1f, 8f,
            8f, 1f, 2f, -3f, -3f, 2f, 1f, 8f,
            11f, -4f, 2f, 2f, 2f, 2f, -4f, 11f,
            -3f, -7f, -4f, 1f, 1f, -4f, -7f, -3f,
            20f, -3f, 11f, 8f, 8f, 11f, -3f, 20f
    )
}