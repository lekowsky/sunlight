/*     */ package pejterek.command;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandExecutor;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.command.TabCompleter;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ import pejterek.gui.GpsMenu;
/*     */ import pejterek.service.GpsPointService;
/*     */ import pejterek.service.MessageService;
/*     */ import pejterek.service.NavigationService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GpsCommand
/*     */   implements CommandExecutor, TabCompleter
/*     */ {
/*     */   private final GpsMenu menu;
/*     */   private final NavigationService navigationService;
/*     */   private final GpsPointService pointService;
/*     */   private final MessageService messages;
/*     */   
/*     */   public GpsCommand(GpsMenu menu, NavigationService navigationService, GpsPointService pointService, MessageService messages) {
/*  36 */     this.menu = menu;
/*  37 */     this.navigationService = navigationService;
/*  38 */     this.pointService = pointService;
/*  39 */     this.messages = messages;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
/*     */     Player player;
/*  49 */     if (args.length > 0 && args[0].equalsIgnoreCase("start")) {
/*  50 */       if (!sender.hasPermission("gps.admin")) {
/*  51 */         this.messages.send(sender, "messages.no-permission");
/*  52 */         return true;
/*     */       } 
/*     */       
/*  55 */       return handleAdminStart(sender, args);
/*     */     } 
/*     */     
/*  58 */     if (sender instanceof Player) { player = (Player)sender; }
/*  59 */     else { this.messages.send(sender, "messages.only-player");
/*  60 */       return true; }
/*     */ 
/*     */     
/*  63 */     if (!player.hasPermission("gps.use")) {
/*  64 */       this.messages.send((CommandSender)player, "messages.no-permission");
/*  65 */       return true;
/*     */     } 
/*     */     
/*  68 */     if (args.length > 0 && args[0].equalsIgnoreCase("stop")) {
/*  69 */       this.navigationService.stopNavigation(player, true);
/*  70 */       return true;
/*     */     } 
/*     */     
/*  73 */     this.menu.open(player, 0);
/*  74 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean handleAdminStart(CommandSender sender, String[] args) {
/*     */     // Byte code:
/*     */     //   0: aload_2
/*     */     //   1: arraylength
/*     */     //   2: iconst_3
/*     */     //   3: if_icmpge -> 18
/*     */     //   6: aload_0
/*     */     //   7: getfield messages : Lpejterek/service/MessageService;
/*     */     //   10: aload_1
/*     */     //   11: ldc 'messages.usage-start'
/*     */     //   13: invokevirtual send : (Lorg/bukkit/command/CommandSender;Ljava/lang/String;)V
/*     */     //   16: iconst_1
/*     */     //   17: ireturn
/*     */     //   18: aload_2
/*     */     //   19: iconst_1
/*     */     //   20: aaload
/*     */     //   21: invokestatic getPlayerExact : (Ljava/lang/String;)Lorg/bukkit/entity/Player;
/*     */     //   24: astore_3
/*     */     //   25: aload_3
/*     */     //   26: ifnull -> 38
/*     */     //   29: aload_3
/*     */     //   30: invokeinterface isOnline : ()Z
/*     */     //   35: ifne -> 58
/*     */     //   38: aload_0
/*     */     //   39: getfield messages : Lpejterek/service/MessageService;
/*     */     //   42: aload_1
/*     */     //   43: ldc 'messages.player-not-found'
/*     */     //   45: ldc 'player'
/*     */     //   47: aload_2
/*     */     //   48: iconst_1
/*     */     //   49: aaload
/*     */     //   50: invokestatic of : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/Map;
/*     */     //   53: invokevirtual send : (Lorg/bukkit/command/CommandSender;Ljava/lang/String;Ljava/util/Map;)V
/*     */     //   56: iconst_1
/*     */     //   57: ireturn
/*     */     //   58: ldc ' '
/*     */     //   60: aload_2
/*     */     //   61: iconst_2
/*     */     //   62: aload_2
/*     */     //   63: arraylength
/*     */     //   64: invokestatic copyOfRange : ([Ljava/lang/Object;II)[Ljava/lang/Object;
/*     */     //   67: checkcast [Ljava/lang/CharSequence;
/*     */     //   70: invokestatic join : (Ljava/lang/CharSequence;[Ljava/lang/CharSequence;)Ljava/lang/String;
/*     */     //   73: invokevirtual trim : ()Ljava/lang/String;
/*     */     //   76: astore #4
/*     */     //   78: aload_0
/*     */     //   79: getfield pointService : Lpejterek/service/GpsPointService;
/*     */     //   82: aload #4
/*     */     //   84: invokevirtual find : (Ljava/lang/String;)Ljava/util/Optional;
/*     */     //   87: astore #5
/*     */     //   89: aload #5
/*     */     //   91: invokevirtual isEmpty : ()Z
/*     */     //   94: ifeq -> 116
/*     */     //   97: aload_0
/*     */     //   98: getfield messages : Lpejterek/service/MessageService;
/*     */     //   101: aload_1
/*     */     //   102: ldc 'messages.point-not-found'
/*     */     //   104: ldc 'name'
/*     */     //   106: aload #4
/*     */     //   108: invokestatic of : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/Map;
/*     */     //   111: invokevirtual send : (Lorg/bukkit/command/CommandSender;Ljava/lang/String;Ljava/util/Map;)V
/*     */     //   114: iconst_1
/*     */     //   115: ireturn
/*     */     //   116: aload #5
/*     */     //   118: invokevirtual get : ()Ljava/lang/Object;
/*     */     //   121: checkcast pejterek/model/GpsPoint
/*     */     //   124: astore #6
/*     */     //   126: aload_0
/*     */     //   127: getfield navigationService : Lpejterek/service/NavigationService;
/*     */     //   130: aload_3
/*     */     //   131: aload #6
/*     */     //   133: invokevirtual startNavigation : (Lorg/bukkit/entity/Player;Lpejterek/model/GpsPoint;)Z
/*     */     //   136: ifne -> 141
/*     */     //   139: iconst_1
/*     */     //   140: ireturn
/*     */     //   141: aload_1
/*     */     //   142: instanceof org/bukkit/entity/Player
/*     */     //   145: ifeq -> 173
/*     */     //   148: aload_1
/*     */     //   149: checkcast org/bukkit/entity/Player
/*     */     //   152: astore #7
/*     */     //   154: aload #7
/*     */     //   156: invokeinterface getUniqueId : ()Ljava/util/UUID;
/*     */     //   161: aload_3
/*     */     //   162: invokeinterface getUniqueId : ()Ljava/util/UUID;
/*     */     //   167: invokevirtual equals : (Ljava/lang/Object;)Z
/*     */     //   170: ifne -> 201
/*     */     //   173: aload_0
/*     */     //   174: getfield messages : Lpejterek/service/MessageService;
/*     */     //   177: aload_1
/*     */     //   178: ldc 'messages.navigation-started-for'
/*     */     //   180: ldc 'player'
/*     */     //   182: aload_3
/*     */     //   183: invokeinterface getName : ()Ljava/lang/String;
/*     */     //   188: ldc 'name'
/*     */     //   190: aload #6
/*     */     //   192: invokevirtual displayName : ()Ljava/lang/String;
/*     */     //   195: invokestatic of : (Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/Map;
/*     */     //   198: invokevirtual send : (Lorg/bukkit/command/CommandSender;Ljava/lang/String;Ljava/util/Map;)V
/*     */     //   201: iconst_1
/*     */     //   202: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #78	-> 0
/*     */     //   #79	-> 6
/*     */     //   #80	-> 16
/*     */     //   #83	-> 18
/*     */     //   #85	-> 25
/*     */     //   #86	-> 38
/*     */     //   #89	-> 50
/*     */     //   #86	-> 53
/*     */     //   #91	-> 56
/*     */     //   #94	-> 58
/*     */     //   #96	-> 64
/*     */     //   #94	-> 70
/*     */     //   #97	-> 73
/*     */     //   #99	-> 78
/*     */     //   #101	-> 89
/*     */     //   #102	-> 97
/*     */     //   #105	-> 108
/*     */     //   #102	-> 111
/*     */     //   #107	-> 114
/*     */     //   #110	-> 116
/*     */     //   #112	-> 126
/*     */     //   #113	-> 139
/*     */     //   #116	-> 141
/*     */     //   #117	-> 156
/*     */     //   #119	-> 173
/*     */     //   #123	-> 183
/*     */     //   #124	-> 192
/*     */     //   #122	-> 195
/*     */     //   #119	-> 198
/*     */     //   #129	-> 201
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   154	19	7	senderPlayer	Lorg/bukkit/entity/Player;
/*     */     //   0	203	0	this	Lpejterek/command/GpsCommand;
/*     */     //   0	203	1	sender	Lorg/bukkit/command/CommandSender;
/*     */     //   0	203	2	args	[Ljava/lang/String;
/*     */     //   25	178	3	targetPlayer	Lorg/bukkit/entity/Player;
/*     */     //   78	125	4	pointNameOrId	Ljava/lang/String;
/*     */     //   89	114	5	optionalPoint	Ljava/util/Optional;
/*     */     //   126	77	6	point	Lpejterek/model/GpsPoint;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   89	114	5	optionalPoint	Ljava/util/Optional<Lpejterek/model/GpsPoint;>;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
/* 139 */     if (args.length == 1) {
/* 140 */       List<String> completions = new ArrayList<>();
/*     */       
/* 142 */       if (sender.hasPermission("gps.use")) {
/* 143 */         completions.add("stop");
/*     */       }
/*     */       
/* 146 */       if (sender.hasPermission("gps.admin")) {
/* 147 */         completions.add("start");
/*     */       }
/*     */       
/* 150 */       return filter(completions, args[0]);
/*     */     } 
/*     */     
/* 153 */     if (!sender.hasPermission("gps.admin")) {
/* 154 */       return List.of();
/*     */     }
/*     */     
/* 157 */     if (!args[0].equalsIgnoreCase("start")) {
/* 158 */       return List.of();
/*     */     }
/*     */     
/* 161 */     if (args.length == 2) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 166 */       List<String> playerNames = Bukkit.getOnlinePlayers().stream().map(Player::getName).sorted(String.CASE_INSENSITIVE_ORDER).toList();
/*     */       
/* 168 */       return filter(playerNames, args[1]);
/*     */     } 
/*     */     
/* 171 */     if (args.length == 3) {
/*     */ 
/*     */ 
/*     */       
/* 175 */       List<String> pointIds = this.pointService.getIds().stream().sorted(String.CASE_INSENSITIVE_ORDER).toList();
/*     */       
/* 177 */       return filter(pointIds, args[2]);
/*     */     } 
/*     */     
/* 180 */     return List.of();
/*     */   }
/*     */   
/*     */   private List<String> filter(List<String> values, String input) {
/* 184 */     String normalizedInput = input.toLowerCase(Locale.ROOT);
/*     */     
/* 186 */     return values.stream()
/* 187 */       .filter(value -> value.toLowerCase(Locale.ROOT).startsWith(normalizedInput))
/*     */       
/* 189 */       .toList();
/*     */   }
/*     */ }


/* Location:              C:\Users\DELL\Desktop\skyriseDEV\plugins\GPS-1.5.jar!\pejterek\command\GpsCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */