/*    */ package pejterek.listener;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.HumanEntity;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.inventory.InventoryClickEvent;
/*    */ import org.bukkit.inventory.Inventory;
/*    */ import org.bukkit.inventory.InventoryHolder;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import pejterek.gui.GpsMenu;
/*    */ import pejterek.gui.GpsMenuHolder;
/*    */ import pejterek.model.GpsPoint;
/*    */ import pejterek.service.GpsPointService;
/*    */ import pejterek.service.MessageService;
/*    */ import pejterek.service.NavigationService;
/*    */ import pejterek.util.GuiItemFactory;
/*    */ 
/*    */ 
/*    */ public final class GpsMenuListener
/*    */   implements Listener
/*    */ {
/*    */   private final GpsMenu menu;
/*    */   private final NavigationService navigationService;
/*    */   private final GpsPointService pointService;
/*    */   private final MessageService messages;
/*    */   private final GuiItemFactory itemFactory;
/*    */   
/*    */   public GpsMenuListener(GpsMenu menu, NavigationService navigationService, GpsPointService pointService, MessageService messages, GuiItemFactory itemFactory) {
/* 32 */     this.menu = menu;
/* 33 */     this.navigationService = navigationService;
/* 34 */     this.pointService = pointService;
/* 35 */     this.messages = messages;
/* 36 */     this.itemFactory = itemFactory;
/*    */   } @EventHandler
/*    */   public void onInventoryClick(InventoryClickEvent event) {
/*    */     GpsMenuHolder holder;
/*    */     Player player;
/* 41 */     Inventory topInventory = event.getView().getTopInventory();
/* 42 */     InventoryHolder inventoryHolder = topInventory.getHolder(); if (inventoryHolder instanceof GpsMenuHolder) { holder = (GpsMenuHolder)inventoryHolder; }
/*    */     else
/*    */     { return; }
/*    */     
/* 46 */     event.setCancelled(true);
/* 47 */     HumanEntity humanEntity = event.getWhoClicked(); if (humanEntity instanceof Player) { player = (Player)humanEntity; }
/*    */     else
/*    */     { return; }
/* 50 */      if (event.getRawSlot() < 0 || event.getRawSlot() >= topInventory.getSize()) {
/*    */       return;
/*    */     }
/*    */     
/* 54 */     ItemStack clicked = event.getCurrentItem();
/* 55 */     if (clicked == null || clicked.getType().isAir()) {
/*    */       return;
/*    */     }
/*    */     
/* 59 */     String pointId = this.itemFactory.getPointId(clicked);
/* 60 */     if (pointId != null) {
/* 61 */       GpsPoint point = this.pointService.getById(pointId);
/* 62 */       if (point == null) {
/* 63 */         this.messages.send((CommandSender)player, "messages.point-not-found", Map.of("name", pointId));
/* 64 */         player.closeInventory();
/*    */         return;
/*    */       } 
/* 67 */       if (this.navigationService.startNavigation(player, point)) {
/* 68 */         player.closeInventory();
/*    */       }
/*    */       
/*    */       return;
/*    */     } 
/* 73 */     String action = this.itemFactory.getAction(clicked);
/* 74 */     if (action == null) {
/*    */       return;
/*    */     }
/*    */     
/* 78 */     switch (action) { case "previous":
/* 79 */         this.menu.open(player, holder.page() - 1); break;
/* 80 */       case "next": this.menu.open(player, holder.page() + 1); break;
/*    */       case "stop":
/* 82 */         this.navigationService.stopNavigation(player, true);
/* 83 */         player.closeInventory(); break;
/*    */       case "close":
/* 85 */         player.closeInventory();
/*    */         break; }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\skyriseDEV\plugins\GPS-1.5.jar!\pejterek\listener\GpsMenuListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */