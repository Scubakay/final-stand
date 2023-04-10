package scubakay.finalstand.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.text.Text;
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
        if (state.isAir()) {
            world.setBlockState(pos, Blocks.CHEST.getDefaultState());
            ChestBlockEntity chestBlockEntity = (ChestBlockEntity) world.getBlockEntity(pos);
            chestBlockEntity.setLootTable(ModLootTables.FINAL_STAND_TREASURE_CHEST, world.getRandom().nextLong());
            world.getServer().getPlayerManager().broadcast(Text.translatable("session.finalstand.treasure_chest_placed"), false);
        } else {
            placeChestRandomly(world);
        }
    }
}