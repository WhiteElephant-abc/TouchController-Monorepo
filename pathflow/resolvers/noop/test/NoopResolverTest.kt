package top.fifthlight.pathflow.resolvers.noop.test

import org.junit.jupiter.api.assertNull
import top.fifthlight.pathflow.resolvers.noop.NoopResolver
import kotlin.test.Test

class NoopResolverTest {
    @Test
    fun testEmpty() {
        assertNull(
            NoopResolver.resolve(
                inputFeatures = emptySet(),
                targetFeatures = emptySet(),
                transformers = emptySet(),
                maxSteps = 0,
            )
        )
    }
}
