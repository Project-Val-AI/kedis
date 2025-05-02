package io.github.domgew.kedis.samples

import io.github.domgew.kedis.KedisClient
import io.github.domgew.kedis.KedisConfiguration
import io.github.domgew.kedis.commands.KedisServerCommands
import io.github.domgew.kedis.utils.TestConfigUtil
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext

class SamplesTest {

    @Test
    fun `PipelineSamples simple`() =
        testSample(PipelineSamples::simple)

    private fun testSample(
        sample: suspend (KedisClient) -> Unit,
    ) = runTest {
        withContext(Dispatchers.Default) {
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
                .use { client ->
                    assertTrue(
                        client.execute(
                            command = KedisServerCommands.flushAll(),
                        ),
                    )

                    sample(client)
                }
        }
    }
}
