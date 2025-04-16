# Crypto Tracker Portfolio Project

## Overview
This is a **Crypto Tracker** application built as part of my portfolio project. The goal of this project is to track cryptocurrency prices, market caps, and other relevant data in real-time. This application integrates with external APIs, such as CoinGecko, to provide accurate and up-to-date information about various cryptocurrencies. The app is designed to help users track the market and make informed decisions.

## Features
- **Real-Time Data Fetching**: Integrates with the CoinGecko API to fetch real-time cryptocurrency data (prices, market cap, etc.).
- **Data Storage**: Allows storing and managing cryptocurrency data within the application.
- **Search Functionality**: Enables users to search for specific cryptocurrencies by symbol.
- **Price and Market Cap Tracking**: Displays price, market cap, and other relevant metrics for tracked cryptocurrencies.

## Technologies Used
- **Spring Boot**: Backend framework for building the RESTful API.
- **WebClient**: For making asynchronous HTTP requests to external APIs (like CoinGecko).
- **Java**: Core programming language for building the application.
- **JUnit & Mockito**: For unit testing and mocking external services.
- **Docker**: Containerization for development and deployment.

## Installation

### Clone the Repository
First, clone the repository to your local machine:
git clone https://github.com/EmerF/crypto-tracker.git


### Prerequisites
Make sure you have the following installed:
- Java 17+
- Maven (for managing dependencies and building the project)
- Docker

### Build Using Docker compose
Creating app with all resources:
```bash
  docker-compose down -v # clean older installations
  docker-compose up -d
```

### Build and Run the Application
Navigate to the project directory(Start Postgres and Splunk):

```bash
  cd crypto-tracker
```

Build the project with Maven:
```bash
  mvn clean install
```
Run the application:
```bash
  mvn spring-boot:run
```
Testing:
##### ** Fetch data from api and save to database. Uses cache, saving only on first execution **
```bash
  curl http://localhost:8080/coins/fetch/btc
```
Getting From Database:
##### ** Get data from Postgres **
```bash
  curl http://localhost:8080/coins/btc
```

Inspect Volume Logs:
```bash
  docker volume inspect cryptotracker_app_logs
```

#### Splunk Search(http://localhost:8000/en-US/app/search/search):

type in search menu:

index=main
