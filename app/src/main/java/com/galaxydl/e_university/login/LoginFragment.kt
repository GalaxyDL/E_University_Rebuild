package com.galaxydl.e_university.login

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.galaxydl.e_university.R
import com.galaxydl.e_university.databinding.LoginFragmentBinding
import com.galaxydl.e_university.utils.LiveDataEventBus
import com.galaxydl.e_university.utils.obtainViewModel
import com.galaxydl.e_university.utils.showSnackbar

class LoginFragment : Fragment() {

    private lateinit var mBinding: LoginFragmentBinding

    private lateinit var mViewModel: LoginViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        mBinding = LoginFragmentBinding.inflate(inflater, container, false)
        mViewModel = obtainViewModel(activity!!, LoginViewModel::class.java)

        setUpSnackBar()

        setUpOnLoginEvent()

        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        mViewModel.start()
    }

    private fun setUpOnLoginEvent() {
        mViewModel.onLoginEvent.observe(this, Observer {
            mViewModel.update { success ->
                if (success) {
                    LiveDataEventBus.get("onUpdated").value = 1
                } else {
                    mViewModel.snackbarMessage.show(R.string.login_server_error)
                }
            }
        })
    }

    private fun setUpSnackBar() {
        mViewModel.snackbarMessage.observe(this) {
            showSnackbar(mBinding.root, getString(it))
        }
    }

}