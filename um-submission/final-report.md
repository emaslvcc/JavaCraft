<script type="text/javascript"
    src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_CHTML">
</script>
<script type="text/x-mathjax-config">
    MathJax.Hub.Config({
        tex2jax: {
            inlineMath: [['$','$'], ['\\(','\\)']],
            processEscapes: true
        },
        jax: ["input/TeX","input/MathML","input/AsciiMath","output/CommonHTML"],
        extensions: ["tex2jax.js","mml2jax.js","asciimath2jax.js","MathMenu.js","MathZoom.js","AssistiveMML.js", "[Contrib]/a11y/accessibility-menu.js"],
        TeX: {
            extensions: ["AMSmath.js","AMSsymbols.js","noErrors.js","noUndefined.js"],
            equationNumbers: {
                autoNumber: "AMS"
            }
        }
    });
</script>

# Tacita’s JavaCraft - Provisional Report (Group 18)

## Table of Contents

1. [Tacita’s JavaCraft - Provisional Report (Group 18)](#tacitas-javacraft---provisional-report-group-18)
   1. [Table of Contents](#table-of-contents)
   2. [Group Details](#group-details)
      1. [Participating Students](#participating-students)
   3. [Introduction](#introduction)
   4. [JavaCraft’s Workflow](#javacrafts-workflow)
   5. [Functionality Exploration](#functionality-exploration)
      1. [Code Repetition](#code-repetition)
   6. [Finite State Automata (FSA) Design](#finite-state-automata-fsa-design)
      1. [Secret door logic (boolean secretDoorUnlocked)](#secret-door-logic-boolean-secretdoorunlocked)
   7. [Git Collaboration \& Version Control](#git-collaboration--version-control)
      1. [Overview](#overview)
   8. [Extending the game code](#extending-the-game-code)
      1. [Blocktypes](#blocktypes)
      2. [Crafted Items](#crafted-items)
      3. [Interacting with Flags API](#interacting-with-flags-api)
   9. [Conclusion](#conclusion)
   10. [Who Did What?](#who-did-what)
   11. [Appendix](#appendix)
       1. [Class JavaCraft](#class-javacraft)
       2. [Extending the Gamecode](#extending-the-gamecode)
       3. [void clearScreen()](#void-clearscreen)
       4. [void craftIronIngot()](#void-craftironingot)
       5. [void craftItem(int recipe)](#void-craftitemint-recipe)
       6. [void craftStick()](#void-craftstick)
       7. [void craftWoodenPlanks()](#void-craftwoodenplanks)
       8. [void displayCraftingRecipes()](#void-displaycraftingrecipes)
       9. [void displayInventory()](#void-displayinventory)
       10. [void fillInventory()](#void-fillinventory)
       11. [void generateWorld()](#void-generateworld)
       12. [char getBlockChar(int blockType)](#char-getblockcharint-blocktype)
       13. [String getBlockName(int blockType)](#string-getblocknameint-blocktype)
       14. [String getBlockSymbol(int blockType)](#string-getblocksymbolint-blocktype)
       15. [String getCraftedItemName(int craftedItem)](#string-getcrafteditemnameint-crafteditem)
       16. [void loadGame(String fileName)](#void-loadgamestring-filename)
       17. [void lookAround()](#void-lookaround)
       18. [void placeBlock(int blockType)](#void-placeblockint-blocktype)
       19. [Additional documentation](#additional-documentation)
   12. [References](#references)

<div style="page-break-after: always;"></div>

## Group Details

<table>
  <tr>
    <th>Group Name</th>
    <td>Tacita</td>
  </tr>
  <tr>
    <th>Group Number</th>
    <td>18</td>
  </tr>
  <tr>
    <th>TA</th>
    <td>TA assigned to Group 18</td>
  </tr>
</table>

### Participating Students

| Student Name    | Student ID |
| --------------- | ---------- |
| Leopold Meinel  | i6352276   |
| Anton Haarmann  | i6367288   |
| Sian Lodde      | i6343174   |
| Tristan Dormans | i6343359   |

## Introduction

Meet JavaCraft, the first project we were assigned in our University journey. JavaCraft is a very simplified version of the game Minecraft that is set in a two dimensional world that is visualized using ASCII characters.

For this project, we are given a code for the JavaCraft game. That code is, what we are meant to work on.

We are supposed to expand the game in different aspects like adding new items or crafting recipes to it and documenting and understanding it, which we should show in the form of code descriptions, flowcharts, pseudocodes, automatas.

So far we've already learned a lot from this project!

<div style="page-break-after: always;"></div>

## JavaCraft’s Workflow

See [Appendix](#class-javacraft)

## Functionality Exploration

See [Appendix](#void-clearscreen) for documentation of all functions and flowcharts and pseudocodes of 16 functions.

### Code Repetition

`getBlockSymbol` contains code repetition in its switch statement, where each block contains a different color that corresponds to a different block.

This also occurs in multiple functions like `getBlockChar`, `getBlockTypeFromCraftedItem`, `getCraftedItemFromBlockType`, `getRequiredItemForMining`, `craftItem`, `craftStonePickaxe`, `craftIronPickaxe`, `craftWoodenPlanks`, `craftStick`, `craftIronIngot`, `interactWithWorld`, `getBlockName` and `getCraftedItemColor`.

`inventoryContains` and `craftedItemsContains` are almost identical and the general concepts are exactly the same.

## Finite State Automata (FSA) Design

<!---
Start ./automata/secretDoorUnlocked.md
-->
### Secret door logic (boolean secretDoorUnlocked)

#### General Description

The secret door logic is triggered when `<boolean> secretDoorUnlocked` is true and will replace the map with an empty map containing a dutch flag. It will also replace the green player symbol with a blue one.

The `<boolean> secretDoorUnlocked` is true if the player supplies the following input in order:
1. `y` (caseless check)
2. Nothing OR anything other than `exit` (caseless check)
3. `unlock` (caseless check)
4. Nothing OR anything other than `exit` (caseless check)
5. Mandatory `a`, `c` AND `m` plus optional `y` AND/OR `unlock` in any order (caseless check, repetition is possible)
6. Nothing OR anything other than `exit` (caseless check)
7. `open` (caseless check)

After point 7, the `<boolean> secretDoorUnlocked` is true and the secret door logic triggers.

<div style="page-break-after: always;"></div>

#### Automaton

<img src="./automata/src/automaton-secretDoorUnlocked.svg" alt="automaton-secretDoorUnlocked.svg"/>

<div style="page-break-after: always;"></div>

#### Table

| State                 | y        | unlock   | a        | c        | m        | open     | exit     |
| --------------------- | -------- | -------- | -------- | -------- | -------- | -------- | -------- |
| $^{\rightarrow}q_{0}$ | $q_{1}$  | $q_{20}$ | $q_{20}$ | $q_{20}$ | $q_{20}$ | $q_{20}$ | $q_{20}$ |
| $q_{1}$               | $q_{1}$  | $q_{2}$  | $q_{1}$  | $q_{1}$  | $q_{1}$  | $q_{1}$  | $q_{19}$ |
| $q_{2}$               | $q_{2}$  | $q_{2}$  | $q_{3}$  | $q_{8}$  | $q_{13}$ | $q_{2}$  | $q_{19}$ |
| $q_{3}$               | $q_{3}$  | $q_{3}$  | $q_{3}$  | $q_{4}$  | $q_{6}$  | $q_{2}$  | $q_{19}$ |
| $q_{4}$               | $q_{4}$  | $q_{4}$  | $q_{4}$  | $q_{4}$  | $q_{5}$  | $q_{2}$  | $q_{19}$ |
| $q_{5}$               | $q_{5}$  | $q_{5}$  | $q_{5}$  | $q_{5}$  | $q_{5}$  | $q_{18}$ | $q_{19}$ |
| $q_{6}$               | $q_{6}$  | $q_{6}$  | $q_{6}$  | $q_{7}$  | $q_{6}$  | $q_{2}$  | $q_{19}$ |
| $q_{7}$               | $q_{7}$  | $q_{7}$  | $q_{7}$  | $q_{7}$  | $q_{7}$  | $q_{18}$ | $q_{19}$ |
| $q_{8}$               | $q_{8}$  | $q_{8}$  | $q_{9}$  | $q_{8}$  | $q_{11}$ | $q_{2}$  | $q_{19}$ |
| $q_{9}$               | $q_{9}$  | $q_{9}$  | $q_{9}$  | $q_{9}$  | $q_{10}$ | $q_{2}$  | $q_{19}$ |
| $q_{10}$              | $q_{10}$ | $q_{10}$ | $q_{10}$ | $q_{10}$ | $q_{10}$ | $q_{18}$ | $q_{19}$ |
| $q_{11}$              | $q_{11}$ | $q_{11}$ | $q_{12}$ | $q_{11}$ | $q_{11}$ | $q_{2}$  | $q_{19}$ |
| $q_{12}$              | $q_{12}$ | $q_{12}$ | $q_{12}$ | $q_{12}$ | $q_{12}$ | $q_{18}$ | $q_{19}$ |
| $q_{13}$              | $q_{13}$ | $q_{13}$ | $q_{16}$ | $q_{14}$ | $q_{13}$ | $q_{1}$  | $q_{19}$ |
| $q_{14}$              | $q_{14}$ | $q_{14}$ | $q_{15}$ | $q_{14}$ | $q_{14}$ | $q_{2}$  | $q_{19}$ |
| $q_{15}$              | $q_{15}$ | $q_{15}$ | $q_{15}$ | $q_{15}$ | $q_{15}$ | $q_{18}$ | $q_{19}$ |
| $q_{16}$              | $q_{16}$ | $q_{16}$ | $q_{16}$ | $q_{17}$ | $q_{16}$ | $q_{2}$  | $q_{19}$ |
| $q_{17}$              | $q_{17}$ | $q_{17}$ | $q_{17}$ | $q_{17}$ | $q_{17}$ | $q_{18}$ | $q_{19}$ |
| $^{*}q_{18}$          | $q_{18}$ | $q_{18}$ | $q_{18}$ | $q_{18}$ | $q_{18}$ | $q_{18}$ | $q_{18}$ |
| $q_{19}$              | $q_{19}$ | $q_{19}$ | $q_{19}$ | $q_{19}$ | $q_{19}$ | $q_{19}$ | $q_{19}$ |
| $q_{20}$              | $q_{20}$ | $q_{20}$ | $q_{20}$ | $q_{20}$ | $q_{20}$ | $q_{20}$ | $q_{20}$ |
<!---
End ./automata/secretDoorUnlocked.md
-->

<div style="page-break-after: always;"></div>

## Git Collaboration & Version Control

### Overview

#### [UM Gitlab Repository, Branch Group 18](https://gitlab.maastrichtuniversity.nl/bcs1110/javacraft/-/tree/group18?ref_type=heads)

##### Git usage

We used Gitlab as our main collaboration method. By splitting up the tasks in a fair manner we divided the workload to be more efficient. Through Gitlab we kept each other up to date by making commits after every completed task.

That way everybody knew in what state the project was and how much still needed to be done. We also made sure to document our commits well, in an effort to better our understanding of the changes made.

Each one of us made multiple commits and used Gitlab extensively. This in return improved our team performance and also kept each other motivated to work on the project.

##### Changes & Conflicts

Merge conflicts were handled efficiently and quickly. As a team we all had our experiences with these conflicts, one example was that a local repository was a few key commits behind. This was solved by choosing what parts of the code to keep, and what parts of the code needed to be replaced by the newer version on the repository.

Some other issue we faced was not being able to merge in the first place, which was inevitably resolved by re-cloning the repository and pasting in our modified files, which we wanted to replace older files on the remote repository.

<div style="page-break-after: always;"></div>

## Extending the game code

### Blocktypes

The blocktypes we added are coal and emerald, we added them to the game by assigning them an integer value and an ANSI color. We had to change a few functions to be able to fully integrate them into the game.

The first being [`generateWorld()`](#void-generateworld) in which we tweaked the rate at which the blocks spawn in the world. We made sure to match their rarity.

We also had to make some minor changes, for instance assigning the color to the integer value in [`getBlockSymbol()`](#string-getblocksymbolint-blocktype), and assigning them ASCII characters in [`getBlockChar()`](#char-getblockcharint-blocktype). Afterwards we changed integer values in [`fillInventory()`](#void-fillinventory), [`placeBlock()`](#void-placeblockint-blocktype) and [`displayInventory()`](#void-displayinventory). This had to be done to match the new amount of blocktypes. Otherwise the game would've only used the old Blocktypes.

Additionally we assigned String values to the new blocktypes in [`getBlockName()`](#string-getblocknameint-blocktype), assigned each block to its color in [`getBlockSymbol()`](#string-getblocksymbolint-blocktype) and added them to the legend in [`displayLegend()`](#void-displaylegend). Whenever one of our blocks is mined, a message will also be printed [`interactWithWorld()`](#void-interactwithworld).

### Crafted Items

Our crafted items we added to the game are iron and stone pickaxe, crafting the stone pickaxe requires three stones and one stick, crafting the iron pickaxe requires three iron ingots and one stick.

We chose these items because we wanted to implement a mechanic, that only lets a player mine a block if he fulfills certain requirements. 

In this case for the player to be able to mine coal and iron blocks, he needs to have a stone pickaxe in their inventory. To be able to mine emerald blocks he needs an iron pickaxe.

To accomplish this, we had to first implement the crafted items. We did this in a similar fashion as the blocktypes by assigning them integer values and adding their values to the preexisting crafted items methods.

Afterwards we implemented the methods [`craftStonePickaxe()`](#void-craftstonepickaxe) and [`craftIronPickaxe()`](#void-craftironpickaxe) in which we specified the crafting requirements for each item. For this to work we had to add a new method [`removeItemFromCraftedItem()`](#void-removeitemfromcrafteditem), that removes items from the crafted items inventory. It als [`craftedItemsContains()`](#boolean-crafteditemcontains) that checks if the player has the amount of crafted items in their inventory.

The biggest change was the implementation of the mining requirements in [`mineBlock()`](#void-mineblock), we did this by checking for the blocktype that is going to be mined first and then checking if the player fits the requirements.

To do this we implemented a new method [`getRequiredItemForMining()`](#int-getrequireditemformining) which gets the blocktype as parameter and gives back the needed crafted item to be able to mine it.

<div style="page-break-after: always;"></div>

### Interacting with Flags API

We have rewritten the template function `getCountryAndQuoteFromServer()` to interact with the flags API at `https://flag.ashish.nl`.

The old code used a now deprecated constructor for URL: `new URL(String)`. Java complains with the following warning: `The constructor URL(String) is deprecated since version 20`. Therefore we decided on using `URI.create(String).toURL()` instead. This is not deprecated.

The rest of our code just uses the provided template which gets a country and a quote from the flags API via a POST request. Within the post request we send a JSON String containing the following:

- "group_number" : "18"
- "group_name" : "group18"
- "difficulty_level" : "hard"

This is meant to identify our group via it's name and number and lets the server know which difficulty level it should choose for the flag.

Since we only use this to know which flag we have to build, it wasn't necessary to pretty-print any response we get. Therefore we didn't work on that and didn't really change the code.

In our current code we have replaced `https://flag.ashish.nl/get_flag` with `https://example.com` to avoid unnecessary interactions with the API.

We got Sri Lanka as our first response and used a String to represent it's flag.

The result is the following:

<img src="./flag/src/screenshot-flag.png" alt="screenshot-flag.png" height="400"/>

<div style="page-break-after: always;"></div>

## Conclusion

We created flowcharts for 16 functions, tried to document the code in an organized fashion and as expected encountered no lack of issues along the way.

For instance, it was really challenging to fit the flowchart of the whole game on one page. Sian managed to do that regardless, even though he was grasping at straws.

Leo encountered some difficulties while constructing the FSA and had to redo the automaton multiple times. This was because of some misunderstandings about what was expected from us.

Anton faced some problems while adding new blocks and crafting recipes to the game. His difficulties were exaggerated due to the fact that he entered the course with minimal programming experience, nevertheless with enough persistance he managed to enrich JavaCraft's gameplay. 

We learned how to work together in a team and to manage and divide team tasks. Also included in our learning experience was learning to maintain a functioning and readable codebase and fighting over who gets to do what. We became skilled at reading and understanding code written by someone else, via pseudocode and flowcharts, this in turn greatly helped us advance our java knowledge.

In the final stages of our project, we managed to create a proper looking and well formatted pdf using markdown. We also learned how to use an API and how to draw a challenging flag using only UNICODE characters and 16 ANSI colors.

This project has been a very good start to our BSc Computer Science and helped us a lot with getting used to working on university projects at Maastricht University.

<div style="page-break-after: always;"></div>

## Who Did What?

| Task                                                              | Who worked on the task        | Participation in percentage  |
| ----------------------------------------------------------------- | ----------------------------- | ---------------------------- |
| Creating initial pseudocode and flowcharts                        | Leopold, Anton, Tristan, Sian | 20%, 20%, 40%, 20%           |
| Deciding on the uniformal format for pseudocode                   | Leopold, Anton, Tristan, Sian | 70%, 10%, 10%, 10%           |
| Converting pseudocode to uniformal format                         | Leopold                       | 100%                         |
| Implementing uniformal directory structure for repository         | Leopold                       | 100%                         |
| Creating initial documentation                                    | Leopold                       | 100%                         |
| Updating  documentation for JavaCraft code                        | Leopold, Anton, Tristan, Sian | Even across all participants |
| Creating FSA, table and documentation for automaton               | Leopold, Tristan              | 90%, 10%                     |
| Converting ODF Flowcharts to .graphml                             | Tristan                       | 100%                         |
| Deciding on the uniformal format for flowcharts                   | Leopold, Anton, Tristan, Sian | Even across all participants |
| Converting flowcharts to uniformal format                         | Sian, Tristan, Anton          | 80%, 10%, 10%                |
| Rework flowcharts to fit PDF                                      | Sian                          | 100%                         |
| Finding repetitions in code                                       | Sian                          | 100%                         |
| Implementing code to interact with flags API                      | Leopold, Sian                 | 10%, 90%                     |
| IMplementing code to create Sri Lanka's flag                      | Sian                          | 100%                         |
| Implementing two new blocks and two new crafting items            | Anton                         | 100%                         |
| Writing Conclusion                                                | Tristan                       | 100%                         |
| Writing Interacting with Flags API                                | Leopold                       | 100%                         |
| Writing Blocktypes and Crafted Items                              | Anton                         | 100%                         |
| Creating provisional report document, checking typos/grammar etc. | Leopold, Tristan, Anton, Sian | 70%, 10%, 10%, 10%           |
| Creating final report document, checking typos/grammar etc.       | Leopold, Tristan, Anton, Sian | Even across all participants |


<div style="page-break-after: always;"></div>

## Appendix

<!---
Start ./classes/description-JavaCraft.md
-->
### Class JavaCraft

#### Pseudocode

```java
BEGIN

Define global constants/variables and assign values to some;
Initialize game by assigning some global variables;
Generate world with different blocks by using randomness;
PRINT INFO `instructions`;
PRINT INFO "Start the game? (Y/N): ";
IF `<String> READ user input` == y (caseless check)
    Set `<boolean> unlockMode` = false;
    Set `<boolean> craftingCommandEntered` = false;
    Set `<boolean> miningCommandEntered` = false;
    Set `<boolean> movementCommandEntered` = false;
    WHILE true
        PRINT INFO `initial UI containing legend, world, inventory`;
        PRINT INFO "Enter your action: 'WASD': Move, 'M': Mine, 'P': Place, 'C': Craft, 'I': Interact, 'Save': Save, 'Load': Load, 'Exit': Quit, 'Unlock': Unlock Secret Door\n" (colored in green);
        IF `<String> READ user input` == "w" OR "up" OR "s" OR "down" OR "a" OR "left" OR "d" OR "right" (caseless check)
            IF `<boolean> unlockMode` == true
                Set `<boolean> movementCommandEntered` = true;
            Move player;
        ELSE IF `<String> READ user input` == "m" (caseless check)
            IF `<boolean> unlockMode` == true
                Set `<boolean> miningCommandEntered` = true;
            Mine block;
        ELSE IF `<String> READ user input` == "p" (caseless check)
            PRINT INFO `players inventory`;
            PRINT INFO "Enter the block type to place: ";
            Place block `<String> READ user input`;
        ELSE IF `<String> READ user input` == "c" (caseless check)
            PRINT INFO `crafting recipes`;
            PRINT INFO "Enter the recipe number to craft: ";
            Craft item `<String> READ user input`;
        ELSE IF `<String> READ user input` == "i" (caseless check)
            Interact with world;
        ELSE IF `<String> READ user input` == "save" (caseless check)
            PRINT INFO "Enter the file name to save the game state: ";
            Save game as `<String> READ user input`;
        ELSE IF `<String> READ user input` == "load" (caseless check)
            PRINT INFO "Enter the file name to load the game state: ";
            Load game from `<String> READ user input`;
        ELSE IF `<String> READ user input` == "exit" (caseless check)
```

<div style="page-break-after: always;"></div>

```java
            PRINT INFO "Exiting the game. Goodbye!\n";
            Exit game;
        ELSE IF `<String> READ user input` == "look" (caseless check)
            Print all blocks sorrounding player;
        ELSE IF `<String> READ user input` == "unlock" (caseless check)
            Set `<boolean> unlockMode` = true;
        ELSE IF `<String> READ user input` == "getflag" (caseless check)
            TRY TO
                Set up connection to a server;
                PRINT INFO " " + `<String> get country from server via a POST request`;
                PRINT INFO " " + `<String> get quote from server via a POST request`;
            ON EXCEPTION
                PRINT ERROR containing `stacktrace`;
                PRINT ERROR "Error connecting to the server";
            Wait on player to press ENTER;
        ELSE IF `<String> READ user input` == "open" (caseless check)
            IF `<boolean> unlockMode` == true AND `<boolean> craftingCommandEntered` == true AND `<boolean> miningCommandEntered` == true AND `<boolean> movementCommandEntered` == true
                Set `<boolean> secretDoorUnlocked` = true;
                Reset world to an empty world;
                PRINT INFO "Secret door unlocked!\n";
                Wait on player to press ENTER;
            ELSE
                PRINT WARNING "Invalid passkey. Try again!\n";
                Set `<boolean> unlockMode` = false;
                Set `<boolean> craftingCommandEntered` = false;
                Set `<boolean> miningCommandEntered` = false;
                Set `<boolean> movementCommandEntered` = false;
        ELSE
            PRINT WARNING "Invalid input. Please try again." (colored in yellow);
        IF `<boolean> unlockMode` == true
            IF `<String> READ user input` == "c" (caseless check)
                Set `<boolean> craftingCommandEntered` = true;
            IF `<String> READ user input` == "m" (caseless check)
                Set `<boolean> miningCommandEntered` = true;
        IF `<boolean> secretDoorUnlocked` == true
            PRINT INFO `description of current state`;
            Set `<boolean> inSecretArea` = true;
            Reset world to an empty world;
            Set `<boolean> secretDoorUnlocked` = false;
            Fill `<Integer list> inventory` with all available blockTypes;
            Wait on player to press ENTER;
ELSE
    Exit game;

END
```

<div style="page-break-after: always;"></div>

#### Flowchart

<img src="./classes/src/flowchart-JavaCraft.svg" alt="flowchart-JavaCraft.svg"/>
<!---
End ./classes/description-JavaCraft.md
-->

<div style="page-break-after: always;"></div>

### Extending the Gamecode

#### boolean craftedItemContains()

##### Documentation

<img src="./docs/src/docs-craftedItemsContains1.png" alt="docs-craftedItemsContains1.png"/>

##### Java

```java
public static boolean craftedItemsContains(int craftedItem, int count) {
        int craftedItemCount = 0;
        for (int i : craftedItems) {
            if (i == craftedItem) {
                craftedItemCount++;
                if (craftedItemCount == count) {
                    return true;
                }
            }
        }
        return false;
    }
```

<div style="page-break-after: always;"></div>

#### void craftIronPickaxe()

##### Documentation

<img src="./docs/src/docs-craftIronPickaxe.png" alt="docs-craftIronPickaxe.png"/>

##### Java

```java
public static void craftIronPickaxe() {
        if (craftedItemsContains(CRAFTED_STICK) && craftedItemsContains(CRAFTED_IRON_INGOT, 3)) {
            removeItemFromCraftedItems(CRAFTED_STICK, 1);
            removeItemFromCraftedItems(CRAFTED_IRON_INGOT, 3);
            addCraftedItem(CRAFTED_IRON_PICKAXE);
            System.out.println("Crafted Iron Pickaxe");
        } else {
            System.out.println("Insufficient resources to craft Stone Pickaxe");
        }
    }
```

<div style="page-break-after: always;"></div>

#### void craftStonePickaxe()

##### Documentation

<img src="./docs/src/docs-craftStonePickaxe.png" alt="docs-craftStonePickaxe.png"/>

##### Java

```java
public static void craftStonePickaxe() {
        if (craftedItemsContains(CRAFTED_STICK) && inventoryContains(STONE, 3)) {
            removeItemFromCraftedItems(CRAFTED_STICK, 1);
            removeItemsFromInventory(STONE, 3);
            addCraftedItem(CRAFTED_STONE_PICKAXE);
            System.out.println("Crafted Stone Pickaxe");
        } else {
            System.out.println("Insufficient resources to craft Stone Pickaxe");
        }
    }
```

<div style="page-break-after: always;"></div>

#### void displayLegend()

##### Documentation

<img src="./docs/src/docs-displayLegend.png" alt="docs-displayLegend.png"/>

##### Java

```java
public static void displayLegend() {
        System.out.println(ANSI_BLUE + "Legend:");
        System.out.println(ANSI_WHITE + "-- - Empty block");
        System.out.println(ANSI_RED + "\u2592\u2592 - Wood block");
        System.out.println(ANSI_GREEN + "\u00A7\u00A7 - Leaves block");
        System.out.println(ANSI_BLUE + "\u2593\u2593 - Stone block");
        System.out.println(ANSI_WHITE + "\u00B0\u00B0- Iron ore block");
        System.out.println(ANSI_COAL_GRAY + "\u2593\u2593 - Coal ore block");
        System.out.println(ANSI_EMERALD_GREEN + "\u00B0\u00B0 - Emerald ore block");
        System.out.println(ANSI_BLUE + "P - Player" + ANSI_RESET);
    }
```

<div style="page-break-after: always;"></div>

#### int getRequiredItemForMining()

##### Documentation

<img src="./docs/src/docs-getRequiredItemForMining.png" alt="docs-getRequiredItemForMining.png"/>

##### Java

```java
 public static int getRequiredItemForMining(int blockType) {
        switch (blockType) {
            case 4:
                return CRAFTED_STONE_PICKAXE;
            case 5:
                return CRAFTED_STONE_PICKAXE;
            case 6:
                return CRAFTED_IRON_PICKAXE;
            default:
                return -1;
        }
    }
```

<div style="page-break-after: always;"></div>

#### void interactWithWorld()

##### Documentation

<img src="./docs/src/docs-interactWithWorld.png" alt="docs-interactWithWorld.png"/>

##### Java

```java
public static void interactWithWorld() {
        int blockType = world[playerX][playerY];
        switch (blockType) {
            case WOOD:
                System.out.println("You gather wood from the tree.");
                inventory.add(WOOD);
                break;
            case LEAVES:
                System.out.println("You gather leaves from the tree.");
                inventory.add(LEAVES);
                break;
            case STONE:
                System.out.println("You gather stones from the ground.");
                inventory.add(STONE);
                break;
            case IRON_ORE:
                System.out.println("You mine iron ore from the ground.");
                inventory.add(IRON_ORE);
                break;
            case EMERALD_ORE:
                System.out.println("You mine emerald ore from the ground.");
                inventory.add(EMERALD_ORE);
                break;
            case COAL_ORE:
                System.out.println("You mine coal ore from the ground.");
                inventory.add(COAL_ORE);
                break;
            case AIR:
                System.out.println("Nothing to interact with here.");
                break;
            default:
                System.out.println("Unrecognized block. Cannot interact.");
        }
        waitForEnter();
    }
```

<div style="page-break-after: always;"></div>

#### void mineBlock()

##### Documentation

<img src="./docs/src/docs-mineBlock.png" alt="docs-mineBlock.png"/>

<div style="page-break-after: always;"></div>

##### Java

```java
public static void mineBlock() {
        int blockType = world[playerX][playerY];
        if (blockType == EMERALD_ORE) {
            if (craftedItems.contains(getRequiredItemForMining(blockType))) {
                inventory.add(blockType);
                world[playerX][playerY] = AIR;
                System.out.println("Mined " + getBlockName(blockType) + ".");
            } else {
                System.out.println(
                        "You need: " + getCraftedItemName(getRequiredItemForMining(blockType))
                                + ", to mine a " + getBlockName(blockType));
            }
        } else if (blockType == IRON_ORE || blockType == COAL_ORE) {
            if (craftedItems.contains(getRequiredItemForMining(blockType))) {
                inventory.add(blockType);
                world[playerX][playerY] = AIR;
                System.out.println("Mined " + getBlockName(blockType) + ".");
            } else {
                System.out.println(
                        "You need: " + getCraftedItemName(getRequiredItemForMining(blockType))
                                + ", to mine a " + getBlockName(blockType));
            }
        } else if (blockType != AIR) {
            inventory.add(blockType);
            world[playerX][playerY] = AIR;
            System.out.println("Mined " + getBlockName(blockType) + ".");
        } else {
            System.out.println("No block to mine here.");
        }
        waitForEnter();
    }
```

<div style="page-break-after: always;"></div>

#### void removeItemFromCraftedItem()

##### Documentation

<img src="./docs/src/docs-removeItemFromCraftedItems.png" alt="docs-removeItemFromCraftedItems.png"/>

##### Java

```java
public static void removeItemFromCraftedItems(int craftedItem, int count) {
        int removedCount = 0;
        Iterator<Integer> iterator = craftedItems.iterator();
        while (iterator.hasNext()) {
            int i = iterator.next();
            if (i == craftedItem) {
                iterator.remove();
                removedCount++;
                if (removedCount == count) {
                    break;
                }
            }
        }
    }
```

<div style="page-break-after: always;"></div>

<!---
Start ./functions/description-clearScreen.md
-->
### void clearScreen()

#### Documentation

<img src="./docs/src/docs-clearScreen.png" alt="docs-clearScreen.png"/>

#### Java

```java
private static void clearScreen() {
    try {
        if (System.getProperty("os.name").contains("Windows")) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } else {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    } catch (IOException | InterruptedException ex) {
        ex.printStackTrace();
    }
}
```

#### Pseudocode

```java
BEGIN

TRY TO
    IF current operating system matches Windows
        Clear screen using Windows cmd.exe by calling "/c cls";
        Wait on process to finish;
    ELSE
        Clear screen using ANSI code;
ON EXCEPTION
    PRINT ERROR containing `stacktrace`;

END
```

<div style="page-break-after: always;"></div>

#### Flowchart

<img src="./functions/src/flowchart-clearScreen.svg" alt="flowchart-clearScreen.svg"/>
<!---
End ./functions/description-clearScreen.md
-->

<div style="page-break-after: always;"></div>

<!---
Start ./functions/description-craftIronIngot.md
-->
### void craftIronIngot()

#### Documentation

<img src="./docs/src/docs-craftIronIngot.png" alt="docs-craftIronIngot.png"/>

#### Java

```java
public static void craftIronIngot() {
    if (inventoryContains(IRON_ORE, 3)) {
        removeItemsFromInventory(IRON_ORE, 3);
        addCraftedItem(CRAFTED_IRON_INGOT);
        System.out.println("Crafted Iron Ingot.");
    } else {
        System.out.println("Insufficient resources to craft Iron Ingot.");
    }
}
```

#### Pseudocode

```java
BEGIN

IF `<list> inventory` contains at least 3 iron ore
    Remove 3 iron ore from `<list> inventory`;
    Add the crafted item 1 iron ingot to `<list> inventory`;
    PRINT INFO "Crafted Iron Ingot.\n";
ELSE
    PRINT WARNING "Insufficient resources to craft Iron Ingot.\n";

END
```

<div style="page-break-after: always;"></div>

#### Flowchart

<img src="./functions/src/flowchart-craftIronIngot.svg" alt="flowchart-craftIronIngot.svg"/>
<!---
End ./functions/description-craftIronIngot.md
-->

<div style="page-break-after: always;"></div>

<!---
Start ./functions/description-craftItem.md
-->
### void craftItem(int recipe)

#### Documentation

<img src="./docs/src/docs-craftItem.png" alt="docs-craftItem.png"/>

#### Java

```java
public static void craftItem(int recipe) {
    switch (recipe) {
        case 1:
            craftWoodenPlanks();
            break;
        case 2:
            craftStick();
            break;
        case 3:
            craftIronIngot();
            break;
        case 4:
            craftStonePickaxe();
            break;
        case 5:
            craftIronPickaxe();
            break;
        default:
            System.out.println("Invalid recipe number.");
    }
    waitForEnter();
}
```

<div style="page-break-after: always;"></div>

#### Pseudocode

```java
BEGIN

IF `<Integer> recipe` == 1
    Craft wooden planks;
ELSE IF `<Integer> recipe` == 2
    Craft stick;
ELSE IF `<Integer> recipe` == 3
    Craft iron ingot;
ELSE IF `<Integer> recipe` == 4
    Craft stone pickaxe;
ELSE IF `<Integer> recipe` == 5
    Craft iron pickaxe;
ELSE
    PRINT WARNING "Invalid recipe number.\n";
Wait on player to press ENTER;

END
```

#### Flowchart

<img src="./functions/src/flowchart-craftItem.svg" alt="flowchart-craftItem.svg"/>
<!---
End ./functions/description-craftItem.md
-->

<div style="page-break-after: always;"></div>

<!---
Start ./functions/description-craftStick.md
-->
### void craftStick()

#### Documentation

<img src="./docs/src/docs-craftStick.png" alt="docs-craftStick.png"/>

#### Java

```java
public static void craftStick() {
    if (inventoryContains(WOOD)) {
        removeItemsFromInventory(WOOD, 1);
        addCraftedItem(CRAFTED_STICK);
        System.out.println("Crafted Stick.");
    } else {
        System.out.println("Insufficient resources to craft Stick.");
    }
}
```

#### Pseudocode

```java
BEGIN

IF `<list> inventory` contains wood
    Remove 1 wood from `<list> inventory`;
    Add the crafted item 1 stick to `<list> inventory`;
    PRINT INFO "Crafted Stick.\n";
ELSE
    PRINT WARNING "Insufficient resources to craft Stick.\n";

END
```

<div style="page-break-after: always;"></div>

#### Flowchart

<img src="./functions/src/flowchart-craftStick.svg" alt="flowchart-craftStick.svg"/>
<!---
End ./functions/description-craftStick.md
-->

<div style="page-break-after: always;"></div>

<!---
Start ./functions/description-craftWoodenPlanks.md
-->
### void craftWoodenPlanks()

#### Documentation

<img src="./docs/src/docs-craftWoodenPlanks.png" alt="docs-craftWoodenPlanks.png"/>

#### Java

```java
public static void craftWoodenPlanks() {
    if (inventoryContains(WOOD, 2)) {
        removeItemsFromInventory(WOOD, 2);
        addCraftedItem(CRAFTED_WOODEN_PLANKS);
        System.out.println("Crafted Wooden Planks.");
    } else {
        System.out.println("Insufficient resources to craft Wooden Planks.");
    }
}
```

#### Pseudocode

```java
BEGIN

IF `<list> inventory` contains at least 2 wood
    Remove 2 wood from `<list> inventory`;
    Add the crafted item 1 wooden planks to `<list> inventory`;
    PRINT INFO "Crafted Wooden Planks.\n";
ELSE
    PRINT WARNING "Insufficient resources to craft Wooden Planks.\n";

END
```

<div style="page-break-after: always;"></div>

#### Flowchart

<img src="./functions/src/flowchart-craftWoodenPlanks.svg" alt="flowchart-craftWoodenPlanks.svg"/>
<!---
End ./functions/description-craftWoodenPlanks.md
-->

<div style="page-break-after: always;"></div>

<!---
Start ./functions/description-displayCraftingRecipes.md
-->
### void displayCraftingRecipes()

#### Documentation

<img src="./docs/src/docs-displayCraftingRecipes.png" alt="docs-displayCraftingRecipes.png"/>

#### Java

```java
public static void displayCraftingRecipes() {
    System.out.println("Crafting Recipes:");
    System.out.println("1. Craft Wooden Planks: 2 Wood");
    System.out.println("2. Craft Stick: 1 Wood");
    System.out.println("3. Craft Iron Ingot: 3 Iron Ore");
    System.out.println("4. Craft Stone Pickaxe: 1 Stick, 3 Stone");
    System.out.println("5. Craft Iron Pickaxe: 1 Stick, 3 Iron Ingot");
}
```

#### Pseudocode

```java
BEGIN

PRINT INFO "Crafting Recipes:\n";
PRINT INFO "1. Craft Wooden Planks: 2 Wood\n";
PRINT INFO "2. Craft Stick: 1 Wood\n";
PRINT INFO "3. Craft Iron Ingot: 3 Iron Ore\n";
PRINT INFO "4. Craft Stone Pickaxe: 1 Stick, 3 Stone\n";
PRINT INFO "5. Craft Iron Pickaxe: 1 Stick, 3 Iron Ingot\n";

END
```

<div style="page-break-after: always;"></div>

#### Flowchart

<img src="./functions/src/flowchart-displayCraftingRecipes.svg" alt="flowchart-displayCraftingRecipes.svg"/>
<!---
End ./functions/description-displayCraftingRecipes.md
-->

<div style="page-break-after: always;"></div>

<!---
Start ./functions/description-displayInventory.md
-->
### void displayInventory()

#### Documentation

<img src="./docs/src/docs-displayInventory.png" alt="docs-displayInventory.png"/>

#### Java

```java
public static void displayInventory() {
    System.out.println("Inventory:");
    if (inventory.isEmpty()) {
        System.out.println(ANSI_YELLOW + "Empty" + ANSI_RESET);
    } else {
        int[] blockCounts = new int[7];
        for (int i = 0; i < inventory.size(); i++) {
            int block = inventory.get(i);
            blockCounts[block]++;
        }
        for (int blockType = 1; blockType < blockCounts.length; blockType++) {
            int occurrences = blockCounts[blockType];
            if (occurrences > 0) {
                System.out.println(getBlockName(blockType) + " - " + occurrences);
            }
        }
    }
    System.out.println("Crafted Items:");
    if (craftedItems == null || craftedItems.isEmpty()) {
        System.out.println(ANSI_YELLOW + "None" + ANSI_RESET);
    } else {
        for (int item : craftedItems) {
            System.out.print(
                    getCraftedItemColor(item) + getCraftedItemName(item) + ", " + ANSI_RESET);
        }
        System.out.println();
    }
    System.out.println();
}
```

<div style="page-break-after: always;"></div>

#### Pseudocode

```java
BEGIN

PRINT INFO "Inventory:\n";
IF `<Integer list> inventory` is empty
    PRINT INFO "Empty\n" (colored in yellow);
ELSE
    CREATE `<Integer array> blockCounts` of size 7;
    FOR EACH `<Integer> element` in `<Integer list> inventory`
        Assign `<Integer> block` = `<Integer> element`;
        Set `<Integer array> blockCounts @ index <Integer> block` += 1;
    FOR `<Integer> blockType` = 1; `<Integer> blockType` < `length of <Integer array> blockCounts`
        Assign `<Integer> occurences` = `<Integer array> blockCounts @ index <Integer> blockType`;
        IF `<Integer> occurences` > 0
            PRINT INFO `<String> get block name matching <Integer> blockType` + " - " + `<Integer> occurences\n`;
        Set `<Integer> blockType` += 1;
PRINT INFO "Crafted Items:\n";
IF `<Integer list> craftedItems` is non-existant or empty
    PRINT INFO "None\n" (colored in yellow);
ELSE
    FOR EACH `<Integer> item` in `<Integer list> craftedItems`
        PRINT INFO `<String> get name matching <Integer> item` + ", " (colored in `<String> get color matching <Integer> item`);
    PRINT INFO "\n";
PRINT INFO "\n";

END
```

<div style="page-break-after: always;"></div>

#### Flowchart

<img src="./functions/src/flowchart-displayInventory.svg" alt="flowchart-displayInventory.svg"/>
<!---
End ./functions/description-displayInventory.md
-->

<div style="page-break-after: always;"></div>

<!---
Start ./functions/description-fillInventory.md
-->
### void fillInventory()

#### Documentation

<img src="./docs/src/docs-fillInventory.png" alt="docs-fillInventory.png"/>

#### Java

```java
private static void fillInventory() {
    inventory.clear();
    for (int blockType = 1; blockType <= 6; blockType++) {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            inventory.add(blockType);
        }
    }
}
```

#### Pseudocode

```java
BEGIN

Clear `<Integer list> inventory`;
FOR `<Integer> blockType` = 1; `<Integer> blockType` <= 6
    FOR EACH `<Integer> element` in `<Integer list> inventory`
        Set `<Integer> member` = `<Integer> blockType`;
    Set `<Integer> blockType` += 1;

END
```

<div style="page-break-after: always;"></div>

#### Flowchart

<img src="./functions/src/flowchart-fillInventory.svg" alt="flowchart-fillInventory.svg"/>
<!---
End ./functions/description-fillInventory.md
-->

<div style="page-break-after: always;"></div>

<!---
Start ./functions/description-generateWorld.md
-->
### void generateWorld()

#### Documentation

<img src="./docs/src/docs-generateWorld.png" alt="docs-generateWorld.png"/>

#### Java

```java
public static void generateWorld() {
    Random rand = new Random();
    for (int y = 0; y < worldHeight; y++) {
        for (int x = 0; x < worldWidth; x++) {
            int randValue = rand.nextInt(100);
            if (randValue < 17) {
                world[x][y] = WOOD;
            } else if (randValue < 30) {
                world[x][y] = LEAVES;
            } else if (randValue < 45) {
                world[x][y] = STONE;
            } else if (randValue < 57) {
                world[x][y] = COAL_ORE;
            } else if (randValue < 65) {
                world[x][y] = IRON_ORE;
            } else if (randValue < 70) {
                world[x][y] = EMERALD_ORE;
            } else {
                world[x][y] = AIR;
            }
        }
    }
}
```

<div style="page-break-after: always;"></div>

#### Pseudocode

```java
BEGIN

FOR `<Integer> y` = 0; `<Integer> y` < `<Integer> worldHeight`
    FOR `<Integer> x` = 0; `<Integer> x` < `<Integer> worldWidth`
        Assign `<Integer> randValue` = `random value between 0 and 99`;
        IF `<Integer> randValue` < 17
            Set `<two dimensional Integer array> world @ indexes <Integer> x, <Integer> y` = `<Integer> wood`;
        ELSE IF `<Integer> randValue` < 30
            Set `<two dimensional Integer array> world @ indexes <Integer> x, <Integer> y` = `<Integer> leaves`;
        ELSE IF `<Integer> randValue` < 45
            Set `<two dimensional Integer array> world @ indexes <Integer> x, <Integer> y` = `<Integer> stone`;
        ELSE IF `<Integer> randValue` < 57
            Set `<two dimensional Integer array> world @ indexes <Integer> x, <Integer> y` = `<Integer> coal ore`;
        ELSE IF `<Integer> randValue` < 65
            Set `<two dimensional Integer array> world @ indexes <Integer> x, <Integer> y` = `<Integer> iron ore`;
        ELSE IF `<Integer> randValue` < 70
            Set `<two dimensional Integer array> world @ indexes <Integer> x, <Integer> y` = `<Integer> emerald ore`;
        ELSE
            Set `<two dimensional Integer array> world @ indexes <Integer> x, <Integer> y` = `<Integer> air`;
        Set `<Integer> x` += 1;
    Set `<Integer> y` += 1;

END
```

<div style="page-break-after: always;"></div>

#### Flowchart

<img src="./functions/src/flowchart-generateWorld.svg" alt="flowchart-generateWorld.svg"/>
<!---
End ./functions/description-generateWorld.md
-->

<div style="page-break-after: always;"></div>

<!---
Start ./functions/description-getBlockChar.md
-->
### char getBlockChar(int blockType)

#### Documentation

<img src="./docs/src/docs-getBlockChar.png" alt="docs-getBlockChar.png"/>

#### Java

```java
private static char getBlockChar(int blockType) {
    switch (blockType) {
        case WOOD:
            return '\u2592';
        case LEAVES:
            return '\u00A7';
        case STONE:
            return '\u2593';
        case IRON_ORE:
            return '\u00B0';
        case COAL_ORE:
            return '\u2593';
        case EMERALD_ORE:
            return '\u00B0';
        default:
            return '-';
    }
}
```

<div style="page-break-after: always;"></div>

#### Pseudocode

```java
BEGIN

IF `<Integer> blockType` == `<Integer> wood`
    RETURN `<Character> medium shade`;
ELSE IF `<Integer> blockType` == `<Integer> leaves`
    RETURN `<Character> section sign`;
ELSE IF `<Integer> blockType` == `<Integer> stone`
    RETURN `<Character> dark shade`;
ELSE IF `<Integer> blockType` == `<Integer> iron ore`
    RETURN `<Character> degree sign`;
ELSE IF `<Integer> blockType` == `<Integer> coal ore`
    RETURN `<Character> dark shade`;
ELSE IF `<Integer> blockType` == `<Integer> emerald ore`
    RETURN `<Character> degree sign`;
ELSE
    RETURN `<Character> -`;

END
```

<div style="page-break-after: always;"></div>

#### Flowchart

<img src="./functions/src/flowchart-getBlockChar.svg" alt="flowchart-getBlockChar.svg"/>
<!---
End ./functions/description-getBlockChar.md
-->

<div style="page-break-after: always;"></div>

<!---
Start ./functions/description-getBlockName.md
-->
### String getBlockName(int blockType)

#### Documentation

<img src="./docs/src/docs-getBlockName.png" alt="docs-getBlockName.png"/>

#### Java

```java
private static String getBlockName(int blockType) {
    switch (blockType) {
        case AIR:
            return "Empty Block";
        case WOOD:
            return "Wood";
        case LEAVES:
            return "Leaves";
        case STONE:
            return "Stone";
        case IRON_ORE:
            return "Iron Ore";
        case COAL_ORE:
            return "Coal Ore";
        case EMERALD_ORE:
            return "Emerald Ore";
        default:
            return "Unknown";
    }
}
```

<div style="page-break-after: always;"></div>

#### Pseudocode

```java
BEGIN

IF `<Integer> blockType` == `<Integer> air`
    RETURN "Empty Block";
ELSE IF `<Integer> blockType` == `<Integer> wood`
    RETURN "Wood";
ELSE IF `<Integer> blockType` == `<Integer> leaves`
    RETURN "Leaves";
ELSE IF `<Integer> blockType` == `<Integer> stone`
    RETURN "Stone";
ELSE IF `<Integer> blockType` == `<Integer> iron ore`
    RETURN "Iron Ore";
ELSE IF `<Integer> blockType` == `<Integer> coal ore`
    RETURN "Coal Ore";
ELSE IF `<Integer> blockType` == `<Integer> emerald ore`
    RETURN "Emerald Ore";
ELSE
    RETURN "Unknown";

END
```

<div style="page-break-after: always;"></div>

#### Flowchart

<img src="./functions/src/flowchart-getBlockName.svg" alt="flowchart-getBlockName.svg"/>
<!---
End ./functions/description-getBlockName.md
-->

<div style="page-break-after: always;"></div>

<!---
Start ./functions/description-getBlockSymbol.md
-->
### String getBlockSymbol(int blockType)

#### Documentation

<img src="./docs/src/docs-getBlockSymbol.png" alt="docs-getBlockSymbol.png"/>

#### Java

```java
private static String getBlockSymbol(int blockType) {
    String blockColor;
    switch (blockType) {
        case AIR:
            return ANSI_RESET + "- ";
        case WOOD:
            blockColor = ANSI_RED;
            break;
        case LEAVES:
            blockColor = ANSI_GREEN;
            break;
        case STONE:
            blockColor = ANSI_BLUE;
            break;
        case IRON_ORE:
            blockColor = ANSI_WHITE;
            break;
        case COAL_ORE:
            blockColor = ANSI_COAL_GRAY;
            break;
        case EMERALD_ORE:
            blockColor = ANSI_EMERALD_GREEN;
            break;
        default:
            blockColor = ANSI_RESET;
            break;
    }
    return blockColor + getBlockChar(blockType) + " ";
}
```

<div style="page-break-after: always;"></div>

#### Pseudocode

```java
BEGIN

Define `<String> blockColor`;
IF `<Integer> blockType` == `<Integer> air`
    RETURN "Empty Block";
ELSE IF `<Integer> blockType` == `<Integer> wood`
    Set `<String> blockColor` = `(color red)`;
ELSE IF `<Integer> blockType` == `<Integer> leaves`
    Set `<String> blockColor` = `(color green)`;
ELSE IF `<Integer> blockType` == `<Integer> stone`
    Set `<String> blockColor` = `(color blue)`;
ELSE IF `<Integer> blockType` == `<Integer> iron ore`
    Set `<String> blockColor` = `(color white)`;
ELSE IF `<Integer> blockType` == `<Integer> coal ore`
    Set `<String> blockColor` = `(color coal gray)`;
ELSE IF `<Integer> blockType` == `<Integer> emerald ore`
    Set `<String> blockColor` = `(color emerald green)`;
ELSE
    Set `<String> blockColor` = `(reset color)`;
RETURN `<String> blockColor` + `<Character> get symbol matching blockType` + " ";

END
```

<div style="page-break-after: always;"></div>

#### Flowchart

<img src="./functions/src/flowchart-getBlockSymbol.svg" alt="flowchart-getBlockSymbol.svg"/>
<!---
End ./functions/description-getBlockSymbol.md
-->

<div style="page-break-after: always;"></div>

<!---
Start ./functions/description-getCraftedItemName.md
-->
### String getCraftedItemName(int craftedItem)

#### Documentation

<img src="./docs/src/docs-getCraftedItemName.png" alt="docs-getCraftedItemName.png"/>

#### Java

```java
private static String getCraftedItemName(int craftedItem) {
    switch (craftedItem) {
        case CRAFTED_WOODEN_PLANKS:
            return "Wooden Planks";
        case CRAFTED_STICK:
            return "Stick";
        case CRAFTED_IRON_INGOT:
            return "Iron Ingot";
        case CRAFTED_STONE_PICKAXE:
            return "Stone Pickaxe";
        case CRAFTED_IRON_PICKAXE:
            return "Iron Pickaxe";
        default:
            return "Unknown";
    }
}
```

<div style="page-break-after: always;"></div>

#### Pseudocode

```java
BEGIN

IF `<Integer> craftedItem` == `<Integer> wooden planks`
    RETURN "Wooden Planks";
ELSE IF `<Integer> blockType` == `<Integer> stick`
    RETURN "Stick";
ELSE IF `<Integer> blockType` == `<Integer> iron ingot`
    RETURN "Iron Ingot";
ELSE IF `<Integer> blockType` == `<Integer> stone pickaxe`
    RETURN "Stone Pickaxe";
ELSE IF `<Integer> blockType` == `<Integer> iron pickaxe`
    RETURN "Iron Pickaxe";
ELSE
    RETURN "Unknown";

END
```

#### Flowchart

<img src="./functions/src/flowchart-getCraftedItemName.svg" alt="flowchart-getCraftedItemName.svg"/>
<!---
End ./functions/description-getCraftedItemName.md
-->

<div style="page-break-after: always;"></div>

<!---
Start ./functions/description-loadGame.md
-->
### void loadGame(String fileName)

#### Documentation

<img src="./docs/src/docs-loadGame.png" alt="docs-loadGame.png"/>

#### Java

```java
public static void loadGame(String fileName) {
    // Implementation for loading the game state from a file goes here
    try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
        // Deserialize game state data from the file and load it into the program
        NEW_WORLD_WIDTH = inputStream.readInt();
        NEW_WORLD_HEIGHT = inputStream.readInt();
        world = (int[][]) inputStream.readObject();
        playerX = inputStream.readInt();
        playerY = inputStream.readInt();
        inventory = (List<Integer>) inputStream.readObject();
        craftedItems = (List<Integer>) inputStream.readObject();
        unlockMode = inputStream.readBoolean();
        System.out.println("Game state loaded from file: " + fileName);
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("Error while loading the game state: " + e.getMessage());
    }
    waitForEnter();
}
```

<div style="page-break-after: always;"></div>

#### Pseudocode

```java
BEGIN

TRY TO
    Set `<stream> inputStream` = `<stream> of contents from file matching <String> fileName relative to current working directory`;
    Set `<Integer> NEW_WORLD_WIDTH` = `<Integer> get next line containing serialized <Integer> in <stream> inputStream`;
    Set `<Integer> NEW_WORLD_HEIGHT` = `<Integer> get next line containing serialized <Integer> in <stream> inputStream`;
    Set `<two dimensional Integer array> world` = `<two dimensional Integer array> get next line containing any serialized object in <stream> inputStream`;
    Set `<Integer> playerX` = `<Integer> get next line containing serialized <Integer> in <stream> inputStream`;
    Set `<Integer> playerY` = `<Integer> get next line containing serialized <Integer> in <stream> inputStream`;
    Set `<Integer list> inventory` = `<Integer list> get next line containing any serialized object in <stream> inputStream` and cast to <Integer list>;
    Set `<Integer list> craftedItems` = `<Integer list> get next line containing any serialized object in <stream> inputStream`  and cast to <Integer list>;
    Set `<boolean> unlockMode` = `<boolean> get next line containing serialized <boolean> in <stream> inputStream`;
    PRINT INFO "Game state loaded from file: " + `<String> fileName` + "\n";
    Close `<stream> inputStream`;
ON EXCEPTION
    PRINT ERROR "Error while loading the game state: " + `errormessage` + "\n";
    Close `<stream> inputStream`;
Wait on player to press ENTER;

END
```

<div style="page-break-after: always;"></div>

#### Flowchart

<img src="./functions/src/flowchart-loadGame.svg" alt="flowchart-loadGame.svg"/>
<!---
End ./functions/description-loadGame.md
-->

<div style="page-break-after: always;"></div>

<!---
Start ./functions/description-lookAround.md
-->
### void lookAround()

#### Documentation

<img src="./docs/src/docs-lookAround.png" alt="docs-lookAround.png"/>

#### Java

```java
private static void lookAround() {
    System.out.println("You look around and see:");
    for (int y = Math.max(0, playerY - 1); y <= Math.min(playerY + 1, worldHeight - 1); y++) {
        for (int x = Math.max(0, playerX - 1); x <= Math.min(playerX + 1,
                worldWidth - 1); x++) {
            if (x == playerX && y == playerY) {
                System.out.print(ANSI_GREEN + "P " + ANSI_RESET);
            } else {
                System.out.print(getBlockSymbol(world[x][y]) + ANSI_RESET);
            }
        }
        System.out.println();
    }
    System.out.println();
    waitForEnter();
}
```

<div style="page-break-after: always;"></div>

#### Pseudocode

```java
BEGIN

PRINT INFO "You look around and see:";
FOR `<Integer> y` = `Maximum {of} 0 and {<Integer> playerY - 1}`; `<Integer> y` <= `Minimum of {<Integer> playerY + 1} and {<Integer> worldHeight - 1}`
    FOR `<Integer> x` = `Maximum of {0} and {<Integer> playerX - 1}`; `<Integer> x` <= `Minimum of {<Integer> playerX + 1} and {<Integer> worldWidth - 1}`
        IF `<Integer> x` == `<Integer> playerX` AND `<Integer> y` == `<Integer> playerY`
            PRINT INFO "P " (colored green);
        ELSE
            PRINT INFO `get block symbol from <two dimensional Integer array> world @ indexes <Integer> x, <Integer> y`;
        Set `<Integer> x` += 1;
    PRINT INFO "\n";
    Set `<Integer> y` += 1;
PRINT INFO "\n";
Wait on player to press ENTER;

END
```

<div style="page-break-after: always;"></div>

#### Flowchart

<img src="./functions/src/flowchart-lookAround.svg" alt="flowchart-lookAround.svg"/>
<!---
End ./functions/description-lookAround.md
-->

<div style="page-break-after: always;"></div>

<!---
Start ./functions/description-placeBlock.md
-->
### void placeBlock(int blockType)

#### Documentation

<img src="./docs/src/docs-placeBlock.png" alt="docs-placeBlock.png"/>

#### Java

```java
public static void placeBlock(int blockType) {
    if (blockType >= 0 && blockType <= 11) {
        if (blockType <= 6) {
            if (inventory.contains(blockType)) {
                inventory.remove(Integer.valueOf(blockType));
                world[playerX][playerY] = blockType;
                System.out.println("Placed " + getBlockName(blockType) + " at your position.");
            } else {
                System.out.println(
                        "You don't have " + getBlockName(blockType) + " in your inventory.");
            }
        } else {
            int craftedItem = getCraftedItemFromBlockType(blockType);
            if (craftedItems.contains(craftedItem)) {
                craftedItems.remove(Integer.valueOf(craftedItem));
                world[playerX][playerY] = blockType;
                System.out.println(
                        "Placed " + getCraftedItemName(craftedItem) + " at your position.");
            } else {
                System.out.println("You don't have " + getCraftedItemName(craftedItem)
                        + " in your crafted items.");
            }
        }
    } else {
        System.out.println("Invalid block number. Please enter a valid block number.");
        System.out.println(BLOCK_NUMBERS_INFO);
    }
    waitForEnter();
}
```

<div style="page-break-after: always;"></div>

#### Pseudocode

```java
BEGIN

IF `<Integer> blockType` >= 0 AND `<Integer> blockType` <= 11
    IF `<Integer> blockType` <= 6
        IF `<Integer list> inventory` contains `<Integer>` blockType
            Remove member `<Integer>` blockType from `<Integer list> inventory`;
            Set `<two dimensional Integer array> world @ indexes <Integer> playerX, <Integer> playerY` = `<Integer>` blockType;
            PRINT INFO "Placed " + `<String> get block name matching <Integer> blockType` + " at your position.";
        ELSE
            PRINT WARNING "You don't have " + `<String> get block name matching <Integer> blockType` + " in your inventory.";
    ELSE
        Assign `<Integer> craftedItem` = `<Integer> get crafted item of <Integer> blockType`;
        IF `<Integer list> craftedItems` contains `<Integer>` craftedItem
            Remove member `<Integer>` craftedItem from `<Integer list> craftedItems`;
            Set `<two dimensional Integer array> world @ indexes <Integer> playerX, <Integer> playerY` = `<Integer>` blockType;
            PRINT INFO "Placed " + `<String> get block name matching <Integer> craftedItem` + " at your position.";
        ELSE
            PRINT WARNING "You don't have " + `<String> get block name matching <Integer> craftedItem` + " in your crafted items.";
ELSE
    PRINT WARNING "Invalid block number. Please enter a valid block number.\n";
    PRINT WARNING `<String> BLOCK_NUMBERS_INFO` + "\n";
Wait on player to press ENTER;

END
```

<div style="page-break-after: always;"></div>

#### Flowchart

<img src="./functions/src/flowchart-placeBlock.svg" alt="flowchart-placeBlock.svg"/>
<!---
End ./functions/description-placeBlock.md
-->

<div style="page-break-after: always;"></div>

### Additional documentation

<!---
Start ./docs/src/*.png
-->
<img src="./docs/src/docs-addCraftedItem.png" alt="docs-addCraftedItem.png"/>
<img src="./docs/src/docs-craftedItemsContains0.png" alt="docs-craftedItemsContains0.png"/>
<img src="./docs/src/docs-displayWorld.png" alt="docs-displayWorld.png"/>
<img src="./docs/src/docs-generateEmptyWorld.png" alt="docs-generateEmptyWorld.png"/>
<img src="./docs/src/docs-getCountryAndQuoteFromServer.png" alt="docs-getCountryAndQuoteFromServer.png"/>
<img src="./docs/src/docs-getCraftedItemColor.png" alt="docs-getCraftedItemColor.png"/>
<img src="./docs/src/docs-getCraftedItemFromBlockType.png" alt="docs-getCraftedItemFromBlockType.png"/>
<img src="./docs/src/docs-initGame.png" alt="docs-initGame.png"/>
<img src="./docs/src/docs-inventoryContains0.png" alt="docs-inventoryContains0.png"/>
<img src="./docs/src/docs-inventoryContains1.png" alt="docs-inventoryContains1.png"/>
<img src="./docs/src/docs-main.png" alt="docs-main.png"/>
<img src="./docs/src/docs-movePlayer.png" alt="docs-movePlayer.png"/>
<img src="./docs/src/docs-removeItemsFromInventory.png" alt="docs-removeItemsFromInventory.png"/>
<img src="./docs/src/docs-resetWorld.png" alt="docs-resetWorld.png"/>
<img src="./docs/src/docs-saveGame.png" alt="docs-saveGame.png"/>
<img src="./docs/src/docs-startGame.png" alt="docs-startGame.png"/>
<img src="./docs/src/docs-waitForEnter.png" alt="docs-waitForEnter.png"/>
<!---
End ./docs/src/*.png
-->

<div style="page-break-after: always;"></div>

## References

- [Template](https://canvas.maastrichtuniversity.nl/courses/15753/assignments/76649) - Canvas task on which this document is based
- [yEd](https://www.yworks.com/products/yed) - Graph Editor we used to make the flowcharts
- [Flags API](https://flag.ashish.nl) - API to get a flag
