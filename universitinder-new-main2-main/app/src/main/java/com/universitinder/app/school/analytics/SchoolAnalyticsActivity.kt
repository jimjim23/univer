package com.universitinder.app.school.analytics

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.models.School
import com.universitinder.app.ui.theme.UniversitinderTheme

@Suppress("DEPRECATION")
class SchoolAnalyticsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val school = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("SCHOOL", School::class.java)
        } else {
            intent.getParcelableExtra("SCHOOL")
        }

        if (school == null) finish()

        val schoolController = SchoolController()
        val viewModel = SchoolAnalyticsViewModel(
            school = school!!,
            schoolController = schoolController,
            popActivity = this::finish
        )

        setContent {
            UniversitinderTheme {
                SchoolAnalyticsScreen(viewModel = viewModel)
            }
        }
    }
}