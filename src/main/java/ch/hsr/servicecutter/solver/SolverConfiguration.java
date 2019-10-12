package ch.hsr.servicecutter.solver;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;

public class SolverConfiguration {

	private Map<String, Double> algorithmParams = new HashMap<>();
	private Map<String, SolverPriority> priorities = new HashMap<>();
	private SolverAlgorithm algorithm = SolverAlgorithm.LEUNG; // default for now

	private Logger log = LoggerFactory.getLogger(SolverConfiguration.class);

	public SolverConfiguration() {
	}

	public void setAlgorithmParam(final String param, final Double value) {
		algorithmParams.put(param, value);
	}

	public Map<String, SolverPriority> getPriorities() {
		return priorities;
	}

	public void setPriority(final String criterionType, final SolverPriority priority) {
		if (priority == null) {
			throw new InvalidParameterException();
		}
		priorities.put(criterionType, priority);
	}

	public Map<String, Double> getAlgorithmParams() {
		return algorithmParams;
	}

	public SolverPriority getPriorityForCouplingCriterion(final String criterionType) {
		if (!priorities.containsKey(criterionType)) {
			log.error("no priority defined for couplingCriterion: " + criterionType + ". Use 1");
			return SolverPriority.S;
		}
		return priorities.get(criterionType);
	}

	public void setAlgorithm(final SolverAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	public SolverAlgorithm getAlgorithm() {
		return algorithm;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(getClass()).add("algorithm", algorithm).add("algorithmParams", algorithmParams).add("priorities", priorities).toString();
	}

}