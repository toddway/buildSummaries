package core.usecase

import core.writeSummaryReports
import core.entity.BuildConfig
import core.entity.Message
import pushArtifacts

class HandleBuildFinishedUseCase(
        private val postStatusUseCase: PostStatusUseCase,
        private val postStatsUseCase: PostStatsUseCase,
        private val summaries: List<GetSummaryUseCase>,
        private val config: BuildConfig,
        private val messageQueue : MutableList<Message>
) {
    fun invoke() {
        if (config.isChecksActivated) {
            summaries.postStatuses(postStatusUseCase)
            postStatsUseCase.post(summaries.toStats(config))
            config.writeSummaryReports(messageQueue)
            config.pushArtifacts(messageQueue)
            messageQueue.distinct().forEach { println(it.toString()) }
        }
    }
}
