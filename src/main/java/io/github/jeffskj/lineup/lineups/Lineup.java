package io.github.jeffskj.lineup.lineups;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

public class Lineup {
    private List<String> battingOrder;
    private Position[][] positions;

    public Lineup(List<String> battingOrder, int innings) {
        this.battingOrder = battingOrder;
        this.positions = new Position[innings][battingOrder.size()];
    }

    public List<String> getBattingOrder() {
        return battingOrder;
    }

    public List<Position> getPositions(int inning) {
        return List.of(positions[inning]);
    }

    public void setPositions(int inning, List<Position> positions) {
        this.positions[inning] = positions.toArray(Position[]::new);
    }

    public List<Position> getPlayerPositions(String player) {
        int index = battingOrder.indexOf(player);
        return Arrays.stream(positions)
                     .map(innPos -> innPos[index])
                     .filter(Objects::nonNull)
                     .collect(toList());
    }

    public String toCsv() {
        List<String> lines = new ArrayList<>();
        lines.add("Player," + IntStream.range(1, positions.length+1).mapToObj(String::valueOf).collect(Collectors.joining(",")));

        for (int i = 0; i < battingOrder.size(); i++) {
            StringBuilder line = new StringBuilder();
            line.append(battingOrder.get(i));
            line.append(',');

            for (int inn = 0; inn < positions.length; inn++) {
                line.append(positions[inn][i].getAbbreviation());
                if (inn != positions.length-1) { line.append(','); }
            }
            lines.add(line.toString());
        }

        return String.join("\n", lines);
    }

    public String toTable() {
        List<String> lines = new ArrayList<>();
        int longestName = battingOrder.stream().mapToInt(String::length).max().getAsInt();

        lines.add(StringUtils.rightPad("Player", longestName + 4) + "|"
                          + IntStream.range(1, positions.length + 1)
                                     .mapToObj(String::valueOf)
                                     .map(s -> StringUtils.center(s, 7))
                                     .collect(Collectors.joining("|")));

        for (int i = 0; i < battingOrder.size(); i++) {
            StringBuilder line = new StringBuilder();
            line.append(StringUtils.rightPad(battingOrder.get(i), longestName + 4));
            line.append('|');
            for (int inn = 0; inn < positions.length; inn++) {
                line.append(StringUtils.center(positions[inn][i].getAbbreviation(), 7));
                if (inn != positions.length-1) { line.append('|'); }
            }
            line.append('|');
            lines.add(line.toString());
        }

        return String.join("\n", lines);
    }
}
