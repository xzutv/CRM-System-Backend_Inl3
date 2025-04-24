package se.yrgo.services.diary;

import java.util.*;

import se.yrgo.domain.Action;

public class DiaryManagementServiceMockImpl implements DiaryManagementService {

	private Set<Action> allActions = new HashSet<Action>();

	@Override
	public void recordAction(Action action) {
		allActions.add(action);
	}

	// Hint:
	// Create a list<Action>
	// In the for each loop going through the list use this condition:
	// "if(action.getOwningUser().equals(requiredUser) && !action.isComplete())" to
	// add a new action to the list.
	public List<Action> getAllIncompleteActions(String requiredUser) {

		List<Action> actions = new ArrayList<>();

		for (Action action : allActions) {
			if (action.getOwningUser().equals(requiredUser) && !action.isComplete()) {
				actions.add(action);
			}
		}
		return actions;
	}

}
