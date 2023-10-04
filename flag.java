public class flag{
    public static final String ANSI_RED = "\u001B[31m";
    public static final String  ANSI_BLUE = "\u001B[34m";
    public static final String  ANSI_RESET = "\u001B[37m";

    public static void main(String[] args){
        int height = 15;
        int width = 25;
        int arr[][] = generateflag(height, width);
        drawstripe(arr, height, width);
        displayflag(arr);
    }



    public static void drawstripe(int arr[][], int h, int w){
        int redcol = 0;
        int redrow = 1;
        int whcol = 1;
        int whrow = 2;
        for (int i = 0; i < h; i++) {
            for (int r = 0; r < w; r++) {
                // ------draws the red lines
                if (r > w * 0.40 && r < w * 0.55){
                    arr[i][r] = 1;
                }
                if (i > h * 0.35 && i < h * 0.55){
                    arr[i][r] = 1;
                }
                // ---draws
                if(i == redrow && r == redcol){
                    arr[i][r] = 1;
                    arr[i][((arr[0].length-1)-redcol)] = 1;
                    if(i < h * 0.40){
                        arr[i][r+1] = 2;
                        arr[i][r+2] = 2;
                        arr[i-1][r] = 2;
                        arr[i-1][r+1] = 2;
                        arr[i+1][r+1] = 2;
                        // ------descendence
                        arr[i][((arr[0].length-1)-redcol)-1] = 2;
                        arr[i][((arr[0].length-1)-redcol)-2] = 2;
                        arr[i-1][((arr[0].length-1)-redcol)] = 2;
                        arr[i-1][((arr[0].length-1)-redcol)-1] = 2;
                        arr[i+1][((arr[0].length-1)-redcol)-1] = 2;
                    }else if(i > h * 0.55){
                        arr[i][r-1] = 2;
                        arr[i][r-2] = 2;
                        arr[i-1][r] = 2;
                        arr[i-1][r-1] = 2;
                        arr[i+1][r-1] = 2;
                        // --------crescendence
                        arr[i][((arr[0].length-1)-redcol)+1] = 2;
                        arr[i][((arr[0].length-1)-redcol)+2] = 2;
                        arr[i-1][((arr[0].length-1)-redcol)] = 2;
                        arr[i-1][((arr[0].length-1)-redcol)+1] = 2;
                        arr[i+1][((arr[0].length-1)-redcol)+1] = 2;
                    }
                    redcol+=2;
                    redrow++;
                }else if(i == arr.length-2 && r == redcol){

                }
            }
        }
    }


    public static int[][] generateflag(int h, int w){
        int[][] arr = new int[h][w];
        for (int i = 0; i < h; i++) {
            for (int r = 0; r < w; r++) {
                arr[i][r] = 0;
            }
        }
        return arr;
    }

    public static void displayflag(int[][] arr){
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                if(arr[i][j] == 1){
                    System.out.print(ANSI_RED + arr[i][j]+" " + ANSI_RESET);
                }else if(arr[i][j] == 0){
                    System.out.print(ANSI_BLUE + arr[i][j]+" " + ANSI_RESET);

                }else{
                    System.out.print(arr[i][j]+" ");
                }
                
            }
            System.out.println();
        }
        System.out.println();
    }
}


// world = new int[NEW_WORLD_WIDTH][NEW_WORLD_HEIGHT];
//     int redBlock = 1;
//     int whiteBlock = 4;
//     int blueBlock = 3;
    
//     int redcol = 0;
//     int redrow = 1;
//     for (int x = 0; x < NEW_WORLD_HEIGHT; x++) {
//       for (int y = 0; y < NEW_WORLD_WIDTH; y++) {
//           // ------draws the red lines
//           if (y > NEW_WORLD_WIDTH * 0.40 && y < NEW_WORLD_WIDTH * 0.55){
//               world[x][y] = redBlock;
//           }
//           if (x > NEW_WORLD_HEIGHT * 0.35 && x < NEW_WORLD_HEIGHT * 0.55){
//               world[x][y] = redBlock;
//           }
//           // -------
//           // ---draws the diagonals
//           if(x == redrow && y == redcol){
//               world[x][y] = redBlock;
//               world[x][((world[0].length-1)-redcol)] = redBlock;
//               if(x < NEW_WORLD_HEIGHT * 0.40){
//                   world[x][y+1] = whiteBlock;
//                   world[x][y-1+2] = whiteBlock;
//                   world[x-1][y] = whiteBlock;
//                   world[x-1][y+1] = whiteBlock;
//                   world[x+1][y+1] = whiteBlock;
//                   // ------descendence
//                   world[x][((world[0].length-1)-redcol)-1] = whiteBlock;
//                   world[x][((world[0].length-1)-redcol)-2] = whiteBlock;
//                   world[x-1][((world[0].length-1)-redcol)] = whiteBlock;
//                   world[x-1][((world[0].length-1)-redcol)-1] = whiteBlock;
//                   world[x+1][((world[0].length-1)-redcol)-1] = whiteBlock;
//               }else if(x > NEW_WORLD_HEIGHT * 0.55){
//                   world[x][y-1] = whiteBlock;
//                   world[x][y-2] = whiteBlock;
//                   world[x-1][y] = whiteBlock;
//                   world[x-1][y-1] = whiteBlock;
//                   world[x+1][y-1] = whiteBlock;
//                   // --------crescendence
//                   world[x][((world[0].length-1)-redcol)+1] = whiteBlock;
//                   world[x][((world[0].length-1)-redcol)+2] = whiteBlock;
//                   world[x-1][((world[0].length-1)-redcol)] = whiteBlock;
//                   world[x-1][((world[0].length-1)-redcol)+1] = whiteBlock;
//                   world[x+1][((world[0].length-1)-redcol)+1] = whiteBlock;
//               }
//               redcol+=2;
//               redrow++;
//           }else if(x == world.length-2 && y == redcol){

//           }
//     }
//   }