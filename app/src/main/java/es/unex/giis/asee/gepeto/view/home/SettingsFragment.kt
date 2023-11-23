package es.unex.giis.asee.gepeto.view.home

import android.content.Context
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import es.unex.giis.asee.gepeto.R
import es.unex.giis.asee.gepeto.data.Session
import es.unex.giis.asee.gepeto.model.User

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setDefaultValues()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)


    }

    private fun setDefaultValues() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val editor = preferences.edit()
        val user = Session.getValue("user") as User

        editor.putString("username", user.name)
        editor.putString("password", user.password)

        editor.apply()
    }

}
