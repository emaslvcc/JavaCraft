public class SecretDoor {

    private static boolean craftingCommandEntered;
    private static boolean miningCommandEntered;
    private static boolean movementCommandEntered;
    private static boolean openCommandEntered;

    public SecretDoor() {
        Reset();
    }

    public static void Reset() {
        craftingCommandEntered = false;
        miningCommandEntered = false;
        movementCommandEntered = false;
        openCommandEntered = false;
    }

    public static boolean isOpenCommandEntered() {
        return openCommandEntered;
    }

    public static void setOpenCommandEntered(boolean openCommandEntered) {
        SecretDoor.openCommandEntered = openCommandEntered;
    }

    public static boolean isMovementCommandEntered() {
        return movementCommandEntered;
    }

    public static void setMovementCommandEntered(boolean movementCommandEntered) {
        SecretDoor.movementCommandEntered = movementCommandEntered;
    }

    public static boolean isMiningCommandEntered() {
        return miningCommandEntered;
    }

    public static void setMiningCommandEntered(boolean miningCommandEntered) {
        SecretDoor.miningCommandEntered = miningCommandEntered;
    }

    public static boolean isCraftingCommandEntered() {
        return craftingCommandEntered;
    }

    public static void setCraftingCommandEntered(boolean craftingCommandEntered) {
        SecretDoor.craftingCommandEntered = craftingCommandEntered;
    }


}
