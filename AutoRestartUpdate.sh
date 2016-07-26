#!/usr/bin/env bash
echo "Starting server..."

rm -rf TelegramBot.jar
wget https://circleci.com/api/v1/project/michael1011/telegrambot/latest/artifacts/0//home/ubuntu/telegrambot/out/TelegramBot.jar
java -jar TelegramBot.jar

