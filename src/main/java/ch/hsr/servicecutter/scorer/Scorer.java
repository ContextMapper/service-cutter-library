package ch.hsr.servicecutter.scorer;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import ch.hsr.servicecutter.model.usersystem.CouplingInstance;
import ch.hsr.servicecutter.model.usersystem.Nanoentity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.hsr.servicecutter.model.criteria.CouplingCriterion;
import ch.hsr.servicecutter.model.criteria.CouplingType;
import ch.hsr.servicecutter.model.solver.EntityPair;
import ch.hsr.servicecutter.model.usersystem.InstanceType;
import ch.hsr.servicecutter.scorer.criterionScorer.CharacteristicsCriteriaScorer;
import ch.hsr.servicecutter.scorer.criterionScorer.CohesiveGroupCriterionScorer;
import ch.hsr.servicecutter.scorer.criterionScorer.ExclusiveGroupCriterionScorer;
import ch.hsr.servicecutter.scorer.criterionScorer.SemanticProximityCriterionScorer;
import ch.hsr.servicecutter.scorer.criterionScorer.SeparatedGroupCriterionScorer;

public class Scorer {

	public static final double MAX_SCORE = 10d;
	public static final double MIN_SCORE = -10d;
	public static final double NO_SCORE = 0d;

	private Logger log = LoggerFactory.getLogger(Scorer.class);

	private List<CouplingInstance> couplingInstanceList;
	private Set<Nanoentity> nanoentitySet;

	public Scorer(final List<CouplingInstance> couplingInstanceList, final Set<Nanoentity> nanoentitySet) {
		this.couplingInstanceList = couplingInstanceList;
		this.nanoentitySet = nanoentitySet;
	}

	public Map<EntityPair, Map<String, Score>> getScores(final Function<String, Double> priorityProvider) {
		if (new HashSet<>(couplingInstanceList).isEmpty()) {
			throw new InvalidParameterException("userSystem needs at least 1 coupling criterion in order for gephi clusterer to work");
		}
		Map<EntityPair, Map<String, Score>> result = new HashMap<>();

		addScoresForCharacteristicsCriteria(priorityProvider, result);
		addScoresForConstraintsCriteria(priorityProvider, result);
		addScoresForProximityCriteria(priorityProvider, result);
		return result;

	}

	private Set<CouplingInstance> getCouplingInstancesByCriterionName(String criterionName) {
		return couplingInstanceList.stream().filter(ci -> criterionName.equals(ci.getCouplingCriterion().getName())).collect(Collectors.toSet());
	}

	private Set<CouplingInstance> getCouplingInstancesByType(InstanceType instanceType) {
		return couplingInstanceList.stream().filter(ci -> instanceType.equals(ci.getType())).collect(Collectors.toSet());
	}

	private Map<String, Set<CouplingInstance>> getCouplingInstancesByCouplingType(CouplingType couplingType) {
		return groupByCriterion(couplingInstanceList.stream().filter(ci -> couplingType.equals(ci.getCouplingCriterion().getType())).collect(Collectors.toSet()));
	}

	private void addScoresForProximityCriteria(final Function<String, Double> priorityProvider, final Map<EntityPair, Map<String, Score>> result) {
		Map<EntityPair, Double> lifecycleScores = new CohesiveGroupCriterionScorer()
				.getScores(getCouplingInstancesByCriterionName(CouplingCriterion.IDENTITY_LIFECYCLE));
		addScoresByCriterionToResult(result, CouplingCriterion.IDENTITY_LIFECYCLE, lifecycleScores, priorityProvider.apply(CouplingCriterion.IDENTITY_LIFECYCLE));

		Map<EntityPair, Double> semanticProximityScores = new SemanticProximityCriterionScorer()
				.getScores(getCouplingInstancesByCriterionName(CouplingCriterion.SEMANTIC_PROXIMITY));
		addScoresByCriterionToResult(result, CouplingCriterion.SEMANTIC_PROXIMITY, semanticProximityScores, priorityProvider.apply(CouplingCriterion.SEMANTIC_PROXIMITY));

		Map<EntityPair, Double> responsibilityScores = new CohesiveGroupCriterionScorer()
				.getScores(getCouplingInstancesByCriterionName(CouplingCriterion.SHARED_OWNER));
		addScoresByCriterionToResult(result, CouplingCriterion.SHARED_OWNER, responsibilityScores, priorityProvider.apply(CouplingCriterion.SHARED_OWNER));

		// latency
		Map<EntityPair, Double> latencyScores = new CohesiveGroupCriterionScorer()
				.getScores(getCouplingInstancesByType(InstanceType.LATENCY_USE_CASE));
		addScoresByCriterionToResult(result, CouplingCriterion.LATENCY, latencyScores, priorityProvider.apply(CouplingCriterion.LATENCY));

		// security contextuality
		Map<EntityPair, Double> securityContextualityScores = new CohesiveGroupCriterionScorer()
				.getScores(getCouplingInstancesByCriterionName(CouplingCriterion.SECURITY_CONTEXUALITY));
		addScoresByCriterionToResult(result, CouplingCriterion.SECURITY_CONTEXUALITY, securityContextualityScores, priorityProvider.apply(CouplingCriterion.SECURITY_CONTEXUALITY));

	}

	private void addScoresForCharacteristicsCriteria(final Function<String, Double> priorityProvider,
			final Map<EntityPair, Map<String, Score>> result) {
		Map<String, Map<EntityPair, Double>> scoresByCriterion = new CharacteristicsCriteriaScorer()
				.getScores(getCouplingInstancesByCouplingType(CouplingType.COMPATIBILITY));
		for (Entry<String, Map<EntityPair, Double>> distanceScores : scoresByCriterion.entrySet()) {
			addScoresByCriterionToResult(result, distanceScores.getKey(), distanceScores.getValue(), priorityProvider.apply(distanceScores.getKey()));
		}
	}

	private void addScoresForConstraintsCriteria(final Function<String, Double> priorityProvider, final Map<EntityPair, Map<String, Score>> result) {
		Map<EntityPair, Double> securityScores = new SeparatedGroupCriterionScorer()
				.getScores(getCouplingInstancesByCriterionName(CouplingCriterion.SECURITY_CONSTRAINT));
		addScoresByCriterionToResult(result, CouplingCriterion.SECURITY_CONSTRAINT, securityScores, priorityProvider.apply(CouplingCriterion.SECURITY_CONSTRAINT));

		Map<EntityPair, Double> predefinedServiceScores = new ExclusiveGroupCriterionScorer(nanoentitySet)
				.getScores(getCouplingInstancesByCriterionName(CouplingCriterion.PREDEFINED_SERVICE));
		addScoresByCriterionToResult(result, CouplingCriterion.PREDEFINED_SERVICE, predefinedServiceScores, priorityProvider.apply(CouplingCriterion.PREDEFINED_SERVICE));

		Map<EntityPair, Double> consistencyConstraintScores = new CohesiveGroupCriterionScorer()
				.getScores(getCouplingInstancesByCriterionName(CouplingCriterion.CONSISTENCY_CONSTRAINT));
		addScoresByCriterionToResult(result, CouplingCriterion.CONSISTENCY_CONSTRAINT, consistencyConstraintScores,
				priorityProvider.apply(CouplingCriterion.CONSISTENCY_CONSTRAINT));
	}

	private void addScoresByCriterionToResult(final Map<EntityPair, Map<String, Score>> result, final String couplingCriterionName, final Map<EntityPair, Double> scores,
			final Double priority) {
		for (Entry<EntityPair, Double> nanoentityScore : scores.entrySet()) {
			addScoresToResult(result, nanoentityScore.getKey(), couplingCriterionName, nanoentityScore.getValue(), priority);
		}
	}

	private void addScoresToResult(final Map<EntityPair, Map<String, Score>> result, final EntityPair nanoentities, final String criterionName, final double score,
			final double priority) {
		if (nanoentities.nanoentityA.equals(nanoentities.nanoentityB)) {
			log.warn("score on same nanoentity ignored. Nanoentity: {}, Score: {}, Criterion: {}", nanoentities.nanoentityA, score, criterionName);
			return;
		}

		if (result.get(nanoentities) == null) {
			result.put(nanoentities, new HashMap<>());
		}
		result.get(nanoentities).put(criterionName, new Score(score, priority));
	}

	private Map<String, Set<CouplingInstance>> groupByCriterion(final Set<CouplingInstance> instances) {
		Map<String, Set<CouplingInstance>> instancesByCriterion = new HashMap<>();
		for (CouplingInstance instance : instances) {
			String ccName = instance.getCouplingCriterion().getName();
			if (instancesByCriterion.get(ccName) == null) {
				instancesByCriterion.put(ccName, new HashSet<CouplingInstance>());
			}
			instancesByCriterion.get(ccName).add(instance);
		}
		return instancesByCriterion;
	}

}
