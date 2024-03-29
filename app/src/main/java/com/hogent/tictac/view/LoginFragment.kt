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
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import org.mindrot.jbcrypt.BCrypt

/**
 * Fragment for login screen
 *
 * Provides login form
 *
 * @property userViewModel viewModel for getting user data
 * @property navController navigation controller for navigating to other fragments
 */
class LoginFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var navController: NavController

    /**
     * Initializes viewModel, navigation controller and adjusts menu accordingly
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid activity")

        navController = this.findNavController()

        val actionBar: ActionBar? = (activity as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setDisplayShowHomeEnabled(false)

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    /**
     * Called when view is created
     *
     * Sets listener for login button. When clicked it checks if username and password are present and tries to log in.
     * If the validation fails, the invalid form element is focused on.
     *
     * Also sets functionality for the register button. When clicked the navigation controller tries to navigate to the registerFragment
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login.setOnClickListener {
            login_username.error = null
            login_password.error = null

            var cancel = false
            var focusView: View? = null

            if (TextUtils.isEmpty(login_username.text.toString())) {
                login_username.error = "Field is required"
                focusView = username
                cancel = true
            }

            if (TextUtils.isEmpty(login_password.text.toString())) {
                login_password.error = "Field is required"
                focusView = password
                cancel = true
            }

            if (cancel) {
                focusView?.requestFocus()
            } else {
                userViewModel.login(
                    Model.Login(
                        login_username.text.toString(),
                        login_password.text.toString()
                    )
                )
            }
        }

        login_to_register.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
}
