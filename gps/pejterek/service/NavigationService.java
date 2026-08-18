/*     */ package pejterek.service;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import net.kyori.adventure.text.Component;
/*     */ import org.bukkit.Color;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Particle;
/*     */ import org.bukkit.SoundCategory;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.scheduler.BukkitTask;
/*     */ import org.bukkit.util.Vector;
/*     */ import pejterek.GPS;
/*     */ import pejterek.model.GpsPoint;
/*     */ 
/*     */ public final class NavigationService
/*     */ {
/*     */   private final GPS plugin;
/*  24 */   private final Map<UUID, NavigationSession> sessions = new HashMap<>();
/*     */   
/*     */   private final GpsPointService pointService;
/*     */   private final MessageService messages;
/*     */   private BukkitTask task;
/*     */   private long currentTick;
/*     */   private int updateIntervalTicks;
/*     */   private double arrivalDistance;
/*     */   private int particleCount;
/*     */   private double particleStartDistance;
/*     */   private double particleSpacing;
/*     */   private Particle.DustOptions dustOptions;
/*     */   private String beepSound;
/*     */   private float beepVolume;
/*     */   private float nearPitch;
/*     */   private float farPitch;
/*     */   private int minBeepInterval;
/*     */   private int maxBeepInterval;
/*     */   private double scalingDistance;
/*     */   private String startSound;
/*     */   private String arrivalSound;
/*     */   
/*     */   public NavigationService(GPS plugin, GpsPointService pointService, MessageService messages) {
/*  47 */     this.plugin = plugin;
/*  48 */     this.pointService = pointService;
/*  49 */     this.messages = messages;
/*     */   }
/*     */   
/*     */   public void reloadSettings() {
/*  53 */     stopTask();
/*     */     
/*  55 */     this.updateIntervalTicks = Math.max(1, this.plugin.getConfig().getInt("navigation.update-interval-ticks", 4));
/*  56 */     this.arrivalDistance = Math.max(0.5D, this.plugin.getConfig().getDouble("navigation.arrival-distance", 2.5D));
/*     */     
/*  58 */     this.particleCount = Math.max(1, this.plugin.getConfig().getInt("navigation.particle.count", 8));
/*  59 */     this.particleStartDistance = Math.max(0.1D, this.plugin.getConfig().getDouble("navigation.particle.start-distance", 1.2D));
/*  60 */     this.particleSpacing = Math.max(0.1D, this.plugin.getConfig().getDouble("navigation.particle.spacing", 0.7D));
/*  61 */     float particleSize = (float)Math.max(0.1D, this.plugin.getConfig().getDouble("navigation.particle.size", 1.0D));
/*  62 */     int red = clampColor(this.plugin.getConfig().getInt("navigation.particle.color.red", 255));
/*  63 */     int green = clampColor(this.plugin.getConfig().getInt("navigation.particle.color.green", 190));
/*  64 */     int blue = clampColor(this.plugin.getConfig().getInt("navigation.particle.color.blue", 45));
/*  65 */     this.dustOptions = new Particle.DustOptions(Color.fromRGB(red, green, blue), particleSize);
/*     */     
/*  67 */     this.beepSound = this.plugin.getConfig().getString("navigation.beep.sound", "minecraft:block.note_block.hat");
/*  68 */     this.beepVolume = (float)this.plugin.getConfig().getDouble("navigation.beep.volume", 0.65D);
/*  69 */     this.nearPitch = (float)this.plugin.getConfig().getDouble("navigation.beep.near-pitch", 1.8D);
/*  70 */     this.farPitch = (float)this.plugin.getConfig().getDouble("navigation.beep.far-pitch", 0.8D);
/*  71 */     this.minBeepInterval = Math.max(1, this.plugin.getConfig().getInt("navigation.beep.min-interval-ticks", 4));
/*  72 */     this.maxBeepInterval = Math.max(this.minBeepInterval, this.plugin.getConfig().getInt("navigation.beep.max-interval-ticks", 40));
/*  73 */     this.scalingDistance = Math.max(1.0D, this.plugin.getConfig().getDouble("navigation.beep.scaling-distance", 200.0D));
/*     */     
/*  75 */     this.startSound = this.plugin.getConfig().getString("navigation.start-sound", "minecraft:block.note_block.pling");
/*  76 */     this.arrivalSound = this.plugin.getConfig().getString("navigation.arrival-sound", "minecraft:entity.player.levelup");
/*     */     
/*  78 */     this.task = this.plugin.getServer().getScheduler().runTaskTimer((Plugin)this.plugin, this::tick, 0L, this.updateIntervalTicks);
/*     */   }
/*     */   
/*     */   public boolean startNavigation(Player player, GpsPoint point) {
/*  82 */     Location target = point.toLocation();
/*     */     
/*  84 */     if (target == null) {
/*  85 */       this.messages.send((CommandSender)player, "messages.world-missing");
/*  86 */       return false;
/*     */     } 
/*     */     
/*  89 */     if (!player.getWorld().equals(target.getWorld())) {
/*  90 */       this.messages.send((CommandSender)player, "messages.different-world", 
/*     */ 
/*     */           
/*  93 */           Map.of("world", point.worldName()));
/*     */       
/*  95 */       return false;
/*     */     } 
/*     */     
/*  98 */     UUID playerId = player.getUniqueId();
/*  99 */     NavigationSession currentSession = this.sessions.get(playerId);
/*     */ 
/*     */     
/* 102 */     boolean targetChanged = (currentSession != null && !currentSession.pointId.equalsIgnoreCase(point.id()));
/*     */     
/* 104 */     this.sessions.put(playerId, new NavigationSession(point
/*     */ 
/*     */           
/* 107 */           .id(), this.currentTick - this.maxBeepInterval));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 112 */     player.sendActionBar((Component)Component.empty());
/*     */     
/* 114 */     player.playSound(player
/* 115 */         .getLocation(), this.startSound, SoundCategory.PLAYERS, 0.8F, 1.2F);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 122 */     if (targetChanged) {
/* 123 */       this.messages.send((CommandSender)player, "messages.navigation-changed", 
/*     */ 
/*     */           
/* 126 */           Map.of("name", point.displayName()));
/*     */     } else {
/*     */       
/* 129 */       this.messages.send((CommandSender)player, "messages.navigation-started", 
/*     */ 
/*     */           
/* 132 */           Map.of("name", point.displayName()));
/*     */     } 
/*     */ 
/*     */     
/* 136 */     return true;
/*     */   }
/*     */   
/*     */   public boolean stopNavigation(Player player, boolean notify) {
/* 140 */     NavigationSession removed = this.sessions.remove(player.getUniqueId());
/* 141 */     if (removed == null) {
/* 142 */       if (notify) {
/* 143 */         this.messages.send((CommandSender)player, "messages.navigation-not-running");
/*     */       }
/* 145 */       return false;
/*     */     } 
/* 147 */     player.sendActionBar((Component)Component.empty());
/* 148 */     if (notify) {
/* 149 */       this.messages.send((CommandSender)player, "messages.navigation-stopped");
/*     */     }
/* 151 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isNavigating(Player player) {
/* 155 */     return this.sessions.containsKey(player.getUniqueId());
/*     */   }
/*     */   
/*     */   public Optional<GpsPoint> getTarget(Player player) {
/* 159 */     NavigationSession session = this.sessions.get(player.getUniqueId());
/* 160 */     return (session == null) ? Optional.<GpsPoint>empty() : Optional.<GpsPoint>ofNullable(this.pointService.getById(session.pointId));
/*     */   }
/*     */   
/*     */   public void remove(Player player) {
/* 164 */     this.sessions.remove(player.getUniqueId());
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 168 */     stopTask();
/* 169 */     this.sessions.clear();
/* 170 */     this.currentTick = 0L;
/*     */   }
/*     */   
/*     */   private void tick() {
/* 174 */     this.currentTick += this.updateIntervalTicks;
/* 175 */     Iterator<Map.Entry<UUID, NavigationSession>> iterator = this.sessions.entrySet().iterator();
/*     */     
/* 177 */     while (iterator.hasNext()) {
/* 178 */       Map.Entry<UUID, NavigationSession> entry = iterator.next();
/* 179 */       Player player = this.plugin.getServer().getPlayer(entry.getKey());
/* 180 */       if (player == null || !player.isOnline()) {
/* 181 */         iterator.remove();
/*     */         
/*     */         continue;
/*     */       } 
/* 185 */       NavigationSession session = entry.getValue();
/* 186 */       GpsPoint point = this.pointService.getById(session.pointId);
/* 187 */       if (point == null) {
/* 188 */         player.sendActionBar((Component)Component.empty());
/* 189 */         iterator.remove();
/*     */         
/*     */         continue;
/*     */       } 
/* 193 */       Location target = point.toLocation();
/* 194 */       if (target == null) {
/* 195 */         this.messages.send((CommandSender)player, "messages.world-missing");
/* 196 */         player.sendActionBar((Component)Component.empty());
/* 197 */         iterator.remove();
/*     */         continue;
/*     */       } 
/* 200 */       if (!player.getWorld().equals(target.getWorld())) {
/* 201 */         this.messages.send((CommandSender)player, "messages.different-world", Map.of("world", point.worldName()));
/* 202 */         player.sendActionBar((Component)Component.empty());
/* 203 */         iterator.remove();
/*     */         
/*     */         continue;
/*     */       } 
/* 207 */       double distance = player.getLocation().distance(target);
/* 208 */       if (distance <= this.arrivalDistance) {
/* 209 */         player.playSound(player.getLocation(), this.arrivalSound, SoundCategory.PLAYERS, 1.0F, 1.0F);
/* 210 */         this.messages.send((CommandSender)player, "messages.arrived", Map.of("name", point.displayName()));
/* 211 */         player.sendActionBar((Component)Component.empty());
/* 212 */         iterator.remove();
/*     */         
/*     */         continue;
/*     */       } 
/* 216 */       spawnDirectionParticles(player, target);
/* 217 */       player.sendActionBar(this.messages.fromConfig("navigation.actionbar", Map.of("name", point
/* 218 */               .displayName(), "distance", 
/* 219 */               String.valueOf(Math.round(distance)))));
/*     */ 
/*     */       
/* 222 */       int interval = calculateBeepInterval(distance);
/* 223 */       if (this.currentTick - session.lastBeepTick >= interval) {
/* 224 */         float pitch = calculatePitch(distance);
/* 225 */         player.playSound(player.getLocation(), this.beepSound, SoundCategory.PLAYERS, this.beepVolume, pitch);
/* 226 */         session.lastBeepTick = this.currentTick;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void spawnDirectionParticles(Player player, Location target) {
/* 233 */     Location origin = player.getEyeLocation().add(0.0D, -1.25D, 0.0D);
/*     */ 
/*     */ 
/*     */     
/* 237 */     Vector vectorToTarget = target.toVector().add(new Vector(0.0D, 0.8D, 0.0D)).subtract(origin.toVector());
/*     */     
/* 239 */     double targetDistance = vectorToTarget.length();
/*     */     
/* 241 */     if (targetDistance < 1.0E-4D) {
/*     */       return;
/*     */     }
/*     */     
/* 245 */     Vector direction = vectorToTarget.clone().normalize();
/* 246 */     double configuredLineLength = this.particleStartDistance + (this.particleCount - 1) * this.particleSpacing;
/*     */ 
/*     */     
/* 249 */     double actualLineLength = Math.min(configuredLineLength, targetDistance);
/*     */ 
/*     */ 
/*     */     
/* 253 */     if (actualLineLength <= this.particleStartDistance) {
/*     */       
/* 255 */       Location particleLocation = origin.clone().add(direction.clone().multiply(actualLineLength));
/*     */       
/* 257 */       player.spawnParticle(Particle.DUST, particleLocation, 1, 0.0D, 0.0D, 0.0D, 0.0D, this.dustOptions);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 270 */     for (int index = 0; index < this.particleCount; index++) {
/* 271 */       double particleDistance = this.particleStartDistance + index * this.particleSpacing;
/*     */       
/* 273 */       if (particleDistance >= actualLineLength) {
/*     */         
/* 275 */         Location location = origin.clone().add(direction.clone().multiply(actualLineLength));
/*     */         
/* 277 */         player.spawnParticle(Particle.DUST, location, 1, 0.0D, 0.0D, 0.0D, 0.0D, this.dustOptions);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         break;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 291 */       Location particleLocation = origin.clone().add(direction.clone().multiply(particleDistance));
/*     */       
/* 293 */       player.spawnParticle(Particle.DUST, particleLocation, 1, 0.0D, 0.0D, 0.0D, 0.0D, this.dustOptions);
/*     */     } 
/*     */   }
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
/*     */   public boolean cancelNavigation(Player player) {
/* 307 */     NavigationSession removed = this.sessions.remove(player.getUniqueId());
/*     */     
/* 309 */     if (removed == null) {
/* 310 */       return false;
/*     */     }
/*     */     
/* 313 */     player.sendActionBar((Component)Component.empty());
/* 314 */     this.messages.send((CommandSender)player, "messages.navigation-cancelled");
/* 315 */     return true;
/*     */   }
/*     */   
/*     */   private int calculateBeepInterval(double distance) {
/* 319 */     double ratio = Math.min(1.0D, distance / this.scalingDistance);
/* 320 */     return (int)Math.round(this.minBeepInterval + (this.maxBeepInterval - this.minBeepInterval) * ratio);
/*     */   }
/*     */   
/*     */   private float calculatePitch(double distance) {
/* 324 */     double ratio = Math.min(1.0D, distance / this.scalingDistance);
/* 325 */     return (float)(this.nearPitch + (this.farPitch - this.nearPitch) * ratio);
/*     */   }
/*     */   
/*     */   private int clampColor(int value) {
/* 329 */     return Math.max(0, Math.min(255, value));
/*     */   }
/*     */   
/*     */   private void stopTask() {
/* 333 */     if (this.task != null) {
/* 334 */       this.task.cancel();
/* 335 */       this.task = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class NavigationSession {
/*     */     private final String pointId;
/*     */     private long lastBeepTick;
/*     */     
/*     */     private NavigationSession(String pointId, long lastBeepTick) {
/* 344 */       this.pointId = pointId;
/* 345 */       this.lastBeepTick = lastBeepTick;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\skyriseDEV\plugins\GPS-1.5.jar!\pejterek\service\NavigationService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */