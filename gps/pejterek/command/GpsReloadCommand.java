/*    */ package pejterek.command;
/*    */ 
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import pejterek.GPS;
/*    */ import pejterek.service.GpsPointService;
/*    */ import pejterek.service.MessageService;
/*    */ import pejterek.service.NavigationService;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class GpsReloadCommand
/*    */   implements CommandExecutor
/*    */ {
/*    */   private final GPS plugin;
/*    */   private final GpsPointService pointService;
/*    */   private final NavigationService navigationService;
/*    */   private final MessageService messages;
/*    */   
/*    */   public GpsReloadCommand(GPS plugin, GpsPointService pointService, NavigationService navigationService, MessageService messages) {
/* 25 */     this.plugin = plugin;
/* 26 */     this.pointService = pointService;
/* 27 */     this.navigationService = navigationService;
/* 28 */     this.messages = messages;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
/* 38 */     if (!sender.hasPermission("gps.admin")) {
/* 39 */       this.messages.send(sender, "messages.no-permission");
/* 40 */       return true;
/*    */     } 
/*    */     
/* 43 */     this.plugin.reloadConfig();
/* 44 */     this.pointService.reload();
/* 45 */     this.navigationService.reloadSettings();
/* 46 */     this.messages.send(sender, "messages.reloaded");
/* 47 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\skyriseDEV\plugins\GPS-1.5.jar!\pejterek\command\GpsReloadCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */