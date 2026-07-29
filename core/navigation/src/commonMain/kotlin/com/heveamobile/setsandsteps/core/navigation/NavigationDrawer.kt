package com.heveamobile.setsandsteps.core.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.heveamobile.setsandsteps.core.designsystem.theme.spacing
import com.heveamobile.setsandsteps.core.navigation.generated.resources.Res
import com.heveamobile.setsandsteps.core.navigation.generated.resources.logo
import com.heveamobile.setsandsteps.core.navigation.generated.resources.logo_dark
import com.heveamobile.setsandsteps.core.navigation.generated.resources.logo_description
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

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
                        .height(40.dp),
                )
                Image(
                    modifier = Modifier
                        .padding(
                            horizontal = MaterialTheme.spacing.large,
                        )
                        .padding(bottom = MaterialTheme.spacing.extraLarge),
                    painter = painterResource(
                        if (isSystemInDarkTheme()) Res.drawable.logo_dark
                        else Res.drawable.logo,
                    ),
                    contentDescription = stringResource(Res.string.logo_description),
                )
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
                    .background(MaterialTheme.colorScheme.outlineVariant),
            )
        }
    }
}
