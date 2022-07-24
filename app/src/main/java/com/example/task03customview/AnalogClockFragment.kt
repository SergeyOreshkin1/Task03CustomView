package com.example.task03customview

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.task03customview.databinding.FragmentAnalogClockBinding

class AnalogClockFragment : Fragment() {

    private var _binding: FragmentAnalogClockBinding? = null

    private val binding get() = requireNotNull(_binding)

    companion object {

        fun newInstance() = AnalogClockFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAnalogClockBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Also, the color of the arrows should change by calling the appropriate methods from the code.
        //  binding.clock.setColor(hourHandColor = R.color.white)

    }

}