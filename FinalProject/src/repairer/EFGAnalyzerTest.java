package repairer;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class EFGAnalyzerTest {

	private EFGAnalyzer analyzer;
	
	@Before
	public void setUp() {
		File file = new File("src\\repairer\\Notepad2.EFG.xml");
		analyzer = new EFGAnalyzer(file);
	}
	
	@Test
	public void testIsEventExisted() {
		assertTrue(analyzer.isEventExisted(0));
		assertTrue(analyzer.isEventExisted(2));
		assertTrue(analyzer.isEventExisted(36));
		assertFalse(analyzer.isEventExisted(-1));
		assertFalse(analyzer.isEventExisted(37));
		assertFalse(analyzer.isEventExisted(57));
	}
	
	@Test
	public void testIsEdgeExisted() {
		assertTrue(analyzer.isEdgeExisted(0, 0));
		assertTrue(analyzer.isEdgeExisted(1, 5));
		assertTrue(analyzer.isEdgeExisted(5, 7));
		assertTrue(analyzer.isEdgeExisted(36, 36));
		assertTrue(analyzer.isEdgeExisted(17, 16));
		assertTrue(analyzer.isEdgeExisted(18, 16));
		assertTrue(analyzer.isEdgeExisted(19, 16));
		assertFalse(analyzer.isEdgeExisted(1, 6));
		assertFalse(analyzer.isEdgeExisted(5, 9));
		assertFalse(analyzer.isEdgeExisted(36, 35));
	}
	
	@Test
	public void testGetSucessors() {
		checkAllSucessors(0, 9);
		checkAllSucessors(35, 10);
		checkAllSucessors(34, 10);
	}
	
	private void checkAllSucessors(int id, int size) {
		List<Integer> sucessors1 = analyzer.getSucessors(id);
		assertEquals(size, sucessors1.size());
		for(int i = 0; i < 36; i++) {
			if(sucessors1.contains(i)) {
				assertTrue(analyzer.isEdgeExisted(id, i));
			} else {
				assertFalse(analyzer.isEdgeExisted(id, i));
			}
		}
	}
}
