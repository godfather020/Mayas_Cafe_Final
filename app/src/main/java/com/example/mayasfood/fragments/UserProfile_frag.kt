package com.example.mayasfood.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.mayasfood.R


class UserProfile_frag : Fragment() {

    lateinit var userProfile_edit: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_user_profile_frag, container, false)

        userProfile_edit = view.findViewById(R.id.userProfile_edit)

        userProfile_edit.setOnClickListener {

            showProfileEditPopup()
        }

        return view
    }

    private fun showProfileEditPopup(){

        var dialog = Dialog(requireContext());
        dialog.setCancelable(false);

        var view = this.layoutInflater.inflate(R.layout.profile_edit_popup, null)

        dialog.setContentView(view);
        if (dialog.getWindow() != null) {
            dialog.getWindow()!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }

        var close = view.findViewById<ImageButton>(R.id.userEdit_close)

        close.setOnClickListener {

            dialog.cancel()
        }

        dialog.show();
    }

}