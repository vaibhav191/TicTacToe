package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.ui.theme.TicTacToeTheme

class GameScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { topBar(Modifier) }) { innerPadding ->
                    Board(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Board(modifier: Modifier) {
    Column {
        Column(

            modifier = Modifier
                .padding(top = 100.dp, start = 50.dp, end = 50.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            Text(
                text = "Hard",
                fontStyle = FontStyle.Italic,
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(50.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .clip(RoundedCornerShape(40.dp))
                .padding(start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val buttonColor = MaterialTheme.colorScheme.primaryContainer
            val buttonElevation = 10.dp
            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Black)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                var press by remember { mutableStateOf(false) }
                var Firstpress by remember { mutableStateOf(true) }
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(buttonColor),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(end = 5.dp, bottom = 5.dp),
                    shape = RectangleShape,
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = buttonElevation)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cross),
                        contentDescription = null
                    )
                }
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(buttonColor),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(end = 5.dp, bottom = 5.dp),

                    elevation = ButtonDefaults.buttonElevation(defaultElevation = buttonElevation),
                    shape = RectangleShape
                ) {
                    Image(painter = painterResource(id = R.drawable.o), contentDescription = null)
                }
                Button(
                    onClick = {
                        if (Firstpress) {
                            press = !press
                            Firstpress = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(buttonColor),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(bottom = 5.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = buttonElevation),
                    shape = RectangleShape
                ) {
                    if (press) {
                        Image(
                            painter = painterResource(id = R.drawable.cross),
                            contentDescription = null
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Black)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(buttonColor),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(end = 5.dp, bottom = 5.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = buttonElevation),
                    shape = RectangleShape
                ) {

                }
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(buttonColor),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(end = 5.dp, bottom = 5.dp),

                    elevation = ButtonDefaults.buttonElevation(defaultElevation = buttonElevation),
                    shape = RectangleShape
                ) {

                }
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(buttonColor),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(bottom = 5.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = buttonElevation),
                    shape = RectangleShape
                ) {

                }
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Black)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(buttonColor),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(end = 5.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = buttonElevation),
                    shape = RectangleShape
                ) {

                }
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(buttonColor),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .padding(end = 5.dp),

                    elevation = ButtonDefaults.buttonElevation(defaultElevation = buttonElevation),
                    shape = RectangleShape
                ) {

                }
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(buttonColor),
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = buttonElevation),
                    shape = RectangleShape
                ) {

                }
            }
        }
    }
}