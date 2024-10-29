package com.example.myfap_v3.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Calendar

class ScheduleViewModel : ViewModel() {
    private val _selectedDate = MutableLiveData(Calendar.getInstance())
    val selectedDate: LiveData<Calendar> = _selectedDate

    private val _scheduleItems = MutableLiveData<List<ScheduleItemData>>()
    val scheduleItems: LiveData<List<ScheduleItemData>> = _scheduleItems

    private val _examScheduleItems = MutableLiveData<Map<String, List<ExamScheduleItemData>>>()
    val examScheduleItems: LiveData<Map<String, List<ExamScheduleItemData>>> = _examScheduleItems

    private val _examResults = MutableLiveData<Map<String, List<ExamResultData>>>()
    val examResults: LiveData<Map<String, List<ExamResultData>>> = _examResults

    private val _selectedSemester = MutableLiveData<String>("Fall24")
    val selectedSemester: LiveData<String> = _selectedSemester

    private val _semesterGrades = MutableLiveData<List<SemesterGrade>>()
    val semesterGrades: LiveData<List<SemesterGrade>> = _semesterGrades

    private val _overallGPA = MutableLiveData<Double>()
    val overallGPA: LiveData<Double> = _overallGPA

    private val _semesterCourses = MutableLiveData<Map<String, List<CourseData>>>()
    val semesterCourses: LiveData<Map<String, List<CourseData>>> = _semesterCourses

    private val _selectedCourse = MutableLiveData<CourseData?>()
    val selectedCourse: LiveData<CourseData?> = _selectedCourse

    init {
        updateScheduleForSelectedDate()
        loadExamSchedule()
        loadExamResults()
        loadSemesterCourses()
    }

    private fun loadSemesterCourses() {
        val coursesBySemester = mapOf(
            "Fall24" to listOf(
                CourseData(
                    "Mobile Programming",
                    "PhuongLHK",
                    "BE-301",
                    listOf(
                        ScheduleItemData("Mobile Programming", "PhuongLHK", "08:00", "10:00", "BE-301", "Lecture", Calendar.MONDAY),
                        ScheduleItemData("Mobile Programming", "PhuongLHK", "13:30", "15:30", "BE-301", "Lab", Calendar.WEDNESDAY)
                    ),
                    listOf(
                        GradeComponent("Midterm", 0.3, 8.5),
                        GradeComponent("Final", 0.5, 9.0),
                        GradeComponent("Lab", 0.2, 8.8)
                    ),
                    "15/12/2024",
                    9.2
                ),
                CourseData(
                    "Web Development",
                    "TuanVM",
                    "BE-302",
                    listOf(
                        ScheduleItemData("Web Development", "TuanVM", "10:30", "12:30", "BE-302", "Lecture", Calendar.TUESDAY),
                        ScheduleItemData("Web Development", "TuanVM", "15:30", "17:30", "BE-302", "Lab", Calendar.THURSDAY)
                    ),
                    listOf(
                        GradeComponent("Progress Test", 0.3, 8.0),
                        GradeComponent("Final", 0.5, 8.5),
                        GradeComponent("Project", 0.2, 9.0)
                    ),
                    "20/12/2024",
                    8.8
                )
            ),
            "Summer24" to listOf(
                CourseData(
                    "SWD392",
                    "HoangNT",
                    "BE-303",
                    listOf(
                        ScheduleItemData("SWD392", "HoangNT", "08:00", "10:00", "BE-303", "Lecture", Calendar.MONDAY),
                        ScheduleItemData("SWD392", "HoangNT", "13:30", "15:30", "BE-303", "Lab", Calendar.WEDNESDAY)
                    ),
                    listOf(
                        GradeComponent("Midterm", 0.3, 8.7),
                        GradeComponent("Final", 0.5, 9.2),
                        GradeComponent("Project", 0.2, 8.5)
                    ),
                    "10/08/2024",
                    8.7
                ),
                CourseData(
                    "JPD316",
                    "ThaoLTT",
                    "BE-304",
                    listOf(
                        ScheduleItemData("JPD316", "ThaoLTT", "10:30", "12:30", "BE-304", "Lecture", Calendar.TUESDAY),
                        ScheduleItemData("JPD316", "ThaoLTT", "15:30", "17:30", "BE-304", "Lab", Calendar.THURSDAY)
                    ),
                    listOf(
                        GradeComponent("Midterm", 0.3, 9.0),
                        GradeComponent("Final", 0.5, 9.5),
                        GradeComponent("Lab", 0.2, 8.8)
                    ),
                    "12/08/2024",
                    9.2
                ),
                CourseData(
                    "SYB",
                    "AnhNT",
                    "BE-305",
                    listOf(
                        ScheduleItemData("SYB", "AnhNT", "08:00", "10:00", "BE-305", "Lecture", Calendar.FRIDAY)
                    ),
                    listOf(
                        GradeComponent("Midterm", 0.4, 8.5),
                        GradeComponent("Final", 0.6, 9.0)
                    ),
                    "14/08/2024",
                    8.9
                ),
                CourseData(
                    "PMG",
                    "LongDT",
                    "BE-306",
                    listOf(
                        ScheduleItemData("PMG", "LongDT", "13:30", "15:30", "BE-306", "Lecture", Calendar.MONDAY),
                        ScheduleItemData("PMG", "LongDT", "10:30", "12:30", "BE-306", "Lab", Calendar.WEDNESDAY)
                    ),
                    listOf(
                        GradeComponent("Midterm", 0.3, 9.2),
                        GradeComponent("Final", 0.4, 9.5),
                        GradeComponent("Project", 0.3, 9.8)
                    ),
                    "16/08/2024",
                    9.5
                )
            ),
            "Spring24" to listOf(
                CourseData(
                    "OJT",
                    "DuyNK",
                    "BE-307",
                    listOf(
                        ScheduleItemData("OJT", "DuyNK", "08:00", "17:00", "BE-307", "Internship", Calendar.MONDAY),
                        ScheduleItemData("OJT", "DuyNK", "08:00", "17:00", "BE-307", "Internship", Calendar.TUESDAY),
                        ScheduleItemData("OJT", "DuyNK", "08:00", "17:00", "BE-307", "Internship", Calendar.WEDNESDAY),
                        ScheduleItemData("OJT", "DuyNK", "08:00", "17:00", "BE-307", "Internship", Calendar.THURSDAY),
                        ScheduleItemData("OJT", "DuyNK", "08:00", "17:00", "BE-307", "Internship", Calendar.FRIDAY)
                    ),
                    listOf(
                        GradeComponent("Company Evaluation", 0.5, 9.5),
                        GradeComponent("Report", 0.3, 10.0),
                        GradeComponent("Presentation", 0.2, 9.8)
                    ),
                    "01/05/2024",
                    9.8
                ),
                CourseData(
                    "ENW",
                    "HungNV",
                    "BE-308",
                    listOf(
                        ScheduleItemData("ENW", "HungNV", "13:30", "15:30", "BE-308", "Lecture", Calendar.TUESDAY),
                        ScheduleItemData("ENW", "HungNV", "15:30", "17:30", "BE-308", "Lab", Calendar.THURSDAY)
                    ),
                    listOf(
                        GradeComponent("Midterm", 0.3, 8.5),
                        GradeComponent("Final", 0.5, 9.0),
                        GradeComponent("Assignments", 0.2, 8.5)
                    ),
                    "03/05/2024",
                    8.8
                )
            )
        )
        _semesterCourses.value = coursesBySemester
    }



//    private fun loadSemesterGrades() {
//        TODO("Not yet implemented")
//    }

    fun setSelectedSemester(semester: String) {
        _selectedSemester.value = semester
        updateSelectedCourses(semester)
    }

    private fun updateSelectedCourses(semester: String) {
        _semesterCourses.value?.get(semester)?.let { courses ->
            _scheduleItems.value = courses.flatMap { course ->
                course.schedule
            }
        }
    }

    fun setSelectedDate(date: Calendar) {
        _selectedDate.value = date
        updateScheduleForSelectedDate()
    }

    private fun updateScheduleForSelectedDate() {
        val selectedDate = _selectedDate.value ?: return
        val selectedDayOfWeek = selectedDate.get(Calendar.DAY_OF_WEEK)

        val filteredItems = generateScheduleItems().filter { item ->
            item.day == selectedDayOfWeek
        }
        _scheduleItems.value = filteredItems
    }


    private fun loadExamSchedule() {
        val allExamSchedules = mapOf(
            "Fall24" to listOf(
                ExamScheduleItemData("Mobile Programming", "01/12/2024", "08:00", "10:00", "100"),
                ExamScheduleItemData("Web Development", "05/12/2024", "10:30", "12:30", "200")
            ),
            "Summer24" to listOf(
                ExamScheduleItemData("SWD392", "10/08/2024", "08:00", "10:00", "301"),
                ExamScheduleItemData("JPD316", "12/08/2024", "10:30", "12:30", "302"),
                ExamScheduleItemData("SYB", "14/08/2024", "13:30", "15:30", "303"),
                ExamScheduleItemData("PMG", "16/08/2024", "08:00", "10:00", "304")
            ),
            "Spring24" to listOf(
                ExamScheduleItemData("OJT", "01/05/2024", "08:00", "10:00", "401"),
                ExamScheduleItemData("ENW", "03/05/2024", "10:30", "12:30", "402")
            )
        )
        _examScheduleItems.value = allExamSchedules
    }

    private fun loadExamResults() {
        val allExamResults = mapOf(
            "Fall24" to listOf(
                ExamResultData("Mobile Programming", "06/12/2024", "8.5/10"),
                ExamResultData("Web Development", "09/12/2024", "9.0/10")
            ),
            "Summer24" to listOf(
                ExamResultData("SWD392", "15/08/2024", "8.7/10"),
                ExamResultData("JPD316", "18/08/2024", "9.2/10"),
                ExamResultData("SYB", "21/08/2024", "8.9/10"),
                ExamResultData("PMG", "24/08/2024", "9.5/10")
            ),
            "Spring24" to listOf(
                ExamResultData("OJT", "10/05/2024", "9.8/10"),
                ExamResultData("ENW", "13/05/2024", "8.8/10")
            )
        )
        _examResults.value = allExamResults
        calculateSemesterGrades()
    }

    private fun calculateSemesterGrades() {
        val examResults = _examResults.value ?: return
        val semesterGrades = examResults.map { (semester, results) ->
            val totalScore = results.sumOf { it.score.split("/").first().toDouble() }
            val averageGPA = totalScore / results.size
            SemesterGrade(semester, averageGPA, results.size)
        }
        _semesterGrades.value = semesterGrades
        calculateOverallGPA()
    }

    fun selectCourse(course: CourseData) {
        _selectedCourse.value = course
    }

    private fun calculateOverallGPA() {
        val grades = _semesterGrades.value ?: return
        var totalQualityPoints = 0.0
        var totalCredits = 0

        grades.forEach { grade ->
            totalQualityPoints += grade.gpa * grade.credits
            totalCredits += grade.credits
        }

        _overallGPA.value = if (totalCredits > 0) totalQualityPoints / totalCredits else 0.0
    }

    private fun String.toScore(): Double {
        return this.split("/").first().toDouble()
    }

    private fun generateScheduleItems(): List<ScheduleItemData> {
        return listOf(
            ScheduleItemData("Mobile Programming", "PhuongLHK", "08:00", "10:00", "BE-301", "Lectures", Calendar.MONDAY),
            ScheduleItemData("Web Development", "TuanVM", "10:30", "12:30", "BE-302", "Labs", Calendar.MONDAY),
            ScheduleItemData("Data Structures", "HungNV", "13:30", "15:30", "BE-303", "Lectures", Calendar.TUESDAY),
            ScheduleItemData("Software Engineering", "ThaoLTT", "08:00", "10:00", "BE-304", "Seminars", Calendar.WEDNESDAY),
            ScheduleItemData("Database Systems", "AnhNT", "10:30", "12:30", "BE-305", "Labs", Calendar.WEDNESDAY),
            ScheduleItemData("Artificial Intelligence", "LongDT", "13:30", "15:30", "BE-306", "Lectures", Calendar.THURSDAY),
            ScheduleItemData("Software Development Project", "HoangNT", "07:30", "09:30", "BE-308", "Lectures", Calendar.FRIDAY),
            ScheduleItemData("Philosophy of Marxism – Leninism", "DuyNK32", "10:00", "12:00", "BE-301", "Lectures", Calendar.FRIDAY)
        )
    }
}


data class CourseData(
    val name: String,
    val lecturer: String,
    val room: String,
    val schedule: List<ScheduleItemData>,
    val gradeComponents: List<GradeComponent>,
    val examDate: String,
    val examScore: Double
)

data class GradeComponent(val name: String, val weight: Double, val score: Double)