package io.github.domgew.kedis.samples

import io.github.domgew.kedis.KedisClient
import io.github.domgew.kedis.commands.KedisValueCommands
import io.github.domgew.kedis.results.value.SetResult
import kotlin.test.assertEquals

object PipelineSamples {

    suspend fun simple(
        client: KedisClient,
    ) {
        val key = "testKey"
        val value = "Test Value"

        val pipeline = client.pipelined()

        val setResult = pipeline.enqueue(
            command = KedisValueCommands.set(
                key = key,
                value = value,
            ),
        )
        val getResult = pipeline.enqueue(
            command = KedisValueCommands.get(
                key = key,
            ),
        )

        pipeline.execute()

        assertEquals(SetResult.Ok, setResult.await())
        assertEquals(value, getResult.await())
    }
}
