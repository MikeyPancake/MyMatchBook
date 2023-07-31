package com.mymatchbook

data class Users(
    val name: String = "",
    val email: String = "",
    val competitions: List<CompetitionType> = emptyList()
)
data class CompetitionType(
    val competitionTypeID: String = " ",
    var matches: List<Match> = emptyList(),
    )

data class Match(
    val matchId: String? = null,
    val matchName: String? = null,
    val matchDate: String? = null,
    val matchLocation: String? = null,
    val matchType: String? = null,
    val matchSeason: String? = null,
    val totalCompetitors: Int? = null,
    val winnersScore: Int? = null,
    val userScore: Int? = null,
    val matchStages: List<Stages> = emptyList()
    )

data class Stages(
    val stageId: String = "",
    val stageName: String = "",
    val stageTargets: List<Targets> = emptyList()
    )

data class Targets(
    val targetId: String = "",
    val targetDistance: Int = 0,
    val targetSize: Double = 0.0,
    val targetImpact: String = "",
    val targetMiss: String = ""
    )


