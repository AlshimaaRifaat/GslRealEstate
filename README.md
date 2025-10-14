# GSL Real Estate - Android Application

A professional real estate listing application built with modern Android development practices, Clean Architecture, and MVI pattern.

## Features

- **Listings Screen**: Browse real estate properties with beautiful cards
- **Details Screen**: View detailed information about each property
- **Responsive Design**: Material Design 3 with light/dark theme support
- **Offline-Ready Architecture**: Clean separation of concerns for scalability

## Architecture

### Clean Architecture + MVI (Model-View-Intent)

The application follows Clean Architecture principles with three main layers:

### Module Structure

- **`app`**: Main application module with navigation and dependency injection setup
- **`presentation`**: UI layer with Jetpack Compose screens, ViewModels, and Design System
- **`domain`**: Business logic with use cases, domain models, and repository interfaces
- **`data`**: Data sources, API services, DTOs, and repository implementations

## Design System

### Atomic Design Approach

- **Atoms**: Basic components (LoadingIndicator)
- **Molecules**: Simple combinations (ErrorMessage)
- **Organisms**: Complex components (PropertyCard)
- **Templates/Pages**: Full screens (ListingsScreen, DetailsScreen)

### White Label Support

- Centralized theming with Material Design 3
- Dynamic color support (Android 12+)
- Easy brand customization

## Tech Stack

### Core
- **100% Kotlin**: Modern, concise, and safe
- **Jetpack Compose**: Declarative UI framework
- **Coroutines & Flow**: Reactive programming
- **Hilt (Dagger)**: Dependency injection

### Architecture & Patterns
- **Clean Architecture**: Separation of concerns
- **MVI Pattern**: Unidirectional data flow
- **SOLID Principles**: Maintainable and scalable code
- **Repository Pattern**: Data abstraction

### Networking
- **Retrofit**: REST API client
- **OkHttp**: HTTP client with logging
- **Gson**: JSON serialization

### UI & Navigation
- **Material Design 3**: Modern UI components
- **Navigation Component**: Type-safe navigation
- **Coil**: Image loading

### Testing
- **JUnit**: Unit testing framework
- **MockK**: Mocking library
- **Turbine**: Flow testing
- **Compose UI Testing**: Automated UI tests

## SOLID Principles Implementation

1. **Single Responsibility Principle (SRP)**
   - Each class has one clear responsibility
   - ViewModels handle only state management
   - Use cases encapsulate single business operations

2. **Open/Closed Principle (OCP)**
   - Base interfaces (UiState, UiIntent, UiEvent) are open for extension
   - Sealed classes for type-safe states and events

3. **Liskov Substitution Principle (LSP)**
   - Repository implementations are interchangeable
   - Interface-based design throughout

4. **Interface Segregation Principle (ISP)**
   - Small, focused interfaces (ListingRepository)
   - Contract-based MVI components

5. **Dependency Inversion Principle (DIP)**
   - Domain layer defines interfaces
   - Data layer implements them
   - High-level modules don't depend on low-level modules

## Testing

### Unit Tests
-  ViewModel tests with coroutine testing
-  Use case tests
-  Repository tests with MockK
-  Flow testing with Turbine

### UI Tests
-  Compose UI tests for all screens
-  Navigation testing
-  User interaction testing

##  Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 11 or higher
- Android SDK 24+ (Nougat)

### Build & Run

1. Clone the repository:
```bash
git clone https://github.com/AlshimaaRifaat/GslRealEstate.git
cd GslRealEstate
```

2. Open in Android Studio

3. Sync Gradle files

4. Run the app:
```bash
./gradlew installDebug
```

### Run Tests

```bash
# Unit tests
./gradlew test

# Instrumentation tests
./gradlew connectedAndroidTest
```

## API Endpoints

- **Listings**: `GET https://gsl-apps-technical-test.dignp.com/listings.json`
- **Details**: `GET https://gsl-apps-technical-test.dignp.com/listings/{listingId}.json`

## Modularization Benefits

1. **Faster Build Times**: Only changed modules rebuild
2. **Better Separation**: Clear boundaries between layers
3. **Reusability**: Modules can be reused across projects
4. **Team Scalability**: Teams can work on different modules
5. **Testing**: Easier to test in isolation

##  Code Quality

- **Detekt**: Static code analysis
- **ProGuard**: Code obfuscation and optimization
- **Consistent Coding Style**: Kotlin conventions
- **Documentation**: KDoc comments throughout

##  Future Enhancements

- [ ] Room database for offline caching
- [ ] DataStore for preferences
- [ ] Firebase Crashlytics for crash reporting
- [ ] Google Tag Manager for analytics
- [ ] SonarQube integration
- [ ] AWS CodeArtifact for package management

## Development Process

This project was developed following professional standards:
- Clean Architecture implementation
- MVI pattern for reactive UI
- Comprehensive testing strategy
- Design System with Atomic Design
- Modular architecture for scalability


