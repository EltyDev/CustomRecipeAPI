# CustomRecipeAPI
API permettant la création de craft avec des ItemStack avant la 1.12 sous Spigot
## Version supportée
- 1.8.8 ✅
- 1.9 ❔
- 1.9.2 ❔
- 1.9.4
- 1.10 ❔
- 1.10.2 ❔
- 1.11 ❔
- 1.11.1 ❔
- 1.11.2 ❔
## Utilisation
S'utiliser comme les classes "ShapedRecipe" & "ShapelessRecipe". Et pour l'activer, il suffit à la place de mettre 
```java
Bukkit.addRecipe(recipe);
```
de mettre
```java
recipe.register();
```
