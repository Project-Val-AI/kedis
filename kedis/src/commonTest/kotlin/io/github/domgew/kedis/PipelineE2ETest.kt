package io.github.domgew.kedis

import io.github.domgew.kedis.commands.KedisServerCommands
import io.github.domgew.kedis.commands.KedisValueCommands
import io.github.domgew.kedis.results.value.SetResult
import io.github.domgew.kedis.utils.TestConfigUtil
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext

class PipelineE2ETest {

    @Test
    fun test() = runTest {
        withContext(Dispatchers.Default) {
            val testKey = "test"
            val testValue = "abc"
            val testValue2 = "def"

            getClient().use { client ->
                assertTrue(
                    client.execute(
                        command = KedisServerCommands.flushAll(),
                    ),
                )

                val pipeline = client.pipelined()

                assertEquals(
                    0,
                    client.execute(
                        command = KedisValueCommands.exists(
                            testKey,
                        ),
                    ),
                )

                val firstExists = pipeline.enqueue(
                    command = KedisValueCommands.exists(
                        testKey,
                    ),
                )
                val firstGet = pipeline.enqueue(
                    command = KedisValueCommands.get(
                        key = testKey,
                    ),
                )
                val firstSet = pipeline.enqueue(
                    command = KedisValueCommands.set(
                        key = testKey,
                        value = testValue,
                    ),
                )
                val secondGet = pipeline.enqueue(
                    command = KedisValueCommands.get(
                        key = testKey,
                    ),
                )
                pipeline.enqueueSend()
                val secondSet = pipeline.enqueue(
                    command = KedisValueCommands.set(
                        key = testKey,
                        value = testValue2,
                    ),
                )
                val thirdGet = pipeline.enqueue(
                    command = KedisValueCommands.get(
                        key = testKey,
                    ),
                )
                val secondExists = pipeline.enqueue(
                    command = KedisValueCommands.exists(
                        testKey,
                    ),
                )

                assertTrue(firstExists.isActive)
                assertFalse(firstExists.isCancelled)
                assertFalse(firstExists.isCompleted)

                pipeline.execute()

                assertTrue(firstExists.isCompleted)
                assertTrue(firstGet.isCompleted)
                assertTrue(firstSet.isCompleted)
                assertTrue(secondGet.isCompleted)
                assertTrue(secondSet.isCompleted)
                assertTrue(thirdGet.isCompleted)
                assertTrue(secondExists.isCompleted)

                assertEquals(0, firstExists.await())
                assertEquals(null, firstGet.await())
                assertEquals(SetResult.Ok, firstSet.await())
                assertEquals(testValue, secondGet.await())
                assertEquals(SetResult.Ok, secondSet.await())
                assertEquals(testValue2, thirdGet.await())
                assertEquals(1, secondExists.await())

                assertFailsWith<IllegalStateException> {
                    pipeline.enqueue(
                        command = KedisValueCommands.exists(
                            testKey,
                        ),
                    )
                }
                assertFailsWith<IllegalStateException> {
                    pipeline.execute()
                }
            }
        }
    }

    private fun getClient(): KedisClient =
        KedisClient(
            configuration = KedisConfiguration(
                endpoint = KedisConfiguration.Endpoint.HostPort(
                    host = "127.0.0.1",
                    port = TestConfigUtil.getPort(),
                ),
                authentication = KedisConfiguration.Authentication.NoAutoAuth,
                connectionTimeout = 2.seconds,
            ),
        )
}
