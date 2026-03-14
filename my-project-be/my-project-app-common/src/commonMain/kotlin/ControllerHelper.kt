import kotlinx.datetime.Clock
import ru.otus.otuskotlin.lrvch.api.logv1.mappers.toLog
import ru.otus.otuskotlin.lrvch.app.common.ICatalogAppSettings
import ru.otus.otuskotlin.lrvch.common.CatalogContext
import ru.otus.otuskotlin.lrvch.common.helpers.asCatalogError
import ru.otus.otuskotlin.lrvch.common.models.CatalogCommand
import ru.otus.otuskotlin.lrvch.common.models.CatalogState
import kotlin.reflect.KClass

suspend inline fun <T> ICatalogAppSettings.controllerHelper(
    crossinline getRequest: suspend CatalogContext.() -> Unit,
    crossinline toResponse: suspend CatalogContext.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = CatalogContext(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId),
            e = e,
        )
        ctx.state = CatalogState.FAILED
        ctx.errors.add(e.asCatalogError())
        processor.exec(ctx)
        if (ctx.command == CatalogCommand.NONE) {
            ctx.command = CatalogCommand.READ
        }
        ctx.toResponse()
    }
}
