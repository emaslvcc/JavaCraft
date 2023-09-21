## startGame() Pseudocode

unlockMode = false

movementCommandEntered = false

craftingCommandEntered = false

miningCommandEntered = false

openCommandEntered = false

- While loop

  - **if (move command)**
    - if (unlock mode)
      - movementCommandEntered = true
  - else if (mine command)
    - if (unlock mode)
      - mineCommandEntered = true
  - else if (craft command)
    - crafting function ()
  - else if (unlock command)
    - unlockMode = true
  - else if (open command)
    - if (u, m, c, m, o commands)
      - secretDoorUnlocked = true
    - else
      reset all variables
  - else
    invalid input

  - **if (unlockMode)**

    - if (crafting command)
      - craftingCommandEntered = true
    - else if (mining command)
      - miningCommandEntered = true
    - else if (open command)
      - openCommandEntered = true

  - **if (secretDoorunlocked)**
    - open door and show flag
