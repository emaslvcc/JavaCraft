public class Crafting {

    public static void displayCraftingRecipes() {
        System.out.println("Crafting Recipes:");
        System.out.println("1. Craft Wooden Planks: 2 Wood");
        System.out.println("2. Craft Stick: 1 Wood");
        System.out.println("3. Craft Iron Ingot: 3 Iron Ore");
        System.out.println("4. Craft Gold Ingot: 3 Gold Ore");
        System.out.println("5. Craft Diamond Totem: 5 Diamond");

    }

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
                craftGoldIngot();
                break;
            case 5:
                craftDiamondTotem();
                break;
            default:
                System.out.println("Invalid recipe number.");
        }
        InputManager.waitForEnter();
    }

    public static int getCraftedItemFromBlockType(int blockType) {
        switch (blockType) {
            case 5:
                return GameValues.CRAFTED_WOODEN_PLANKS;
            case 6:
                return GameValues.CRAFTED_STICK;
            case 7:
                return GameValues.CRAFTED_IRON_INGOT;
            case 8:
                return GameValues.DIAMOND_TOTEM;
            default:
                return -1;
        }
    }

    public static String getCraftedItemName(int craftedItem) {
        switch (craftedItem) {
            case GameValues.CRAFTED_WOODEN_PLANKS:
                return "Wooden Planks";
            case GameValues.CRAFTED_STICK:
                return "Stick";
            case GameValues.CRAFTED_IRON_INGOT:
                return "Iron Ingot";
            case GameValues.CRAFTED_GOLD_INGOT:
                return "Gold Ingot";
            case GameValues.DIAMOND_TOTEM:
                return "Diamond Totem";
            default:
                return "Unknown";
        }
    }

    public static String getCraftedItemColor(int craftedItem) {
        switch (craftedItem) {
            case GameValues.CRAFTED_WOODEN_PLANKS:
            case GameValues.CRAFTED_STICK:
            case GameValues.CRAFTED_IRON_INGOT:
            case GameValues.GOLD_ORE:
                return GameValues.ANSI_BROWN;
            default:
                return "";
        }
    }


    public static void craftWoodenPlanks() {
        if (GameLoop.inventoryManager.containsItemOfNumber(GameValues.WOOD, 2)) {
            GameLoop.inventoryManager.removeItems(GameValues.WOOD, 2);
            GameLoop.inventoryManager.addCraftedItem(GameValues.CRAFTED_WOODEN_PLANKS);
            System.out.println("Crafted Wooden Planks.");
        } else {
            System.out.println("Insufficient resources to craft Wooden Planks.");
        }
    }

    public static void craftStick() {
        if (GameLoop.inventoryManager.containsItem(GameValues.WOOD)) {
            GameLoop.inventoryManager.removeItems(GameValues.WOOD, 1);
            GameLoop.inventoryManager.addCraftedItem(GameValues.CRAFTED_STICK);
            System.out.println("Crafted Stick.");
        } else {
            System.out.println("Insufficient resources to craft Stick.");
        }
    }
    // Added by David
    public static void craftDiamondTotem() {
        if (GameLoop.inventoryManager.containsItemOfNumber(GameValues.Diamond, 5)){
            GameLoop.inventoryManager.removeItems(GameValues.Diamond, 5);
            GameLoop.inventoryManager.addCraftedItem(GameValues.DIAMOND_TOTEM);
            System.out.println("Crafted Diamond Totem");
        }else{
            System.out.println("Insufficient resources to craft Diamond Totem");
        }

    }

    public static void craftIronIngot() {
        if (GameLoop.inventoryManager.containsItemOfNumber(GameValues.IRON_ORE, 3)) {
            GameLoop.inventoryManager.removeItems(GameValues.IRON_ORE, 3);
            GameLoop.inventoryManager.addCraftedItem(GameValues.CRAFTED_IRON_INGOT);
            System.out.println("Crafted Iron Ingot.");
        } else {
            System.out.println("Insufficient resources to craft Iron Ingot.");
        }
    }
    public static void craftGoldIngot() {
        if (GameLoop.inventoryManager.containsItemOfNumber(GameValues.GOLD_ORE, 3)) {
            GameLoop.inventoryManager.removeItems(GameValues.GOLD_ORE, 3);
            GameLoop.inventoryManager.addCraftedItem(GameValues.CRAFTED_GOLD_INGOT);
            System.out.println("Crafted Gold Ingot.");
        } else {
            System.out.println("Insufficient resources to craft Gold Ingot.");
        }
    }


}
