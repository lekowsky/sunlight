/*    */ package pejterek.model;public final class GpsPoint extends Record { private final String id; private final String displayName; private final String worldName; private final double x; private final double y; private final double z;
/*    */   private final float yaw;
/*    */   private final float pitch;
/*    */   private final int order;
/*    */   private final IconSettings icon;
/*    */   
/*  7 */   public GpsPoint(String id, String displayName, String worldName, double x, double y, double z, float yaw, float pitch, int order, IconSettings icon) { this.id = id; this.displayName = displayName; this.worldName = worldName; this.x = x; this.y = y; this.z = z; this.yaw = yaw; this.pitch = pitch; this.order = order; this.icon = icon; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lpejterek/model/GpsPoint;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lpejterek/model/GpsPoint; } public String id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lpejterek/model/GpsPoint;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lpejterek/model/GpsPoint; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lpejterek/model/GpsPoint;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lpejterek/model/GpsPoint;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public String displayName() { return this.displayName; } public String worldName() { return this.worldName; } public double x() { return this.x; } public double y() { return this.y; } public double z() { return this.z; } public float yaw() { return this.yaw; } public float pitch() { return this.pitch; } public int order() { return this.order; } public IconSettings icon() { return this.icon; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Location toLocation() {
/* 20 */     World world = Bukkit.getWorld(this.worldName);
/* 21 */     if (world == null) {
/* 22 */       return null;
/*    */     }
/* 24 */     return new Location(world, this.x, this.y, this.z, this.yaw, this.pitch);
/*    */   } }


/* Location:              C:\Users\DELL\Desktop\skyriseDEV\plugins\GPS-1.5.jar!\pejterek\model\GpsPoint.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */