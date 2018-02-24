package com.example.pramod.justkotlin

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.text.NumberFormat

/**
 * Created by pramod on 13/12/17.
 * This app is similar to JustJava app written in Kotlin with little UI polish
 */
class MainActivity : AppCompatActivity() {

    private var quantity = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun increment(view: View) {
        if (quantity == 100) Toast.makeText(this, getString(R.string.no_more_than_99), Toast.LENGTH_SHORT).show() else displayQuantity(++quantity)
    }

    fun decrement(view: View) {
        if (quantity == 1) Toast.makeText(this, getString(R.string.no_less_than_1), Toast.LENGTH_SHORT).show() else displayQuantity(--quantity)
    }

    private fun displayQuantity(quantity: Int) {
        order_quantity.text = quantity.toString()
    }

    fun submitOrder(view: View) {
        val name = name.text.toString()
        if (name.length >= 3) {
            val hasWhipped = whipped_cream.isChecked
            val hasChocolate = chocolate.isChecked
            val summary = createOrderSummary(name, calculatePrice(hasWhipped, hasChocolate), hasWhipped, hasChocolate)
            displayMessage(summary)
        } else {
            Toast.makeText(this, getString(R.string.no_name), Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculatePrice(hasWhipped: Boolean, hasChocolate: Boolean): Int {
        var price = 5
        if (hasWhipped) price += 1
        if (hasChocolate) price += 2
        return quantity * price
    }

    private fun createOrderSummary(name: String, price: Int, whipped: Boolean, chocolate: Boolean): String {
        return "${getString(R.string.name)} : $name\n" +
                "${getString(R.string.added_whipped)} : ${if (whipped) getString(R.string.yes) else getString(R.string.no)}\n" +
                "${getString(R.string.added_chocolate)} : ${if (chocolate) getString(R.string.yes) else getString(R.string.no)}\n" +
                "${getString(R.string.quantity)} : $quantity\n" +
                "${getString(R.string.total)} : ${NumberFormat.getCurrencyInstance().format(price)} \n" +
                getString(R.string.thank_you)
    }

    private fun displayMessage(summary: String) {
        order_summary_view.text = summary
    }

    fun confirm(view: View) {
        val customerName = name.text
        val orderSummary = order_summary_view.text
        if (orderSummary.length > 3) {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "${getString(R.string.order_from)} : " + customerName)
            emailIntent.putExtra(Intent.EXTRA_TEXT, orderSummary)
            if (emailIntent.resolveActivity(packageManager) != null) {
                startActivity(emailIntent)
            }
        } else {
            Toast.makeText(this, getString(R.string.no_order), Toast.LENGTH_SHORT).show()
        }
    }

    fun reset(view: View) {
        order_summary_view.text = ""
        order_quantity.text = "1"
        quantity = 1
        whipped_cream.isChecked = false
        chocolate.isChecked = false
    }
}
