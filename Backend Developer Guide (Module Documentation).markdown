## Backend Developer Guide (Module Documentation)

**Target Audience:** Backend Engineers

**Module:** `com.swipetoeat.backend.dietary`

**Base Package:** `com.swipetoeat.backend`

### 1. Domain Model Logic

The core business logic is encapsulated within the `UserPreference` entity and the associated Enums.

#### Enum: `Allergen`

* **Structure**: Each enum constant (e.g., `PEANUT`, `DAIRY`) is initialized with a `List<String>` containing specific forbidden ingredients.
* **Mappings**: For example, `PEANUT` maps to "PEANUT", "PEANUT BUTTER", and "SATAY SAUCE".
* **Purpose**: This allows the system to filter by specific ingredient keywords rather than just high-level categories.

#### Enum: `DietType`

* **Structure**: Defines the foundational dietary restriction (e.g., `VEGETARIAN`, `VEGAN`).
* **Items**: Contains the baseline list of `forbiddenItems` (e.g., `VEGETARIAN` forbids "BEEF", "PORK", etc.).

#### Entity: `UserPreference`

* **Primary Key**: `userId` (String).
* **DietType Mapping**: Stored as a string in the database via `@Enumerated(EnumType.STRING)`.
* **Allergen Collection**:
* Uses `@ElementCollection` to store a list of `Allergen` enums.
* **Fetch Strategy**: Set to `FetchType.EAGER` to ensure that when a user profile is loaded, their allergies are immediately available for filtering calculations without additional database queries.


* **Logic Engine (`getAllForbiddenItems`)**:
* Initializes a list with items from the selected `DietType`.
* Iterates through the `allergies` list to append all ingredient-specific forbidden items.
* **Stream Processing**: Uses `.stream().distinct().toList()` to return a unique, flat list of all restricted keywords.



---

### 2. Service Layer Logic

The `DietaryService` handles the orchestration and input validation.

* **Options Retrieval**: `getDietaryOptions()` uses Java Streams to map the `DietType` and `Allergen` enum values into `List<String>` for frontend consumption.
* **Update Logic**:
* **Fail-Fast Validation**: Explicitly checks if `dietType` is null before creating the entity.
* **Exception Handling**: Throws an `IllegalArgumentException` with the message `"You must select a Diet Type"` if validation fails.
* **Persistence**: Leverages `dietaryRepository.save()` to perform an upsert based on the `userId`.



---

### 3. API Controller & Exception Mapping

The `DietaryController` manages the REST interface and specialized error responses.

* **Request Mapping**:
* `GET /options`: Calls service to provide enum names.
* `PUT /users/{userId}`: Uses a static inner class `UpdateRequest` to capture the `dietType` and `List<Allergen>` from the request body.


* **Global Handler**:
* The `@ExceptionHandler(IllegalArgumentException.class)` captures validation failures from the service layer.
* **Response**: Returns a `400 Bad Request` status with the exception message as the body.



---

### 4. Technical Environment

* **Java Version**: 21 (Temurin).
* **ORM**: Hibernate/JPA.
* **Database**: H2 (In-memory/Runtime) or configured SQL database via `DietaryRepository`.
