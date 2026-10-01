# PotatoMod (Minecraft 1.7.10, Forge)

## What it does
Client:
- On first launch, lowers render distance (default 4), sets Fast graphics, smooth lighting off, clouds off, minimal particles (and optional FPS cap).
- Mobs farther than about 32 blocks are culled before rendering (configurable).
- Hides rain/snow/thunder rendering and skips sun, moon and stars (flat sky). Both are client-side only and configurable.
- Also turns off view bobbing and Advanced OpenGL.

Server / singleplayer:
- Dropped items despawn after 1 minute (default) instead of 5.
- Caps dropped items per dimension (default 150, oldest removed first).
- Caps XP orbs per dimension (default 40, extras are merged, no XP lost).

Config: `config/potatomod.cfg` (created on first run).

## How to build (recommended: GTNH ExampleMod template)
Setting up 1.7.10 Forge builds from scratch is fragile, so use a maintained template:
1. Fork/clone https://github.com/GTNewHorizons/ExampleMod1.7.10
2. Copy `src/main/java/com/potatomod/*` and `src/main/resources/mcmod.info` into it
   (delete the template's example classes).
3. Edit `gradle.properties` (modName, modId=potatomod, modGroup=com.potatomod, rootPackage, mainClass).
4. Push to GitHub: the template's GitHub Actions workflow builds the jar (Java 8/17 handled by the template).
5. Put the jar from the build artifacts into `mods/` of a Forge 1.7.10 instance in Zalith.

Needs Forge 1.7.10 (10.13.4.1614 or newer) and Java 8.
