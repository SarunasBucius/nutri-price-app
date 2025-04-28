# Nutri Price

**Nutri Price** is a mobile app that allows users to store product prices and nutritional values,
create recipes using these products, and automatically calculate the total cost and nutritional
information of recipes.  
It serves as a helpful tool for **meal planning**, **budgeting**, and **maintaining dietary
awareness**.

## 🚀 Features

- **Product Management**: Add, edit, and view product details including prices and nutritional
  values.
- **Recipe Creation**: Combine products to create custom recipes.
- **Recipe Viewing**: View recipe details (ingredients list, instructions).
- **Upcoming**: Automatic calculation of **recipe total nutritional values** and **cost**.

## 🛠️ Tech Stack

- **Kotlin** (programming language)
- **Jetpack Compose** (UI toolkit)
- **Jetpack Navigation** (navigation component)
- **Apollo GraphQL** (networking with GraphQL APIs)

## 🌐 Backend / API

Nutri Price communicates primarily with a backend service via **GraphQL**, using the **Apollo
GraphQL Client**.  
In some cases, it also uses **OkHttp** or **Retrofit** for RESTful API calls.

Backend repo: https://github.com/SarunasBucius/nutri-price-server