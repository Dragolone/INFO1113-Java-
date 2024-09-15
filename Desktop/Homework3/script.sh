#!/bin/bash

if [ "$#" -ne 2 ]; then
    echo "Usage: $0 student.csv 'Name'"
    exit 1
fi

csv_file=$1
name=$2

grep "$name" "$csv_file" | while IFS=',' read -r full_name id email; do
    if [[ "$full_name" == *"$name"* ]]; then
        echo "Name: $full_name, Email: $email"
    fi
done
