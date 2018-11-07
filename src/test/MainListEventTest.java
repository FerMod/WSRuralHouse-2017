package test;

import java.util.ArrayList;
import java.util.Arrays;

import domain.event.ListValueChangeListener;
import domain.observer.ObservedList;

public class MainListEventTest {
	
	public static void main(String[] args) {
		
		ObservedList<Integer> observedList = new ObservedList<>(new ArrayList<>());
		observedList.registerListener(new ListValueChangeListener() {
			@Override
			public void onValueAdded(Object obj) {
				System.out.println("Added Single:" + obj);
			}
			@Override
			public void onMultipleValuesAdded(Object[] objects) {
				for (int i = 0; i < objects.length; i++) {
					System.out.println("Added All:" + objects[i]);
				}
			}
		});

		observedList.add(10);
		observedList.add(12);
		observedList.add(-42);

		observedList.addAll(Arrays.asList(3, -5, 7));
		
	}
}
