package com.example.paintapp

import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.paintapp.databinding.ActivityMainBinding
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var selectedStrokeWidth: Int = 4
    private var selectedColor: Int = Color.BLACK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.canvasView.selectedColors(selectedColor)
        binding.canvasView.setStrokeWidth(selectedStrokeWidth)
        binding.seekStrokeWidth.progress = selectedStrokeWidth
        binding.clearBtn.setOnClickListener {
            binding.canvasView.clearDrawing()

        }
        binding.undoBtn.setOnClickListener {
            binding.canvasView.undo()
        }

        binding.selectColorBtn.setOnClickListener {
            ColorPickerDialog.Builder(this)
                .setTitle("Select a color")
                .setPreferenceName("color_preference")
                .setPositiveButton("Apply", object : ColorEnvelopeListener {
                    override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                        selectedColor = envelope?.color ?: Color.BLACK
                        binding.canvasView.selectedColors(selectedColor)

                    }

                })
                .setNegativeButton("cancel") { dialog, i ->
                    dialog.dismiss()
                }
                .attachAlphaSlideBar(false)
                .attachBrightnessSlideBar(false).show()
        }

        binding.seekStrokeWidth.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                selectedStrokeWidth = progress
                binding.canvasView.setStrokeWidth(selectedStrokeWidth)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
    }
}