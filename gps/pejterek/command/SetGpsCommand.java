/*    */ package pejterek.command;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import pejterek.service.GpsPointService;
/*    */ import pejterek.service.MessageService;
/*    */ 
/*    */ public final class SetGpsCommand
/*    */   implements CommandExecutor {
/*    */   private final GpsPointService pointService;
/*    */   private final MessageService messages;
/*    */   
/*    */   public SetGpsCommand(GpsPointService pointService, MessageService messages) {
/* 18 */     this.pointService = pointService;
/* 19 */     this.messages = messages;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
/*    */     Player player;
/* 29 */     if (sender instanceof Player) { player = (Player)sender; }
/* 30 */     else { this.messages.send(sender, "messages.only-player");
/* 31 */       return true; }
/*    */     
/* 33 */     if (!player.hasPermission("gps.admin")) {
/* 34 */       this.messages.send((CommandSender)player, "messages.no-permission");
/* 35 */       return true;
/*    */     } 
/* 37 */     if (args.length == 0) {
/* 38 */       this.messages.send((CommandSender)player, "messages.usage-set");
/* 39 */       return true;
/*    */     } 
/*    */     
/* 42 */     String name = String.join(" ", (CharSequence[])args).trim();
/* 43 */     GpsPointService.CreateResult result = this.pointService.create(name, player.getLocation());
/* 44 */     if (!result.created()) {
/* 45 */       this.messages.send((CommandSender)player, "messages.point-exists", Map.of("id", result.id()));
/* 46 */       return true;
/*    */     } 
/*    */     
/* 49 */     this.messages.send((CommandSender)player, "messages.point-created", Map.of("name", name, "id", result
/*    */           
/* 51 */           .id()));
/*    */     
/* 53 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\skyriseDEV\plugins\GPS-1.5.jar!\pejterek\command\SetGpsCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */