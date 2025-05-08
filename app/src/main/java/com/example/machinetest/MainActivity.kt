package com.example.machinetest

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import com.example.machinetest.Fragmets.FavFragmnet
import com.example.machinetest.Fragmets.FragmentUserlist
import com.example.machinetest.Navitionmodule.Routedetination

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setupNavigation()
    }

    private fun setupNavigation() {
        try {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
            if (navHostFragment == null) {
                Log.e(TAG, "NavHostFragment are not found ")
                return
            }

            navController = navHostFragment.navController
            if (navController == null) {
                Log.e(TAG, "NavController are not found ")
                return
            }

            val graph = createGraph(startDestination = Routedetination.FragmentUserlist)
            navController?.graph = graph
            Log.i(TAG, "Navigation graph successfully set")
        } catch (e: Exception) {
            Log.e(TAG, "Navigation setup mein error: ${e.message}", e)
        }
    }

    private fun createGraph(startDestination: Routedetination): NavGraph {
        return try {
            navController?.createGraph(startDestination = startDestination.route) {
                fragment<FragmentUserlist>(Routedetination.FragmentUserlist.route) {
                    label = "User List"
                }
                fragment<FavFragmnet>(Routedetination.FragmentFavorite.route) {
                    label = "Fav List"
                }
            } ?: run {
                Log.e(TAG, "NavController null h")
                throw IllegalStateException("NavController null")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Graph creation failed: ${e.message}", e)
            throw e
        }
    }
}