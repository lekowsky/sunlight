/*     */ package pejterek.service;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.text.Normalizer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.configuration.ConfigurationSection;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import pejterek.GPS;
/*     */ import pejterek.model.GpsPoint;
/*     */ import pejterek.model.IconSettings;
/*     */ 
/*     */ public final class GpsPointService
/*     */ {
/*     */   private final GPS plugin;
/*     */   private final File file;
/*  26 */   private final Map<String, GpsPoint> points = new LinkedHashMap<>(); private YamlConfiguration data;
/*     */   public static final String DLA_OSOBY_KTORA_TU_TRAFILA = "Jeśli to czytasz, to prawdopodobnie przeglądasz mój kod. Współczuję i życzę powodzenia XDD ~Pejterek";
/*     */   
/*     */   public GpsPointService(GPS plugin) {
/*  30 */     this.plugin = plugin;
/*  31 */     this.file = new File(plugin.getDataFolder(), "locations.yml");
/*     */   }
/*     */   
/*     */   public void reload() {
/*  35 */     this.data = YamlConfiguration.loadConfiguration(this.file);
/*  36 */     this.points.clear();
/*     */     
/*  38 */     ConfigurationSection locations = this.data.getConfigurationSection("locations");
/*  39 */     if (locations == null) {
/*  40 */       this.data.createSection("locations");
/*  41 */       save();
/*     */       
/*     */       return;
/*     */     } 
/*  45 */     for (String id : locations.getKeys(false)) {
/*  46 */       ConfigurationSection section = locations.getConfigurationSection(id);
/*  47 */       if (section == null) {
/*     */         continue;
/*     */       }
/*     */       
/*     */       try {
/*  52 */         GpsPoint point = readPoint(id, section);
/*  53 */         this.points.put(id.toLowerCase(Locale.ROOT), point);
/*  54 */       } catch (IllegalArgumentException exception) {
/*  55 */         this.plugin.getLogger().warning("Pominięto błędny punkt GPS '" + id + "': " + exception.getMessage());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public CreateResult create(String displayName, Location location) {
/*  61 */     String id = slugify(displayName);
/*  62 */     if (id.isBlank()) {
/*  63 */       id = "punkt";
/*     */     }
/*  65 */     if (this.points.containsKey(id)) {
/*  66 */       return new CreateResult(false, id, null);
/*     */     }
/*     */     
/*  69 */     String path = "locations." + id;
/*  70 */     this.data.set(path + ".display-name", displayName);
/*  71 */     this.data.set(path + ".world", location.getWorld().getName());
/*  72 */     this.data.set(path + ".x", Double.valueOf(location.getX()));
/*  73 */     this.data.set(path + ".y", Double.valueOf(location.getY()));
/*  74 */     this.data.set(path + ".z", Double.valueOf(location.getZ()));
/*  75 */     this.data.set(path + ".yaw", Float.valueOf(location.getYaw()));
/*  76 */     this.data.set(path + ".pitch", Float.valueOf(location.getPitch()));
/*  77 */     this.data.set(path + ".order", Integer.valueOf(this.points.size()));
/*     */     
/*  79 */     ConfigurationSection defaults = this.plugin.getConfig().getConfigurationSection("default-icon");
/*  80 */     if (defaults != null) {
/*  81 */       this.data.set(path + ".icon.material", defaults.getString("material", "COMPASS"));
/*  82 */       this.data.set(path + ".icon.name", defaults.getString("name", "<gold>{name}"));
/*  83 */       this.data.set(path + ".icon.lore", defaults.getStringList("lore"));
/*  84 */       this.data.set(path + ".icon.item-model", defaults.getString("item-model", ""));
/*  85 */       this.data.set(path + ".icon.custom-model-data", Integer.valueOf(defaults.getInt("custom-model-data", -1)));
/*  86 */       this.data.set(path + ".icon.glow", Boolean.valueOf(defaults.getBoolean("glow", false)));
/*     */     } 
/*     */     
/*  89 */     save();
/*  90 */     reload();
/*  91 */     return new CreateResult(true, id, this.points.get(id));
/*     */   }
/*     */   
/*     */   public boolean delete(String id) {
/*  95 */     String normalized = id.toLowerCase(Locale.ROOT);
/*  96 */     if (!this.points.containsKey(normalized)) {
/*  97 */       return false;
/*     */     }
/*  99 */     this.data.set("locations." + normalized, null);
/* 100 */     save();
/* 101 */     reload();
/* 102 */     return true;
/*     */   }
/*     */   
/*     */   public Optional<GpsPoint> find(String idOrName) {
/* 106 */     GpsPoint byId = this.points.get(idOrName.toLowerCase(Locale.ROOT));
/* 107 */     if (byId != null) {
/* 108 */       return Optional.of(byId);
/*     */     }
/* 110 */     return this.points.values().stream()
/* 111 */       .filter(point -> point.displayName().equalsIgnoreCase(idOrName))
/* 112 */       .findFirst();
/*     */   }
/*     */   
/*     */   public GpsPoint getById(String id) {
/* 116 */     return this.points.get(id.toLowerCase(Locale.ROOT));
/*     */   }
/*     */   
/*     */   public List<GpsPoint> getSortedPoints() {
/* 120 */     return this.points.values().stream()
/* 121 */       .sorted(Comparator.<GpsPoint>comparingInt(GpsPoint::order)
/* 122 */         .thenComparing(GpsPoint::displayName, String.CASE_INSENSITIVE_ORDER))
/* 123 */       .toList();
/*     */   }
/*     */   
/*     */   public Collection<String> getIds() {
/* 127 */     return new ArrayList<>(this.points.keySet());
/*     */   }
/*     */   
/*     */   public int size() {
/* 131 */     return this.points.size();
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 135 */     this.points.clear();
/* 136 */     this.data = null;
/*     */   }
/*     */   
/*     */   private GpsPoint readPoint(String id, ConfigurationSection section) {
/* 140 */     String displayName = section.getString("display-name", id);
/* 141 */     String world = section.getString("world");
/* 142 */     if (world == null || world.isBlank()) {
/* 143 */       throw new IllegalArgumentException("brak świata");
/*     */     }
/*     */     
/* 146 */     ConfigurationSection iconSection = section.getConfigurationSection("icon");
/* 147 */     IconSettings icon = readIcon(iconSection);
/*     */     
/* 149 */     return new GpsPoint(id
/* 150 */         .toLowerCase(Locale.ROOT), displayName, world, section
/*     */ 
/*     */         
/* 153 */         .getDouble("x"), section
/* 154 */         .getDouble("y"), section
/* 155 */         .getDouble("z"), 
/* 156 */         (float)section.getDouble("yaw"), 
/* 157 */         (float)section.getDouble("pitch"), section
/* 158 */         .getInt("order", 0), icon);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private IconSettings readIcon(ConfigurationSection iconSection) {
/* 164 */     ConfigurationSection defaults = this.plugin.getConfig().getConfigurationSection("default-icon");
/*     */     
/* 166 */     String materialName = string(iconSection, defaults, "material", "COMPASS");
/* 167 */     Material material = Material.matchMaterial(materialName);
/* 168 */     if (material == null || !material.isItem()) {
/* 169 */       this.plugin.getLogger().warning("Nieprawidłowy materiał ikony GPS: " + materialName);
/* 170 */       material = Material.COMPASS;
/*     */     } 
/*     */     
/* 173 */     String name = string(iconSection, defaults, "name", "<gold>{name}");
/* 174 */     List<String> lore = list(iconSection, defaults, "lore");
/* 175 */     String itemModel = string(iconSection, defaults, "item-model", "");
/* 176 */     int rawCustomModelData = integer(iconSection, defaults, "custom-model-data", -1);
/* 177 */     Integer customModelData = (rawCustomModelData < 0) ? null : Integer.valueOf(rawCustomModelData);
/* 178 */     boolean glow = bool(iconSection, defaults, "glow", false);
/*     */     
/* 180 */     return new IconSettings(material, name, List.copyOf(lore), itemModel, customModelData, glow);
/*     */   }
/*     */   
/*     */   private String string(ConfigurationSection own, ConfigurationSection defaults, String key, String fallback) {
/* 184 */     if (own != null && own.contains(key)) {
/* 185 */       return own.getString(key, fallback);
/*     */     }
/* 187 */     return (defaults == null) ? fallback : defaults.getString(key, fallback);
/*     */   }
/*     */   
/*     */   private List<String> list(ConfigurationSection own, ConfigurationSection defaults, String key) {
/* 191 */     if (own != null && own.isList(key)) {
/* 192 */       return own.getStringList(key);
/*     */     }
/* 194 */     return (defaults == null) ? List.<String>of() : defaults.getStringList(key);
/*     */   }
/*     */   
/*     */   private int integer(ConfigurationSection own, ConfigurationSection defaults, String key, int fallback) {
/* 198 */     if (own != null && own.contains(key)) {
/* 199 */       return own.getInt(key, fallback);
/*     */     }
/* 201 */     return (defaults == null) ? fallback : defaults.getInt(key, fallback);
/*     */   }
/*     */   
/*     */   private boolean bool(ConfigurationSection own, ConfigurationSection defaults, String key, boolean fallback) {
/* 205 */     if (own != null && own.contains(key)) {
/* 206 */       return own.getBoolean(key, fallback);
/*     */     }
/* 208 */     return (defaults == null) ? fallback : defaults.getBoolean(key, fallback);
/*     */   }
/*     */   
/*     */   private void save() {
/*     */     try {
/* 213 */       this.data.save(this.file);
/* 214 */     } catch (IOException exception) {
/* 215 */       throw new IllegalStateException("Nie udało się zapisać locations.yml", exception);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String slugify(String input) {
/* 224 */     String normalized = Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "_").replaceAll("^_+|_+$", "");
/* 225 */     return normalized;
/*     */   }
/*     */   public static final class CreateResult extends Record { private final boolean created; private final String id; private final GpsPoint point;
/* 228 */     public CreateResult(boolean created, String id, GpsPoint point) { this.created = created; this.id = id; this.point = point; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lpejterek/service/GpsPointService$CreateResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #228	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 228 */       //   0	7	0	this	Lpejterek/service/GpsPointService$CreateResult; } public boolean created() { return this.created; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lpejterek/service/GpsPointService$CreateResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #228	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lpejterek/service/GpsPointService$CreateResult; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lpejterek/service/GpsPointService$CreateResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #228	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lpejterek/service/GpsPointService$CreateResult;
/* 228 */       //   0	8	1	o	Ljava/lang/Object; } public String id() { return this.id; } public GpsPoint point() { return this.point; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              C:\Users\DELL\Desktop\skyriseDEV\plugins\GPS-1.5.jar!\pejterek\service\GpsPointService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */