# Recipe Finder

## Project Goal

Whenever I am home, I often have ingredients lying around, but it's hard to figure out what I can actually make with them. Looking up recipe websites makes me search for foods with one ingredient at a time and then compare the results myself, which takes up too much time. I want a program that takes the ingredients I have and shows meals that I could make with them.

I am going to build an app where the user can enter the ingredients they have and get back a list that ranks the meals that use the most of the ingredients they have. The user will be able to pick a meal and see the full list of ingredients as well as if they are missing any.

## MVP Scope

- Enter ingredients you have
- Search for meals that use each ingredient
- Count how many ingredients your meal uses and rank the top 5
- Pick a meal to see its full ingredient list and instructions
- Mark what ingredients are missing

## Future Plans

- Save favorite recipes to view them later
- Export missing ingredients for a shopping list
- Filter results by a certain cuisine or category of food

## Tech Stack

- **Language:** Java
- **Libraries and external tools:** Gson is the only outside library. The rest are built into Java.
- **Data Storage:** The MVP keeps everything in memory, so it doesn't save to a file.
- **Data Source:** Uses TheMealDB API called over HTTPS