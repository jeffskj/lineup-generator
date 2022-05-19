package io.github.jeffskj.lineup.lineups;

import static io.github.jeffskj.lineup.lineups.Position.PositionCategory.INFIELD;
import static io.github.jeffskj.lineup.lineups.Position.PositionCategory.OUTFIELD;

public enum Position {
    CATCHER("C", INFIELD),
    PITCHER("P", INFIELD),
    FIRST("1B", INFIELD),
    SECOND("2B", INFIELD),
    SHORTSTOP("SS", INFIELD),
    THIRD("3B", INFIELD),
    LEFT("LF", OUTFIELD),
    LEFT_CENTER("L-CF", OUTFIELD),
    CENTER("CF", OUTFIELD),
    RIGHT_CENTER("R-CF", OUTFIELD),
    RIGHT("RF", OUTFIELD),
    BENCH("B", PositionCategory.BENCH);

    private String abbreviation;
    private PositionCategory category;

    Position(String abbreviation, PositionCategory category) {
        this.abbreviation = abbreviation;
        this.category = category;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public PositionCategory getCategory() {
        return category;
    }

    enum PositionCategory {
        INFIELD, OUTFIELD, BENCH
    }
}
