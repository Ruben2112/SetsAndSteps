package com.heveamobile.setsandsteps.core.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing

@Composable
fun NavigationDrawer(
    modifier: Modifier = Modifier,
    onDrawerItemClicked: (NavigationDrawerRoute) -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(0.666F)
            .fillMaxHeight(),
    ) {
        Row {
            Column(
                modifier = Modifier.weight(1F),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .height(MaterialTheme.spacing.large),
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
                ) {
                    CardText(
                        text = "SETS&",
                        fontSize = 32.sp,
                        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large),
                    )
                    CardText(
                        text = "STEPS",
                        fontSize = 32.sp,
                        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large),
                    )
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
                NavigationDrawerRoute.entries.forEach { route ->
                    NavigationDrawerItem(
                        route = route,
                        onClick = {
                            onDrawerItemClicked(route)
                        },
                    )
                }
            }
            Box(
                // Side border
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .background(MaterialTheme.colorScheme.outline),
            )
        }
    }
}
