import java.util.Random;

public class World {

  public static int width = 100; // Adjust as needed
  public static int height = 30; // Adjust as needed

  public static Block[][] world = new Block[width][height];
  public static Animal[][] animals = new Animal[width][height];

  public static void generateWorld() {
    Random rand = new Random();
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int randValue = rand.nextInt(100);
        if (randValue < 20) {
          world[x][y] = Block.WOOD;
        } else if (randValue < 35) {
          world[x][y] = Block.LEAVES;
        } else if (randValue < 50) {
          world[x][y] = Block.STONE;
        } else if (randValue < 70) {
          world[x][y] = Block.IRON_ORE;
        } else {
          world[x][y] = Block.AIR;
        }
      }
    }
  }

  public static void populateWorldWithAnimals() {
    Random rand = new Random();
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (rand.nextInt(100) < 1) { // 1 percent chance
          animals[x][y] = rand.nextBoolean() ? Animal.SHEEP : Animal.COW;
        }
      }
    }
  }

  public static void moveAnimals() {
    Random rand = new Random();

    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        if (animals[x][y] != null && rand.nextInt(100) < 10) { // 10% chance for each animal
          int dx = rand.nextInt(3) - 1; // Generates -1, 0, or 1
          int dy = rand.nextInt(3) - 1;

          // Check the boundaries
          if (x + dx >= 0 && x + dx < width && y + dy >= 0 && y + dy < height) {
            // Move the animal only if the target location is free
            if (animals[x + dx][y + dy] == null) {
              animals[x + dx][y + dy] = animals[x][y];
              animals[x][y] = null;
            }
          }
        }
      }
    }
  }

  public static void removeEntity(int x, int y) {
    if (x >= 0 && x < width && y >= 0 && y < height) {
      animals[x][y] = null;
    } else {
      System.out.println("Invalid coordinates: Cannot remove entity.");
    }
  }
}
