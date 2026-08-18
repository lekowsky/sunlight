/*     */ package pejterek.gui;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.configuration.ConfigurationSection;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import pejterek.GPS;
/*     */ import pejterek.model.GpsPoint;
/*     */ import pejterek.service.GpsPointService;
/*     */ import pejterek.service.MessageService;
/*     */ import pejterek.service.NavigationService;
/*     */ import pejterek.util.GuiItemFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GpsMenu
/*     */ {
/*     */   public static final int PREVIOUS_SLOT = 18;
/*     */   public static final int STOP_SLOT = 22;
/*     */   public static final int NEXT_SLOT = 25;
/*     */   public static final int CLOSE_SLOT = 26;
/*     */   private final GPS plugin;
/*     */   private final GpsPointService pointService;
/*     */   private final NavigationService navigationService;
/*     */   private final MessageService messages;
/*     */   private final GuiItemFactory itemFactory;
/*     */   
/*     */   public GpsMenu(GPS plugin, GpsPointService pointService, NavigationService navigationService, MessageService messages, GuiItemFactory itemFactory) {
/*  39 */     this.plugin = plugin;
/*  40 */     this.pointService = pointService;
/*  41 */     this.navigationService = navigationService;
/*  42 */     this.messages = messages;
/*  43 */     this.itemFactory = itemFactory;
/*     */   }
/*     */   
/*     */   public void open(Player player, int requestedPage) {
/*  47 */     List<GpsPoint> points = this.pointService.getSortedPoints();
/*  48 */     int itemsPerPage = Math.min(18, Math.max(1, this.plugin.getConfig().getInt("menu.items-per-page", 18)));
/*  49 */     int pages = Math.max(1, (int)Math.ceil(points.size() / itemsPerPage));
/*  50 */     int page = Math.max(0, Math.min(requestedPage, pages - 1));
/*     */     
/*  52 */     Map<String, String> titlePlaceholders = Map.of("page", 
/*  53 */         String.valueOf(page + 1), "pages", 
/*  54 */         String.valueOf(pages));
/*     */     
/*  56 */     Component title = this.messages.fromConfig("menu.title", titlePlaceholders);
/*     */     
/*  58 */     GpsMenuHolder holder = new GpsMenuHolder(page);
/*  59 */     Inventory inventory = Bukkit.createInventory(holder, 27, title);
/*  60 */     holder.setInventory(inventory);
/*     */     
/*  62 */     ItemStack filler = createFiller();
/*  63 */     for (int slot = 18; slot < 27; slot++) {
/*  64 */       inventory.setItem(slot, filler);
/*     */     }
/*     */     
/*  67 */     if (points.isEmpty()) {
/*  68 */       inventory.setItem(13, this.itemFactory.createConfiguredItem("menu.empty", "", Map.of()));
/*     */     } else {
/*  70 */       int start = page * itemsPerPage;
/*  71 */       int end = Math.min(start + itemsPerPage, points.size());
/*  72 */       for (int index = start; index < end; index++) {
/*  73 */         inventory.setItem(index - start, this.itemFactory.createPointItem(points.get(index), player));
/*     */       }
/*     */     } 
/*     */     
/*  77 */     if (page > 0) {
/*  78 */       inventory.setItem(18, this.itemFactory.createConfiguredItem("menu.previous", "previous", Map.of()));
/*     */     }
/*  80 */     if (page + 1 < pages) {
/*  81 */       inventory.setItem(25, this.itemFactory.createConfiguredItem("menu.next", "next", Map.of()));
/*     */     }
/*     */     
/*  84 */     if (this.navigationService.isNavigating(player)) {
/*     */ 
/*     */       
/*  87 */       String target = this.navigationService.getTarget(player).map(GpsPoint::displayName).orElse("nieznany");
/*  88 */       inventory.setItem(22, this.itemFactory.createConfiguredItem("menu.stop", "stop", 
/*     */ 
/*     */             
/*  91 */             Map.of("target", target)));
/*     */     } 
/*     */ 
/*     */     
/*  95 */     inventory.setItem(26, this.itemFactory.createConfiguredItem("menu.close", "close", Map.of()));
/*  96 */     player.openInventory(inventory);
/*     */   }
/*     */   
/*     */   private ItemStack createFiller() {
/* 100 */     ConfigurationSection section = this.plugin.getConfig().getConfigurationSection("menu.filler");
/* 101 */     Material material = Material.GRAY_STAINED_GLASS_PANE;
/* 102 */     String name = "<gray>";
/* 103 */     if (section != null) {
/* 104 */       Material parsed = Material.matchMaterial(section.getString("material", "GRAY_STAINED_GLASS_PANE"));
/* 105 */       if (parsed != null && parsed.isItem()) {
/* 106 */         material = parsed;
/*     */       }
/* 108 */       name = section.getString("name", name);
/*     */     } 
/* 110 */     return this.itemFactory.createSimple(material, name, List.of(), "filler");
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\skyriseDEV\plugins\GPS-1.5.jar!\pejterek\gui\GpsMenu.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */