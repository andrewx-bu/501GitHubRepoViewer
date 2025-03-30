package com.example.githubrepoviewerapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.ForkLeft
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.PublicOff
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.githubrepoviewerapp.ui.theme.GitHubRepoViewerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GitHubRepoViewerAppTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: MainActivityViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val repoList by viewModel.repoList.collectAsState()
    val errorMsg by viewModel.errorMsg.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val currPage by viewModel.currPage.collectAsState()
    val hasMorePages by viewModel.hasMorePages.collectAsState()

    Scaffold {
        Column(modifier = Modifier.padding(15.dp)) {
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = query,
                onQueryChange = { query = it },
                onSearch = {
                    active = false
                    if (query.isNotEmpty()) {
                        viewModel.resetState()
                        viewModel.fetchRepos(query, 1)
                    }
                },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text(text = "Search GitHub username") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon") },
                trailingIcon = {
                    if (active) {
                        Icon(
                            modifier = Modifier.clickable {
                                if (query.isNotEmpty()) {
                                    query = ""
                                } else {
                                    active = false
                                }
                            },
                            imageVector = Icons.Default.Close, contentDescription = "Close Icon"
                        )
                    }
                }
            ) {
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    !errorMsg.isNullOrEmpty() -> {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = { viewModel.loadPreviousPage(query) },
                                    enabled = currPage > 1,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Previous")
                                }

                                Text(
                                    text = "Page $currPage",
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(horizontal = 15.dp)
                                )

                                Button(
                                    onClick = { viewModel.loadNextPage(query) },
                                    enabled = hasMorePages,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Next")
                                }
                            }

                            Text(
                                modifier = Modifier.padding(15.dp),
                                text = errorMsg!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    repoList.isEmpty() -> {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = { viewModel.loadPreviousPage(query) },
                                    enabled = currPage > 1,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Previous")
                                }

                                Text(
                                    text = "Page $currPage",
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(horizontal = 15.dp)
                                )

                                Button(
                                    onClick = { viewModel.loadNextPage(query) },
                                    enabled = hasMorePages,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Next")
                                }
                            }

                            Text(
                                modifier = Modifier.padding(15.dp),
                                text = "Enter a username to search"
                            )
                        }
                    }
                    else -> {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = { viewModel.loadPreviousPage(query) },
                                    enabled = currPage > 1,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Previous")
                                }

                                Text(
                                    text = "Page $currPage",
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(horizontal = 15.dp)
                                )

                                Button(
                                    onClick = { viewModel.loadNextPage(query) },
                                    enabled = hasMorePages,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Next")
                                }
                            }

                            RepoListScreen(repoList)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RepoListScreen(repos: List<Repo>) {
    LazyColumn(
        modifier = Modifier.padding(top = 15.dp)
    ) {
        items(repos) { repo ->
            RepoItem(repo)
        }

    }
}

@Composable
fun RepoItem(repo: Repo) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AsyncImage(
                    model = repo.owner.avatarURL,
                    contentDescription = "Owner avatar",
                    modifier = Modifier
                        .size(50.dp)
                )

                Spacer(modifier = Modifier.size(25.dp))

                Column(
                    modifier = Modifier.weight(5f)
                ) {
                    Text(
                        text = repo.name,
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Row(
                        modifier = Modifier.offset(x = (-10).dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatBox(icon = Icons.Default.Star, text = formatNumber(repo.stars))
                        Spacer(modifier = Modifier.width(8.dp))
                        StatBox(icon = Icons.Default.ForkLeft, text =  formatNumber(repo.forks))
                        Spacer(modifier = Modifier.width(8.dp))
                        StatBox(icon = Icons.Default.RemoveRedEye, text = formatNumber(repo.watchers))
                    }
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandMore else Icons.Default.ChevronRight,
                    contentDescription = "Expand",
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable { isExpanded = !isExpanded }
                        .size(28.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (repo.private) Icons.Default.PublicOff else Icons.Default.Public,
                            contentDescription = "Privacy Icon",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = if (repo.private) "Private: ${repo.htmlURL}" else "Public:  ${repo.htmlURL}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (!repo.description.isNullOrEmpty()) {
                        Text(
                            text = repo.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Text(
                            text = "No description available",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatBox(icon: ImageVector, text: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

fun formatNumber(number: Int): String {
    return when {
        number >= 1_000_000 -> "%.1fM".format(number / 1_000_000.0)
        number >= 1_000 -> "%.1fk".format(number / 1_000.0)
        else -> number.toString()
    }.removeSuffix(".0").replace(".0", "")
}

@Preview(showBackground = true)
@Composable
fun PreviewRepoSearchScreen() {
    GitHubRepoViewerAppTheme {
        MainScreen()
    }
}