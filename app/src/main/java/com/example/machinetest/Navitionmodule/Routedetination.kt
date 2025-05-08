package com.example.machinetest.Navitionmodule

sealed class Routedetination(val route:String){
    data object FragmentUserlist : Routedetination("userlist")
    data object FragmentUnFavorite : Routedetination("unfavorite")
    data object FragmentFavorite : Routedetination("favorite")


    companion object{
      fun fromroute(route: String?): Routedetination {
          return when(route){
              FragmentUserlist.route -> FragmentUserlist
              FragmentUnFavorite.route -> FragmentUnFavorite
              FragmentFavorite.route -> FragmentFavorite
              else -> FragmentUserlist
          }
      }
    }

}