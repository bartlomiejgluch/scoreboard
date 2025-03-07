package org.example;

import java.time.Instant;
import java.util.*;

public class ScoreBoard {

    private final Map<UUID, Match> onGoingMatches = new HashMap<>();
    private final List<Match> finishedMatches = new ArrayList<>();

    public void startGame(Match match) {
        UUID id = match.getMatchId();
        if (onGoingMatches.containsKey(id)) {
            throw new IllegalArgumentException("Match with this ID already exists: " + match.getMatchId());
        }

        match.startMatch();
        onGoingMatches.put(id, match);
    }

    public void finishGame(UUID matchId) {
        Match match = Optional.ofNullable(onGoingMatches.remove(matchId))
                .orElseThrow(() -> new IllegalArgumentException("Match with this ID does not exist: " + matchId));
        finishedMatches.add(match);
    }

    public void updateScore(UUID matchId, int homeScore, int awayScore) {
        Optional.ofNullable(onGoingMatches.get(matchId))
                .orElseThrow(() -> new IllegalArgumentException("Cannot update. Match with this ID does not exist: " + matchId))
                .updateScore(homeScore, awayScore);

    }

    public List<Match> getFinishedMatchesSorted() {
        return finishedMatches.stream()
                .sorted(
                        Comparator.comparingInt(Match::getTotalScore).reversed()
                                .thenComparing(match -> match.getStartMatchTime().orElse(Instant.MIN),
                                        Comparator.reverseOrder())
                )
                .toList();
    }
}
