package com.ikersoft.cryptotest.mainview

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ikersoft.cryptotest.adapters.CryptoAdapter
import com.ikersoft.cryptotest.databinding.MainFragmentBinding

import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import com.ikersoft.cryptotest.R


class MainFragment : Fragment() {

    private lateinit var cryptoAdapter: CryptoAdapter
    private lateinit var binding: MainFragmentBinding


    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setHasOptionsMenu(true)
        val view = binding.root


        initObservers()
        initAdapter()
        viewModel.getCoinList()
        return view

    }


    private fun initObservers() {
        viewModel.coinlist.observe(viewLifecycleOwner, {
            cryptoAdapter.submitList(it)
            if (it.size > 0){
                binding.loadingProgress.visibility = View.GONE
                binding.errorTextView.visibility = View.GONE
                binding.CryptoRecycler.visibility = View.VISIBLE
                binding.recyclerHeader.visibility = View.VISIBLE
            }
        })

        viewModel.error.observe(viewLifecycleOwner, {
            binding.loadingProgress.visibility = View.GONE
            Toast.makeText(context,"Error, try refreshing again later (rate limit reached?)", Toast.LENGTH_LONG).show()
        })
    }

    private fun initAdapter(){
        cryptoAdapter = CryptoAdapter(requireContext())

        val linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        binding.CryptoRecycler.layoutManager = linearLayoutManager
        binding.CryptoRecycler.adapter = cryptoAdapter
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                binding.loadingProgress.visibility = View.VISIBLE
                viewModel.getCoinList()
            }

            else -> {}
        }
        return true
    }


}