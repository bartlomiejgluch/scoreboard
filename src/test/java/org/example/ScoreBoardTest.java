package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScoreBoardTest {

    @Test
    void shouldStartGameSuccessfully() {
        //given
        ScoreBoard scoreBoard = new ScoreBoard();
        Match match = new Match("Team A", "Team B");

        //when
        scoreBoard.startGame(match);

        //then
        assertThat(scoreBoard.getFinishedMatchesSorted()).isEmpty();
    }

    @Test
    void shouldNotStartGameIfMatchAlreadyExists() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();
        Match match = new Match("Team A", "Team B");
        scoreBoard.startGame(match);

        // When & Then
        assertThatThrownBy(() -> scoreBoard.startGame(match))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Match with this ID already exists");
    }

    @Test
    void shouldUpdateScoreOfOngoingMatch() {
        //given
        ScoreBoard scoreBoard = new ScoreBoard();
        Match match = new Match("Team A", "Team B");
        scoreBoard.startGame(match);

        //when
        scoreBoard.updateScore(match.getMatchId(), 2, 3);

        //then
        assertThat(match.getHomeScore()).isEqualTo(2);
        assertThat(match.getAwayScore()).isEqualTo(3);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingScoreOfNonExistingMatch() {
        //given
        ScoreBoard scoreBoard = new ScoreBoard();
        UUID invalidMatchId = UUID.randomUUID();

        // When & Then
        assertThatThrownBy(() -> scoreBoard.updateScore(invalidMatchId, 1, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cannot update");

    }

    @Test
    void shouldFinishGameSuccessfully() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();
        Match match = new Match("Team A", "Team B");
        scoreBoard.startGame(match);

        // When
        scoreBoard.finishGame(match.getMatchId());

        // Then
        assertThat(scoreBoard.getFinishedMatchesSorted()).containsExactly(match);
    }

    @Test
    void shouldNotFinishNonExistentGame() {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();
        UUID invalidMatchId = UUID.randomUUID();

        // When & Then
        assertThatThrownBy(() -> scoreBoard.finishGame(invalidMatchId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Match with this ID does not exist");
    }

    @Test
    void shouldReturnSummaryOfGamesSortedByTotalScoreAndTime() throws InterruptedException {
        // Given
        ScoreBoard scoreBoard = new ScoreBoard();

        Match match1 = new Match("Mexico", "Canada");
        Match match2 = new Match("Spain", "Brazil");
        Match match3 = new Match("Germany", "France");
        Match match4 = new Match("Uruguay", "Italy");
        Match match5 = new Match("Argentina", "Australia");

        scoreBoard.startGame(match1);
        Thread.sleep(10);
        scoreBoard.startGame(match2);
        Thread.sleep(10);
        scoreBoard.startGame(match3);
        Thread.sleep(10);
        scoreBoard.startGame(match4);
        Thread.sleep(10);
        scoreBoard.startGame(match5);

        match1.updateScore(0, 5);
        match2.updateScore(10, 2);
        match3.updateScore(2, 2);
        match4.updateScore(6, 6);
        match5.updateScore(3, 1);

        scoreBoard.finishGame(match1.getMatchId());
        scoreBoard.finishGame(match2.getMatchId());
        scoreBoard.finishGame(match3.getMatchId());
        scoreBoard.finishGame(match4.getMatchId());
        scoreBoard.finishGame(match5.getMatchId());

        // When
        List<Match> sortedMatches = scoreBoard.getFinishedMatchesSorted();

        // Then
        List<String> expectedSummary = List.of(
                "Uruguay 6 - Italy 6",
                "Spain 10 - Brazil 2",
                "Mexico 0 - Canada 5",
                "Argentina 3 - Australia 1",
                "Germany 2 - France 2"
        );

        List<String> actualSummary = sortedMatches.stream()
                .map(Match::getMatchSummary)
                .toList();

        assertThat(actualSummary).containsExactlyElementsOf(expectedSummary);

    }
}