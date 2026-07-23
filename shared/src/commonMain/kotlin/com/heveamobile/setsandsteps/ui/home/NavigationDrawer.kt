package com.heveamobile.setsandsteps.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.heveamobile.setsandsteps.theme.spacing
import com.heveamobile.setsandsteps.ui.common.StepsDivider
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import setsandsteps.shared.generated.resources.Res
import setsandsteps.shared.generated.resources.logo_description
import setsandsteps.shared.generated.resources.logo_svg

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
                    painter = painterResource(Res.drawable.logo_svg),
                    contentDescription = stringResource(Res.string.logo_description),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                )
                StepsDivider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
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
