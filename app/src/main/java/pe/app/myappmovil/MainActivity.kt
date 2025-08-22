package pe.app.myappmovil

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    // Declaración de variables de vista con un ámbito de clase
    private lateinit var spinnerProductos: Spinner
    private lateinit var editCantidad: EditText
    private lateinit var textviewPrecio: TextView
    private lateinit var textviewTotal: TextView
    private lateinit var textviewDescuento: TextView
    private lateinit var textviewPagar: TextView
    private lateinit var checkDelivery: CheckBox
    private lateinit var bottonCalcular: Button

    // Mapa para almacenar los productos y sus precios.
    // Esto hace que el código sea más fácil de mantener y escalar.
    private val productosPrecios = mapOf(
        "1 Pollo a la brasa" to 65.5,
        "1/2 Pollo a la brasa" to 34.5,
        "1/4 Pollo a la brasa" to 18.5,
        "Porción de anticucho" to 17.5,
        "Pechuga de pollo" to 21.5
    )

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Inicialización de las vistas
        spinnerProductos = findViewById(R.id.spnProducto)
        editCantidad = findViewById(R.id.edtCantidad)
        textviewPrecio = findViewById(R.id.tvPrecio)
        textviewTotal = findViewById(R.id.tvTotal)
        textviewDescuento = findViewById(R.id.tvDescuento)
        textviewPagar = findViewById(R.id.tvTotalPagar)
        checkDelivery = findViewById(R.id.chkDelivery)
        bottonCalcular = findViewById(R.id.btnCalcular)

        // Preparamos la lista para el spinner. Agregamos el texto de selección.
        val productos = listOf("[seleccionar]") + productosPrecios.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, productos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProductos.adapter = adapter

        // Establecer el listener para el botón de calcular
        bottonCalcular.setOnClickListener {
            calcularTotal()
        }
    }

    // Función que contiene toda la lógica de cálculo, mejorando la organización
    @SuppressLint("SetTextI18n")
    private fun calcularTotal() {
        // Obtenemos el producto seleccionado
        val nombreProducto = spinnerProductos.selectedItem.toString()
        val cantidadStr = editCantidad.text.toString()

        // Validamos que se haya seleccionado un producto válido
        if (nombreProducto == "[seleccionar]") {
            Toast.makeText(this, "Por favor, selecciona un producto.", Toast.LENGTH_SHORT).show()
            return
        }

        // Validamos que la cantidad no esté vacía y sea un número válido
        val cantidad: Int = cantidadStr.toIntOrNull() ?: run {
            Toast.makeText(this, "Por favor, ingresa una cantidad válida.", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtenemos el precio del mapa y calculamos el total
        val pre = productosPrecios[nombreProducto] ?: 0.0
        val total: Double = pre * cantidad

        var deliveryMonto = 0.0
        var descuentoMonto = 0.0

        // Aplicamos el cargo por envío si la casilla está marcada
        if (checkDelivery.isChecked) {
            deliveryMonto = 10.0
        }

        // Aplicamos el descuento si el total supera los 60
        if (total > 60) {
            descuentoMonto = 5.0
        }

        // Calculamos el monto final a pagar
        val pagar: Double = (total - descuentoMonto) + deliveryMonto

        // Mostramos los resultados en los TextViews, usando el formato de moneda
        textviewPrecio.text = "S/. %.2f".format(pre)
        textviewTotal.text = "S/. %.2f".format(total)
        textviewDescuento.text = "S/. %.2f".format(descuentoMonto)
        textviewPagar.text = "S/. %.2f".format(pagar)
    }
}
