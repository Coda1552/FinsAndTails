package teamdraco.fins.common.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import teamdraco.fins.common.container.slot.CrabCruncherResultSlot;
import teamdraco.fins.common.container.slot.CrabCruncherSlot;
import teamdraco.fins.common.crafting.CrunchingRecipe;
import teamdraco.fins.init.FinsBlocks;
import teamdraco.fins.init.FinsContainers;
import teamdraco.fins.init.FinsRecipes;
import teamdraco.fins.init.FinsSounds;

import java.util.Optional;

public class CrabCruncherContainer extends Container {
    private final IWorldPosCallable worldPosCallable;
    private final PlayerEntity player;
    private final CraftingInventory inventory = new CraftingInventory(this, 2, 1);
    private final CraftResultInventory craftResult = new CraftResultInventory();

    public CrabCruncherContainer(final int windowId, PlayerInventory playerInventory) {
        this(windowId, playerInventory, IWorldPosCallable.NULL);
    }

    public CrabCruncherContainer(final int windowId, PlayerInventory playerInventory, IWorldPosCallable worldPosCallable) {
        super(FinsContainers.CRAB_CRUNCHER.get(), windowId);
        this.worldPosCallable = worldPosCallable;
        this.player = playerInventory.player;

        // Result Slot
        this.addSlot(new CrabCruncherResultSlot(player, inventory, craftResult, 0, 134, 47));

        // Input Slots
        this.addSlot(new CrabCruncherSlot(inventory, 0, 27, 47));
        this.addSlot(new CrabCruncherSlot(inventory, 1, 76, 47));

        // Main Player Inv
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 166 - (4 - row) * 18 - 10));
            }
        }

        // Player Hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return stillValid(worldPosCallable, playerIn, FinsBlocks.CRAB_CRUNCHER.get());
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index == 0) {
                this.worldPosCallable.execute((p_217067_2_, p_217067_3_) -> {
                    itemstack1.getItem().onCraftedBy(itemstack1, p_217067_2_, playerIn);
                });
                if (!this.moveItemStackTo(itemstack1, 3, 39,true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index >= 3 && index < 39) {
                if (!this.moveItemStackTo(itemstack1, 1, 3, false)) {
                    if (index < 37) {
                        if (!this.moveItemStackTo(itemstack1, 37, 39, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.moveItemStackTo(itemstack1, 3, 30, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);
            if (index == 0) {
                playerIn.drop(itemstack2, false);
            }
        }

        return itemstack;
    }

    protected void updateCraftingResult(World world) {
        if (!world.isClientSide) {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)player;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<CrunchingRecipe> optional = world.getServer().getRecipeManager().getRecipeFor(FinsRecipes.CRUNCHING_TYPE, inventory, world);
            if (optional.isPresent()) {
                itemstack = optional.get().assemble(inventory);
            }
            world.playSound(player, player.blockPosition(), FinsSounds.CRAB_CRUNCH.get(), SoundCategory.BLOCKS, 0.6F, 1.0F);

            craftResult.setItem(0, itemstack);
            serverplayerentity.connection.send(new SSetSlotPacket(containerId, 0, itemstack));
        }
    }

    @Override
    public void slotsChanged(IInventory inventoryIn) {
        this.worldPosCallable.execute((p_217069_1_, p_217069_2_) -> updateCraftingResult(p_217069_1_));
    }

    @Override
    public void removed(PlayerEntity playerIn) {
        super.removed(playerIn);
        this.worldPosCallable.execute((p_217068_2_, p_217068_3_) -> this.clearContainer(playerIn, p_217068_2_, inventory));
    }
}
