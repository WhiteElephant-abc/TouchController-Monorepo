package top.fifthlight.pathflow.resolver

import top.fifthlight.pathflow.data.FlowData
import top.fifthlight.pathflow.feature.FlowFeature
import top.fifthlight.pathflow.transformer.FlowTransformer
import top.fifthlight.pathflow.transformer.process

/**
 * Path resolver for finding transformation paths from input features to target features.
 *
 * FlowResolver is the core interface of the PathFlow path resolution system, responsible for
 * finding a transformation path from input data features to target features within a given
 * set of transformers.
 *
 * ### Path Resolution Problem
 * Given:
 * - Input data feature set (inputFeatures)
 * - Desired target feature set (targetFeatures)
 * - Available transformer set (transformers)
 * - Maximum transformation step limit (maxSteps)
 *
 * Solve:
 * - Find a sequence of transformers that, when applied sequentially, gives the data all target features
 * - Path length does not exceed maxSteps
 * - Preconditions (requireFeatures) of each transformer must be satisfied
 *
 * ### Resolution Result
 * - If a valid path is found, return a transformer list (in application order)
 * - If no path is found, return null
 * - If multiple paths exist, the implementation can choose the shortest path or the first valid path
 *
 * @see FlowData Input and output types of transformation
 * @see FlowFeature Features used by transformation
 * @see FlowTransformer Transformation steps in path
 * @see process Extension function for direct data processing
 */
interface FlowResolver {
    /**
     * Resolves a transformation path from input features to target features.
     *
     * This method searches for a valid transformation path within a given set of transformers.
     * A path is a sequence of transformers that, when applied sequentially, gives the data
     * all target features.
     *
     * @param T The concrete type of FlowData
     * @param inputFeatures The feature set currently held by the input data
     * @param targetFeatures The desired feature set after transformation
     * @param transformers The available transformer set
     * @param maxSteps The maximum allowed transformation step limit, must be positive
     * @return List of transformers arranged in application order, or null if no path is found
     * @throws IllegalArgumentException If maxSteps <= 0
     *
     * @sample top.fifthlight.pathflow.resolver.samples.flowResolverSample
     * @see process Extension function for chaining transformations
     */
    fun <T : FlowData<T>> resolve(
        inputFeatures: Set<FlowFeature<T>>,
        targetFeatures: Set<FlowFeature<T>>,
        transformers: Set<FlowTransformer<T>>,
        maxSteps: Int,
    ): List<FlowTransformer<T>>?
}

/**
 * Extension function: Directly use FlowResolver to process data.
 *
 * This convenience function simplifies the data transformation flow, automatically extracting
 * features from the input data, resolving a path, and applying the transformer chain to return
 * the final result.
 *
 * @param T The concrete type of FlowData
 * @param input The input data to transform
 * @param targetFeatures The desired target feature set
 * @param transformers The available transformer set
 * @param maxSteps The maximum allowed transformation steps, must be positive
 * @return The transformed data, or null if no path is found
 * @throws IllegalArgumentException If maxSteps <= 0
 * @see FlowResolver.resolve Underlying path resolution method
 */
fun <T : FlowData<T>> FlowResolver.process(
    input: T,
    targetFeatures: Set<FlowFeature<T>>,
    transformers: Set<FlowTransformer<T>>,
    maxSteps: Int,
): T? = resolve(
    inputFeatures = input.features,
    targetFeatures = targetFeatures,
    transformers = transformers,
    maxSteps = maxSteps,
)?.process(input)
