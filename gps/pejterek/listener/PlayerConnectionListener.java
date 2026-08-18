/*    */ package pejterek.listener;
/*    */ 
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.player.PlayerKickEvent;
/*    */ import org.bukkit.event.player.PlayerQuitEvent;
/*    */ import pejterek.service.NavigationService;
/*    */ 
/*    */ public final class PlayerConnectionListener
/*    */   implements Listener {
/*    */   private final NavigationService navigationService;
/*    */   
/*    */   public PlayerConnectionListener(NavigationService navigationService) {
/* 14 */     this.navigationService = navigationService;
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onQuit(PlayerQuitEvent event) {
/* 19 */     this.navigationService.remove(event.getPlayer());
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onKick(PlayerKickEvent event) {
/* 24 */     this.navigationService.remove(event.getPlayer());
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\skyriseDEV\plugins\GPS-1.5.jar!\pejterek\listener\PlayerConnectionListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */