package ch.hsr.servicecutter.solver;

public enum SolverPriority {

    IGNORE(0), XS(0.5), S(1), M(3), L(5), XL(8), XXL(13);

    private double priorityValue;

    SolverPriority(double priorityValue) {
        this.priorityValue = priorityValue;
    }

    public Double toValue() {
        return priorityValue;
    }

    @Override
    public String toString() {
        return Double.toString(priorityValue);
    }
}
