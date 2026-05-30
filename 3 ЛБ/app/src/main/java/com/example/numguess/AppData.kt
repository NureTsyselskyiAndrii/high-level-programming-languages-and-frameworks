package com.example.numguess

object AppData {
    var currentUser: String = ""
    val players = mutableMapOf<String, Player>()

    fun getOrCreate(username: String): Player {
        if (!players.containsKey(username)) {
            players[username] = Player(username)
        } else {
            players[username]!!.currentSecret = (1..100).random()
            players[username]!!.attempts = 0
        }
        return players[username]!!
    }

    fun getLeaderboard(): List<Player> =
        players.values
            .filter { it.wins > 0 }
            .sortedWith(compareByDescending<Player> { it.wins }
                .thenBy { it.bestScore ?: Int.MAX_VALUE })
}
