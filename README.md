# Anti-Fraud System

## Project Description

Welcome to the Anti-Fraud System project! In this project, we are building an
authentication and authorization system using the Spring Security module to
enhance the security of our enterprise application. Our goal is to provide
different levels of access and permissions to various parts of the system for
different users, ensuring a robust and secure environment.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Authentication Setup](#authentication-setup)
- [Authorization Implementation](#authorization-implementation)

## Prerequisites

Before you dive into setting up the authentication and authorization for our
Anti-Fraud System, make sure you have a solid understanding of the following
concepts:

- Relational Data Model
- SQL and its basics
- Basic data types
- Literals in programming
- Arithmetic expressions
- Familiarity with joining tables

## Authentication Setup

Our enterprise application requires a robust authentication mechanism to ensure
secure access for different types of users. For this purpose, we will be using
Spring Security, a battle-tested module that offers reliable authentication
methods.

In this stage of the project, your tasks include:

1. Implementing HTTP Basic authentication for our REST service.
2. Utilizing JDBC implementations of `UserDetailService` for effective user
   management.
3. Creating an endpoint for user registration at `POST /api/auth/user`.

Feel free to explore and elaborate on these tasks, but it's a good practice to
leverage the Spring Security module's existing functionality for efficiency and
reliability.

## Authorization Implementation

Authorization is a crucial step following authentication, where the system
determines whether an authenticated client has permission to access the
requested resource. In our system, we have defined different roles for various
types of users:

- `ANONYMOUS`: Limited access, primarily for registering new users.
- `MERCHANT`: Access to check the validity of transactions.
- `ADMINISTRATOR`: Full control over user management and system resources.
- `SUPPORT`: Access to manage reported stolen cards/IPs.

Here is an overview of the permissions associated with each role:

| Endpoint                        | ANONYMOUS | MERCHANT | ADMINISTRATOR | SUPPORT |
|---------------------------------|-----------|----------|---------------|---------|
| POST /api/auth/user             | +         | +        | +             | +       |
| DELETE /api/auth/user           | -         | -        | +             | -       |
| GET /api/auth/list              | -         | -        | +             | +       |
| POST /api/antifraud/transaction | -         | +        | -             | -       |
| PUT /api/auth/access            | -         | -        | +             | -       |
| PUT /api/auth/role              | -         | -        | +             | -       |

Roles are assigned as follows:

- The first registered user is automatically assigned the `ADMINISTRATOR` role.
- All subsequent users receive the `MERCHANT` role by default and are initially
  locked.
- The `ADMINISTRATOR` role is responsible for unlocking new users and assigning
  the `SUPPORT` role.

By implementing these roles and permissions, we ensure a secure and controlled
access environment for our Anti-Fraud System.

