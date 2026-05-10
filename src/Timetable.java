import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Timetable {

    // Структура:
    // HashMap — ключ это день недели, значение — TreeMap
    // TreeMap — ключ это время, значение — список тренировок
    // List — потому что в одно время может быть несколько тренировок
    private HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        timetable = new HashMap<>();
    }

    // Добавляем тренировку в расписание
    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        // Если для этого дня ещё нет TreeMap — создаём его
        if (!timetable.containsKey(day)) {
            timetable.put(day, new TreeMap<>());
        }

        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(day);

        // Если для этого времени ещё нет списка — создаём его
        if (!dayMap.containsKey(time)) {
            dayMap.put(time, new ArrayList<>());
        }

        dayMap.get(time).add(trainingSession);
    }

    // Получаем все тренировки за день, отсортированные по времени
    // Сложность O(1) — HashMap.get по дню
    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);

        // Если дня нет в расписании — возвращаем пустой список
        if (dayMap == null) {
            return new ArrayList<>();
        }

        // Собираем все тренировки в порядке времени
        // navigableKeySet() возвращает ключи TreeMap уже в отсортированном виде
        List<TrainingSession> result = new ArrayList<>();
        for (TimeOfDay time : dayMap.navigableKeySet()) {
            result.addAll(dayMap.get(time));
        }

        return result;
    }

    // Получаем тренировки за конкретный день и конкретное время
    // Сложность O(log n) — TreeMap.get по времени
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

    // Считаем сколько тренировок у каждого тренера и сортируем по убыванию
    public List<CounterOfTrainings> getCountByCoaches() {

        // Шаг 1: считаем тренировки каждого тренера через HashMap
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

        // Шаг 2: перекладываем из HashMap в список CounterOfTrainings
        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : counters.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        // Шаг 3: сортируем по убыванию количества тренировок
        Collections.sort(result, new Comparator<CounterOfTrainings>() {
            @Override
            public int compare(CounterOfTrainings a, CounterOfTrainings b) {
                return Integer.compare(b.getCount(), a.getCount()); // b перед a = убывание
            }
        });

        return result;
    }
}