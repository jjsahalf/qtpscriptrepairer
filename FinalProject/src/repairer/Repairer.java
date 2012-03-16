package repairer;

import java.util.ArrayList;
import java.util.List;

public class Repairer {

	private List<Integer> eventSequence;
	private EFGAnalyzer analyzer;
	//cursor of the last valid event
	private int currentIndex;
	private List<Integer> repairedSequence;
	
	public Repairer(EventSequenceReader reader, EFGAnalyzer analyzer) {
		eventSequence = reader.readEventSequence();
		this.analyzer = analyzer;
	}
	
	//this insecure method is just for test
	protected void setEventSequence(List<Integer> eventSequence) {
		this.eventSequence = eventSequence;
	}
	
	//this insecure method is just for test
	protected int getCurrentIndex() {
		return currentIndex;
	}
	
	public List<String> getRepairedEventSequence() {
		repair();
		List<String> result = new ArrayList<String>();
		for(int i : repairedSequence) {
			result.add("e" + i);
		}
		return result;
	}
	
	private List<Integer> repair() {
		repairedSequence = new ArrayList<Integer>();
		if(!getFirstValidEventIndex()) {
			return repairedSequence;
		}
		repairedSequence.add(eventSequence.get(currentIndex));
		while(currentIndex < eventSequence.size() - 1) {
			int currentEventId = eventSequence.get(currentIndex);
			int nextEventId = eventSequence.get(currentIndex + 1);
			if(!analyzer.isEventExisted(nextEventId) 
					|| !analyzer.isEdgeExisted(currentEventId, nextEventId)) {
				if(!repairBySkip() && !repairByInsert()) {
					break;
				}
			} else {
				currentIndex++;
				repairedSequence.add(eventSequence.get(currentIndex));
			}
		}
		return repairedSequence;
	}
	
	protected boolean getFirstValidEventIndex() {
		currentIndex = 0;
		while(currentIndex < eventSequence.size()) {
			if(analyzer.isEventExisted(eventSequence.get(currentIndex))) {
				return true;
			}
			currentIndex++;
		}
		return false;
	}
	
	//the next event of current event is deleted
	private boolean repairBySkip() {
		int previousEventId = eventSequence.get(currentIndex);
		for(int nextValidIndex = currentIndex + 2; 
				nextValidIndex < eventSequence.size(); nextValidIndex++) {
			int nextValidEventId = eventSequence.get(nextValidIndex);
			if(analyzer.isEventExisted(nextValidEventId) 
					&& analyzer.isEdgeExisted(previousEventId, nextValidEventId)) {
				currentIndex = nextValidIndex;
				repairedSequence.add(nextValidEventId);
				return true;
			}
		}
		return false;
	}
	
	//the next event of current event is not available from current event
	private boolean repairByInsert() {
		int previousEventId = eventSequence.get(currentIndex);
		for(int nextValidIndex = currentIndex + 1; 
				nextValidIndex < eventSequence.size(); nextValidIndex++) {
			List<Integer> sucessorsOfPrevious = analyzer.getSucessors(previousEventId);
			int nextValidEventId = eventSequence.get(nextValidIndex);
			if(analyzer.isEventExisted(nextValidEventId)) {
				for(int sucessor : sucessorsOfPrevious) {
					if(analyzer.isEdgeExisted(previousEventId, sucessor) 
							&& analyzer.isEdgeExisted(sucessor, nextValidEventId)) {
						repairedSequence.add(sucessor);
						repairedSequence.add(nextValidEventId);
						currentIndex = nextValidIndex;
						return true;
					}
				}
			}
		}
		currentIndex = eventSequence.size();
		return false;
	}
}
