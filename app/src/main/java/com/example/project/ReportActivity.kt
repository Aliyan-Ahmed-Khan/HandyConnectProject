package com.example.project

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity

class ReportActivity : AppCompatActivity() {

    private lateinit var workerName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        val reportInput = findViewById<EditText>(R.id.reportInput)
        val reportButton = findViewById<Button>(R.id.submitButton)

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
        val dialog = AlertDialog.Builder(this)
            .setTitle("Confirm Report")
            .setMessage("Are you sure you want to report $workerName?")
            .setPositiveButton("Yes") { _, _ ->
                // TODO: Send reportMessage to backend
                Toast.makeText(this, "Your report has been submitted.", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY)
    }
}
