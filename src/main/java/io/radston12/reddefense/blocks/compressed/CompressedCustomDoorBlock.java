package io.radston12.reddefense.blocks.compressed;

import io.radston12.reddefense.RedDefenseMod;
import io.radston12.reddefense.blockentities.custom.OwnableBlockEntity;
import io.radston12.reddefense.datagen.ModBlockStateProvider;
import io.radston12.reddefense.datagen.interfaces.LootTableProviderData;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class CompressedCustomDoorBlock extends CompressedDoorBlock {

    private final String dataName;
    private final boolean onlyOwnerCanOpen, isReactiveToRedstone, hasTint;

    public CompressedCustomDoorBlock(String name, Block vanilla, String dataName) {
        this(name, vanilla, dataName, true, false, true);
    }

    public CompressedCustomDoorBlock(String name, Block vanilla, String dataName, boolean onlyOwnerCanOpen, boolean isReactiveToRedstone, boolean hasTint) {
        super(name, vanilla);

        this.dataName = dataName;
        this.onlyOwnerCanOpen = onlyOwnerCanOpen;
        this.isReactiveToRedstone = isReactiveToRedstone;
        this.hasTint = hasTint;
    }

    @Override
    public String getRegistryName() {
        return this.dataName;
    }

    @Override
    public ResourceLocation getTexture(ModBlockStateProvider provider) {
        return new ResourceLocation(RedDefenseMod.MOD_ID, "block/" + dataName);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level lvl, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult hitResult) {
        if (!lvl.isClientSide()) return InteractionResult.PASS;

        if (onlyOwnerCanOpen) {
            OwnableBlockEntity entity = (OwnableBlockEntity) lvl.getBlockEntity(pos);
            if (!entity.isOwner(player)) return InteractionResult.PASS;

            cylceOpen(lvl, state, pos, player);

            return InteractionResult.PASS;
        }

        return super.use(state, lvl, pos, player, interactionHand, hitResult);
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, Level lvl, @NotNull BlockPos pos, @NotNull Block block, @NotNull BlockPos neighbor, boolean unusedBool) {
        handleNeighborChange(lvl, pos, neighbor);

        if (isReactiveToRedstone) super.neighborChanged(state, lvl, pos, block, neighbor, unusedBool);
    }

    @Override
    public Block getCraftingBlock() {
        return null; // don't generate a recipe because it will crash because there is already another recipe using f.e. obsidian
    }

    @Override
    public boolean hasTint() {
        return hasTint;
    }
}
