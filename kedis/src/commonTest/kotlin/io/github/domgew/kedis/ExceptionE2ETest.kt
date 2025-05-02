package io.github.domgew.kedis

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext

class ExceptionE2ETest {

    @Test
    fun failedToConnect() = runTest {
        withContext(Dispatchers.Default) {
            assertFailsWith(
                exceptionClass = KedisException::class,
            ) {
                KedisClient.newClient(
                    KedisConfiguration(
                        endpoint = KedisConfiguration.Endpoint.HostPort(
                            host = "127.0.0.1",
                            port = 62345, // should not be used
                        ),
                        authentication = KedisConfiguration.Authentication.NoAutoAuth,
                        connectionTimeout = 5.seconds,
                    ),
                )
                    .connect()
            }
        }
    }
}
