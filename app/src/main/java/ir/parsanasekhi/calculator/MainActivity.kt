package ir.parsanasekhi.calculator

import android.os.Bundle
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ir.parsanasekhi.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var parenthesisCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClickedNumber()
        onClickedOperator()
    }

    private fun onClickedOperator() {

        binding.buttonDelete.setOnClickListener {
            isLastCharEqual("delete")
            if (binding.textViewBig.text.isNotEmpty()) {
                val lastChar = binding.textViewBig.text.last().toString()
                binding.textViewBig.text =
                    binding.textViewBig.text.substring(0, binding.textViewBig.text.length - 1)
                if (lastChar == ")") {
                    parenthesisCounter++
                } else if (lastChar == "(") {
                    parenthesisCounter--
                }
            }
        }

        binding.buttonAc.setOnClickListener {
            binding.textViewBig.text = ""
            binding.textViewSmall.text = ""
            parenthesisCounter = 0
        }

        binding.buttonOpenParenthesis.setOnClickListener {
            isLastCharEqual("(")
            if (binding.textViewBig.text.isNotEmpty()) {
                if (binding.textViewBig.text.last().toString() != ".") {
                    appendCharToTextView("(")
                    parenthesisCounter++
                }
            } else {
                appendCharToTextView("(")
                parenthesisCounter++
            }
        }

        binding.buttonCloseParenthesis.setOnClickListener {
            if (binding.textViewBig.text.isNotEmpty() && parenthesisCounter > 0) {
                val lastChar: String = binding.textViewBig.text.last().toString()
                if (lastChar != "(" && lastChar != "/" && lastChar != "*"
                    && lastChar != "-" && lastChar != "+" && lastChar != "."
                ) {
                    isLastCharEqual(")")
                    appendCharToTextView(")")
                    parenthesisCounter--
                }
            }
        }

        binding.buttonDivisive.setOnClickListener {
            setOperatorOnTextView("/")
        }

        binding.buttonStar.setOnClickListener {
            setOperatorOnTextView("*")
        }

        binding.buttonMine.setOnClickListener {
            setOperatorOnTextView("-")
        }

        binding.buttonPlus.setOnClickListener {
            setOperatorOnTextView("+")
        }

        binding.buttonNegative.setOnClickListener {
            isLastCharEqual("-")
            if (binding.textViewBig.text.isEmpty() || binding.textViewBig.text.last().toString() == "(") {
                appendCharToTextView("-")
            } else if (binding.textViewBig.text.last().toString() != ".") {
                appendCharToTextView("(-")
                parenthesisCounter++
            }
        }

        binding.buttonEqual.setOnClickListener {
            if (binding.textViewBig.text.isNotEmpty() && binding.textViewBig.text.last()
                    .toString() == "."
            ) {
                Toast.makeText(this, "The last character can not be '.'", Toast.LENGTH_LONG).show()
            } else {
                try {
                    val expression = ExpressionBuilder(binding.textViewBig.text.toString()).build()
                    val result = expression.evaluate()
                    val longResult = result.toLong()
                    if (result == longResult.toDouble()) {
                        binding.textViewSmall.text = longResult.toString()
                    } else {
                        binding.textViewSmall.text = result.toString()
                    }
                    appendCharToTextView("=")
                    parenthesisCounter = 0
                } catch (e: Exception) {
                    Toast.makeText(this, "${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun onClickedNumber() {

        binding.buttonDot.setOnClickListener {
            isLastCharEqual(".")
            if (binding.textViewBig.text.isEmpty()) {
                appendCharToTextView("0.")
            } else {
                val lastChar = binding.textViewBig.text.last().toString()
                if (lastChar != "/" && lastChar != "*" && lastChar != "-" && lastChar != "+"
                    && lastChar != "." && lastChar != "(" && lastChar != ")"
                ) {
                    try {
                        val expression =
                            ExpressionBuilder(binding.textViewBig.text.toString() + ".").build()
                        expression.evaluate()
                        appendCharToTextView(".")
                    } catch (e: NumberFormatException) {
                    } catch (e: Exception) {
                        appendCharToTextView(".")
                    }
                } else if (lastChar != ".") {
                    appendCharToTextView("0.")
                }
            }
        }

        binding.button0.setOnClickListener {
            isLastCharEqual(0)
            if (binding.textViewBig.text.isEmpty()) {
                appendCharToTextView("0")
            } else {
                val length = binding.textViewBig.text.length
                val lastChar = binding.textViewBig.text.toString()[length - 1].toString()
                if (lastChar != "0") {
                    appendCharToTextView("0")
                } else if (length > 1) {
                    val beforeLastChar = binding.textViewBig.text.toString()[length - 2].toString()
                    if (beforeLastChar == "1" || beforeLastChar == "2" || beforeLastChar == "3"
                        || beforeLastChar == "4" || beforeLastChar == "5" || beforeLastChar == "6"
                        || beforeLastChar == "7" || beforeLastChar == "8" || beforeLastChar == "9"
                        || beforeLastChar == "0" || beforeLastChar == "."
                    ) {
                        appendCharToTextView("0")
                    }
                }
            }
        }

        binding.button1.setOnClickListener {
            setNumberOnTextView(1)
        }

        binding.button2.setOnClickListener {
            setNumberOnTextView(2)
        }

        binding.button3.setOnClickListener {
            setNumberOnTextView(3)
        }

        binding.button4.setOnClickListener {
            setNumberOnTextView(4)
        }

        binding.button5.setOnClickListener {
            setNumberOnTextView(5)
        }

        binding.button6.setOnClickListener {
            setNumberOnTextView(6)
        }

        binding.button7.setOnClickListener {
            setNumberOnTextView(7)
        }

        binding.button8.setOnClickListener {
            setNumberOnTextView(8)
        }

        binding.button9.setOnClickListener {
            setNumberOnTextView(9)
        }

    }

    private fun appendCharToTextView(newChar: String) {
        binding.textViewBig.append(newChar)
        val viewTree: ViewTreeObserver = binding.scrollTextViewBig.viewTreeObserver
        viewTree.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.scrollTextViewBig.viewTreeObserver.removeOnGlobalLayoutListener(this)
                binding.scrollTextViewBig.scrollTo(binding.textViewBig.width, 0)
            }
        })
    }

    private fun <T> isLastCharEqual(clickedButton: T) {
        if (binding.textViewBig.text.isNotEmpty()) {
            if (binding.textViewBig.text.last().toString() == "=") {
                if (clickedButton is Int || clickedButton.toString() == ".") {
                    binding.textViewBig.text = ""
                    binding.textViewSmall.text = ""
                } else if (clickedButton == "delete") {
                    binding.textViewSmall.text = ""
                } else {
                    binding.textViewBig.text = binding.textViewSmall.text
                    binding.textViewSmall.text = ""
                }
            }
        }
    }

    private fun setNumberOnTextView(num: Int) {
        if (binding.textViewBig.text.isEmpty()) {
            appendCharToTextView("$num")
        } else {
            val length = binding.textViewBig.text.length
            val lastChar = binding.textViewBig.text.toString()[length - 1].toString()
            if (lastChar != "0") {
                isLastCharEqual(num)
                appendCharToTextView("$num")
            } else if (length > 1) {
                val beforeLastChar = binding.textViewBig.text.toString()[length - 2].toString()
                if (beforeLastChar == "1" || beforeLastChar == "2" || beforeLastChar == "3"
                    || beforeLastChar == "4" || beforeLastChar == "5" || beforeLastChar == "6"
                    || beforeLastChar == "7" || beforeLastChar == "8" || beforeLastChar == "9"
                    || beforeLastChar == "0" || beforeLastChar == "."
                ) {
                    appendCharToTextView("$num")
                } else {
                    binding.textViewBig.text = binding.textViewBig.text.substring(0, length - 1)
                    appendCharToTextView("$num")
                }
            } else {
                binding.textViewBig.text = "$num"
            }
        }
    }

    private fun setOperatorOnTextView(operator: String) {
        isLastCharEqual(operator)
        if (binding.textViewBig.text.isNotEmpty()) {
            val lastChar: String = binding.textViewBig.text.last().toString()
            if (lastChar != "/" && lastChar != "*" && lastChar != "-"
                && lastChar != "+" && lastChar != "." && lastChar != "("
            ) {
                appendCharToTextView(operator)
            }
        }
    }

}