import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TimetableTest {

    // --- Тесты из прекода ---

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // За понедельник — одно занятие
        List<TrainingSession> monday = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, monday.size());

        // За вторник — ничего нет
        List<TrainingSession> tuesday = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertEquals(0, tuesday.size());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdult = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));
        timetable.addNewTrainingSession(thursdayAdult);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChild = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChild = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChild = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChild);
        timetable.addNewTrainingSession(thursdayChild);
        timetable.addNewTrainingSession(saturdayChild);

        // Понедельник — 1 занятие
        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());

        // Четверг — 2 занятия, сначала 13:00 потом 20:00
        List<TrainingSession> thursday = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursday.size());
        assertEquals(13, thursday.get(0).getTimeOfDay().getHours());
        assertEquals(20, thursday.get(1).getTimeOfDay().getHours());

        // Вторник — пусто
        assertEquals(0, timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession session = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(session);

        // Понедельник 13:00 — одно занятие
        assertEquals(1, timetable
                .getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0))
                .size());

        // Понедельник 14:00 — пусто
        assertEquals(0, timetable
                .getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0))
                .size());
    }

    // --- Новые тесты ---

    @Test
    void testEmptyTimetableReturnsEmptyList() {
        // Пустое расписание не должно падать с ошибкой
        Timetable timetable = new Timetable();
        List<TrainingSession> result = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testTwoSessionsAtSameTime() {
        // В одно время могут быть две разные группы
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Иванов", "Иван", "Иванович");

        Group childGroup = new Group("Гимнастика детская", Age.CHILD, 60);
        Group adultGroup = new Group("Гимнастика взрослая", Age.ADULT, 60);

        timetable.addNewTrainingSession(new TrainingSession(childGroup, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(adultGroup, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));

        // По дню — 2 занятия
        assertEquals(2, timetable.getTrainingSessionsForDay(DayOfWeek.WEDNESDAY).size());

        // По дню и времени — тоже 2
        assertEquals(2, timetable
                .getTrainingSessionsForDayAndTime(DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0))
                .size());
    }

    @Test
    void testSessionsOrderedByTime() {
        // Добавляем в обратном порядке — должны вернуться в хронологическом
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

        assertEquals(8,  sessions.get(0).getTimeOfDay().getHours());
        assertEquals(14, sessions.get(1).getTimeOfDay().getHours());
        assertEquals(20, sessions.get(2).getTimeOfDay().getHours());
    }

    @Test
    void testGetCountByCoachesEmpty() {
        Timetable timetable = new Timetable();
        List<CounterOfTrainings> result = timetable.getCountByCoaches();
        assertNotNull(result);
        assertEquals(0, result.size());
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
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getCount());
    }

    @Test
    void testGetCountByCoachesSortedDescending() {
        Timetable timetable = new Timetable();
        Group group = new Group("Фитнес", Age.ADULT, 60);

        Coach busyCoach = new Coach("Занятой", "Борис", "Борисович");
        Coach lazyCoach = new Coach("Редкий",  "Виктор", "Викторович");

        // busyCoach — 3 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group, busyCoach, DayOfWeek.MONDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, busyCoach, DayOfWeek.TUESDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, busyCoach, DayOfWeek.WEDNESDAY, new TimeOfDay(9, 0)));

        // lazyCoach — 1 тренировка
        timetable.addNewTrainingSession(new TrainingSession(group, lazyCoach, DayOfWeek.FRIDAY, new TimeOfDay(18, 0)));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        // Первым должен быть тот, у кого больше тренировок
        assertEquals(3, result.get(0).getCount());
        assertEquals(1, result.get(1).getCount());
    }
}