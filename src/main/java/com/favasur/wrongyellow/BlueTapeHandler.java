package com.favasur.wrongyellow;

import com.favasur.wrongyellow.frame.FramingSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.world.World;

import java.util.logging.Level;

/**
 * Handles Blue Tape item usage — replaces the right-clicked block with
 * the blue-taped transition variant ({@code Block_Blue_Taped}).
 * <p>
 * Integrates with the existing {@link FramingSystem} interaction queue
 * via {@link #onBlueTapeUse(FramingSystem, long, String)}.
 * <p>
 * <h3>Planned flow</h3>
 * <ol>
 *   <li>Player right-clicks a block while holding {@code Item_Blue_Tape}.</li>
 *   <li>The interaction is queued through {@link FramingSystem#queueInteraction}.</li>
 *   <li>During the next tick, {@link #onBlueTapeUse} is called:</li>
 *   <li>→ reads the current block at the position</li>
 *   <li>→ if it's not already taped, replaces it with {@code Block_Blue_Taped}</li>
 *   <li>→ plays a sound / consumes one tape item</li>
 * </ol>
 */
public final class BlueTapeHandler {

    /** The item ID for the Blue Tape item. */
    public static final String BLUE_TAPE_ITEM_ID = "Item_Blue_Tape";

    /** The block ID placed when tape is applied. */
    public static final String TAPED_BLOCK_ID = "Block_Blue_Taped";

    private BlueTapeHandler() {
        // utility class — no instances
    }

    /**
     * Called when a player uses Blue Tape on a block.
     *
     * @param system         the active {@link FramingSystem}
     * @param packedPosition the packed chunk+local position of the clicked block
     * @param clickedBlockId the block ID of the clicked block
     * @param world          the {@link World} to modify
     * @return {@code true} if the tape was applied, {@code false} if skipped
     */
    public static boolean onBlueTapeUse(
            FramingSystem system,
            long packedPosition,
            String clickedBlockId,
            World world
    ) {
        // Don't re-tape an already taped block
        if (TAPED_BLOCK_ID.equals(clickedBlockId)) {
            return false;
        }

        // Replace the block with the taped variant via the world reference
        boolean success = system.replaceBlock(packedPosition, TAPED_BLOCK_ID, world);
        if (success) {
            log("Applied blue tape to block at " + Long.toHexString(packedPosition)
                    + " (was: " + clickedBlockId + ")");
            // Future: play a sticking sound, consume one Item_Blue_Tape from stack
        }
        return success;
    }

    private static void log(String message) {
        HytaleLogger.getLogger().at(Level.INFO).log("[BlueTape] " + message);
    }
}
