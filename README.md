# Posts app

# Features
The app displays a list of posts and allows the user to add, edit, delete a post. Users can see details about each post.

# Development Environment
The app is written entirely in Kotlin and uses the Gradle build system.

# Architecture
The architecture pattern is MVVM
[Android Architecture Components](https://developer.android.com/topic/libraries/architecture/).

- When deciding on the architecture for the app. We kept logic away from Activities and Fragments and moved it to ViewModels.
- We used a Repository layer for handling data operations.
- We used Navigation component to simplify app into a single Activity app.
- We used Jetpack Room Library for data caching and Retrofit for fetching data from service - applied the single source of truth principle.
- We used Hilt for dependency injection which allows us to remove 75% of the dependency injection code compared to Dagger2.
- We used safe args gradle plugins which is strongly recommended for navigating and passing data, because it ensures type-safety.
- We used Kotlin coroutines for concurrency as they lightweight, support cancellation and simplify code that executes asynchronously.
- We used View binding feature that allows to more easily write code that interacts with view.
- We used Jetpack Paging 2 which allows app to use both network bandwidth and system resources more efficiently. 

