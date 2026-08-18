/*    */ package pejterek.service;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ import net.kyori.adventure.text.Component;
/*    */ import net.kyori.adventure.text.format.TextDecoration;
/*    */ import net.kyori.adventure.text.minimessage.MiniMessage;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ import pejterek.GPS;
/*    */ 
/*    */ public final class MessageService
/*    */ {
/*    */   private final GPS plugin;
/* 15 */   private final MiniMessage miniMessage = MiniMessage.miniMessage();
/*    */   
/*    */   public MessageService(GPS plugin) {
/* 18 */     this.plugin = plugin;
/*    */   }
/*    */   
/*    */   public void send(CommandSender sender, String path) {
/* 22 */     send(sender, path, Collections.emptyMap());
/*    */   }
/*    */   
/*    */   public void send(CommandSender sender, String path, Map<String, String> placeholders) {
/* 26 */     FileConfiguration config = this.plugin.getConfig();
/* 27 */     String prefix = config.getString("messages.prefix", "");
/* 28 */     String message = config.getString(path, "<red>Brak wiadomości: " + path);
/* 29 */     sender.sendMessage(parse(prefix + prefix, placeholders));
/*    */   }
/*    */   
/*    */   public Component fromConfig(String path, Map<String, String> placeholders) {
/* 33 */     return parse(this.plugin.getConfig().getString(path, ""), placeholders);
/*    */   }
/*    */   
/*    */   public Component parse(String input) {
/* 37 */     return parse(input, Collections.emptyMap());
/*    */   }
/*    */   
/*    */   public Component parse(String input, Map<String, String> placeholders) {
/* 41 */     String parsed = (input == null) ? "" : input;
/* 42 */     for (Map.Entry<String, String> entry : placeholders.entrySet()) {
/* 43 */       parsed = parsed.replace("{" + (String)entry.getKey() + "}", entry.getValue());
/*    */     }
/* 45 */     return this.miniMessage.deserialize(parsed)
/* 46 */       .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
/*    */   }
/*    */ }


/* Location:              C:\Users\DELL\Desktop\skyriseDEV\plugins\GPS-1.5.jar!\pejterek\service\MessageService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */