package org.openbakery.racecontrol.data;

import java.util.List;

import org.openbakery.racecontrol.data.Driver;
import org.openbakery.racecontrol.data.RaceEntry;
import org.openbakery.racecontrol.data.Result;
import org.testng.annotations.Test;

public class RaceEntryResultTest {

	@Test
	public void qualifyingResultTest() {
		RaceEntry entry = new RaceEntry();
		
		entry.addResult(createQualifyingResult(10), createDriver(0));
		entry.addResult(createQualifyingResult(11), createDriver(1));
		
		List<Result> results = entry.getResults();
		assert 2  == results.size();
		assert results.get(0).getBestLapTime() == 10;
		assert results.get(1).getBestLapTime() == 11;
		
	}
	
	@Test
	public void qualifyingResultTestReverse() {
		RaceEntry entry = new RaceEntry();
		
		entry.addResult(createQualifyingResult(11), createDriver(0));
		entry.addResult(createQualifyingResult(10), createDriver(1));
		entry.addResult(createQualifyingResult(9), createDriver(2));
		
		List<Result> results = entry.getResults();
		assert 3  == results.size();
		assert results.get(0).getBestLapTime() == 9;
		assert results.get(1).getBestLapTime() == 10;
		assert results.get(2).getBestLapTime() == 11;
		assert results.get(0).getPosition() == 1;
		assert results.get(1).getPosition() == 2;
		assert results.get(2).getPosition() == 3;

	}
	
	@Test
	public void qualifyingResultTestSame() {
		RaceEntry entry = new RaceEntry();
		
		Driver first = createDriver(0);
		Driver second = createDriver(1);
		entry.addResult(createQualifyingResult(10), first);
		entry.addResult(createQualifyingResult(10), second);
		
		List<Result> results = entry.getResults();
		assert 2  == results.size();
		
		assert first.getResult().getPosition() == 1;
		assert second.getResult().getPosition() == 2;
	}
	
	private Result createQualifyingResult(int time) {
		Result result = new Result();
		result.setBestLapTime(time);
		result.setRaceTime(0);
		result.setPosition(1);
		result.setLapsCompleted(0);
		return result;
	}
	
	private Driver createDriver(int id) {
		Driver driver = new Driver(0);
		driver.setConnectionId(0);
		driver.setPlayerId(id);
		driver.setName(Integer.toString(id));
		return driver;
	}
}
