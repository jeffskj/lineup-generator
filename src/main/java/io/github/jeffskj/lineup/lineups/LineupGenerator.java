package io.github.jeffskj.lineup.lineups;

import static all.lineups.Position.*;
import static all.lineups.Position.PositionCategory.OUTFIELD;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LineupGenerator {
    private static final Map<Integer, List<Position>> POSITIONS_FOR_NUM_PLAYERS = Map.of(
            11, List.of(CATCHER, PITCHER, FIRST, SECOND, SHORTSTOP, THIRD, LEFT, CENTER, RIGHT, BENCH, BENCH),
            10, List.of(CATCHER, PITCHER, FIRST, SECOND, SHORTSTOP, THIRD, LEFT, LEFT_CENTER, RIGHT_CENTER, RIGHT),
            9, List.of(CATCHER, PITCHER, FIRST, SECOND, SHORTSTOP, THIRD, LEFT, CENTER, RIGHT),
            8, List.of(CATCHER, PITCHER, FIRST, SECOND, SHORTSTOP, THIRD, LEFT, RIGHT),
            7, List.of(PITCHER, FIRST, SECOND, SHORTSTOP, THIRD, LEFT, RIGHT),
            6, List.of(PITCHER, FIRST, SECOND, SHORTSTOP, THIRD, RIGHT)
    );
    public static final int INNINGS = 5;

    private static List<String> PLAYERS = List.of(
            "William",
            "Calvin Deux",
            "Easton",
            "Kyson",
            "Jackson",
            "Jon",
            "Avery",
            "Dylan",
            "Dominic",
            "Zeke",
            "Charles"
    );

    private static Set<String> ABSENT_PLAYERS = Set.of();

    private static Map<String, Set<Position>> BANNED_POSITIONS = Map.of("Dominic", EnumSet.of(CATCHER),
                                                                        "Avery", EnumSet.of(CATCHER));

    public static void main(String[] args) throws IOException {
        List<String> battingOrder = PLAYERS.stream()
                                          .filter(p -> !ABSENT_PLAYERS.contains(p))
                                          .collect(toList());
        Collections.shuffle(battingOrder);

        Lineup lineup = new Lineup(battingOrder, INNINGS);

        List<Position> positions = POSITIONS_FOR_NUM_PLAYERS.get(battingOrder.size());
        for (int i = 0; i < INNINGS; i++) {
            List<Position> inningPositions = new ArrayList<>(positions);

            do {
                Collections.shuffle(inningPositions);
            } while (!passesLineupChecks(inningPositions, lineup));

            lineup.setPositions(i, inningPositions);
        }

        System.out.println(lineup.toTable());
        Files.write(Paths.get("/tmp/linup.csv"), lineup.toCsv().getBytes(StandardCharsets.UTF_8));
    }

    private static boolean passesLineupChecks(List<Position> inningPositions, Lineup lineup) {

        for (int i = 0; i < lineup.getBattingOrder().size(); i++) {
            String player = lineup.getBattingOrder().get(i);
            Position position = inningPositions.get(i);
            if (BANNED_POSITIONS.getOrDefault(player, Set.of()).contains(position)) {
                return false;
            }

            List<Position> playerPositions = lineup.getPlayerPositions(player);
            if (playerPositions.isEmpty()) {
                continue;
            }

            // can't be benched twice
            if (position == BENCH && playerPositions.contains(BENCH)) {
                return false;
            }

            Position lastPosition = playerPositions.get(playerPositions.size()-1);
            if (position == lastPosition) {
                return false;
            }

            if (position.getCategory() == OUTFIELD && lastPosition.getCategory() == OUTFIELD) {
                return false;
            }
        }
        return true;
    }
}
