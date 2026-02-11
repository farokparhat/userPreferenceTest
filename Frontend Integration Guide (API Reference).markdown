## Frontend Integration Guide (API Reference)

**Target Audience:** Frontend Developers

**Base URL:** `http://<server-ip>:8080`

**Module:** Dietary Preference Management

### 1. [GET] /options

**Description:** Fetches all valid categories for the UI selection menus. Use these strings exactly as provided when sending data back to the server.

* **Response Body (200 OK):**

```json
{
  "dietType": ["OMNIVORE", "VEGETARIAN", "VEGAN", "HALAL", "NOPORKNOLARD"],
  "allergens": ["PEANUT", "DAIRY", "GLUTEN", "SHELLFISH", "SOY"]
}

```

---

### 2. [PUT] /users/{userId}

**Description:** Updates or creates a preference profile for a specific user.

* **Path Parameter:** `userId` (String) — The unique identifier for the user.
* **Request Body (JSON):**

| Field | Type | Mandatory | Description |
| --- | --- | --- | --- |
| `dietType` | String | **Yes** | Must be one of the values from `/options`. |
| `allergies` | Array[String] | No | A list of `Allergen` values. Can be an empty array `[]`. |

#### Example Request:

```json
{
  "dietType": "HALAL",
  "allergies": ["PEANUT"]
}

```

#### Success Response (200 OK):

Returns the updated `UserPreference` object, including the generated `allForbiddenItems` list.

```json
{
  "userId": "user123",
  "dietType": "HALAL",
  "allergies": ["PEANUT"],
  "allForbiddenItems": ["NONHALAL", "PEANUT", "PEANUT BUTTER", "SATAY SAUCE"]
}

```

---

### 3. Error Handling (400 Bad Request)

The API implements strict validation for the `dietType` field.

#### Scenario: Missing Diet Type

If the `dietType` field is null or missing from the request body, the server will reject the update.

* **Status Code:** `400 Bad Request`
* **Response Body:**

```text
You must select a Diet Type

```

#### Scenario: Invalid Enum Value

If a string is provided that does not match the defined Enums (e.g., `"dietType": "KETO"`), the server will return a `400 Bad Request` due to JSON deserialization failure.

---

### Implementation Tips for Frontend:

1. **Mandatory Field:** Ensure UI form does not allow a "Save" action unless a `dietType` is selected.
2. **Case Sensitivity:** All values sent to the server (e.g., `VEGAN`, `DAIRY`) must be **UPPERCASE**.
3. **Data Persistence:** This endpoint uses an "upsert" logic; it will create a new record if the `userId` doesn't exist or update the existing one.
