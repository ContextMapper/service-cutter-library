package ch.hsr.servicecutter.solver;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SolverAlgorithm {

    LEUNG("Leung"), CHINESE_WHISPERS("Chinese Whispers"), MARKOV_CLUSTERING("Markov Clustering");

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
