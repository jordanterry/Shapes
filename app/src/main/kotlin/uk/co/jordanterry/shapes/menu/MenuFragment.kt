package uk.co.jordanterry.shapes.menu

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.jordanterry.shapes.R
import uk.co.jordanterry.shapes.databinding.FragmentMenuBinding

import javax.inject.Inject

@AndroidEntryPoint
class MenuFragment : Fragment(R.layout.fragment_menu) {

    private lateinit var binding: FragmentMenuBinding

    private val viewModel: MenuViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMenuBinding.bind(view)

        binding.bStartGame.setOnClickListener {
            viewModel.gameStarted()
            findNavController().navigate(R.id.action_menuFragment_to_gameFragment)
        }
    }
}

@HiltViewModel
class MenuViewModel @Inject constructor() : ViewModel() {
    fun gameStarted() {
        Log.d("MenuViewModel", "gameStarted")
    }
}