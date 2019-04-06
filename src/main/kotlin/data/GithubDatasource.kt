package data

import core.entity.BuildConfig
import core.entity.BuildStatus
import core.usecase.PostStatusUseCase
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

class GithubDatasource(retrofit: Retrofit, val config : BuildConfig) : PostStatusUseCase.Datasource {
    override fun isRemote() = true

    override fun post(status: BuildStatus, message: String, key: String): Observable<Boolean> {
        val body = GithubBuildStatusBody(status.githubFormat(), key, message, config.buildUrl)
        return service.postBuildStatus(config.git.commitHash, body).map { true }.onErrorReturn { false }
    }

    private val service = retrofit.create(Service::class.java)

    override fun isActive(): Boolean {
        return config.isPostActivated && config.baseUrl.contains("github")
    }

    override fun name() = "Github"

    interface Service {
        @Headers("Accept: application/json", "User-Agent: gradle build")
        @POST("statuses/{hash}")
        fun postBuildStatus(@Path("hash") hash : String, @Body body : GithubBuildStatusBody) : Observable<ResponseBody>
    }

    data class GithubBuildStatusBody(
            val state : String = "",
            val context : String = "",
            var description : String = "",
            var target_url : String? = null
    )
}

fun BuildStatus.githubFormat() = when(this) {
    BuildStatus.FAILURE -> "failure"
    BuildStatus.PENDING -> "pending"
    BuildStatus.SUCCESS -> "success"
}
