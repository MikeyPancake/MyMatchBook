package com.mymatchbook


data class Competitions(
    val competitionID: String = " ",
    val name: String = "",
    val matchType: String = "",
    val competitionMatches: List<Matches> = emptyList()
    )

data class Matches(
    val matchId: String = "",
    val matchName: String = "",
    val matchDate: String = "",
    val matchLocation: String = " ",
    val matchType: String = " ",
    val season: String = "",
    val totalCompetitors: Int = 0,
    val winnersScore: Int = 0,
    val userScore: Int = 0,
    val matchStages: List<Stage> = emptyList()
    )

data class Stage(
    val stageId: String = "",
    val name: String = "",
    val stageTargets: List<Target> = emptyList()
    )

data class Target(
    val targetId: String = "",
    val targetDistance: Int = 0,
    val targetSize: Double = 0.0,
    val targetImpact: String = "",
    val targetMiss: String = ""
    )


