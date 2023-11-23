package es.unex.giis.asee.gepeto.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import es.unex.giis.asee.gepeto.database.GepetoDatabase
import es.unex.giis.asee.gepeto.databinding.FragmentRestorePopUpBinding
import es.unex.giis.asee.gepeto.model.User
import es.unex.giis.asee.gepeto.utils.CredentialCheck
import kotlinx.coroutines.launch

class PopUpFragment : DialogFragment() {

    //binding
    private lateinit var binding: FragmentRestorePopUpBinding

    private lateinit var db: GepetoDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRestorePopUpBinding.inflate(inflater, container, false)

        db = GepetoDatabase.getInstance(requireContext())!!

        return binding.root
    }

    //funcion que se ejecuta al crear la vista
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            //boton que llama a la funcion que cambia la contraseña
            restorebtn.setOnClickListener {
                restorePassword()
                dismiss()
            }
        }
    }

    //funcion que cambia la contraseña del usuario que se la ha olvidado
    private fun restorePassword() {
        with(binding) {
            val username = restUsername.text.toString()
            val newPassword = newPassword.text.toString()

            //dado el nombre de usuario, se busca en la base de datos y se actualiza la contraseña antigua por la nueva
            lifecycleScope.launch {
                //comprueba si la contraseña es correcta
                val check = CredentialCheck.newPasswordOk(newPassword)
                if (check.fail) {
                    Toast.makeText(context, check.msg, Toast.LENGTH_SHORT).show()
                    return@launch
                }
                else{
                    val user = db?.userDao()?.findByName(restUsername.text.toString()) //?: User(-1, etUsername.text.toString(), etPassword.text.toString())
                    if (user != null) {
                        Toast.makeText(context, "Contraseña cambiada", Toast.LENGTH_SHORT).show()
                        db?.userDao()?.update(User(user.userId, username, newPassword))
                    } else {
                        Toast.makeText(context, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}