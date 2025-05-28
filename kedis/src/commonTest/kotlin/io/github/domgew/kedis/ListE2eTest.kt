package io.github.domgew.kedis

import io.github.domgew.kedis.arguments.list.ListEnd
import io.github.domgew.kedis.arguments.list.ListRemoveCount
import io.github.domgew.kedis.arguments.server.SyncOption
import io.github.domgew.kedis.commands.KedisListCommands
import io.github.domgew.kedis.commands.KedisServerCommands
import io.github.domgew.kedis.utils.TestConfigUtil
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext

class ListE2eTest {

    @Test
    fun test() = runTest {
        withContext(Dispatchers.Default) {
            val key1 = "testKey-1"
            val key2 = "testKey-2"
            val value1 = "val1"
            val value2 = "val2"
            val value3 = "val3"
            val value4 = "val4"
            val value5 = "val5"

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

                    assertNull(
                        client.execute(
                            command = KedisListCommands.lpop(
                                key = key1,
                            ),
                        ),
                    )

                    assertEquals(
                        1,
                        client.execute(
                            command = KedisListCommands.lpush(
                                key = key1,
                                values = listOf(
                                    value1,
                                ),
                            ),
                        ),
                    )
                    assertEquals(
                        2,
                        client.execute(
                            command = KedisListCommands.lpush(
                                key = key1,
                                values = listOf(
                                    value2,
                                ),
                            ),
                        ),
                    )
                    assertEquals(
                        3,
                        client.execute(
                            command = KedisListCommands.lpush(
                                key = key1,
                                values = listOf(
                                    value3,
                                ),
                            ),
                        ),
                    )
                    assertEquals(
                        4,
                        client.execute(
                            command = KedisListCommands.lpush(
                                key = key1,
                                values = listOf(
                                    value4,
                                ),
                            ),
                        ),
                    )

                    assertEquals(
                        4,
                        client.execute(
                            command = KedisListCommands.llen(
                                key = key1,
                            ),
                        ),
                    )

                    assertEquals(
                        listOf(
                            value4,
                            value3,
                            value2,
                            value1,
                        ),
                        client.execute(
                            command = KedisListCommands.lrange(
                                key = key1,
                                start = 0,
                                end = -1,
                            ),
                        ),
                    )
                    assertEquals(
                        listOf(
                            value2,
                            value1,
                        ),
                        client.execute(
                            command = KedisListCommands.lrange(
                                key = key1,
                                start = -2,
                                end = -1,
                            ),
                        ),
                    )
                    assertEquals(
                        listOf(
                            value4,
                            value3,
                        ),
                        client.execute(
                            command = KedisListCommands.lrange(
                                key = key1,
                                start = 0,
                                end = 1,
                            ),
                        ),
                    )

                    assertEquals(
                        listOf(
                            value4,
                        ),
                        client.execute(
                            command = KedisListCommands.lpop(
                                key = key1,
                                n = 1,
                            ),
                        ),
                    )
                    assertEquals(
                        listOf(
                            value3,
                            value2,
                        ),
                        client.execute(
                            command = KedisListCommands.lpop(
                                key = key1,
                                n = 2,
                            ),
                        ),
                    )
                    assertEquals(
                        listOf(
                            value1,
                        ),
                        client.execute(
                            command = KedisListCommands.lpop(
                                key = key1,
                                n = 2,
                            ),
                        ),
                    )

                    assertNull(
                        client.execute(
                            command = KedisListCommands.lpop(
                                key = key1,
                                n = 2,
                            ),
                        ),
                    )
                    assertEquals(
                        0,
                        client.execute(
                            command = KedisListCommands.llen(
                                key = key1,
                            ),
                        ),
                    )

                    assertEquals(
                        1,
                        client.execute(
                            command = KedisListCommands.lpush(
                                key = key1,
                                values = listOf(
                                    value1,
                                ),
                            ),
                        ),
                    )
                    assertEquals(
                        2,
                        client.execute(
                            command = KedisListCommands.lpush(
                                key = key1,
                                values = listOf(
                                    value2,
                                ),
                            ),
                        ),
                    )
                    assertEquals(
                        3,
                        client.execute(
                            command = KedisListCommands.lpush(
                                key = key1,
                                values = listOf(
                                    value3,
                                ),
                            ),
                        ),
                    )
                    assertEquals(
                        4,
                        client.execute(
                            command = KedisListCommands.lpush(
                                key = key1,
                                values = listOf(
                                    value4,
                                ),
                            ),
                        ),
                    )

                    assertEquals(
                        listOf(
                            value1,
                            value2,
                        ),
                        client.execute(
                            command = KedisListCommands.rpop(
                                key = key1,
                                n = 2,
                            ),
                        ),
                    )

                    assertEquals(
                        listOf(
                            value3,
                            value4,
                        ),
                        client.execute(
                            command = KedisListCommands.rpop(
                                key = key1,
                                n = 2,
                            ),
                        ),
                    )

                    assertNull(
                        client.execute(
                            command = KedisListCommands.lmove(
                                sourceKey = key1,
                                destinationKey = key2,
                                sourceEnd = ListEnd.TAIL,
                                destinationEnd = ListEnd.HEAD,
                            ),
                        ),
                    )

                    assertEquals(
                        1,
                        client.execute(
                            command = KedisListCommands.lpush(
                                key = key1,
                                values = listOf(
                                    value1,
                                ),
                            ),
                        ),
                    )
                    assertEquals(
                        2,
                        client.execute(
                            command = KedisListCommands.lpush(
                                key = key1,
                                values = listOf(
                                    value2,
                                ),
                            ),
                        ),
                    )
                    assertEquals(
                        3,
                        client.execute(
                            command = KedisListCommands.lpush(
                                key = key1,
                                values = listOf(
                                    value3,
                                ),
                            ),
                        ),
                    )
                    assertEquals(
                        4,
                        client.execute(
                            command = KedisListCommands.lpush(
                                key = key1,
                                values = listOf(
                                    value4,
                                ),
                            ),
                        ),
                    )
                    assertEquals(
                        1,
                        client.execute(
                            command = KedisListCommands.lpush(
                                key = key2,
                                values = listOf(
                                    value5,
                                ),
                            ),
                        ),
                    )

                    assertEquals(
                        value1,
                        client.execute(
                            command = KedisListCommands.lmove(
                                sourceKey = key1,
                                destinationKey = key2,
                                sourceEnd = ListEnd.TAIL,
                                destinationEnd = ListEnd.HEAD,
                            ),
                        ),
                    )

                    assertEquals(
                        3,
                        client.execute(
                            command = KedisListCommands.llen(
                                key = key1,
                            ),
                        ),
                    )
                    assertEquals(
                        2,
                        client.execute(
                            command = KedisListCommands.llen(
                                key = key2,
                            ),
                        ),
                    )

                    assertEquals(
                        listOf(
                            value1,
                            value5,
                        ),
                        client.execute(
                            command = KedisListCommands.lrange(
                                key = key2,
                                start = 0,
                                end = -1,
                            ),
                        ),
                    )

                    client.execute(
                        command = KedisListCommands.lpop(
                            key = key1,
                            n = client.execute(
                                command = KedisListCommands.llen(
                                    key = key1,
                                ),
                            )
                                .toInt(),
                        ),
                    )

                    assertEquals(
                        0,
                        client.execute(
                            command = KedisListCommands.llen(
                                key = key1,
                            ),
                        ),
                    )
                    assertEquals(
                        0,
                        client.execute(
                            command = KedisListCommands.lrem(
                                key = key1,
                                count = ListRemoveCount.FromHead(
                                    n = 2,
                                ),
                                value = value1,
                            ),
                        ),
                    )

                    assertEquals(
                        5,
                        client.execute(
                            command = KedisListCommands.lpush(
                                key = key1,
                                values = listOf(
                                    value1,
                                    value3,
                                    value1,
                                    value2,
                                    value1,
                                ),
                            ),
                        ),
                    )
                    assertEquals(
                        listOf(
                            value1,
                            value2,
                            value1,
                            value3,
                            value1,
                        ),
                        client.execute(
                            command = KedisListCommands.lrange(
                                key = key1,
                                start = 0,
                                end = -1,
                            ),
                        ),
                    )
                    assertEquals(
                        2,
                        client.execute(
                            command = KedisListCommands.lrem(
                                key = key1,
                                count = ListRemoveCount.FromHead(
                                    n = 2,
                                ),
                                value = value1,
                            ),
                        ),
                    )
                    assertEquals(
                        listOf(
                            value2,
                            value3,
                            value1,
                        ),
                        client.execute(
                            command = KedisListCommands.lrange(
                                key = key1,
                                start = 0,
                                end = -1,
                            ),
                        ),
                    )
                    client.execute(
                        command = KedisListCommands.lpop(
                            key = key1,
                            n = 3,
                        ),
                    )
                    assertEquals(
                        0,
                        client.execute(
                            command = KedisListCommands.llen(
                                key = key1,
                            ),
                        ),
                    )

                    assertEquals(
                        5,
                        client.execute(
                            command = KedisListCommands.lpush(
                                key = key1,
                                values = listOf(
                                    value1,
                                    value3,
                                    value1,
                                    value2,
                                    value1,
                                ),
                            ),
                        ),
                    )
                    assertEquals(
                        listOf(
                            value1,
                            value2,
                            value1,
                            value3,
                            value1,
                        ),
                        client.execute(
                            command = KedisListCommands.lrange(
                                key = key1,
                                start = 0,
                                end = -1,
                            ),
                        ),
                    )
                    assertEquals(
                        2,
                        client.execute(
                            command = KedisListCommands.lrem(
                                key = key1,
                                count = ListRemoveCount.FromTail(
                                    n = 2,
                                ),
                                value = value1,
                            ),
                        ),
                    )
                    assertEquals(
                        listOf(
                            value1,
                            value2,
                            value3,
                        ),
                        client.execute(
                            command = KedisListCommands.lrange(
                                key = key1,
                                start = 0,
                                end = -1,
                            ),
                        ),
                    )
                    client.execute(
                        command = KedisListCommands.lpop(
                            key = key1,
                            n = 3,
                        ),
                    )
                    assertEquals(
                        0,
                        client.execute(
                            command = KedisListCommands.llen(
                                key = key1,
                            ),
                        ),
                    )

                    assertEquals(
                        5,
                        client.execute(
                            command = KedisListCommands.lpush(
                                key = key1,
                                values = listOf(
                                    value1,
                                    value3,
                                    value1,
                                    value2,
                                    value1,
                                ),
                            ),
                        ),
                    )
                    assertEquals(
                        listOf(
                            value1,
                            value2,
                            value1,
                            value3,
                            value1,
                        ),
                        client.execute(
                            command = KedisListCommands.lrange(
                                key = key1,
                                start = 0,
                                end = -1,
                            ),
                        ),
                    )
                    assertEquals(
                        3,
                        client.execute(
                            command = KedisListCommands.lrem(
                                key = key1,
                                count = ListRemoveCount.All,
                                value = value1,
                            ),
                        ),
                    )
                    assertEquals(
                        listOf(
                            value2,
                            value3,
                        ),
                        client.execute(
                            command = KedisListCommands.lrange(
                                key = key1,
                                start = 0,
                                end = -1,
                            ),
                        ),
                    )
                }
        }
    }
}
