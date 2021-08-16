# Backend app for creating poll and casting vote in different choices

## Setup

1. Clone:
```bash
git clone https://github.com/nhAnik/spring-poll-app-server.git
cd spring-poll-app-server
```
2. Install and setup postgres. You can use any other relational database. In that case, change `spring.datasource.url` in `application.yml` file.

3. Create a database.
```bash
CREATE DATABASE poll;
```
4. Change `spring.datasource.username` and `spring.datasource.password` in `application.yml` file.

5. Now, you are all set to run the app.
