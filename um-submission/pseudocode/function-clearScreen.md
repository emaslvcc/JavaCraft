# void clearScreen()

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

```
BEGIN/END: Start/End of the pseudocode.
IF…THEN…ELSE: Represents a decision.
FOR…DO: Represents a loop with a known number of iterations.
WHILE…DO: Represents a loop with an unknown number of iterations.
READ/PRINT: Represents input and output operations.

```


```

```

1. **TRY TO:**

   **CATCH:** for InterruptedException or IOException: Print ERROR `<stacktrace>`"
   1. If system property OS-Name contains "Windows"
      1. Clear screen using Windows `cmd.exe` by calling "/c cls"
      2. Wait for process to finish
   2. Else
      1. Clear screen using ANSI escape codes
