package repairer;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class EventSequenceReaderTest {

	@Test
	public void reader() {
		File file = new File("src\\repairer\\new.xml.txt");
		EventSequenceReader esr = new EventSequenceReader(file);
		List<Integer> eventSequence = esr.readEventSequence();
		assertEquals("[-1, 16, 17, 16, 18, 16, 19, -1, -1]", eventSequence.toString());
	}
}
