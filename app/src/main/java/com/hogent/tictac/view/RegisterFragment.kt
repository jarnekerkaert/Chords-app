package com.hogent.tictac.view

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.hogent.tictac.MainActivity
import com.hogent.tictac.R
import com.hogent.tictac.persistence.Model
import com.hogent.tictac.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_register.*
import org.mindrot.jbcrypt.BCrypt

class RegisterFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        userViewModel = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid activity")

        navController = this.findNavController()

        val actionBar: ActionBar? = (activity as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register.setOnClickListener {
            username.error = null
            password.error = null

            var cancel = false
            var focusView: View? = null

            if (TextUtils.isEmpty(username.text.toString())) {
                username.error = "Field is required"
                focusView = username
                cancel = true
            }

            if (TextUtils.isEmpty(password.text.toString())) {
                password.error = "Field is required"
                focusView = password
                cancel = true
            }

            if(cancel) {
                focusView?.requestFocus()
            }
            else {
                userViewModel.register(Model.Register(username.text.toString(), BCrypt.hashpw(password.text.toString(), BCrypt.gensalt())))
            }
        }
    }
}
