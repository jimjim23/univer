package com.universitinder.app.school.schoolCourses.createCourse

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.CourseController
import com.universitinder.app.models.School
import com.universitinder.app.ui.theme.UniversitinderTheme

class CreateCourseActivity : AppCompatActivity() {

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val school = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("SCHOOL", School::class.java)
        } else {
            intent.getParcelableExtra("SCHOOL")
        }

        if (school == null) finish()

        val courseController = CourseController()
        val createCourseViewModel = CreateCourseViewModel(school = school!!, courseController = courseController, popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                CreateCourseScreen(createCourseViewModel = createCourseViewModel)
            }
        }
    }
}