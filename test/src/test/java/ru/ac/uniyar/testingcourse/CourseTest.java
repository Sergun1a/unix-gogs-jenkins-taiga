package ru.ac.uniyar.testingcourse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class CourseTest {
    private Course course;
    /**
     * Размер курса в тестах
     */
    private int maxStudents = 10;
    /**
     * Размер очереди ожидания в тестах
     */
    private int waitingListSize = 5;

    @BeforeEach
    public void init() {
        course = new Course(maxStudents);
    }

    /**
     * Функция возвращает заполненный курс
     *
     * @param id                       - id студента
     * @param must_contain_received_id - должен ли переданный студент содержаться в курсе
     * @param need_waiting_list        - нужно ли добавлять людей в список ожидания
     * @return Course
     */
    private void fullCourse(int id, boolean must_contain_received_id, boolean need_waiting_list) {
        for (int i = 1; i < maxStudents; i++) {
            course.enroll(id + i);
        }
        if (must_contain_received_id) {
            course.enroll(id);
        } else {
            course.enroll(id + maxStudents);
        }

        if (need_waiting_list) {
            for (int i = 1; i <= waitingListSize; i++) {
                course.enroll(id + i + maxStudents);
            }
        }
    }

    private static int[] testData() {
        return new int[]{1, 20, 347};
    }

    // тесты на корректность работы внутренних методов и корректность первичных данных курса.

    /**
     * Проверка курса на отрицательное количество студентов доступных для записи
     */
    @Order(1)
    @Test
    void positiveMaxStudents() {
        assertThat(course.getMaxStudents()).withFailMessage("Отрицательное количество студентов доступных для записи в курс").isGreaterThan(0);
    }


    /**
     * Проверка метода getEnrolledList на полностью заполненном курсе.
     *
     * @param id - id студента
     */
    @Order(2)
    @ParameterizedTest
    @MethodSource("testData")
    void courseFullyEnrolledList(int id) {
        fullCourse(id, true, false);
        assertThat(course.getEnrollmentList().size()).withFailMessage("Количество записанных студентов в getEnrollmentList не соответствует реальному").isEqualTo(maxStudents);
        assertThat(course.getEnrollmentList().contains(id)).withFailMessage("getEnrollmentList не содержит информацию о записанном студенте с id = " + id).isEqualTo(true);
    }

    /**
     * Проверка метода getEnrolledList на пустом курсе.
     */
    @Order(3)
    @Test
    void courseEmptyEnrolledList() {
        assertThat(course.getEnrollmentList().size()).withFailMessage("Количество записанных студентов в getEnrollmentList не соответствует реальному").isEqualTo(0);
        assertThat(course.getEnrollmentList().contains(1)).withFailMessage("getEnrollmentList содержит информацию о студенте с id = 1, хотя никто ещё не записывался").isEqualTo(false);
    }

    /**
     * Проверка метода getEnrolledList на частично заполненном курсе.
     *
     * @param id - id студента
     */
    @Order(4)
    @ParameterizedTest
    @MethodSource("testData")
    void courseSmallEnrolledList(int id) {
        course.enroll(id);
        assertThat(course.getEnrollmentList().size()).withFailMessage("Количество записанных студентов в getEnrollmentList не соответствует реальному").isEqualTo(1);
        assertThat(course.getEnrollmentList().contains(id)).withFailMessage("getEnrollmentList не содержит информацию о записанном студенте с id = " + id).isEqualTo(true);
    }

    /**
     * Проверка метода getWaitingList на полностью заполненном курсе c очередью.
     *
     * @param id - id студента
     */
    @Order(5)
    @ParameterizedTest
    @MethodSource("testData")
    void courseWaitingList(int id) {
        fullCourse(id, false, true);
        assertThat(course.getWaitingList().size()).withFailMessage("Количество студентов в getWaitingList не соответствует реальному").isEqualTo(waitingListSize);
        assertThat(course.getWaitingList().contains(id + maxStudents + 1)).withFailMessage("getWaitingList не содержит информацию о записанном студенте").isEqualTo(true);
    }

    /**
     * Проверка метода getWaitingList на пустом курсе.
     */
    @Order(6)
    @Test
    void emptyCourseWaitingList() {
        assertThat(course.getWaitingList().size()).withFailMessage("Количество студентов в getWaitingList не соответствует реальному").isEqualTo(0);
        assertThat(course.getEnrollmentList().contains(1)).withFailMessage("getWaitingList содержит информацию о студенте с id = 1, хотя в списке никого нет").isEqualTo(false);
    }

    /**
     * Проверка метода getWaitingList на частично заполненном курсе.
     *
     * @param id - id студента
     */
    @Order(7)
    @ParameterizedTest
    @MethodSource("testData")
    void smallCourseWaitingList(int id) {
        course.enroll(id);
        assertThat(course.getWaitingList().size()).withFailMessage("Количество студентов в getWaitingList не соответствует реальному").isEqualTo(0);
        assertThat(course.getWaitingList().contains(id)).withFailMessage("getWaitingList содержит информацию о записанном студенте с id = " + id + ", хотя в очереди никого нет").isEqualTo(false);
    }

    /**
     * Проверка метода isFullyEnrolled на полностью заполненном курсе.
     *
     * @param id - id студента
     */
    @Order(8)
    @ParameterizedTest
    @MethodSource("testData")
    void courseFullEnrolled(int id) {
        fullCourse(id, true, false);
        assertThat(course.isFullyEnrolled()).withFailMessage("Курс полностью заполнен, но метод isFullyEnrolled вернул не true").isEqualTo(true);
    }

    /**
     * Проверка метода isFullyEnrolled на пустом курсе.
     */
    @Order(9)
    @Test
    void emptyCourseFullEnrolled() {
        assertThat(course.isFullyEnrolled()).withFailMessage("Курс пуст, но метод isFullyEnrolled вернул true").isEqualTo(false);
    }

    /**
     * Проверка метода isFullyEnrolled на неполностью заполненном курсе.
     *
     * @param id - id студента
     */
    @Order(10)
    @ParameterizedTest
    @MethodSource("testData")
    void smallCourseFullEnrolled(int id) {
        if (maxStudents > 1) course.enroll(id);
        assertThat(course.isFullyEnrolled()).withFailMessage("Курс не полностью заполнен, но метод isFullyEnrolled вернул true").isEqualTo(false);
    }

    /**
     * Проверка метода hasWaitingList на полностью заполненном курсе c очередью ожидания.
     *
     * @param id - id студента
     */
    @Order(11)
    @ParameterizedTest
    @MethodSource("testData")
    void courseHasWaitingList(int id) {
        fullCourse(id, true, true);
        assertThat(course.hasWaitingList()).withFailMessage("Курс имеет очередь ожидания, но hasWaitingList вернул false").isEqualTo(true);
    }

    /**
     * Проверка метода hasWaitingList на полностью заполненном курсе без очереди ожидания.
     *
     * @param id - id студента
     */
    @Order(12)
    @ParameterizedTest
    @MethodSource("testData")
    void fullCourseNoWaitingList(int id) {
        fullCourse(id, false, false);
        assertThat(course.hasWaitingList()).withFailMessage("Курс не имеет очередь ожидания, но hasWaitingList вернул true").isEqualTo(false);
    }

    /**
     * Проверка метода hasWaitingList на неполностью заполненном курсе без очереди ожидания.
     *
     * @param id - id студента
     */
    @Order(13)
    @ParameterizedTest
    @MethodSource("testData")
    void smallCourseNoWaitingList(int id) {
        course.enroll(id);
        assertThat(course.hasWaitingList()).withFailMessage("Курс не имеет очередь ожидания, но hasWaitingList вернул true").isEqualTo(false);
    }

    /**
     * Проверка метода hasWaitingList на пустом курсе.
     *
     * @param id - id студента
     */
    @Order(14)
    @ParameterizedTest
    @MethodSource("testData")
    void emptyCourseNoWaitingList(int id) {
        assertThat(course.hasWaitingList()).withFailMessage("Курс пуст, но hasWaitingList вернул true").isEqualTo(false);
    }

    // ряд тестов для незаполненного курса

    /**
     * Тест записи студента в незаполненный курс
     *
     * @param id - id студента
     */
    @ParameterizedTest
    @MethodSource("testData")
    void enrollStudentTest(int id) {
        assertThat(course.getMaxStudents()).withFailMessage("Неположительное количество студентов доступных для записи в курс").isGreaterThan(0);
        course.enroll(id);
        assertThat(course.getEnrollmentList().contains(id)).isEqualTo(true);
    }

    /**
     * Тест записи уже записанного студента в незаполненный курс
     *
     * @param id - id студента
     */
    @ParameterizedTest
    @MethodSource("testData")
    void enrollEnrolledStudentTest(int id) {
        assertThat(course.getMaxStudents()).withFailMessage("Неположительное количество студентов доступных для записи в курс").isGreaterThan(0);
        course.enroll(id);
        int enrollSize = course.getEnrollmentList().size();
        course.enroll(id);
        assertThat(course.getEnrollmentList().size()).withFailMessage("Запись уже записанного студента на курс").isEqualTo(enrollSize);
    }

    /**
     * Тест удаления уже записанного студента из незаполненного курса
     *
     * @param id - id студента
     */
    @ParameterizedTest
    @MethodSource("testData")
    void unenrollEnrolledStudentTest(int id) {
        assertThat(course.getMaxStudents()).withFailMessage("Неположительное количество студентов доступных для записи в курс").isGreaterThan(0);
        course.enroll(id);
        assertThat(course.hasWaitingList()).withFailMessage("Курс не заполнен, но имеет список ожидания").isEqualTo(false);
        course.unenroll(id);
        assertThat(course.getEnrollmentList().size()).withFailMessage("Не удалось удалить студента из курса").isEqualTo(0);
    }

    /**
     * Тест удаления студента из курса в котором он не записан
     *
     * @param id - id студента
     */
    @ParameterizedTest
    @MethodSource("testData")
    void unenrollNotEnrolledStudentTest(int id) {
        assertThat(course.getMaxStudents()).withFailMessage("Неположительное количество студентов доступных для записи в курс").isGreaterThan(0);
        course.unenroll(id);
        assertThat(course.getEnrollmentList().size()).withFailMessage("При удалении незаписанного студента произошло его добавление").isEqualTo(0);
        assertThat(course.getEnrollmentList().contains(id)).withFailMessage("При удалении незаписанного студента произошло его добавление").isEqualTo(false);
    }

    // ряд тестов для заполненного курса

    /**
     * Тест удаления студента из заполненного курса в котором он не записан
     *
     * @param id - id студента
     */
    @ParameterizedTest
    @MethodSource("testData")
    void unenrollNotEnrolledStudentFullCourseTest(int id) {
        assertThat(course.getMaxStudents()).withFailMessage("Неположительное количество студентов доступных для записи в курс").isGreaterThan(0);
        fullCourse(id, false, false);
        int defaultCourseSize = course.getEnrollmentList().size();
        course.unenroll(id);
        assertThat(course.getEnrollmentList().size()).withFailMessage("При удалении незаписанного студента произошло изменение списка записанных студентов").isEqualTo(defaultCourseSize);
        assertThat(course.getEnrollmentList().contains(id)).withFailMessage("При удалении незаписанного студента произошло его добавление в курс").isEqualTo(false);

    }


    /**
     * Тест записи уже записанного студента в заполненный курс
     *
     * @param id - id студента
     */
    @ParameterizedTest
    @MethodSource("testData")
    void enrollEnrolledStudentFullCourseTest(int id) {
        assertThat(course.getMaxStudents()).withFailMessage("Неположительное количество студентов доступных для записи в курс").isGreaterThan(0);
        fullCourse(id, true, false);
        int defaultCourseSize = course.getEnrollmentList().size();
        course.enroll(id);
        assertThat(course.getEnrollmentList().size()).withFailMessage("Запись уже записанного студента на курс").isEqualTo(defaultCourseSize);
    }


    // ряд тестов для проверки очереди ожидания

    /**
     * Тест записи студента в заполненный курс
     *
     * @param id - id студента
     */
    @ParameterizedTest
    @MethodSource("testData")
    void enrollNotEnrolledStudentFullCourseTest(int id) {
        assertThat(course.getMaxStudents()).withFailMessage("Неположительное количество студентов доступных для записи в курс").isGreaterThan(0);
        fullCourse(id, false, false);
        assertThat(course.isFullyEnrolled()).withFailMessage("Курс не заполнен, хотя должен был быть заполнен").isEqualTo(true);
        course.enroll(id);
        assertThat(course.hasWaitingList()).withFailMessage("Курс заполнен, но не имеет списка ожидания").isEqualTo(true);
        assertThat(course.getWaitingList().contains(id)).withFailMessage("При записи нового студента" +
                " в заполненный курс он не попал в список ожидающих").isEqualTo(true);

    }

    /**
     * Тест записи студента в заполненный курс, если кто-то из записанных ранее студентов покинул курс.
     *
     * @param id - id студента
     */
    @ParameterizedTest
    @MethodSource("testData")
    void enrollStudentFromWaitingListTest(int id) {
        assertThat(course.getMaxStudents()).withFailMessage("Неположительное количество студентов доступных для записи в курс").isGreaterThan(0);
        fullCourse(id, false, false);
        assertThat(course.isFullyEnrolled()).withFailMessage("Курс не заполнен, хотя должен был быть заполнен").isEqualTo(true);
        course.enroll(id);
        assertThat(course.hasWaitingList()).withFailMessage("Курс заполнен, но при добавлении нового студента не был создан список ожидания").isEqualTo(true);
        int firstWaitingListStudentId = course.getWaitingList().get(0);
        assertThat(course.getEnrollmentList().size()).withFailMessage("Курс заполнен, но список записанных студентов не полон").isEqualTo(course.getMaxStudents());
        course.unenroll(course.getEnrollmentList().get(0));
        assertThat(id).withFailMessage("Неверный порядок записи пользователей в список ожидания").isEqualTo(firstWaitingListStudentId);
        assertThat(course.getEnrollmentList().contains(id)).withFailMessage("При записи нового студента" +
                " из списка ожидающих в список записанных произошла ошибка").isEqualTo(true);
        assertThat(course.getWaitingList().contains(id)).withFailMessage("Студент попал в список " +
                "записанных, но и остался в списке ожидания").isEqualTo(false);
    }


    /**
     * Тест удаления студента из списка ожидания
     *
     * @param id - id студента
     */
    @ParameterizedTest
    @MethodSource("testData")
    void unenrollStudentFromWaitingListTest(int id) {
        assertThat(course.getMaxStudents()).withFailMessage("Неположительное количество студентов доступных для записи в курс").isGreaterThan(0);
        fullCourse(id, false, false);
        assertThat(course.isFullyEnrolled()).withFailMessage("Курс не заполнен, хотя должен был быть заполнен").isEqualTo(true);
        course.enroll(id);
        assertThat(course.hasWaitingList()).withFailMessage("Курс заполнен, но при добавлении нового студента не был создан список ожидания").isEqualTo(true);
        course.unenroll(id);
        assertThat(course.getWaitingList().contains(id)).withFailMessage("После удаления студента из " +
                "списка ожидания, студент остался в нем").isEqualTo(false);
    }

    /**
     * Тест добавления в список ожидания уже добавленного в курс студента
     *
     * @param id - id студента
     */
    @ParameterizedTest
    @MethodSource("testData")
    void enrollEnrolledStudentWithWaitingListTest(int id) {
        assertThat(course.getMaxStudents()).withFailMessage("Неположительное количество студентов доступных для записи в курс").isGreaterThan(0);
        fullCourse(id, true, false);
        assertThat(course.isFullyEnrolled()).withFailMessage("Курс не заполнен, хотя должен был быть заполнен").isEqualTo(true);
        int defaultWaitListSize = course.getWaitingList().size();
        course.enroll(id);
        assertThat(course.hasWaitingList()).withFailMessage("Курс заполнен, но при добавлении уже добавленного студента был создан список ожидания").isEqualTo(false);
        assertThat(course.getWaitingList().size()).withFailMessage("После добавления уже существующего" +
                " студента, список ожидания увеличился").isEqualTo(defaultWaitListSize);
    }

    /**
     * Тест добавления в список ожидания уже добавленного в список ожидания студента
     *
     * @param id - id студента
     */
    @ParameterizedTest
    @MethodSource("testData")
    void enrollEnrolledStudentFromWaitingListTest(int id) {
        assertThat(course.getMaxStudents()).withFailMessage("Неположительное количество студентов доступных для записи в курс").isGreaterThan(0);
        fullCourse(id, false, false);
        assertThat(course.isFullyEnrolled()).withFailMessage("Курс не заполнен, хотя должен был быть заполнен").isEqualTo(true);
        course.enroll(id);
        assertThat(course.hasWaitingList()).withFailMessage("Курс заполнен, но при добавлении нового студента не был создан список ожидания").isEqualTo(true);
        int defaultWaitListSize = course.getWaitingList().size();
        course.enroll(id);
        assertThat(course.getWaitingList().size()).withFailMessage("После добавления уже существующего" +
                " студента, список ожидания увеличился").isEqualTo(defaultWaitListSize);
    }

    /**
     * Тест добавления в список ожидания студента
     *
     * @param id - id студента
     */
    @ParameterizedTest
    @MethodSource("testData")
    void enrollNotEnrolledStudentWithWaitingListTest(int id) {
        assertThat(course.getMaxStudents()).withFailMessage("Неположительное количество студентов доступных для записи в курс").isGreaterThan(0);
        fullCourse(id, false, false);
        assertThat(course.isFullyEnrolled()).withFailMessage("Курс не заполнен, хотя должен был быть заполнен").isEqualTo(true);
        course.enroll(id);
        assertThat(course.hasWaitingList()).withFailMessage("Курс заполнен, но при добавлении нового студента не был создан список ожидания").isEqualTo(true);
        assertThat(course.getWaitingList().contains(id)).withFailMessage("После добавления нового студента" +
                " в полный курс, он не попал в список ожидания").isEqualTo(true);
    }


    /**
     * Тест удаления студента которого нет ни в курсе, ни в списке ожидания
     *
     * @param id - id студента
     */
    @ParameterizedTest
    @MethodSource("testData")
    void unenrollNotEnrolledStudentWithWaitingListTest(int id) {
        assertThat(course.getMaxStudents()).withFailMessage("Неположительное количество студентов доступных для записи в курс").isGreaterThan(0);
        fullCourse(id, false, true);
        assertThat(course.isFullyEnrolled()).withFailMessage("Курс не заполнен, хотя должен был быть заполнен").isEqualTo(true);
        assertThat(course.hasWaitingList()).withFailMessage("Курс заполнен, но при добавлении нового студента не был создан список ожидания").isEqualTo(true);
        int defaultEnrolledSize = course.getEnrollmentList().size();
        int defaultWaitingSize = course.getWaitingList().size();
        course.unenroll(id);
        assertThat(course.getWaitingList().size()).withFailMessage("После удаления не записанного ни на курс, " +
                "ни в очередь ожидания студента, список ожидания изменился").isEqualTo(defaultWaitingSize);
        assertThat(course.getEnrollmentList().size()).withFailMessage("После удаления не записанного ни на курс, " +
                "ни в очередь ожидания студента, список записанных студентов изменился").isEqualTo(defaultEnrolledSize);
    }

    /**
     * Тест удаления студента из списка ожидания, если в списке ожидания больше одного человека
     *
     * @param id - id студента
     */
    @ParameterizedTest
    @MethodSource("testData")
    void unenrollStudentFromWaitingListManyTest(int id) {
        assertThat(course.getMaxStudents()).withFailMessage("Неположительное количество студентов доступных для записи в курс").isGreaterThan(0);
        fullCourse(id, false, true);
        assertThat(course.isFullyEnrolled()).withFailMessage("Курс не заполнен, хотя должен был быть заполнен").isEqualTo(true);
        assertThat(course.hasWaitingList()).withFailMessage("Курс заполнен, но при добавлении нового студента не был создан список ожидания").isEqualTo(true);
        course.enroll(id);
        course.unenroll(id);
        assertThat(course.getWaitingList().contains(id)).withFailMessage("После удаления записанного " +
                "в очередь ожидания студента, список ожидания содержит его id").isEqualTo(false);
    }

    /**
     * Тест удаления студента из списка записанных на курс, если в списке ожидания больше одного человека
     *
     * @param id - id студента
     */
    @ParameterizedTest
    @MethodSource("testData")
    void unenrollEnrolledStudentWithWaitingListManyTest(int id) {
        assertThat(course.getMaxStudents()).withFailMessage("Неположительное количество студентов доступных для записи в курс").isGreaterThan(0);
        fullCourse(id, true, true);
        assertThat(course.isFullyEnrolled()).withFailMessage("Курс не заполнен, хотя должен был быть заполнен").isEqualTo(true);
        assertThat(course.hasWaitingList()).withFailMessage("Курс заполнен, но при добавлении нового студента не был создан список ожидания").isEqualTo(true);
        int firstWaitingId = course.getWaitingList().get(0);
        int waitingLength = course.getWaitingList().size();
        course.unenroll(id);
        assertThat(course.getEnrollmentList().contains(id)).withFailMessage("После удаления студента из списка курса, он всё ещё в нём").isEqualTo(false);
        assertThat(course.getEnrollmentList().contains(firstWaitingId)).withFailMessage("Первый студент из списка ожидания не попал в список курса").isEqualTo(true);
        assertThat(course.getWaitingList().size()).withFailMessage("После переноса первого студента из списка ожидания в список курса, размер списка ожидания не стал меньше на единицу").isEqualTo(waitingLength - 1);
    }

}
