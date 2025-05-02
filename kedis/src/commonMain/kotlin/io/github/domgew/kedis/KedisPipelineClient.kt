package io.github.domgew.kedis

import io.github.domgew.kedis.commands.KedisCommand
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Deferred

/**
 * A pipelined redis client that uses its parent's connection on demand.
 *
 * It does not block its parent's connection until [execute] is called.
 * All queued commands get executed when [execute] is called, not before.
 * When [execute] completes, all queued commands were fully executed.
 *
 * There is no guarantee that all commands are sent before results start coming in.
 * Nor is there a guarantee that all commands are sent at once.
 *
 * [https://redis.io/docs/latest/develop/use/pipelining/](https://redis.io/docs/latest/develop/use/pipelining/)
 *
 * @see KedisClient.pipelined
 * @sample io.github.domgew.kedis.samples.PipelineSamples.simple
 */
public interface KedisPipelineClient {

    /**
     * Enqueues a [command] for future execution.
     *
     * The execution begins once [execute] is called.
     * The returned [Deferred] cannot resolve before [execute] was called.
     * The returned [Deferred] will resolve, fail, or be cancelled before [execute] finishes.
     *
     * The queue's order of operations is enforced.
     * When the first command fails, the second will be cancelled, resumed exceptionally, or depending on the timing not started.
     * The cancellation also affects commands that were completely sent and might have already been processed by redis, but the result reading was cancelled, so the [Deferred] will be cancelled.
     *
     * It cannot be called after [execute] or [cancel] was called.
     * Otherwise, it will result in an [IllegalStateException].
     *
     * @throws KedisException
     * @throws IllegalStateException
     */
    public fun <T> enqueue(
        command: KedisCommand<T>,
    ): Deferred<T>

    /**
     * Enqueues a hint of when to send all previously written commands to the redis server and wait for their responses to be read.
     *
     * When the buffer is full before, it will be sent regardless without waiting for their responses.
     *
     * It cannot be called after [execute] or [cancel] was called.
     * Otherwise, it will result in an [IllegalStateException].
     *
     * @throws KedisException
     * @throws IllegalStateException
     */
    public fun enqueueSend()

    /**
     * Executes the pipeline by executing all queued commands in order (see [enqueue]).
     *
     * Can only be called once per [KedisPipelineClient].
     * Cannot be called after [cancel].
     * If one or more of those conditions are ignored, an [IllegalStateException] will be thrown.
     *
     * @throws KedisException
     * @throws IllegalStateException
     * @throws CancellationException
     */
    public suspend fun execute()

    /**
     * Cancels the pipeline's execution if running, blocks its future execution otherwise.
     *
     * Has a similar effect as cancelling the [execute] method's coroutine scope.
     *
     * This leads future calls of [enqueue], [enqueueSend], and [execute] to fail with an [IllegalStateException].
     */
    public fun cancel()
}
