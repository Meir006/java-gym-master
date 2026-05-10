package ru.yandex.practicum.gym;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Timetable {

    private HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        timetable = new HashMap<>();
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        if (!timetable.containsKey(day)) {
            timetable.put(day, new TreeMap<>());
        }

        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(day);

        if (!dayMap.containsKey(time)) {
            dayMap.put(time, new ArrayList<>());
        }

        dayMap.get(time).add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);

        if (dayMap == null) {
            return new ArrayList<>();
        }

        List<TrainingSession> result = new ArrayList<>();
        for (TimeOfDay time : dayMap.navigableKeySet()) {
            result.addAll(dayMap.get(time));
        }
        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);

        if (dayMap == null) {
            return new ArrayList<>();
        }

        List<TrainingSession> sessions = dayMap.get(timeOfDay);

        if (sessions == null) {
            return new ArrayList<>();
        }

        return sessions;
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        HashMap<Coach, Integer> counters = new HashMap<>();

        for (TreeMap<TimeOfDay, List<TrainingSession>> dayMap : timetable.values()) {
            for (List<TrainingSession> sessions : dayMap.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    if (!counters.containsKey(coach)) {
                        counters.put(coach, 0);
                    }
                    counters.put(coach, counters.get(coach) + 1);
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : counters.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        Collections.sort(result, new Comparator<CounterOfTrainings>() {
            @Override
            public int compare(CounterOfTrainings a, CounterOfTrainings b) {
                return Integer.compare(b.getCount(), a.getCount());
            }
        });

        return result;
    }
}
