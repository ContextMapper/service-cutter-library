package ch.hsr.servicecutter.api.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SolverResult {

	private Set<Service> services;
	private List<ServiceRelation> relations;
	private Map<String, List<String>> useCaseResponsibility;

	public SolverResult() {
		this.services = Collections.emptySet();
		this.relations = Collections.emptyList();
		this.useCaseResponsibility = Collections.emptyMap();
	}

	public Set<Service> getServices() {
		return services;
	}

	public void setServices(Set<Service> services) {
		this.services = services;
	}

	public void setUseCaseResponsibility(final Map<String, List<String>> responsibilities) {
		this.useCaseResponsibility = responsibilities;
	}

	public Map<String, List<String>> getUseCaseResponsibility() {
		return useCaseResponsibility;
	}

	public List<ServiceRelation> getRelations() {
		return relations;
	}

	public void setRelations(final List<ServiceRelation> relations) {
		this.relations = relations;
	}
}
