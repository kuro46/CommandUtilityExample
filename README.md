# CommandUtilityExample

Example project for [CommandUtility](https://github.com/kuro46/CommandUtility).

**Following Revision:** [v0.2.0](https://github.com/kuro46/CommandUtility/tree/v0.2.0)

## Commands

### `/commandutilityexample tp <world> <x> <y> <z> [pitch] [yaw]`

**Permisson:** `commandutilityexample.admin`  
**Handled at** [TeleportHandler](src/main/java/com/github/kuro46/commandutilityexample/Main.java#L202)

Teleport executor to specified location.

### `/commandutilityexample gamemode <gamemode>`

**Permission:** `commandutilityexample.admin`  
**Handled at** [GameModeHandler](src/main/java/com/github/kuro46/commandutilityexample/Main.java#L95)

Change executor's gamemode to `<gamemode>`.

### `/commandutilityexample name <name>`

**Permission:** `commandutilityexample.admin`  
**Handled at** [NameHandler](src/main/java/com/github/kuro46/commandutilityexample/Main.java#L150)

Set executor's display name to `<name>`. Space character is allowed.

### `/commandutilityexample help`

**Permisson:** `commandutilityexample.admin`  
**Registered at** [here](src/main/java/com/github/kuro46/commandutilityexample/Main.java#L64)

Display helps.

## Tested version

1.12.2

## Requirement

Java 8 or newer
