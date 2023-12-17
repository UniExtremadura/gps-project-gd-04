package es.unex.giis.asee.gepeto.view.home

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import es.unex.giis.asee.gepeto.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        // Obtener la referencia a la preferencia de edición de sugerencias
        val editSuggestionPreference: Preference? = findPreference("edit_suggestion")

        // Configurar el click listener para la preferencia de edición de sugerencias
        editSuggestionPreference?.setOnPreferenceClickListener {
            // Mostrar un cuadro de diálogo de edición de sugerencias
            showSuggestionDialog()
            true
        }
    }

    private fun showSuggestionDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Enviar Sugerencia")

        // Configurar un EditText en el cuadro de diálogo para que los usuarios ingresen su sugerencia
        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        builder.setView(input)

        // Configurar los botones del cuadro de diálogo
        builder.setPositiveButton("Enviar") { _, _ ->
            // Obtener la sugerencia ingresada por el usuario
            val userSuggestion = input.text.toString()

            // Aquí la lógica de la sugerencia

            // Mostrar un mensaje de confirmación (esto es solo un ejemplo)
            Toast.makeText(requireContext(), "Sugerencia enviada: $userSuggestion", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }

        // Mostrar el cuadro de diálogo
        builder.show()
    }
}
