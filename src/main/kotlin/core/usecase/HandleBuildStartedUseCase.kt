package core.usecase

import core.entity.BuildConfig
import core.entity.BuildStatus
import java.io.File

class HandleBuildStartedUseCase(
        val postStatusUseCase: PostStatusUseCase,
        val config: BuildConfig
) {
    fun invoke() {
        if (config.isPluginActivated) {
            config.artifactsDir().deleteRecursively()
            postStatusUseCase.post(BuildStatus.PENDING, config.startedMessage(), "build")
        } else {
            config.log = null
        }
    }
}