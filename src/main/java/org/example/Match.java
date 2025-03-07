package org.example;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Match {
    private final UUID matchId;
    private final String homeTeam;
    private final String awayTeam;
    private int homeScore;
    private int awayScore;
    private final Instant matchTime;

    public Match(String homeTeam, String awayTeam) {
        this.matchId = UUID.randomUUID();
        this.awayTeam = awayTeam;
        this.homeTeam = homeTeam;
        this.homeScore = 0;
        this.awayScore = 0;
        this.matchTime = Instant.now();
    }

    public UUID getMatchId() {
        return matchId;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public Instant getMatchTime() {
        return matchTime;
    }

    public void updateScore(int homeScore, int awayScore) {
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Invalid score");
        }
        this.homeScore = homeScore;
        this.awayScore = awayScore;

    }

    public int getTotalScore() {
        return homeScore + awayScore;
    }

    public String getMatchSummary() {
        return homeTeam + " " + homeScore + " - " + awayTeam + " " + awayScore;
    }

    @Override
    public String toString() {
        return "Match{" +
                "matchId=" + matchId +
                ", homeTeam='" + homeTeam + '\'' +
                ", awayTeam='" + awayTeam + '\'' +
                ", homeScore=" + homeScore +
                ", awayScore=" + awayScore +
                ", matchTime=" + matchTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Match match = (Match) o;
        return homeScore == match.homeScore && awayScore == match.awayScore && matchId.equals(match.matchId) && Objects.equals(homeTeam, match.homeTeam) && Objects.equals(awayTeam, match.awayTeam) && Objects.equals(matchTime, match.matchTime);
    }

    @Override
    public int hashCode() {
        int result = matchId.hashCode();
        result = 31 * result + Objects.hashCode(homeTeam);
        result = 31 * result + Objects.hashCode(awayTeam);
        result = 31 * result + homeScore;
        result = 31 * result + awayScore;
        result = 31 * result + Objects.hashCode(matchTime);
        return result;
    }
}
