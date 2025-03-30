# GitHub Repo Viewer App

For CS501 - Mobile App Development class. Uses MVVM architecture.

## Features

- **User Search**: Search for any GitHub user's public repositories
- **Pagination**: Load repositories in pages (30 per page) with Next/Previous buttons
- **API Integration**: Fetches data from [GitHub REST API](https://docs.github.com/en/rest)
- **Repo Details**: Displays repository name, description, stars, forks, and watchers
- **State Management**: Uses `StateFlow` for handling loading, pagination, data, and error states

## Technologies

- **Retrofit** for API calls
- **Moshi** for JSON parsing
- **OkHttp** as HTTP client with logging interceptor
- **Coil** for image loading
- **Jetpack Compose** for modern UI
- **Flow/StateFlow** for reactive state management
