/*   */ package pejterek.model;public final class IconSettings extends Record { private final Material material; private final String name; private final List<String> lore;
/*   */   private final String itemModel;
/*   */   private final Integer customModelData;
/*   */   private final boolean glow;
/*   */   
/* 6 */   public IconSettings(Material material, String name, List<String> lore, String itemModel, Integer customModelData, boolean glow) { this.material = material; this.name = name; this.lore = lore; this.itemModel = itemModel; this.customModelData = customModelData; this.glow = glow; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lpejterek/model/IconSettings;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 6 */     //   0	7	0	this	Lpejterek/model/IconSettings; } public Material material() { return this.material; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lpejterek/model/IconSettings;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lpejterek/model/IconSettings; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lpejterek/model/IconSettings;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lpejterek/model/IconSettings;
/* 6 */     //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public List<String> lore() { return this.lore; } public String itemModel() { return this.itemModel; } public Integer customModelData() { return this.customModelData; } public boolean glow() { return this.glow; }
/*   */    }


/* Location:              C:\Users\DELL\Desktop\skyriseDEV\plugins\GPS-1.5.jar!\pejterek\model\IconSettings.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */