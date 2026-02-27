package top.fifthlight.pathflow.feature

/**
 * Represents a feature or marker for data.
 *
 * FlowFeature is a marker interface used to describe the state, capability, or constraint of FlowData.
 * Features are type-safe, and each feature is associated with a specific FlowData type.
 *
 * Features have two main purposes:
 * 1. **State Description**: Describe properties that data currently has (e.g., "has-transparency")
 * 2. **Transformation Constraint**: Describe preconditions for transformers (e.g., "requires-color-data")
 *
 * The FlowFeature interface itself contains no methods, and concrete implementation classes define
 * the meaning of features. It is recommended to use singleton objects (object) or sealed classes
 * to define features.
 *
 * @param T The associated FlowData type, ensuring features are only used for correct data types
 */
interface FlowFeature<T: Any>
