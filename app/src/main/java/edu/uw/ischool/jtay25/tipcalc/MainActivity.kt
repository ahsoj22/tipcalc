package edu.uw.ischool.jtay25.tipcalc

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat
import java.util.Locale
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val amountInput = findViewById<TextInputEditText>(R.id.amountInput)
        val tipButton = findViewById<Button>(R.id.tipbutton)

        tipButton.isEnabled = !amountInput.text.isNullOrEmpty()

        amountInput.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    amountInput.removeTextChangedListener(this)

                    val cleanString = s.toString().replace("[^\\d]".toRegex(), "")

                    if (cleanString.isNotEmpty()) {
                        try {
                            val parsed = cleanString.toDouble()
                            val formatted = NumberFormat.getCurrencyInstance(Locale.US).format(parsed / 100)
                            current = formatted
                            amountInput.setText(formatted)
                            amountInput.setSelection(formatted.length)
                        } catch (e: NumberFormatException) {
                            // Handle NumberFormatException
                        }
                    } else {
                        current = ""
                    }

                    amountInput.addTextChangedListener(this)
                }

                tipButton.isEnabled = !s.isNullOrEmpty()
            }
        })
        tipButton.setOnClickListener{
            val amountText = amountInput.text.toString().replace("[^\\d.]".toRegex(), "")
            if(amountText.isNotEmpty()){
                val amount = amountText.toDouble()
                val tip = amount * 0.15
                Toast.makeText(this, "15% Tip: $${"%.2f".format(tip)}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
