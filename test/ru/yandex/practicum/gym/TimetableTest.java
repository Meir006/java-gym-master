package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(singleTrainingSession);

        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        Assertions.assertEquals(0, timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));
        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());

        List<TrainingSession> thursday = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thursday.size());
        Assertions.assertEquals(13, thursday.get(0).getTimeOfDay().getHours());
        Assertions.assertEquals(20, thursday.get(1).getTimeOfDay().getHours());

        Assertions.assertEquals(0, timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(singleTrainingSession);

        Assertions.assertEquals(1, timetable
                .getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0)).size());
        Assertions.assertEquals(0, timetable
                .getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)).size());
    }

    @Test
    void testEmptyTimetableReturnsEmptyList() {
        Timetable timetable = new Timetable();
        Assertions.assertNotNull(timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY));
        Assertions.assertEquals(0, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
    }

    @Test
    void testTwoSessionsAtSameTime() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Иванов", "Иван", "Иванович");

        timetable.addNewTrainingSession(new TrainingSession(
                new Group("Детская", Age.CHILD, 60), coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(
                new Group("Взрослая", Age.ADULT, 60), coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));

        Assertions.assertEquals(2, timetable.getTrainingSessionsForDay(DayOfWeek.WEDNESDAY).size());
        Assertions.assertEquals(2, timetable
                .getTrainingSessionsForDayAndTime(DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)).size());
    }

    @Test
    void testSessionsOrderedByTime() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Петров", "Пётр", "Петрович");
        Group group = new Group("Йога", Age.ADULT, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(8, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(14, 0)));

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDay(DayOfWeek.FRIDAY);
        Assertions.assertEquals(8, sessions.get(0).getTimeOfDay().getHours());
        Assertions.assertEquals(14, sessions.get(1).getTimeOfDay().getHours());
        Assertions.assertEquals(20, sessions.get(2).getTimeOfDay().getHours());
    }

    @Test
    void testGetCountByCoachesEmpty() {
        Timetable timetable = new Timetable();
        Assertions.assertNotNull(timetable.getCountByCoaches());
        Assertions.assertEquals(0, timetable.getCountByCoaches().size());
    }

    @Test
    void testGetCountByCoachesSingleCoach() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Сидоров", "Алексей", "Михайлович");
        Group group = new Group("Акробатика", Age.CHILD, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(2, result.get(0).getCount());
    }

    @Test
    void testGetCountByCoachesSortedDescending() {
        Timetable timetable = new Timetable();
        Group group = new Group("Фитнес", Age.ADULT, 60);

        Coach busyCoach = new Coach("Занятой", "Борис", "Борисович");
        Coach lazyCoach = new Coach("Редкий", "Виктор", "Викторович");

        timetable.addNewTrainingSession(new TrainingSession(group, busyCoach,
                DayOfWeek.MONDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, busyCoach,
                DayOfWeek.TUESDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, busyCoach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, lazyCoach,
                DayOfWeek.FRIDAY, new TimeOfDay(18, 0)));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();
        Assertions.assertEquals(3, result.get(0).getCount());
        Assertions.assertEquals(1, result.get(1).getCount());
    }
}
