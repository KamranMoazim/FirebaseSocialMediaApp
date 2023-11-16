package com.example.firebasechatapp

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.firebasechatapp.data.User
import com.example.firebasechatapp.utils.MyUtils
import com.example.firebasechatapp.utils.SharedPreferencesHelper
import io.reactivex.rxjava3.annotations.NonNull
import javax.annotation.Nullable


class ProfileFragment : Fragment() {

    private lateinit var usernameTextView: TextView
    private lateinit var fullnameTextView: TextView
    private lateinit var aboutTextView: TextView
    private lateinit var imageTextView: TextView
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var savedCredentials:Triple<String?, String?, User?>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(@NonNull view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find TextViews by their IDs
        usernameTextView = view.findViewById(R.id.username)
        fullnameTextView = view.findViewById(R.id.fullname)
        aboutTextView = view.findViewById(R.id.about)
        imageTextView = view.findViewById(R.id.imageText)

        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())
        savedCredentials = sharedPreferencesHelper.getSavedCredentials()

        // Set new text values
        usernameTextView.setText(savedCredentials.third?.UserName)
        fullnameTextView.setText(savedCredentials.third?.FullName)
        aboutTextView.setText(savedCredentials.third?.About)
        imageTextView.setText(MyUtils.getInitials(savedCredentials.third?.FullName!!))
    }


}