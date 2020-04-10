# CommandUtilityExample

**This project has been archived because examples are moved to [CommandUtility](https://github.com/kuro46/CommandUtility).**

Example project for [CommandUtility](https://github.com/kuro46/CommandUtility).

**Following Revision:** [v0.3.0](https://github.com/kuro46/CommandUtility/tree/v0.3.0)

## Commands

### `/commandutilityexample tp <world> <x> <y> <z> [pitch] [yaw]`

**Permisson:** `commandutilityexample.admin`  
**Handled at** [TeleportHandler](src/main/java/com/github/kuro46/commandutilityexample/Main.java#L202)

Teleport executor to specified location.

![teleport](https://user-images.githubusercontent.com/36619228/61878729-7a7dd700-af2c-11e9-932a-5e9655b7241b.gif)

### `/commandutilityexample gamemode <gamemode>`

**Permission:** `commandutilityexample.admin`  
**Handled at** [GameModeHandler](src/main/java/com/github/kuro46/commandutilityexample/Main.java#L95)

Change executor's gamemode to `<gamemode>`.

![gamemode](https://user-images.githubusercontent.com/36619228/61878730-7b166d80-af2c-11e9-9a86-7f2ad379472b.gif)

### `/commandutilityexample name <name>`

**Permission:** `commandutilityexample.admin`  
**Handled at** [NameHandler](src/main/java/com/github/kuro46/commandutilityexample/Main.java#L150)

Set executor's display name to `<name>`. Space character is allowed.

![name](https://user-images.githubusercontent.com/36619228/61878728-7a7dd700-af2c-11e9-8686-38f76f9fa476.gif)

### `/commandutilityexample help`

**Permisson:** `commandutilityexample.admin`  
**Registered at** [here](src/main/java/com/github/kuro46/commandutilityexample/Main.java#L64)

Display helps.

## Tested version

1.12.2

## Requirement

Java 8 or newer
