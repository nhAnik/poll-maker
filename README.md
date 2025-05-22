# Poll Maker

A RESTful API-based polling application built with Spring Boot and PostgreSQL. Users can register, create polls, add choices, and cast votes.

---

## Tech Stack

- Java 17+
- Spring Boot
- PostgreSQL (via Docker Compose)
- Maven
- Docker & Docker Compose

---

## Features

- **User Registration & Login**
- **Create & View Poll Questions**
- **Add, Update & Delete Choices**
- **Cast Votes**
- **JWT-based Authentication**

---

## API Endpoints

### Auth
- `POST /register` — Register a new user
- `POST /login` — Login and retrieve JWT token

### Polls
- `GET /polls` — Get all poll questions
- `POST /polls` — Create a new poll with choices
- `GET /polls/{qid}` — Get specific poll by ID
- `PUT /polls/{qid}` — Update poll question text
- `DELETE /polls/{qid}` — Delete poll question
- `GET polls/{qid}/result` - Get result of a poll

### Choices
- `POST /polls/{qid}/choices` — Add a new choice to a poll
- `PUT /polls/{qid}/choices/{cid}` — Update a choice
- `DELETE /polls/{qid}/choices/{cid}` — Delete a choice

### Voting
- `POST /polls/{qid}/votes` — Cast a vote for a choice

---

## Build & Run

### 1. Clone the Repository

```bash
git clone https://github.com/nhAnik/poll-maker.git
cd poll-maker
```

### 2. Run

```bash
./mvnw spring-boot:run
```

App runs at: `http://localhost:8080`

---

## Testing

### Run Unit & Integration Tests

```bash
./mvnw test
```
