/*    */ package pejterek.command;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandExecutor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.command.TabCompleter;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import org.jetbrains.annotations.Nullable;
/*    */ import pejterek.model.GpsPoint;
/*    */ import pejterek.service.GpsPointService;
/*    */ import pejterek.service.MessageService;
/*    */ 
/*    */ public final class DeleteGpsCommand
/*    */   implements CommandExecutor, TabCompleter {
/*    */   private final GpsPointService pointService;
/*    */   private final MessageService messages;
/*    */   
/*    */   public DeleteGpsCommand(GpsPointService pointService, MessageService messages) {
/* 23 */     this.pointService = pointService;
/* 24 */     this.messages = messages;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
/* 34 */     if (!sender.hasPermission("gps.admin")) {
/* 35 */       this.messages.send(sender, "messages.no-permission");
/* 36 */       return true;
/*    */     } 
/* 38 */     if (args.length == 0) {
/* 39 */       this.messages.send(sender, "messages.usage-delete");
/* 40 */       return true;
/*    */     } 
/*    */     
/* 43 */     String query = String.join(" ", (CharSequence[])args).trim();
/* 44 */     Optional<GpsPoint> point = this.pointService.find(query);
/* 45 */     if (point.isEmpty()) {
/* 46 */       this.messages.send(sender, "messages.point-not-found", Map.of("name", query));
/* 47 */       return true;
/*    */     } 
/*    */     
/* 50 */     GpsPoint found = point.get();
/* 51 */     this.pointService.delete(found.id());
/* 52 */     this.messages.send(sender, "messages.point-deleted", Map.of("name", found.displayName()));
/* 53 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
/* 63 */     if (args.length != 1) {
/* 64 */       return List.of();
/*    */     }
/* 66 */     String prefix = args[0].toLowerCase(Locale.ROOT);
/* 67 */     return this.pointService.getIds().stream()
/* 68 */       .filter(id -> id.startsWith(prefix))
/* 69 */       .sorted()
/* 70 */       .toList();
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\skyriseDEV\plugins\GPS-1.5.jar!\pejterek\command\DeleteGpsCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */