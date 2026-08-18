/*    */ package pejterek.listener;
/*    */ 
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.entity.PlayerDeathEvent;
/*    */ import pejterek.service.NavigationService;
/*    */ 
/*    */ public final class PlayerDeathListener
/*    */   implements Listener {
/*    */   private final NavigationService navigationService;
/*    */   
/*    */   public PlayerDeathListener(NavigationService navigationService) {
/* 13 */     this.navigationService = navigationService;
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onPlayerDeath(PlayerDeathEvent event) {
/* 18 */     this.navigationService.cancelNavigation(event.getEntity());
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\skyriseDEV\plugins\GPS-1.5.jar!\pejterek\listener\PlayerDeathListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */