package scubakay.finalstand.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.random.Random;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.border.WorldBorder;

public class ChestPlacer {
    private static final Random RANDOM = Random.create();

    public static void placeChestRandomly(ServerWorld world) {
        WorldBorder border = world.getWorldBorder();
        double centerX = border.getCenterX();
        double centerZ = border.getCenterZ();
        double size = border.getSize();
        double minX = centerX - size / 2;
        double maxX = centerX + size / 2;
        double minZ = centerZ - size / 2;
        double maxZ = centerZ + size / 2;
        int x = (int) (RANDOM.nextDouble() * (maxX - minX) + minX);
        int z = (int) (RANDOM.nextDouble() * (maxZ - minZ) + minZ);
        int y = world.getTopY(Heightmap.Type.WORLD_SURFACE, x, z);
        BlockPos pos = new BlockPos(x, y, z);
        BlockState state = world.getBlockState(pos);
        BlockState blockUnderneath = world.getBlockState(new BlockPos(x, y-1, z));
        if (canChestSpawn(state, blockUnderneath)) {
            world.setBlockState(pos, Blocks.CHEST.getDefaultState());
            ChestBlockEntity chestBlockEntity = (ChestBlockEntity) world.getBlockEntity(pos);
            chestBlockEntity.setLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, ModLootTables.FINAL_STAND_TREASURE_CHEST), world.getRandom().nextLong());
            world.getServer().getPlayerManager().broadcast(Text.translatable("session.finalstand.treasure_chest_placed").formatted(Formatting.BLUE), false);
        } else {
            placeChestRandomly(world);
        }
    }

    private static boolean canChestSpawn(BlockState state, BlockState blockUnderneath) {
        return state.isAir() &&
                blockUnderneath.getBlock() != Blocks.WATER &&
                blockUnderneath.getBlock() != Blocks.ACACIA_LEAVES &&
                blockUnderneath.getBlock() != Blocks.BIRCH_LEAVES &&
                blockUnderneath.getBlock() != Blocks.JUNGLE_LEAVES &&
                blockUnderneath.getBlock() != Blocks.OAK_LEAVES &&
                blockUnderneath.getBlock() != Blocks.JUNGLE_LEAVES &&
                blockUnderneath.getBlock() != Blocks.MANGROVE_LEAVES &&
                blockUnderneath.getBlock() != Blocks.AZALEA_LEAVES &&
                blockUnderneath.getBlock() != Blocks.FLOWERING_AZALEA_LEAVES &&
                blockUnderneath.getBlock() != Blocks.DARK_OAK_LEAVES &&
                blockUnderneath.getBlock() != Blocks.SPRUCE_LEAVES;
    }
}