# Amet Fps Booster - Minecraft 26.2 / Fabric

Sadece FPS odakli. Sag Shift ile menu acilir.

## Derleme
1. JDK 25
2. Gradle 9.5.1+ (veya Fabric example mod'dan wrapper dosyalari)
3. `gradle build` -> `build/libs/amet-fps-booster-1.1.0.jar`

## Mods klasoru
Fabric API (zorunlu). Onerilen: Sodium, Lithium, ImmediatelyFast, Entity Culling, FerriteCore.
Poison PVP Optimizer ile ayni anda kullanma (ikisi de Sag Shift kullanir ve ayni ayarlara dokunur).

## Menu (Sag Shift)
- Presetler: Dengeli / Max / EXTREME / Kapat
- 3x3 izgara: bir butona tikla -> ac/kapat, degerli butonlarda (Render, Simulation, Entity, Parcacik) tiklayinca degerler doner
- Adaptif: FPS hedefin altindaysa render distance otomatik azalir, FPS yukselince geri artar
- Arka Plan Limiti: pencere arkadayken FPS 20
- Hedef FPS: 60 / 90 / 120 / 144 / 240
