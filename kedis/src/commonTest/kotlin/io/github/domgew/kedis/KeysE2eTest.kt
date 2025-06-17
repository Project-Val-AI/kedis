package io.github.domgew.kedis

import io.github.domgew.kedis.arguments.server.SyncOption
import io.github.domgew.kedis.commands.KedisKeysCommands
import io.github.domgew.kedis.commands.KedisServerCommands
import io.github.domgew.kedis.commands.KedisValueCommands
import io.github.domgew.kedis.utils.TestConfigUtil
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext

class KeysE2eTest {

    @Test
    fun test() = runTest {
        withContext(Dispatchers.Default) {
            val key1 = "key1"
            val key2 = "key2"
            val value1 = "v1"
            val value2 = "v2"

            KedisClient.newClient(
                KedisConfiguration(
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
                            command = KedisServerCommands.flushAll(
                                sync = SyncOption.SYNC,
                            ),
                        ),
                    )

                    client.execute(
                        command = KedisValueCommands.set(
                            key = key1,
                            value = value1,
                        ),
                    )
                    client.execute(
                        command = KedisValueCommands.set(
                            key = key2,
                            value = value2,
                        ),
                    )

                    assertEquals(
                        listOf(
                            key1,
                            key2,
                        ),
                        client.execute(
                            command = KedisKeysCommands.keys(
                                pattern = "key*",
                            ),
                        ),
                    )
                }
        }
    }
}
