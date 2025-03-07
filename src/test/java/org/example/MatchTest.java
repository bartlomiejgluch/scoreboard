package org.example;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MatchTest {

    @Test
    void shouldCreateMatchWithZeroScore() {
        // Given
        String homeTeam = "Team A";
        String awayTeam = "Team B";

        // When
        Match match = new Match(homeTeam, awayTeam);

        // Then
        assertThat(match.getHomeTeam()).isEqualTo(homeTeam);
        assertThat(match.getAwayTeam()).isEqualTo(awayTeam);
        assertThat(match.getHomeScore()).isEqualTo(0);
        assertThat(match.getAwayScore()).isEqualTo(0);
        assertThat(match.getMatchId()).isNotNull();
    }

    @Test
    void shouldUpdateScoreSuccessfully() {
        // Given
        Match match = new Match("Team A", "Team B");

        // When
        match.updateScore(2, 3);

        // Then
        assertThat(match.getHomeScore()).isEqualTo(2);
        assertThat(match.getAwayScore()).isEqualTo(3);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingScoreWithNegativeValues() {
        // Given
        Match match = new Match("Team A", "Team B");

        // When & Then
        assertThatThrownBy(() -> match.updateScore(-1, 2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid score");

        assertThatThrownBy(() -> match.updateScore(1, -2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid score");
    }

    @Test
    void shouldReturnCorrectTotalScore() {
        // Given
        Match match = new Match("Team A", "Team B");
        match.updateScore(3, 2);

        // When
        int totalScore = match.getTotalScore();

        // Then
        assertThat(totalScore).isEqualTo(5);
    }

    @Test
    void shouldDisplayMatchDetailsCorrectly() {
        // Given
        Match match = new Match("Team A", "Team B");
        match.updateScore(1, 2);

        // When
        String matchDetails = match.getMatchSummary();

        // Then
        assertThat(matchDetails).isEqualTo("Team A 1 - Team B 2");
    }

    @Test
    void shouldSetStartTimeWhenMatchStarts() {
        // Given
        Match match = new Match("Team A", "Team B");

        // When
        match.startMatch();

        // Then
        assertThat(match.getStartMatchTime()).isPresent();
    }

    @Test
    void shouldThrowExceptionIfMatchAlreadyStarted() {
        // Given
        Match match = new Match("Team A", "Team B");
        match.startMatch();

        // When & Then
        assertThatThrownBy(match::startMatch)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Match has already started.");
    }

    @Test
    void shouldNotSetStartTimeTwice() throws InterruptedException {
        // Given
        Match match = new Match("Team A", "Team B");

        // When
        match.startMatch();
        Instant firstStartTime = match.getStartMatchTime().orElseThrow();
        Thread.sleep(10);

        // Then
        assertThatThrownBy(match::startMatch)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Match has already started.");
    }

}