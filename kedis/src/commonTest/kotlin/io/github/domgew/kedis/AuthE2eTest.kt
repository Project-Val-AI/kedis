package io.github.domgew.kedis

import io.github.domgew.kedis.commands.KedisServerCommands
import io.github.domgew.kedis.utils.RedisUtil
import io.github.domgew.kedis.utils.TestConfigUtil
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext

class AuthE2eTest {

    @Test
    fun testAutoAuth() = runTest {
        withContext(Dispatchers.Default) {
            val username = "testUser"
            val password = "testPassword"

            RedisUtil.createUser(
                username = username,
                password = password,
            )

            val client = KedisClient.newClient(
                configuration = KedisConfiguration(
                    endpoint = KedisConfiguration.Endpoint.HostPort(
                        host = "127.0.0.1",
                        port = TestConfigUtil.getPort(),
                    ),
                    authentication = KedisConfiguration.Authentication.AutoAuth(
                        username = username,
                        password = password,
                    ),
                    connectionTimeout = 2.seconds,
                ),
            )

            try {
                client.connect()
                assertEquals(
                    username,
                    client.execute(
                        KedisServerCommands.whoAmI(),
                    ),
                )
            } finally {
                client.close()
                RedisUtil.removeUser(
                    username = username,
                )
            }
        }
    }

    @Test
    fun testManualAuth() = runTest {
        withContext(Dispatchers.Default) {
            val username = "testUser"
            val password = "testPassword"

            RedisUtil.createUser(
                username = username,
                password = password,
            )

            val client = KedisClient.newClient(
                configuration = KedisConfiguration(
                    endpoint = KedisConfiguration.Endpoint.HostPort(
                        host = "127.0.0.1",
                        port = TestConfigUtil.getPort(),
                    ),
                    authentication = KedisConfiguration.Authentication.NoAutoAuth,
                    connectionTimeout = 2.seconds,
                ),
            )

            try {
                client.connect()
                assertNotEquals(
                    username,
                    client.execute(
                        command = KedisServerCommands.whoAmI(),
                    )
                        .lowercase(),
                )
                client.execute(
                    command = KedisServerCommands.auth(
                        username = username,
                        password = password,
                    ),
                )
                assertEquals(
                    username,
                    client.execute(
                        command = KedisServerCommands.whoAmI(),
                    ),
                )
            } finally {
                client.close()
                RedisUtil.removeUser(
                    username = username,
                )
            }
        }
    }
}
