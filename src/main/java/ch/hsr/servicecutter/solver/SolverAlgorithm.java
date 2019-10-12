package ch.hsr.servicecutter.solver;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SolverAlgorithm {

    LEUNG("Leung"); // default mode for now

    private String name;

    SolverAlgorithm(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @JsonCreator
    public static SolverAlgorithm forValue(String value) {
        return SolverAlgorithm.valueOf(value.toUpperCase());
    }
}
