package ch.hsr.servicecutter.solver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;


public class SolverConfigurationTest {

	@Test
	public void testNullConfiguration() {
		Assertions.assertThrows(InvalidParameterException.class, () -> {
			SolverConfiguration config = new SolverConfiguration();
			config.setPriority("TestCriterion", null);
		});
	}

	@Test
	public void testEmptyConfig() {
		SolverConfiguration config = new SolverConfiguration();
		assertEquals(SolverPriority.S, config.getPriorityForCouplingCriterion("sameEntity"));
	}

	@Test
	public void testRealConfig() {
		SolverConfiguration config = new SolverConfiguration();
		config.setPriority("sameEntity", SolverPriority.M);
		assertEquals(SolverPriority.M, config.getPriorityForCouplingCriterion("sameEntity"));
	}

}
