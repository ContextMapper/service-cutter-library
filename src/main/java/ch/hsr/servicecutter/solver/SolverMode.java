package ch.hsr.servicecutter.solver;

public enum SolverMode {

    MODE_LEUNG("Leung"); // default mode for now

    private String name;

    SolverMode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
