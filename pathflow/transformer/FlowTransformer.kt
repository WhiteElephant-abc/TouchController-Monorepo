package top.fifthlight.pathflow.transformer

import top.fifthlight.pathflow.data.FlowData
import top.fifthlight.pathflow.feature.FlowFeature

/**
 * Represents a data transformer, defining how to convert one FlowData to another.
 *
 * FlowTransformer is a core component of the PathFlow transformation system, defining
 * the rules and constraints for data transformation. Each transformer explicitly declares
 * its input features, output features, and required features.
 *
 * ### Feature Declaration
 * - `removeFeatures`: Features that will be removed from the data after transformation
 * - `addFeatures`: Features that will be added to the data after transformation
 * - `requireFeatures`: Features that the data must have before transformation (preconditions)
 *
 * ### Transformation Flow
 * 1. Check if the input data has all `requireFeatures` (transformation fails if missing)
 * 2. Execute the `process` method to perform the actual transformation
 * 3. The returned new data should contain: `(input.features - removeFeatures) + addFeatures`
 *
 * @param T The FlowData type being transformed
 *
 * @see FlowData Input and output types of transformation
 * @see FlowFeature Features used by transformation
 */
interface FlowTransformer<T : FlowData<T>> {
    /**
     * Features that will be removed from the data after this transformation.
     *
     * These features no longer apply to the result data after the transformation is complete.
     * For example, after converting a color image to grayscale, the "has-color" feature is removed.
     *
     * @return The set of features to remove after transformation, may be an empty set
     */
    val removeFeatures: Set<FlowFeature<T>>

    /**
     * Features that will be added to the data after this transformation.
     *
     * These features describe the new state or capabilities of the transformation result.
     * For example, after converting an image to grayscale, add the "grayscale" feature.
     *
     * @return The set of features to add after transformation, may be an empty set
     */
    val addFeatures: Set<FlowFeature<T>>

    /**
     * Features that the data must have before this transformation is executed.
     *
     * These features are preconditions for the transformation, and if the input data lacks any required
     * features, the transformer should refuse the transformation or throw an exception.
     *
     * @return The set of features required before transformation, may be an empty set
     */
    val requireFeatures: Set<FlowFeature<T>>

    /**
     * Performs the data transformation.
     *
     * This method implements the actual transformation logic, receiving input data and returning
     * the transformed new data. The transformation should be a pure function (no side effects),
     * and the input data should not be modified.
     *
     * The transformer should ensure that the returned new data matches the feature declaration:
     * `result.features == (input.features - removeFeatures) + addFeatures`
     *
     * @param input The input data to transform
     * @return The new transformed data
     */
    fun process(input: T): T
}

/**
 * Extension function: Performs a chain of data transformations.
 *
 * This convenience function applies a list of transformers sequentially to the input data,
 * returning the final transformed result.
 *
 * @param T The FlowData type being transformed
 * @param input The input data to transform
 * @return The final transformed data
 * @see FlowTransformer Individual transformer interface
 */
fun <T : FlowData<T>> List<FlowTransformer<T>>.process(input: T): T =
    fold(input) { acc, transformer -> transformer.process(acc) }
