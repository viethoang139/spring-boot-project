# BLOG REST APIS

- Features:

  - CRUD on post, category and comment
  - Register a user
  - Access token (expiration after 1 day)
  - Refresh token (expiration after 7 days)
  - Login, logout
  - Validation on request body

- Technology used:

  - Programming Language: Java
  - Frameworks: Spring Boot, Spring Security, Spring Validation, Spring Data JPA
  - Database: MySQL
  - Other: Docker to build MySQL, JWT to perform Login, logout, access token and refresh token, Lombok to reduce boilerplate code

- Roles:

  - Admin: CURD on post, comment and category
  - User: CRUD on comment and only allow to fetch post and category

- Relationship between entities:

  - Relationship between user and role: Many to Many (Many users can have many roles, and many roles can belong to many users)

    ![image](https://github.com/viethoang139/spring-boot-project/assets/93482932/e98de4d4-4505-442d-a1b2-4a602135fb29)

  - Relationship between user and token: One to Many and Many to One (One user can have many tokens, and many tokens belong to one user)

    ![image](https://github.com/viethoang139/spring-boot-project/assets/93482932/dc710b2f-2d30-4102-946d-0710d09eddf3)

  - Relationship between post and comment: One to Many and Many to One (One post can have many comments, and many comments belong to one post)

    ![image](https://github.com/viethoang139/spring-boot-project/assets/93482932/4d1d00d3-8bd3-42d4-aa7f-008162aecc2d)

  - Relationship between category and post: One to Many and Many to One (One category can have many posts, and many posts belong to one category)

    ![image](https://github.com/viethoang139/spring-boot-project/assets/93482932/c9333e6a-122b-47b9-ba24-51cf3682f506)




