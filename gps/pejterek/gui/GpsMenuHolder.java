/*    */ package pejterek.gui;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import org.bukkit.inventory.Inventory;
/*    */ import org.bukkit.inventory.InventoryHolder;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public final class GpsMenuHolder
/*    */   implements InventoryHolder {
/*    */   private final int page;
/*    */   private Inventory inventory;
/*    */   
/*    */   public GpsMenuHolder(int page) {
/* 14 */     this.page = page;
/*    */   }
/*    */   
/*    */   public int page() {
/* 18 */     return this.page;
/*    */   }
/*    */   
/*    */   public void setInventory(Inventory inventory) {
/* 22 */     this.inventory = inventory;
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public Inventory getInventory() {
/* 27 */     return Objects.<Inventory>requireNonNull(this.inventory, "Inventory nie został jeszcze utworzony");
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\skyriseDEV\plugins\GPS-1.5.jar!\pejterek\gui\GpsMenuHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */