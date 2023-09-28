public class Player {

    static int playerX;
    static int playerY;

    public static void movePlayer(String direction) {
        switch (direction.toUpperCase()) {
            case "W":
            case "UP":
                if (playerY > 0) {
                    playerY--;
                }
                break;
            case "S":
            case "DOWN":
                if (playerY < World.worldHeight - 1) {
                    playerY++;
                }
                break;
            case "A":
            case "LEFT":
                if (playerX > 0) {
                    playerX--;
                }
                break;
            case "D":
            case "RIGHT":
                if (playerX < World.worldWidth - 1) {
                    playerX++;
                }
                break;
            default:
                break;
        }
    }

    public static void mineBlock() {
        int blockType = World.world[playerX][playerY];
        if (blockType != GameValues.AIR) {
            GameLoop.inventoryManager.addItem(blockType);
            World.world[playerX][playerY] = GameValues.AIR;
            System.out.println("Mined " + Blocks.getBlockName(blockType) + ".");
        } else {
            System.out.println("No block to mine here.");
        }
        InputManager.waitForEnter();
    }

    public static void placeBlock(int blockType) {
        if (blockType >= 0 && blockType <= 7) {
            if (blockType <= 4) {
                if (GameLoop.inventoryManager.containsItem(blockType)) {
                    GameLoop.inventoryManager.removeItem(Integer.valueOf(blockType));
                    World.world[playerX][playerY] = blockType;
                    System.out.println("Placed " + Blocks.getBlockName(blockType) + " at your position.");
                } else {
                    System.out.println("You don't have " + Blocks.getBlockName(blockType) + " in your inventory.");
                }
            } else {
                int craftedItem = Crafting.getCraftedItemFromBlockType(blockType);
                if (GameLoop.inventoryManager.containsCraftedItem(craftedItem)) {
                    GameLoop.inventoryManager.removeCraftedItem(Integer.valueOf(craftedItem));
                    World.world[playerX][playerY] = blockType;
                    System.out.println("Placed " + Crafting.getCraftedItemName(craftedItem) + " at your position.");
                } else {
                    System.out.println("You don't have " + Crafting.getCraftedItemName(craftedItem) + " in your crafted items.");
                }
            }
        } else {
            System.out.println("Invalid block number. Please enter a valid block number.");
            System.out.println(GameValues.BLOCK_NUMBERS_INFO);
        }
        InputManager.waitForEnter();
    }

    public static void interactWithWorld() {
        int blockType = World.world[playerX][playerY];
        switch (blockType) {
            case GameValues.WOOD:
                System.out.println("You gather wood from the tree.");
                GameLoop.inventoryManager.addItem(GameValues.WOOD);
                break;
            case GameValues.LEAVES:
                System.out.println("You gather leaves from the tree.");
                GameLoop.inventoryManager.addItem(GameValues.LEAVES);
                break;
            case GameValues.STONE:
                System.out.println("You gather stones from the ground.");
                GameLoop.inventoryManager.addItem(GameValues.STONE);
                break;
            case GameValues.IRON_ORE:
                System.out.println("You mine iron ore from the ground.");
                GameLoop.inventoryManager.addItem(GameValues.IRON_ORE);
                break;
            case GameValues.GOLD_ORE:
                System.out.println("You mine gold ore from the ground.");
                GameLoop.inventoryManager.addItem(GameValues.GOLD_ORE);
                break;
            case GameValues.Diamond:
                System.out.println("You gather diamond from the ground.");
                GameLoop.inventoryManager.addItem(GameValues.Diamond);
                break;
            case GameValues.AIR:
                System.out.println("Nothing to interact with here.");
                break;
            default:
                System.out.println("Unrecognized block. Cannot interact.");
        }
        InputManager.waitForEnter();
    }


    public static void lookAround() {
        System.out.println("You look around and see:");
        for (int y = Math.max(0, playerY - 1); y <= Math.min(playerY + 1, World.worldHeight - 1); y++) {
            for (int x = Math.max(0, playerX - 1); x <= Math.min(playerX + 1, World.worldWidth - 1); x++) {
                if (x == playerX && y == playerY) {
                    System.out.print(GameValues.ANSI_GREEN + "P " + GameValues.ANSI_RESET);
                } else {
                    System.out.print(Blocks.getBlockSymbol(World.world[x][y]));
                }
            }
            System.out.println();
        }
        System.out.println();
        InputManager.waitForEnter();
    }

    public static void setPosition(int x, int y) {
        playerX = x;
        playerY = y;
    }

}
