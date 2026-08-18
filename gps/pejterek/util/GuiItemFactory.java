/*     */ package pejterek.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.NamespacedKey;
/*     */ import org.bukkit.configuration.ConfigurationSection;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemFlag;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.inventory.meta.components.CustomModelDataComponent;
/*     */ import org.bukkit.persistence.PersistentDataType;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import pejterek.GPS;
/*     */ import pejterek.model.GpsPoint;
/*     */ import pejterek.model.IconSettings;
/*     */ import pejterek.service.MessageService;
/*     */ 
/*     */ public final class GuiItemFactory {
/*     */   private final GPS plugin;
/*     */   private final MessageService messages;
/*     */   private final NamespacedKey pointIdKey;
/*     */   private final NamespacedKey actionKey;
/*     */   
/*     */   public GuiItemFactory(GPS plugin, MessageService messages) {
/*  30 */     this.plugin = plugin;
/*  31 */     this.messages = messages;
/*  32 */     this.pointIdKey = new NamespacedKey((Plugin)plugin, "point_id");
/*  33 */     this.actionKey = new NamespacedKey((Plugin)plugin, "menu_action");
/*     */   }
/*     */   
/*     */   public ItemStack createPointItem(GpsPoint point, Player player) {
/*  37 */     IconSettings icon = point.icon();
/*  38 */     ItemStack item = new ItemStack(icon.material());
/*  39 */     ItemMeta meta = item.getItemMeta();
/*     */     
/*  41 */     Map<String, String> placeholders = placeholders(point, player);
/*  42 */     meta.displayName(this.messages.parse(icon.name(), placeholders));
/*     */ 
/*     */ 
/*     */     
/*  46 */     List<Component> lore = icon.lore().stream().map(line -> this.messages.parse(line, placeholders)).toList();
/*  47 */     meta.lore(lore);
/*     */     
/*  49 */     applyModel(meta, icon.itemModel(), icon.customModelData());
/*  50 */     meta.setEnchantmentGlintOverride(Boolean.valueOf(icon.glow()));
/*  51 */     meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
/*  52 */     meta.getPersistentDataContainer().set(this.pointIdKey, PersistentDataType.STRING, point.id());
/*  53 */     item.setItemMeta(meta);
/*  54 */     return item;
/*     */   }
/*     */   
/*     */   public ItemStack createConfiguredItem(String path, String action, Map<String, String> placeholders) {
/*  58 */     ConfigurationSection section = this.plugin.getConfig().getConfigurationSection(path);
/*  59 */     Material material = Material.BARRIER;
/*  60 */     String name = "<red>Brak konfiguracji";
/*  61 */     List<String> lore = List.of();
/*     */     
/*  63 */     if (section != null) {
/*  64 */       Material parsed = Material.matchMaterial(section.getString("material", "BARRIER"));
/*  65 */       if (parsed != null && parsed.isItem()) {
/*  66 */         material = parsed;
/*     */       }
/*  68 */       name = section.getString("name", name);
/*  69 */       lore = section.getStringList("lore");
/*     */     } 
/*     */     
/*  72 */     return createSimple(material, name, lore, action, placeholders);
/*     */   }
/*     */   
/*     */   public ItemStack createSimple(Material material, String name, List<String> lore, String action) {
/*  76 */     return createSimple(material, name, lore, action, Map.of());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack createSimple(Material material, String name, List<String> lore, String action, Map<String, String> placeholders) {
/*  86 */     ItemStack item = new ItemStack(material);
/*  87 */     ItemMeta meta = item.getItemMeta();
/*  88 */     meta.displayName(this.messages.parse(name, placeholders));
/*  89 */     meta.lore(lore.stream().map(line -> this.messages.parse(line, placeholders)).toList());
/*  90 */     meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
/*  91 */     if (action != null && !action.isBlank()) {
/*  92 */       meta.getPersistentDataContainer().set(this.actionKey, PersistentDataType.STRING, action);
/*     */     }
/*  94 */     item.setItemMeta(meta);
/*  95 */     return item;
/*     */   }
/*     */   
/*     */   public String getPointId(ItemStack item) {
/*  99 */     if (item == null || !item.hasItemMeta()) {
/* 100 */       return null;
/*     */     }
/* 102 */     return (String)item.getItemMeta().getPersistentDataContainer().get(this.pointIdKey, PersistentDataType.STRING);
/*     */   }
/*     */   
/*     */   public String getAction(ItemStack item) {
/* 106 */     if (item == null || !item.hasItemMeta()) {
/* 107 */       return null;
/*     */     }
/* 109 */     return (String)item.getItemMeta().getPersistentDataContainer().get(this.actionKey, PersistentDataType.STRING);
/*     */   }
/*     */   
/*     */   private Map<String, String> placeholders(GpsPoint point, Player player) {
/* 113 */     Map<String, String> values = new HashMap<>();
/* 114 */     values.put("name", point.displayName());
/* 115 */     values.put("world", point.worldName());
/* 116 */     values.put("x", String.valueOf(Math.round(point.x())));
/* 117 */     values.put("y", String.valueOf(Math.round(point.y())));
/* 118 */     values.put("z", String.valueOf(Math.round(point.z())));
/*     */     
/* 120 */     if (player.getWorld().getName().equals(point.worldName())) {
/* 121 */       double distance = player.getLocation().distance(point.toLocation());
/* 122 */       values.put("distance", String.valueOf(Math.round(distance)));
/*     */     } else {
/* 124 */       values.put("distance", "inny świat");
/*     */     } 
/* 126 */     return values;
/*     */   }
/*     */   
/*     */   private void applyModel(ItemMeta meta, String itemModel, Integer customModelData) {
/* 130 */     if (itemModel != null && !itemModel.isBlank()) {
/* 131 */       NamespacedKey key = NamespacedKey.fromString(itemModel);
/* 132 */       if (key != null) {
/* 133 */         meta.setItemModel(key);
/*     */       } else {
/* 135 */         this.plugin.getLogger().warning("Nieprawidłowy item-model: " + itemModel);
/*     */       } 
/*     */     } 
/*     */     
/* 139 */     if (customModelData != null) {
/* 140 */       CustomModelDataComponent component = meta.getCustomModelDataComponent();
/* 141 */       component.setFloats(new ArrayList(List.of(Float.valueOf(customModelData.floatValue()))));
/* 142 */       meta.setCustomModelDataComponent(component);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\skyriseDEV\plugins\GPS-1.5.jar!\pejtere\\util\GuiItemFactory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */