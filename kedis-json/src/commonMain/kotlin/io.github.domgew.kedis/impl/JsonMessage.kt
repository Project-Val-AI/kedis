import io.github.domgew.kedis.impl.RedisMessage
import kotlinx.serialization.json.Json

public typealias JsonMessage = RedisMessage.BulkStringMessage

public inline fun <reified T:Any> JsonMessage.value():T {
    return Json.decodeFromString(this.value)
}
