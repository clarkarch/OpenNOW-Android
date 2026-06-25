package com.opencloudgaming.opennow.ui.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.opencloudgaming.opennow.R

data class NavItem(val route: Route, val label: String, val iconRes: Int)

val navItems = listOf(
    NavItem(Route.Home, "Store", R.drawable.ic_tab_store),
    NavItem(Route.Library, "Library", R.drawable.ic_tab_library),
    NavItem(Route.Settings, "Settings", R.drawable.ic_tab_settings),
)

@Composable
fun AppBottomBar(
    currentRoute: Route?,
    onNavigate: (Route) -> Unit,
) {
    NavigationBar {
        navItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(painterResource(item.iconRes), contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
            )
        }
    }
}

@Composable
fun AppNavRail(
    currentRoute: Route?,
    onNavigate: (Route) -> Unit,
) {
    NavigationRail {
        Spacer(Modifier.height(8.dp))
        navItems.forEach { item ->
            NavigationRailItem(
                icon = { Icon(painterResource(item.iconRes), contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
            )
        }
    }
}
