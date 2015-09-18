package ch.hsr.servicestoolkit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.hsr.servicestoolkit.model.Model;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EngineServiceAppication.class)
public class EngineServiceTest {

	@Autowired
	private EngineService service;

	@Test
	public void testCreateModel() {
		service.createModel(new Model(), "first");
		Model result = service.getModel("first");

		assertEquals("first", result.getName());
	}

	@Test
	public void testCreateModelSameName() {
		Model model = new Model();
		model.setName("second");
		service.createModel(model, "second");
		Model result = service.getModel("second");

		assertEquals("second", result.getName());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateModelDifferentName() {
		Model model = new Model();
		model.setName("whateverName");
		service.createModel(model, "third");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateModelNoName() {
		Model model = new Model();
		service.createModel(model, "");
	}

	@Test
	public void testCreateModelNoParamName() {
		Model model = new Model();
		model.setName("fourth");
		service.createModel(model, "");
		Model result = service.getModel("fourth");

		assertEquals("fourth", result.getName());
	}
}
