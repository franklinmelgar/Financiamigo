package com.example.financiamigo

import PageAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ABFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPagerContenedor: ViewPager
    private lateinit var addContact: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_a_b, container, false)

        tabLayout = view.findViewById(R.id.tabLayout)
        viewPagerContenedor = view.findViewById(R.id.viewPager)
        addContact = view.findViewById(R.id.addContact)

        addContact.setOnClickListener{
            val ContactActivity = Intent(activity, ABActivity::class.java)
            startActivity(ContactActivity)
        }


        viewPagerContenedor.adapter = PageAdapter(childFragmentManager)

        tabLayout.setupWithViewPager(viewPagerContenedor)

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ABFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}