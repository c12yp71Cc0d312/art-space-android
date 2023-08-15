package com.example.artspace

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.artspace.ui.theme.ArtSpaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtSpaceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Art()
                }
            }
        }
    }
}

@Composable
fun Art() {
    var index by remember { mutableStateOf(1) }

    val imgSrc: Int
    val title: String
    val artist: String
    val year: String

    val desc: String
    val loc: String

    var showContextMenu by remember { mutableStateOf(false) }

    when (index) {
        1 -> {
            imgSrc = R.drawable.img_1
            title = stringResource(id = R.string.title_1)
            artist = stringResource(id = R.string.artist_1)
            year = stringResource(id = R.string.year_1)
            desc = stringResource(id = R.string.desc_1)
            loc = stringResource(id = R.string.loc_1)
        }
        2 -> {
            imgSrc = R.drawable.img_2
            title = stringResource(id = R.string.title_2)
            artist = stringResource(id = R.string.artist_2)
            year = stringResource(id = R.string.year_2)
            desc = stringResource(id = R.string.desc_2)
            loc = stringResource(id = R.string.loc_2)
        }
        else -> {
            imgSrc = R.drawable.img_3
            title = stringResource(R.string.title_3)
            artist = stringResource(R.string.artist_3)
            year = stringResource(R.string.year_3)
            desc = stringResource(id = R.string.desc_3)
            loc = stringResource(id = R.string.loc_3)
        }
    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {

        Image(
            painter = painterResource(id = imgSrc),
            contentDescription = null,
            modifier = Modifier.padding(16.dp).fillMaxHeight(0.7F)
        )
        Column {
            ArtDescription(title, artist, year, setShowContextMenu = {newShowContextMenu -> showContextMenu = newShowContextMenu})
            Spacer(modifier = Modifier.height(8.dp))
            NavButtons(index, onIndexChange = {newIndex -> index = newIndex})
        }
    }

    if(showContextMenu) {
        ContextMenu(loc, desc, setShowContextMenu = {newShowContextMenu -> showContextMenu = newShowContextMenu})
    }
}

@Composable
private fun NavButtons(index: Int, onIndexChange: (Int) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            modifier = Modifier
                .weight(1F)
                .padding(8.dp),
            onClick = {
                if(index != 1)
                    onIndexChange(index - 1)
                Log.d(TAG, "NavButtons: Button clicked")
            }
        ) {
            Text(text = "Previous")
        }
        Button(
            modifier = Modifier
                .weight(1F)
                .padding(8.dp),
            onClick = {
                if(index != 3)
                    onIndexChange(index + 1)
                Log.d(TAG, "NavButtons: Button clicked")
            }
        ) {
            Text(text = "Next")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ArtDescription(title: String, artist: String, year: String, setShowContextMenu: (Boolean) -> Unit) {
    val haptics = LocalHapticFeedback.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .fillMaxWidth()
            .combinedClickable(
                enabled = true,
                onClick = { },
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    setShowContextMenu(true)
                },
            )
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "$artist ($year)",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun ContextMenu(loc: String, desc: String, setShowContextMenu: (Boolean) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.75F)
            .clickable(
                onClick = {
                    Log.d(TAG, "ContextMenu: clicked surface")
                    setShowContextMenu(false)
                },
                interactionSource = MutableInteractionSource(),
                indication = null
            )
    ) {

    }
    Column(
        modifier = Modifier.padding(32.dp)
    ) {
        Spacer(modifier = Modifier.weight(1F))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .verticalScroll(rememberScrollState())
                .weight(3F)
        ) {
            Text(
                text = "Location: ${loc}",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = desc,
                modifier = Modifier.padding(16.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1F))
    }
}

@Preview(showBackground = true)
@Composable
fun ArtSpacePreview() {
    ArtSpaceTheme {
        Art()
    }
}