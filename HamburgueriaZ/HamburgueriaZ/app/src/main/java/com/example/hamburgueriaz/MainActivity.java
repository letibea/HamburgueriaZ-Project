package com.example.hamburgueriaz;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends Activity {
    private static final int BASE_PRICE_CENTS = 2_000;
    private static final int BACON_PRICE_CENTS = 200;
    private static final int CHEESE_PRICE_CENTS = 200;
    private static final int ONION_RINGS_PRICE_CENTS = 300;

    private EditText customerNameInput;
    private CheckBox baconCheckBox;
    private CheckBox cheeseCheckBox;
    private CheckBox onionRingsCheckBox;
    private TextView quantityValue;
    private TextView totalValue;
    private TextView orderSummary;
    private int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customerNameInput = findViewById(R.id.customer_name);
        baconCheckBox = findViewById(R.id.add_bacon);
        cheeseCheckBox = findViewById(R.id.add_cheese);
        onionRingsCheckBox = findViewById(R.id.add_onion_rings);
        quantityValue = findViewById(R.id.quantity_value);
        totalValue = findViewById(R.id.total_value);
        orderSummary = findViewById(R.id.order_summary);

        Button decreaseButton = findViewById(R.id.decrease_quantity);
        Button increaseButton = findViewById(R.id.increase_quantity);
        Button sendOrderButton = findViewById(R.id.send_order);

        decreaseButton.setOnClickListener(this::subtrair);
        increaseButton.setOnClickListener(this::somar);
        sendOrderButton.setOnClickListener(this::enviarPedido);

        baconCheckBox.setOnCheckedChangeListener((button, checked) -> atualizarTotal());
        cheeseCheckBox.setOnCheckedChangeListener((button, checked) -> atualizarTotal());
        onionRingsCheckBox.setOnCheckedChangeListener((button, checked) -> atualizarTotal());

        atualizarQuantidade();
        atualizarTotal();
    }

    private void somar(View view) {
        quantity++;
        atualizarQuantidade();
        atualizarTotal();
    }

    private void subtrair(View view) {
        if (quantity > 0) {
            quantity--;
            atualizarQuantidade();
            atualizarTotal();
        }
    }

    private void atualizarQuantidade() {
        quantityValue.setText(String.valueOf(quantity));
        quantityValue.setContentDescription(
                getString(R.string.quantity_accessibility, quantity)
        );
    }

    private int calcularPrecoTotalEmCentavos() {
        int unitPrice = BASE_PRICE_CENTS;

        if (baconCheckBox.isChecked()) {
            unitPrice += BACON_PRICE_CENTS;
        }
        if (cheeseCheckBox.isChecked()) {
            unitPrice += CHEESE_PRICE_CENTS;
        }
        if (onionRingsCheckBox.isChecked()) {
            unitPrice += ONION_RINGS_PRICE_CENTS;
        }

        return unitPrice * quantity;
    }

    private void atualizarTotal() {
        totalValue.setText(formatarMoeda(calcularPrecoTotalEmCentavos()));
    }

    private String formatarMoeda(int cents) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(
                new Locale("pt", "BR")
        );
        return currencyFormat.format(cents / 100.0);
    }

    private String montarResumo(String customerName) {
        return getString(
                R.string.order_summary_format,
                customerName,
                resposta(baconCheckBox.isChecked()),
                resposta(cheeseCheckBox.isChecked()),
                resposta(onionRingsCheckBox.isChecked()),
                quantity,
                formatarMoeda(calcularPrecoTotalEmCentavos())
        );
    }

    private String resposta(boolean selected) {
        return getString(selected ? R.string.yes : R.string.no);
    }

    private void enviarPedido(View view) {
        String customerName = customerNameInput.getText().toString().trim();

        if (customerName.isEmpty()) {
            customerNameInput.setError(getString(R.string.name_required));
            customerNameInput.requestFocus();
            return;
        }

        if (quantity < 1) {
            Toast.makeText(this, R.string.quantity_required, Toast.LENGTH_SHORT).show();
            return;
        }

        String summary = montarResumo(customerName);
        orderSummary.setText(summary);

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject, customerName));
        emailIntent.putExtra(Intent.EXTRA_TEXT, summary);

        try {
            startActivity(Intent.createChooser(emailIntent, getString(R.string.email_chooser_title)));
        } catch (ActivityNotFoundException exception) {
            Toast.makeText(this, R.string.no_email_app, Toast.LENGTH_LONG).show();
        }
    }
}
