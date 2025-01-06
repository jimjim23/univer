package com.universitinder.app.faq

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universitinder.app.R
import com.universitinder.app.models.FAQ
import com.universitinder.app.models.UserState
import compose.icons.FeatherIcons
import compose.icons.feathericons.Send
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQScreen(faqViewModel: FAQViewModel) {
    val uiState by faqViewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val currentUser = UserState.currentUser
    val coroutineScope = rememberCoroutineScope()
    val autoGenerated = stringResource(R.string.faq_autogenerated_startup)

    LaunchedEffect(true) {
        faqViewModel.refresh(autoGenerated)
    }

    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            coroutineScope.launch {
                lazyListState.animateScrollToItem(uiState.messages.size - 1)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "FAQ") },
                navigationIcon = {
                    IconButton(onClick = { faqViewModel.popActivity() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        when (uiState.fetchingLoading) {
            true -> {
                Box(modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            false -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    LazyColumn(
                        state = lazyListState,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxHeight(0.8f)
                            .fillMaxWidth()
                    ) {
                        items(uiState.messages) { message ->
                            if (message.from == MessagesFrom.SYSTEM) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(0.7f)
                                        .padding(10.dp)
                                ){
                                    Column {
                                        Text(text = "SYSTEM", fontSize = 10.sp)
                                        OutlinedCard {
                                            Text(modifier = Modifier.padding(8.dp), text = message.message, fontSize = 12.sp)
                                        }
                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.End
                                ){
                                    Column(
                                        horizontalAlignment = Alignment.End
                                    ){
                                        Text(text = currentUser?.name!!, fontSize = 10.sp)
                                        OutlinedCard(
                                            modifier = Modifier.fillMaxWidth(0.7f)
                                        ){
                                            Text(modifier = Modifier.padding(8.dp), text = message.message, fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ){
                        items(uiState.faqs) {document ->
                            val faq = document.toObject(FAQ::class.java)
                            Card(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .clickable { faqViewModel.onFaqClick(faq!!) }
                            ) {
                                Text(text = faq?.question!!, fontSize = 12.sp, modifier = Modifier.padding(10.dp))
                            }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ){
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(0.9f),
                            placeholder = { Text(text = "Type a message...", fontSize = 12.sp) },
                            value = uiState.userMessage,
                            singleLine = false,
                            shape = RoundedCornerShape(50),
                            textStyle = TextStyle.Default.copy(fontSize = 12.sp),
                            onValueChange = faqViewModel::onUserMessageChange
                        )
                        Icon(
                            FeatherIcons.Send,
                            contentDescription = "Send",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable { faqViewModel.sendMessage(autoGenerated) }
                        )
                    }
                }
            }
        }
    }
}