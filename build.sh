#!/bin/bash

# Build script for Android LLM project

echo "Building Android LLM project..."

# Check if gradle is available
if ! command -v gradle &> /dev/null; then
    echo "Gradle is not installed. Please install Gradle first."
    exit 1
fi

# Clean build
echo "Cleaning previous builds..."
./gradlew clean

# Build the project
echo "Building project..."
./gradlew build

echo "Build completed!"