package top.fifthlight.pathflow.data

import top.fifthlight.pathflow.feature.FlowFeature

/**
 * Represents a data type in the path flow system.
 *
 * FlowData is the core abstraction of the PathFlow library, used to describe data types
 * that can participate in feature-based transformations. Any data type that needs to participate
 * in path resolution and data transformation must implement this interface.
 *
 * FlowData holds a set of features that describe the current state and available transformation
 * capabilities of the data. By analyzing the features of the input data and the target features,
 * the FlowResolver can calculate the required transformation path.
 *
 * @param T The data type itself, used for type-safe recursive constraints. This ensures that
 *           interface methods return the same concrete type as the input
 */
interface FlowData<T: FlowData<T>> {
    /**
     * The set of all features currently held by the data.
     *
     * Features are a key concept in FlowData, used to describe the state and capabilities
     * of the data. For example, image data might have a "has-color" feature, and after
     * transformation might have a "grayscale" feature.
     *
     * The feature set should be immutable, and any data transformation should return a new
     * FlowData instance.
     *
     * @return The current feature set of the data, may be an empty set
     */
    val features: Set<FlowFeature<T>>
}
