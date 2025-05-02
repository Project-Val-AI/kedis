package io.github.domgew.kedis

import io.github.domgew.kedis.arguments.server.SyncOption
import io.github.domgew.kedis.commands.KedisHashCommands
import io.github.domgew.kedis.commands.KedisServerCommands
import io.github.domgew.kedis.commands.KedisValueCommands
import io.github.domgew.kedis.utils.TestConfigUtil
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext

class HashE2ETest {

    @Test
    fun test() = runTest {
        withContext(Dispatchers.Default) {
            val key = "testKey"
            val field1 = "field1"
            val value1 = "value1"
            val field2 = "field2"
            val value2 = "value2"
            val field3 = "field3"
            val value3 = "value3"

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
                            command = KedisHashCommands.hashGetAll(
                                key = key,
                            ),
                        ),
                    )
                    assertEquals(
                        0L,
                        client.execute(
                            command = KedisHashCommands.hashLength(
                                key = key,
                            ),
                        ),
                    )
                    assertNull(
                        client.execute(
                            command = KedisHashCommands.hashKeys(
                                key = key,
                            ),
                        ),
                    )
                    assertFalse(
                        client.execute(
                            command = KedisHashCommands.hashExists(
                                key = key,
                                field = field1,
                            ),
                        ),
                    )
                    assertEquals(
                        0L,
                        client.execute(
                            command = KedisHashCommands.hashDel(
                                key = key,
                                field = arrayOf(
                                    field1,
                                    field2,
                                    field3,
                                ),
                            ),
                        ),
                    )

                    assertEquals(
                        2L,
                        client.execute(
                            command = KedisHashCommands.hashSet(
                                key = key,
                                fieldValues = mapOf(
                                    field1 to value1,
                                    field2 to value2,
                                ),
                            ),
                        ),
                    )
                    assertEquals(
                        2L,
                        client.execute(
                            command = KedisHashCommands.hashLength(
                                key = key,
                            ),
                        ),
                    )
                    assertTrue(
                        client.execute(
                            command = KedisHashCommands.hashExists(
                                key = key,
                                field = field1,
                            ),
                        ),
                    )
                    assertTrue(
                        client.execute(
                            command = KedisHashCommands.hashExists(
                                key = key,
                                field = field2,
                            ),
                        ),
                    )
                    assertFalse(
                        client.execute(
                            command = KedisHashCommands.hashExists(
                                key = key,
                                field = field3,
                            ),
                        ),
                    )
                    assertEquals(
                        listOf(
                            field1,
                            field2,
                        ),
                        client.execute(
                            command = KedisHashCommands.hashKeys(
                                key = key,
                            ),
                        ),
                    )
                    assertEquals(
                        mapOf(
                            field1 to value1,
                            field2 to value2,
                        ),
                        client.execute(
                            command = KedisHashCommands.hashGetAll(
                                key = key,
                            ),
                        ),
                    )
                    assertEquals(
                        value2,
                        client.execute(
                            command = KedisHashCommands.hashGet(
                                key = key,
                                field = field2,
                            ),
                        ),
                    )
                    assertEquals(
                        1L,
                        client.execute(
                            command = KedisHashCommands.hashSet(
                                key = key,
                                fieldValues = mapOf(
                                    field2 to value3,
                                    field3 to value3,
                                ),
                            ),
                        ),
                    )
                    assertEquals(
                        3L,
                        client.execute(
                            command = KedisHashCommands.hashLength(
                                key = key,
                            ),
                        ),
                    )
                    assertEquals(
                        value3,
                        client.execute(
                            command = KedisHashCommands.hashGet(
                                key = key,
                                field = field2,
                            ),
                        ),
                    )
                    assertEquals(
                        0L,
                        client.execute(
                            command = KedisHashCommands.hashSet(
                                key = key,
                                fieldValues = mapOf(
                                    field2 to value2,
                                ),
                            ),
                        ),
                    )
                    assertTrue(
                        client.execute(
                            command = KedisHashCommands.hashExists(
                                key = key,
                                field = field1,
                            ),
                        ),
                    )
                    assertTrue(
                        client.execute(
                            command = KedisHashCommands.hashExists(
                                key = key,
                                field = field2,
                            ),
                        ),
                    )
                    assertTrue(
                        client.execute(
                            command = KedisHashCommands.hashExists(
                                key = key,
                                field = field3,
                            ),
                        ),
                    )
                    assertEquals(
                        listOf(
                            field1,
                            field2,
                            field3,
                        ),
                        client.execute(
                            command = KedisHashCommands.hashKeys(
                                key = key,
                            ),
                        ),
                    )
                    assertEquals(
                        mapOf(
                            field1 to value1,
                            field2 to value2,
                            field3 to value3,
                        ),
                        client.execute(
                            command = KedisHashCommands.hashGetAll(
                                key = key,
                            ),
                        ),
                    )
                    assertEquals(
                        1L,
                        client.execute(
                            command = KedisHashCommands.hashDel(
                                key = key,
                                field = arrayOf(field2),
                            ),
                        ),
                    )
                    assertEquals(
                        2L,
                        client.execute(
                            command = KedisHashCommands.hashLength(
                                key = key,
                            ),
                        ),
                    )
                    assertEquals(
                        mapOf(
                            field1 to value1,
                            field3 to value3,
                        ),
                        client.execute(
                            command = KedisHashCommands.hashGetAll(
                                key = key,
                            ),
                        ),
                    )
                    assertEquals(
                        1L,
                        client.execute(
                            command = KedisValueCommands.del(
                                key = arrayOf(key),
                            ),
                        ),
                    )
                    assertNull(
                        client.execute(
                            command = KedisHashCommands.hashGetAll(
                                key = key,
                            ),
                        ),
                    )
                    assertEquals(
                        0L,
                        client.execute(
                            command = KedisHashCommands.hashLength(
                                key = key,
                            ),
                        ),
                    )
                }
        }
    }
}
