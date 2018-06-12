package core.entity

data class StatsEntity(val coveragePercent : Double,
                       val issueCount : Int,
                       val durationSeconds : Double,
                       val lineCount : Int,
                       val commitDate : Long,
                       val commitHash : String,
                       val commitBranch : String
)