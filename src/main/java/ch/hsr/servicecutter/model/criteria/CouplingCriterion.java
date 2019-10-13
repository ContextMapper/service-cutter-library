package ch.hsr.servicecutter.model.criteria;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class CouplingCriterion {

    public static final String SEMANTIC_PROXIMITY = "Semantic Proximity";
    public static final String IDENTITY_LIFECYCLE = "Identity & Lifecycle Commonality";
    public static final String SHARED_OWNER = "Shared Owner";
    public static final String SECURITY_CONSTRAINT = "Security Constraint";
    public static final String PREDEFINED_SERVICE = "Predefined Service Constraint";
    public static final String CONSISTENCY_CONSTRAINT = "Consistency Constraint";
    public static final String CONTENT_VOLATILITY = "Content Volatility";
    public static final String STRUCTURAL_VOLATILITY = "Structural Volatility";
    public static final String CONSISTENCY = "Consistency Criticality";
    public static final String SECURITY_CRITICALITY = "Security Criticality";
    public static final String STORAGE_SIMILARITY = "Storage Similarity";
    public static final String AVAILABILITY = "Availability Criticality";
    public static final String SECURITY_CONTEXUALITY = "Security Contextuality";
    public static final String LATENCY = "Latency";

    private String name;
    private String description;
    private CouplingType type;

    public CouplingCriterion(String name, String description, CouplingType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof CouplingCriterion) {
            CouplingCriterion other = (CouplingCriterion) obj;
            return this == other || Objects.equal(name, other.name);
        } else {
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public CouplingType getType() {
        return type;
    }

    public boolean is(String name) {
        return name.equals(this.name);
    }

}
