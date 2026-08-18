/*     */ package pejterek;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import org.bukkit.command.CommandExecutor;
/*     */ import org.bukkit.command.PluginCommand;
/*     */ import org.bukkit.command.TabCompleter;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.HandlerList;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import pejterek.command.DeleteGpsCommand;
/*     */ import pejterek.command.GpsCommand;
/*     */ import pejterek.command.GpsReloadCommand;
/*     */ import pejterek.command.SetGpsCommand;
/*     */ import pejterek.gui.GpsMenu;
/*     */ import pejterek.listener.GpsMenuListener;
/*     */ import pejterek.listener.PlayerConnectionListener;
/*     */ import pejterek.listener.PlayerDeathListener;
/*     */ import pejterek.service.GpsPointService;
/*     */ import pejterek.service.MessageService;
/*     */ import pejterek.service.NavigationService;
/*     */ import pejterek.util.GuiItemFactory;
/*     */ 
/*     */ public final class GPS
/*     */   extends JavaPlugin {
/*     */   private MessageService messages;
/*     */   private GpsPointService pointService;
/*     */   
/*     */   public void onEnable() {
/*  31 */     saveDefaultConfig();
/*  32 */     saveResource("locations.yml", false);
/*     */     
/*  34 */     this.messages = new MessageService(this);
/*  35 */     this.pointService = new GpsPointService(this);
/*  36 */     this.pointService.reload();
/*     */     
/*  38 */     GuiItemFactory itemFactory = new GuiItemFactory(this, this.messages);
/*     */     
/*  40 */     this.navigationService = new NavigationService(this, this.pointService, this.messages);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  46 */     this.gpsMenu = new GpsMenu(this, this.pointService, this.navigationService, this.messages, itemFactory);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  54 */     registerCommands();
/*     */     
/*  56 */     getServer().getPluginManager().registerEvents((Listener)new GpsMenuListener(this.gpsMenu, this.navigationService, this.pointService, this.messages, itemFactory), (Plugin)this);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  67 */     getServer().getPluginManager().registerEvents((Listener)new PlayerConnectionListener(this.navigationService), (Plugin)this);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  72 */     getServer().getPluginManager().registerEvents((Listener)new PlayerDeathListener(this.navigationService), (Plugin)this);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  77 */     this.navigationService.reloadSettings();
/*     */     
/*  79 */     getLogger().info("GPS został uruchomiony. Wczytano punktów: " + this.pointService
/*     */         
/*  81 */         .size());
/*     */   }
/*     */   private NavigationService navigationService;
/*     */   private GpsMenu gpsMenu;
/*     */   
/*     */   public void onDisable() {
/*  87 */     closeOpenGpsMenus();
/*     */     
/*  89 */     if (this.navigationService != null) {
/*  90 */       this.navigationService.shutdown();
/*     */     }
/*  92 */     if (this.pointService != null) {
/*  93 */       this.pointService.shutdown();
/*     */     }
/*     */     
/*  96 */     HandlerList.unregisterAll((Plugin)this);
/*  97 */     getServer().getScheduler().cancelTasks((Plugin)this);
/*     */     
/*  99 */     this.gpsMenu = null;
/* 100 */     this.navigationService = null;
/* 101 */     this.pointService = null;
/* 102 */     this.messages = null;
/*     */   }
/*     */   
/*     */   private void closeOpenGpsMenus() {
/* 106 */     for (Player player : getServer().getOnlinePlayers()) {
/* 107 */       if (player.getOpenInventory().getTopInventory().getHolder() instanceof pejterek.gui.GpsMenuHolder) {
/* 108 */         player.closeInventory();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void registerCommands() {
/* 114 */     GpsCommand gpsCommand = new GpsCommand(this.gpsMenu, this.navigationService, this.pointService, this.messages);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     PluginCommand gps = command("gps");
/* 122 */     gps.setExecutor((CommandExecutor)gpsCommand);
/* 123 */     gps.setTabCompleter((TabCompleter)gpsCommand);
/*     */     
/* 125 */     SetGpsCommand setGpsCommand = new SetGpsCommand(this.pointService, this.messages);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     PluginCommand ustawGps = command("ustawgps");
/* 131 */     ustawGps.setExecutor((CommandExecutor)setGpsCommand);
/*     */     
/* 133 */     DeleteGpsCommand deleteGpsCommand = new DeleteGpsCommand(this.pointService, this.messages);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     PluginCommand usunGps = command("usungps");
/* 139 */     usunGps.setExecutor((CommandExecutor)deleteGpsCommand);
/* 140 */     usunGps.setTabCompleter((TabCompleter)deleteGpsCommand);
/*     */     
/* 142 */     GpsReloadCommand reloadCommand = new GpsReloadCommand(this, this.pointService, this.navigationService, this.messages);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     command("gpsreload").setExecutor((CommandExecutor)reloadCommand);
/*     */   }
/*     */   
/*     */   private PluginCommand command(String name) {
/* 153 */     return Objects.<PluginCommand>requireNonNull(
/* 154 */         getCommand(name), "Brak komendy");
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\skyriseDEV\plugins\GPS-1.5.jar!\pejterek\GPS.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */