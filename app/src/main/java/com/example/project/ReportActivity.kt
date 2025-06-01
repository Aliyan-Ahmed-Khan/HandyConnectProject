package com.example.yourapp

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project.R

class ReportActivity : AppCompatActivity() {

    private lateinit var workerName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        val titleLabel = findViewById<TextView>(R.id.report_title)
        val reportInput = findViewById<EditText>(R.id.report_input)
        val reportButton = findViewById<Button>(R.id.submit_button)

        // Get worker name from intent
        workerName = intent.getStringExtra("worker_name") ?: "the worker"

        reportButton.setOnClickListener {
            val reportText = reportInput.text.toString().trim()

            if (reportText.isEmpty()) {
                Toast.makeText(this, "Please write your report message.", Toast.LENGTH_SHORT).show()
            } else {
                showConfirmationDialog(reportText)
            }
        }
    }

    private fun showConfirmationDialog(reportMessage: String) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Report")
            .setMessage("Are you sure you want to report $workerName?")
            .setPositiveButton("Yes") { _, _ ->
                // TODO: Send reportMessage to backend
                Toast.makeText(this, "Your report has been submitted.", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
