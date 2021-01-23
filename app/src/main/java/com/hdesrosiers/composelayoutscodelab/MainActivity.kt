package com.hdesrosiers.composelayoutscodelab

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ScrollableRow
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hdesrosiers.composelayoutscodelab.ui.theme.ComposeLayoutsCodelabTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeLayoutsCodelabTheme {
                LayoutsCodelab()
            }
        }
    }
}


// Custom layout modifier
fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = Modifier.layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)

    // Check the composable has a first baseline
    check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
    val firstBaseline = placeable[FirstBaseline]

    // Height of the composable with padding - first baseline
    val placeableY = firstBaselineToTop.toIntPx() - firstBaseline
    val height = placeable.height + placeableY
    layout(placeable.width, height) {
        placeable.placeRelative(0, placeableY)
    }
}

// Custom column composable
@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.map { measurable ->
            // Measure each child
            measurable.measure(constraints)
        }

        // Track the y co-ord we have placed children up to
        var yPosition = 0

        // Set the size of the layout as big as it can
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Place children in the parent layout
            placeables.forEach { placeable ->
                // Position item on the screen
                placeable.placeRelative(x = 0, y = yPosition)

                // Record the y co-ord placed up to
                yPosition += placeable.height
            }
        }
    }
}

@Composable
fun LayoutsCodelab() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Layouts Codelab"
                    )
                },
                actions = {
                    IconButton(onClick = { /* doSomething()*/ }) {
                        Icon(Icons.Filled.Favorite)
                    }
                }
            )
        }
    ) { innerPadding ->
        BodyContent(
            Modifier
                .padding(innerPadding)
                .padding(8.dp)
        )
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    Surface(Modifier.padding(8.dp)) {
        MyOwnColumn {
            Text("MyOwnColumn")
            Text("places items")
            Text("vertically.")
            Text("We've done it by hand!")
            PhotographCard()
            ScrollableRow(modifier = modifier) {
                StaggeredGrid(modifier = modifier, rows = 5) {
                    for (topic in topics) {
                        Chip(modifier = Modifier.padding(8.dp), text = topic)
                    }
                }
            }
        }
    }
}

@Composable
fun PhotographCard(modifier: Modifier = Modifier) {
    Row(
        modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.surface)
            .clickable(onClick = { /* Ignoring onClick */ })
            .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier
                .preferredSize(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        ) {

        }
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = "Alfred Sisley",
                fontWeight = FontWeight.Bold
            )
            Providers(AmbientContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = "3 minutes ago",
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PhotographerCardPreview() {
//    ComposeLayoutsCodelabTheme {
//        PhotographCard()
//    }
//}


@Preview(showBackground = true)
@Composable
fun LayoutsCodelabPreview() {
    ComposeLayoutsCodelabTheme {
        LayoutsCodelab()
    }
}

//@Preview(showBackground = true)
//@Composable
//fun TextWithPaddingToBaselinePreview() {
//    ComposeLayoutsCodelabTheme {
//        Text("Hi there!", Modifier.firstBaselineToTop(32.dp))
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun TextWithNormalPaddingPreview() {
//    ComposeLayoutsCodelabTheme {
//        Text("Hi there!", Modifier.padding(top = 32.dp))
//    }
//}