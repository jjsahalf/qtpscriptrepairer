package repairer;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class RepairerTest {

	private Repairer repairer;
	
	@Before
	public void setUp() {
		File testCase = new File("src\\repairer\\new.xml.txt");
		File efg = new File("src\\repairer\\Notepad2.EFG.xml");
		repairer = new Repairer(new EventSequenceReader(testCase), new EFGAnalyzer(efg));
	}
	
	@Test
	public void testGetFirstValidEventIndex() {
		List<Integer> eventSequence1 = Arrays.asList(-1, 48, -2, 326);
		repairer.setEventSequence(eventSequence1);
		assertFalse(repairer.getFirstValidEventIndex());
		
		List<Integer> eventSequence2 = Arrays.asList(-1, 39, 28, -2, 68);
		repairer.setEventSequence(eventSequence2);
		assertTrue(repairer.getFirstValidEventIndex());
		assertEquals(2, repairer.getCurrentIndex());
	}
	
	@Test
	public void testRepairByDelete() {
		List<Integer> allInvalidSequence = Arrays.asList(-1, 39, -1, 52, -1);
		repairer.setEventSequence(allInvalidSequence);
		assertEquals("[]", repairer.getRepairedEventSequence().toString());
		
		List<Integer> eventSequence1 = Arrays.asList(-1, 16, 17, 16, 18, -1, -1);
		repairer.setEventSequence(eventSequence1);
		assertEquals("[e16, e17, e16, e18]", repairer.getRepairedEventSequence().toString());
	}
	
	@Test
	public void testRepairByInsert() {
		List<Integer> eventSequence2 = Arrays.asList(-1, 16, -1, -1, 17, 18, -1, -1);
		repairer.setEventSequence(eventSequence2);
		assertEquals("[e16, e17, e16, e18]", repairer.getRepairedEventSequence().toString());
		
		//move "new" from "file" to "action"
		List<Integer> eventSequence3 = Arrays.asList(16, 21);
		repairer.setEventSequence(eventSequence3);
		assertEquals("[e16, e20, e21]", repairer.getRepairedEventSequence().toString());
	}
}
